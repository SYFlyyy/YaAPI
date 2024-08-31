package com.shaoya.yaapicommon.service;

import com.shaoya.yaapicommon.model.entity.UserInterfaceInfo;

/**
 * 内部用户接口信息服务
 *
 * @author shaoyafan
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口次数统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    Boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 获取用户接口信息
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    UserInterfaceInfo getUserInterfaceInfo(long interfaceInfoId, long userId);

    /**
     * 添加默认用户接口信息
     * @param interfaceId
     * @param userId
     * @return
     */
    Boolean addDefaultUserInterfaceInfo(long interfaceId, long userId);
}
