package com.cloud.common.security.exception.handler;

import com.cloud.common.core.R;
import com.cloud.common.core.enums.ErrorEnum;
import com.cloud.common.security.exception.IllegalParamException;
import com.cloud.common.security.exception.OriginIllegalException;
import com.cloud.common.security.exception.RemoteCallFailedException;
import com.cloud.common.security.exception.RoleNotMatchException;
import com.cloud.common.security.HttpResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 角色不匹配异常
     */
    @ExceptionHandler(RoleNotMatchException.class)
    public HttpResult handleRoleNotMatchException(RoleNotMatchException e) {
        return HttpResult.error(HttpStatus.FORBIDDEN);
    }

    /**
     * 请求源地址非法异常
     */
    @ExceptionHandler(OriginIllegalException.class)
    public HttpResult handleOriginIllegalException(OriginIllegalException e) {
        return HttpResult.error(HttpStatus.BAD_GATEWAY);
    }

    // @ExceptionHandler(RuntimeException.class)
    // public R<?> handleSystemException(RuntimeException e) {
    //     return R.fail(ErrorEnum.SYSTEM_ERROR);
    // }

    /**
     * 控制层请求过程中请求参数出错
     */
    @ExceptionHandler(IllegalParamException.class)
    public R<?> handleIllegalParamException(IllegalParamException e) {
        return R.fail(ErrorEnum.ILLEGAL_PARAM);
    }

    /**
     * 远程网络调用失败
     */
    @ExceptionHandler(RemoteCallFailedException.class)
    public R<ErrorEnum> handleRemoteCallFailedException(RemoteCallFailedException e) {
        return R.fail(ErrorEnum.REMOTE_CALL_EXCEPTION);
    }
}
