package com.cloud.common.security.aspect;

import com.cloud.common.security.exception.OriginIllegalException;
import com.cloud.common.security.annotation.RequiredOrigin;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求源甄别
 *
 * @author likain
 */
@Aspect
@Component
public class OriginAspect implements Ordered {
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

    /**
     * 进入controller之前切入
     *
     * @param joinPoint 切面对象
     */
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        // 获取HttpServletRequest请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // attributes为空，直接抛出异常拒绝继续执行
        if (attributes == null) {
            throw new RuntimeException("请求发生了未知错误，请重试！");
        }
        HttpServletRequest request = attributes.getRequest();

        // 反射取到类上的注释
        RequiredOrigin requiredOrigin = joinPoint.getTarget().getClass().getAnnotation(RequiredOrigin.class);

        // 类上没注解，放行此次aop
        if (requiredOrigin == null) {
            return;
        }

        // 甄别请求源是否合法
        if (!requiredOrigin.value().equals(request.getHeader("origin"))) {
            throw new OriginIllegalException();
        }
    }

    /**
     * 切面优先级
     *
     * @return 切入顺序
     */
    @Override
    public int getOrder() {
        return -200;
    }
}
