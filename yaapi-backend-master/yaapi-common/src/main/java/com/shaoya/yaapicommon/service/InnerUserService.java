package com.shaoya.yaapicommon.service;

import com.shaoya.yaapicommon.model.entity.User;

/**
 * 内部用户服务
 *
 * @author shaoyafan
 */
public interface InnerUserService {

    /**
     * 获取调用用户
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
