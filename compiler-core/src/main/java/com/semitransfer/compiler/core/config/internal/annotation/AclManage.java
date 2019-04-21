package com.semitransfer.compiler.core.config.internal.annotation;

import java.lang.annotation.*;


/**
 * <p>
 *权限操作
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AclManage {

    /**
     * 权限名
     */
    String value() default "";
}
