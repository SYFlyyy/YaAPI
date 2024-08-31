package com.shaoya.yaapibackend.service.impl.inner;

import com.shaoya.yaapibackend.exception.BusinessException;
import com.shaoya.yaapibackend.service.UserService;
import com.shaoya.yaapicommon.common.ErrorCode;
import com.shaoya.yaapicommon.model.entity.User;
import com.shaoya.yaapicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部用户服务实现类
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;

//    @Resource
//    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.query()
                .eq("accessKey", accessKey)
                .one();
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("accessKey", accessKey);
//        return userMapper.selectOne(queryWrapper);
    }
}
