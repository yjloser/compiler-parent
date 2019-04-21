package com.semitransfer.compiler.core.config.internal.api.util;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 授权实体
 * </p>
 *
 * @author Mr.Yang
 * @since 2019-01-21
 */
@Data
public class AclUtils implements Serializable {

    /**
     * 权限码
     */
    private String aclCode;
    /**
     * 权限名称
     */
    private String aclName;
    /**
     * 主键
     */
    private int id;

    /**
     * 类型
     */
    private int type;
    /**
     * url
     */
    private String url;
}
