package com.iot.gateway.mqtt.event;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/30 15:48
 */
public abstract class AbstractSendEvent extends MqttMsgEvent {

    private String message;

    public AbstractSendEvent(Object source) {
        super(source);
    }

    public AbstractSendEvent setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }
}
