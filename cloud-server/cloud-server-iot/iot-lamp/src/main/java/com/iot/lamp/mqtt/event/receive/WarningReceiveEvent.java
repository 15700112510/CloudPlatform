package com.iot.lamp.mqtt.event.receive;

import com.iot.lamp.mqtt.event.AbstractMsgReceiveEvent;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/10 21:06
 */
public class WarningReceiveEvent extends AbstractMsgReceiveEvent {

    public WarningReceiveEvent(Object source) {
        super(source);
    }
}
