package com.iot.lamp.mqtt.event;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/10 16:53
 */
public abstract class AbstractMsgReceiveEvent extends AbstractMqttMsgCenter {

    public AbstractMsgReceiveEvent(Object source) {
        super(source);
    }
}
