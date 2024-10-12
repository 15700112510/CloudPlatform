package com.iot.lamp.mqtt.event.receive;

import com.iot.lamp.mqtt.event.AbstractMsgReceiveEvent;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/10 16:57
 */
public class HeartbeatReceiveEvent extends AbstractMsgReceiveEvent {

    public HeartbeatReceiveEvent(Object source) {
        super(source);
    }
}
