package com.cloud.common.security.aspect;

import com.cloud.common.security.exception.RoleNotMatchException;
import com.cloud.common.security.annotation.Logical;
import com.cloud.common.security.annotation.RequiredRoles;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 注解鉴权
 *
 * @author likain
 */
@Aspect
@Component
public class AuthAspect implements Ordered {
    /**
     * AOP切入点 (切入所有使用鉴权注解的方法)
     */
    private static final String POINTCUT_SIGN = "execution(* com..controller.Web*Controller.*(..))";

    /**
     * 声明AOP切入点
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut() {
    }

    /**
     * 环绕切入
     *
     * @param proceedingJoinPoint 切面对象
     * @return 底层方法执行后的返回值
     * @throws Throwable 底层方法抛出的异常
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // attributes为空，直接抛出异常拒绝继续执行
        if (attributes == null) {
            throw new RuntimeException("请求发生了未知错误，请重试！");
        }

        // 切入时取出请求头中的角色信息
        HttpServletRequest request = attributes.getRequest();
        String userRole = request.getHeader("role");

        // 通过反射获取类上注解
        RequiredRoles requiredRoles = proceedingJoinPoint.getTarget().getClass().getAnnotation(RequiredRoles.class);

        // 若类上RequiredRoles注解为空，则从方法上获取该注解并赋值
        if (requiredRoles == null) {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            requiredRoles = signature.getMethod().getAnnotation(RequiredRoles.class);
        }

        // 解析注解
        checkAnnotation(requiredRoles, userRole);

        // 执行业务方法
        return proceedingJoinPoint.proceed();
    }

    /**
     * 对Method对象进行注解检查
     */
    private void checkAnnotation(RequiredRoles requiredRoles, String userRole) {
        // 校验 @RequiredRoles 注解
        if (requiredRoles != null) {
            if (requiredRoles.logical() == Logical.AND) {
                checkRoleAnd(userRole, requiredRoles.value());
            }
        }
    }

    private void checkRoleAnd(String userRole, String... roles) {
        for (String role : roles) {
            if (!role.equals(userRole)) {
                throw new RoleNotMatchException();
            }
        }
    }

    /**
     * 切面优先级
     *
     * @return 切入顺序
     */
    @Override
    public int getOrder() {
        return -100;
    }
}
