package com.shaoya.yaapicommon.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 下线接口请求
 *
 * @author shaoyafan
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
