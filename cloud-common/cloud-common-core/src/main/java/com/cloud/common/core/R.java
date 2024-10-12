package com.cloud.common.core;

import com.cloud.common.core.constant.ResultConstant;
import com.cloud.common.core.enums.ErrorEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {
    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应体
     */
    private T data;

    public R() {
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> ok(T data) {
        return new R<>(ResultConstant.RESULT_OK_CODE, ResultConstant.RESULT_OK_MSG, data);
    }

    public static <T> R<T> ok() {
        return new R<>(ResultConstant.RESULT_OK_CODE, ResultConstant.RESULT_OK_MSG, null);
    }

    public static <T> R<T> fail(ErrorEnum errorEnum, T data) {
        return new R<>(errorEnum.getCode(), errorEnum.getMsg(), data);
    }

    public static <T> R<T> fail(ErrorEnum errorEnum) {
        return new R<>(errorEnum.getCode(), errorEnum.getMsg(), null);
    }
}
