package com.iot.gateway.mqtt.callback;

import com.iot.gateway.mqtt.event.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.yzu.cloud.core.AbstractMessageProcessor;

import java.util.concurrent.TimeUnit;

/**
 * TODO: MQTT消息处理中心
 *
 * @author likain
 * @since 2023/5/29 14:23
 */
@Slf4j
@Component
public class MqttCallbackProcessor extends AbstractMessageProcessor {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    private final Object lock = new Object();

    @Override
    protected void process(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        String msgID = message.split(" ")[0];

        // 检查是否被消费，避免重复消费
        Boolean success = redisTemplate.opsForValue().setIfAbsent(msgID, lock, 10, TimeUnit.SECONDS);
        if (success == null || !success) {
            log.info("A client has consume this message");
            return;
        }
        redisTemplate.delete(msgID);

        if (topic.startsWith("WEGW2/WriteOut/")) {
            publishEvent(new WriteReplyEvent(this).setMsgID(msgID).setReply(message));
        } else if (topic.startsWith("WEGW2/ReadOut/")) {
            publishEvent(new ReadReplyEvent(this).setMsgID(msgID).setReply(message));
        } else if (topic.startsWith("WEGW2/Login/")) {
            publishEvent(new LoginEvent(this).setMessage(message));
        } else if (topic.startsWith("WEGW2/DataReport/")) {
            String serialNum = topic.split("/")[2];
            publishEvent(new DataReportEvent(this).setMessage(serialNum + "," + message));
        } else if (topic.startsWith("WEGW2/HeartBeat/")) {
            String serialNum = topic.split("/")[2];
            publishEvent(new HeartbeatEvent(this).setMessage(serialNum));
        }
    }

    private void publishEvent(ApplicationEvent event) {
        if (event != null) {
            publisher.publishEvent(event);
        }
    }
}
