package com.shaoya.yaapigateway.filter;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import com.shaoya.yaapiclientsdk.utils.SignUtils;
import com.shaoya.yaapicommon.common.ErrorCode;
import com.shaoya.yaapicommon.model.entity.InterfaceInfo;
import com.shaoya.yaapicommon.model.entity.User;
import com.shaoya.yaapicommon.model.entity.UserInterfaceInfo;
import com.shaoya.yaapicommon.service.InnerInterfaceInfoService;
import com.shaoya.yaapicommon.service.InnerUserInterfaceInfoService;
import com.shaoya.yaapicommon.service.InnerUserService;
import com.shaoya.yaapigateway.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHILE_LIST = Collections.singletonList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String IP_ADDRESS = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        String path = request.getPath().value();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + path);
        log.info("请求参数：" + request.getQueryParams());
        log.info("请求来源地址：" + IP_ADDRESS);
        log.info("请求来源地址：" + request.getRemoteAddress());
        ServerHttpResponse response = exchange.getResponse();
        // 黑白名单
        if (!IP_WHILE_LIST.contains(IP_ADDRESS)) {
            return handleNoAuth(response);
        }
        // 用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String method = headers.getFirst("method");
        String body = URLUtil.decode(headers.getFirst("body"), CharsetUtil.CHARSET_UTF_8);
        if (StringUtils.isBlank(nonce) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(sign) || StringUtils.isBlank(method) || StringUtils.isBlank(body)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "请求参数不完整！");
        }
        // 通过 accessKey 查询是否存在该用户
        User invokeUser = innerUserService.getInvokeUser(accessKey);
        if (invokeUser == null) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "accessKey 不合法！");
        }
        // 判断随机数是否存在，防止重放攻击
        if (Long.parseLong(nonce) > 10000L) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "请求重复！");
        }
        // 时间戳和当前时间不能超过5分钟
        long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_MINUTES = 5 * 60L;
        if (currentTime - Long.parseLong(timestamp) > FIVE_MINUTES) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "请求超时！");
        }
        // 签名校验
        // 从数据库中查出 secretKey 生成 sign 与前端传递的 sign 对比
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (!sign.equals(serverSign)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "签名错误！");
        }
        // 从数据库中查询接口是否存在，已经方法是否匹配
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "接口不存在！");
        }
        // 是否还有调用次数
        if (!hasLeftNum(interfaceInfo.getId(), invokeUser.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "调用次数不足！");
        }
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    private Boolean hasLeftNum(long interfaceInfoId, long userId) {
        UserInterfaceInfo userInterfaceInfo = innerUserInterfaceInfoService.getUserInterfaceInfo(interfaceInfoId, userId);
        if (userInterfaceInfo == null) {
            // 用户首次使用接口分配默认次数
            Boolean save = innerUserInterfaceInfoService.addDefaultUserInterfaceInfo(interfaceInfoId, userId);
            if (save == null || !save) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口绑定用户失败！");
            }
        }
        if (userInterfaceInfo != null && userInterfaceInfo.getLeftNum() <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.setRawStatusCode(HttpStatus.FORBIDDEN.value());
        return response.setComplete();
    }
}
