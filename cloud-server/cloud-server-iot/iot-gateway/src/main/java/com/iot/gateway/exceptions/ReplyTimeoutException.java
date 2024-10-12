package com.iot.gateway.exceptions;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/29 14:38
 */
public class ReplyTimeoutException extends RuntimeException {

    private String message;

    public ReplyTimeoutException(String message) {
        super(message);
    }
}
