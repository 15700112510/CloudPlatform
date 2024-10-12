package com.cloud.common.security.annotation;

import java.lang.annotation.*;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/4 22:33
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullCheck {
}
