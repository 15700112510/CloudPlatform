package com.cloud.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 只允许从指定路径来的请求访问服务
 *
 * @author likain
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RequiredOrigin {
    /**
     * 指定访问源
     */
    String value() default "";
}
