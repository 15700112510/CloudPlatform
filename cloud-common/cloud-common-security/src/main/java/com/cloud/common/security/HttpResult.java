package com.cloud.common.security;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Http相关错误信息返回
 *
 * @author likain
 */
@Data
public class HttpResult implements Serializable {
    /**
     * 状态码
     */
    private int status;

    /**
     * 状态信息
     */
    private String message;

    public HttpResult() {
    }

    public HttpResult(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static HttpResult success() {
        return new HttpResult(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    public static HttpResult error(HttpStatus status) {
        return new HttpResult(status.value(), status.getReasonPhrase());
    }
}
