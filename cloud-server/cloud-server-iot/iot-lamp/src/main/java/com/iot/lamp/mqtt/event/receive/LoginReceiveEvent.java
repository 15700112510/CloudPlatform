package com.iot.lamp.mqtt.event.receive;

import com.iot.lamp.mqtt.event.AbstractMsgReceiveEvent;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/10 18:11
 */
public class LoginReceiveEvent extends AbstractMsgReceiveEvent {

    public LoginReceiveEvent(Object source) {
        super(source);
    }
}
