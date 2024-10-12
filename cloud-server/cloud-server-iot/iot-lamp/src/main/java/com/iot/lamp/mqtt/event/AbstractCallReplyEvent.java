package com.iot.lamp.mqtt.event;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/10 20:14
 */
public abstract class AbstractCallReplyEvent extends AbstractMqttMsgCenter {

    public AbstractCallReplyEvent(Object source) {
        super(source);
    }
}
