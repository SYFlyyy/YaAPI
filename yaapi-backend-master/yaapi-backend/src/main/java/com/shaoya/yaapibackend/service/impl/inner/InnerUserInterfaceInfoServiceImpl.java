package com.shaoya.yaapibackend.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shaoya.yaapibackend.exception.BusinessException;
import com.shaoya.yaapibackend.service.UserInterfaceInfoService;
import com.shaoya.yaapicommon.common.ErrorCode;
import com.shaoya.yaapicommon.model.entity.UserInterfaceInfo;
import com.shaoya.yaapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部用户接口信息服务实现类
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public Boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return userInterfaceInfoService.update(updateWrapper);
    }

    @Override
    public UserInterfaceInfo getUserInterfaceInfo(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userInterfaceInfoService.query()
                .eq("interfaceInfoId", interfaceInfoId)
                .eq("userId", userId)
                .one();
    }

    @Override
    public Boolean addDefaultUserInterfaceInfo(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
        userInterfaceInfo.setUserId(userId);
        userInterfaceInfo.setLeftNum(1000);
        return userInterfaceInfoService.save(userInterfaceInfo);
    }
}
