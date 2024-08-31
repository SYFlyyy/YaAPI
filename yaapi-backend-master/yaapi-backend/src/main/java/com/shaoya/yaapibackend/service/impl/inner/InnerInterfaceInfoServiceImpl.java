package com.shaoya.yaapibackend.service.impl.inner;

import com.shaoya.yaapibackend.exception.BusinessException;
import com.shaoya.yaapibackend.service.InterfaceInfoService;
import com.shaoya.yaapicommon.common.ErrorCode;
import com.shaoya.yaapicommon.model.entity.InterfaceInfo;
import com.shaoya.yaapicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部接口服务实现类
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

//    @Resource
//    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return interfaceInfoService.query()
                .eq("url", url)
                .eq("method", method)
                .one();
//        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("url", url);
//        queryWrapper.eq("method", method);
//        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
