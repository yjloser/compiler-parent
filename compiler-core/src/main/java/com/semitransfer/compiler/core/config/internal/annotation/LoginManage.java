package com.semitransfer.compiler.core.config.internal.annotation;

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
public @interface LoginManage {

    /**
     * 拦截类型
     */
    LoginEnum value() default LoginEnum.EMNU;

    /**
     * 模块
     */
    String module() default "";

    /**
     * 分组
     */
    String group() default "";
}
