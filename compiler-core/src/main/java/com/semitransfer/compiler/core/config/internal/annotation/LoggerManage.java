package com.semitransfer.compiler.core.config.internal.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * <p>
 * 登录拦截操作
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggerManage {

    /**
     * 模块名称
     */
    @AliasFor("module")
    String value() default "";

    /**
     * 模块
     */
    @AliasFor("value")
    String module() default "";

    /**
     * 日志登记
     */
    LoggerEnum level() default LoggerEnum.DEBUG;

}
