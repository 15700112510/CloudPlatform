package com.cloud.common.security.aspect;

import com.cloud.common.core.R;
import com.cloud.common.core.enums.ErrorEnum;
import com.cloud.common.security.annotation.BasicValueCheck;
import com.cloud.common.security.annotation.StringValueCheck;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/7 10:14
 */
@Aspect
@Component
public class ValueCheckAspect implements Ordered {

    @Pointcut("execution(* com..controller.Web*Controller.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        Signature signature = pjp.getSignature();
        MethodSignature ms = (MethodSignature) signature;
        Method method = ms.getMethod();
        Parameter[] parameters = method.getParameters();
        int parameterCount = method.getParameterCount();

        for (int i = 0; i < parameterCount; i++) {
            // controller请求参数校验
            Class<?> clazz = parameters[i].getType();

            if (clazz.equals(String.class) && parameters[i].isAnnotationPresent(StringValueCheck.class)) {
                String temp = (String) args[i];
                if (temp == null || temp.length() == 0) {
                    return R.fail(ErrorEnum.ILLEGAL_PARAM);
                }
            } else if ((clazz.equals(Long.class) || (clazz.equals(Integer.class))
                    && parameters[i].isAnnotationPresent(BasicValueCheck.class))) {
                if (args[i] == null) {
                    return R.fail(ErrorEnum.ILLEGAL_PARAM);
                }
            }

            // 实体类成员变量校验
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().equals(String.class) && field.isAnnotationPresent(StringValueCheck.class)) {
                    field.setAccessible(true);
                    String s = (String) field.get(args[i]);
                    if (s == null || s.length() == 0) {
                        return R.fail(ErrorEnum.ILLEGAL_PARAM);
                    }
                } else if ((field.getType().equals(Long.class) || field.getType().equals(Integer.class))
                        && field.isAnnotationPresent(BasicValueCheck.class)) {
                    field.setAccessible(true);
                    if (field.get(args[i]) == null) {
                        return R.fail(ErrorEnum.ILLEGAL_PARAM);
                    }
                }
            }
        }

        return pjp.proceed();
    }

    private void defaultValueSet(Field field, Object obj) throws IllegalAccessException {
        if (field.getType().equals(String.class)) {
            field.set(obj, "");
        }
    }

    @Override
    public int getOrder() {
        return 300;
    }
}
