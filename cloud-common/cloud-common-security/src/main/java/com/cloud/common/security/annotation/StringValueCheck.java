package com.cloud.common.security.annotation;

import java.lang.annotation.*;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/7 10:13
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringValueCheck {
}
