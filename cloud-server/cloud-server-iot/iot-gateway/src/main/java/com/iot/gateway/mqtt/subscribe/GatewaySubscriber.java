package com.iot.gateway.mqtt.subscribe;

import com.iot.gateway.common.Constants;
import com.iot.gateway.mapper.GatewayMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.yzu.cloud.common.TopicProperties;

import java.util.List;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/9 16:35
 */
@Slf4j
@Component
public class GatewaySubscriber implements ApplicationRunner {

    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private TopicProperties topicProperties;
    @Autowired
    private GatewayMapper gatewayMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 从数据库中拉取所有网关设备
        List<String> allSerialNum = gatewayMapper.getAllSerialNum();

        // 订阅相应的主题
        for (String serialNum : allSerialNum) {
            invokeSubscribe(serialNum);
        }
    }

    public void invokeSubscribe(String serialNum) throws MqttException {
        for (String prefix : Constants.TOPIC_PREFIX) {
            String topic = prefix + serialNum;
            mqttClient.subscribe(topic);
            topicProperties.addTopic(topic);
            log.info("已订阅主题: [{}]", topic);
        }
    }
}
