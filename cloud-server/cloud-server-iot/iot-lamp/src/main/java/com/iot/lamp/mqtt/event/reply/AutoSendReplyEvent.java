package com.iot.lamp.mqtt.event.reply;

import com.iot.lamp.mqtt.event.AbstractCallReplyEvent;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/10 20:37
 */
public class AutoSendReplyEvent extends AbstractCallReplyEvent {

    public AutoSendReplyEvent(Object source) {
        super(source);
    }
}
