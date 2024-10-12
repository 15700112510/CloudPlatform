package com.cloud.common.security.annotation;

import com.cloud.common.core.enums.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.cloud.common.core.enums.RoleEnum.SYS_ADMIN;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AllowRoles {

    RoleEnum value() default SYS_ADMIN;
}
