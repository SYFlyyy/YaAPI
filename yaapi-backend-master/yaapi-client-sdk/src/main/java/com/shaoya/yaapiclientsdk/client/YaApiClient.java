package com.shaoya.yaapiclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

import static com.shaoya.yaapiclientsdk.utils.SignUtils.genSign;

/**
 * 调用第三方接口的客户端
 *
 * @author shaoyafan
 */
public class YaApiClient {

    private static final String GATEWAY_URL = "http://localhost:8090";

    private String accessKey;

    private String secretKey;

    public YaApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

//    public String getNameByGet(String name) {
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("name", name);
//        String result = HttpUtil.get(GATEWAY_URL + "/api/name/", paramMap);
//        System.out.println(result);
//        return result;
//    }
//
//    public String getNameByPost(String name) {
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("name", name);
//        String result = HttpUtil.post(GATEWAY_URL + "/api/name/", paramMap);
//        System.out.println(result);
//        return result;
//    }
//
//    public String getUserNameByPost(User user) {
//        String json = JSONUtil.toJsonStr(user);
//        HttpResponse httpResponse = HttpRequest.post(GATEWAY_URL + "/api/name/user")
//                .addHeaders(getHeaderMap(json))
//                .body(json)
//                .execute();
//        System.out.println(httpResponse.getStatus());
//        String result = httpResponse.body();
//        System.out.println(result);
//        return result;
//    }

    public String invokeInterface(String url, String method, String params) {
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_URL + url)
                .header("Accept-Charset", CharsetUtil.UTF_8)
                .addHeaders(getHeaderMap(method, params))
                .body(params)
                .execute();
        return JSONUtil.formatJsonStr(httpResponse.body());
    }

    private Map<String, String> getHeaderMap(String method, String body) {
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", genSign(body, secretKey));
        map.put("method", method);
        body = URLUtil.encode(body, CharsetUtil.CHARSET_UTF_8);
        map.put("body", body);
        return map;
    }
}
