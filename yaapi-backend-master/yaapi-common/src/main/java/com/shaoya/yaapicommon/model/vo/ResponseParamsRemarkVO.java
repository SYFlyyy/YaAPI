package com.shaoya.yaapicommon.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shaoyafan
 */
@Data
public class ResponseParamsRemarkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 说明
     */
    private String remark;
}
