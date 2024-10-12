package com.iot.lamp.mqtt.subscribe;

import com.iot.lamp.common.Constants;
import com.iot.lamp.mapper.LampMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/10 23:17
 */
@Slf4j
@Component
public class LampSubscriber implements ApplicationRunner {

    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private LampMapper lampMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 首先订阅初始通用主题
        for (String initTopic : Constants.INIT_TOPIC) {
            mqttClient.subscribe(initTopic);
            log.info("已订阅: [{}]", initTopic);
        }

        // 其次订阅带有卡号的主题
        for (String topicPrefix : Constants.TOPIC_PREFIX) {
            for (String cardNo : lampMapper.getAllCardNo()) {
                String topic = topicPrefix + cardNo;
                mqttClient.subscribe(topic);
                log.info("已订阅: [{}]", topic);
            }
        }
    }
}
