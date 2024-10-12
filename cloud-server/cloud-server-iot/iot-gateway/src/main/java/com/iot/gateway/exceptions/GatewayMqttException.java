package com.iot.gateway.exceptions;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/4 22:08
 */
public class GatewayMqttException extends RuntimeException {

    public GatewayMqttException(Throwable throwable) {
        super(throwable);
    }
}
