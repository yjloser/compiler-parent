package com.semitransfer.compiler.core.config.internal.annotation;

/**
 * <p>
 * 日志登记
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public enum LoggerEnum {

    /**
     * 日志登记
     */
    ERROR("ERROR"),
    WARN("WARN"),
    INFO("INFO"),
    DEBUG("DEBUG");

    private String value;

    LoggerEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
