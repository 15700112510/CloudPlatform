package com.iot.gateway.exceptions;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/4 14:22
 */
public class MessageParseException extends RuntimeException {

    public MessageParseException(String message, Throwable t) {
        super(message, t);
    }
}
