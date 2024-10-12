package com.iot.lamp.controller;

import com.cloud.api.model.Lamp;
import com.cloud.common.redis.service.RedisService;
import com.iot.lamp.common.Constants;
import com.iot.lamp.domain.DefaultConfig;
import com.iot.lamp.mapper.LampLocationMapper;
import com.iot.lamp.mapper.LampMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 远程调用控制器
 *
 * @author likain
 */
@Slf4j
@RestController
@RequestMapping("/remote/lamp")
public class RemoteLampController {

    @Autowired
    private LampMapper lampMapper;
    @Autowired
    private LampLocationMapper lampLocationMapper;
    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private RedisService redisService;

    @GetMapping("/get/cardNo/count")
    public int getCardNoCount(@RequestParam String cardNo) {
        return lampMapper.getCardNoCount(cardNo);
    }

    @GetMapping("/get/total/num")
    public int getTotalNum() {
        return lampMapper.getTotalNum();
    }

    @PostMapping("/add")
    public int addLamp(@RequestBody Lamp lamp, @RequestParam("opsBy") String opsBy) {
        lampMapper.addLamp(lamp);
        redisService.deleteObject(Constants.ALL_CARD_NO_KEY);

        Long lampId = lampMapper.getLampIdByDeviceId(lamp.getDeviceId());
        lampLocationMapper.insertLocation(lampId);
        DefaultConfig defaultConfig = new DefaultConfig();
        defaultConfig.setCardNo(lamp.getCardNo());
        defaultConfig.setUserId(opsBy);
        lampMapper.addDefaultConfig(defaultConfig);
        return 1;
    }

    @PostMapping("/sub")
    public boolean subscribe(@RequestBody String cardNo) {
        for (String topicPrefix : Constants.TOPIC_PREFIX) {
            String topic = topicPrefix + cardNo;
            try {
                mqttClient.subscribe(topic);
            } catch (MqttException e) {
                log.error("订阅失败: [{}]", topic, e);
                return false;
            }
            log.info("已订阅: [{}]", topic);
        }
        return true;
    }
}
