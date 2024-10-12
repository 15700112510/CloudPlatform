package com.cloud.common.security.aspect;

import com.cloud.common.core.utils.JwtUtil;
import com.cloud.common.security.annotation.AllowRoles;
import com.cloud.common.security.exception.RoleNotMatchException;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect
public class RoleAspect {
    /**
     * AOP切入点 (切入所有控制层方法)
     */
    private static final String POINTCUT_SIGN = "execution(* com..controller.Web*Controller.*(..))";

    /**
     * 声明AOP切入点
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 类上注解
        AllowRoles allowedRole = joinPoint.getTarget().getClass().getAnnotation(AllowRoles.class);
        if (allowedRole == null) {
            // 方法上注解
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            allowedRole = method.getDeclaredAnnotation(AllowRoles.class);
        }
        if (allowedRole == null) {
            return joinPoint.proceed();
        }
        // 获取请求
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        // 获取参数
        String token = request.getHeader("token");
        // 解析token
        Claims claims = JwtUtil.parseToken(token);
        int role = Integer.parseInt(claims.get("role") + "");
        // 注解role
        int role1 = Integer.parseInt(allowedRole.value().getRole() + "");
        if (role > role1) {
            throw new RoleNotMatchException();
        }
        return joinPoint.proceed();
    }
}
