package com.semitransfer.compiler.plugin.config.internal.util;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/10/17
 */
@Data
public class Sms {

    /**
     * 消息id
     */
    private String mid;

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 短信提交错误代码 手机号
     */
    private Integer result;
}
