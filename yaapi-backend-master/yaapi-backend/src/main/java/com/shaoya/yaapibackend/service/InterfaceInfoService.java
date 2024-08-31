package com.shaoya.yaapibackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoya.yaapiclientsdk.client.YaApiClient;
import com.shaoya.yaapicommon.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.shaoya.yaapicommon.model.entity.InterfaceInfo;
import com.shaoya.yaapicommon.model.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author shaoyafan
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-07-15 17:19:44
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 获取 SDK 客户端
     *
     * @param request
     * @return
     */
    YaApiClient getYaApiClient(HttpServletRequest request);

    /**
     * 对象转视图对象
     * @param interfaceInfo
     * @return
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo);

    /**
     * 获取接口信息视图对象
     * @param interfaceInfo
     * @param request
     * @return
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

    /**
     * 获取查询条件
     * @param interfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 获取接口信息视图对象列表
     * @param interfaceInfoPage
     * @param request
     * @return
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);

    /**
     * 根据用户id获取接口信息视图对象列表
     * @param interfaceInfoPage
     * @param request
     * @return
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOByUserIdPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);
}
