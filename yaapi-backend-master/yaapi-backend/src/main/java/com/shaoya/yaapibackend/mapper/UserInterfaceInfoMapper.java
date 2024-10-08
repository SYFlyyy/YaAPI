package com.shaoya.yaapibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shaoya.yaapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author shaoyafan
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-07-23 17:24:11
* @Entity com.shaoya.yaapibackend.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




