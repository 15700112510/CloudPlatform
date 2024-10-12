package com.iot.lamp.mqtt.event;

import org.springframework.context.ApplicationEvent;

/**
 * TODO: Mqtt消息的根类
 *
 * @author likain
 * @since 2023/6/10 16:51
 */
public abstract class AbstractMqttMsgCenter extends ApplicationEvent {

    private String topic;
    private String payload;

    public AbstractMqttMsgCenter(Object source) {
        super(source);
    }

    public String getTopic() {
        return this.topic;
    }

    public String getPayload() {
        return this.payload;
    }

    public AbstractMqttMsgCenter setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public AbstractMqttMsgCenter setPayload(String payload) {
        this.payload = payload;
        return this;
    }
}
