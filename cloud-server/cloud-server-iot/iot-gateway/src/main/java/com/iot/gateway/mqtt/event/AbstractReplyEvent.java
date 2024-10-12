package com.iot.gateway.mqtt.event;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/30 09:38
 */
public abstract class AbstractReplyEvent extends MqttMsgEvent {

    private String msgID;
    private String reply;

    public AbstractReplyEvent(Object source) {
        super(source);
    }

    public AbstractReplyEvent setMsgID(String msgID) {
        this.msgID = msgID;
        return this;
    }

    public String getMsgID() {
        return msgID;
    }

    public AbstractReplyEvent setReply(String reply) {
        this.reply = reply;
        return this;
    }

    public String getReply() {
        return reply;
    }
}
