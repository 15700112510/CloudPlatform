package com.iot.gateway.mqtt.event;

import org.springframework.context.ApplicationEvent;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/30 15:51
 */
public abstract class MqttMsgEvent extends ApplicationEvent {

    public MqttMsgEvent(Object source) {
        super(source);
    }
}
