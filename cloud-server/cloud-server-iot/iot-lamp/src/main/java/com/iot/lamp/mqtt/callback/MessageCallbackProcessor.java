package com.iot.lamp.mqtt.callback;

import com.cloud.common.redis.service.RedisService;
import com.iot.lamp.common.Constants;
import com.iot.lamp.mapper.LampMapper;
import com.iot.lamp.mqtt.event.AbstractMqttMsgCenter;
import com.iot.lamp.mqtt.event.receive.HeartbeatReceiveEvent;
import com.iot.lamp.mqtt.event.receive.LoginReceiveEvent;
import com.iot.lamp.mqtt.event.receive.WarningReceiveEvent;
import com.iot.lamp.mqtt.event.reply.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.yzu.cloud.core.AbstractMessageProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO: MQTT消息处理与事件发布中心
 *
 * @author likain
 * @since 2023/5/31 22:46
 */
@Slf4j
@Component
public class MessageCallbackProcessor extends AbstractMessageProcessor {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private RedisService redisService;
    @Autowired
    private LampMapper lampMapper;

    @Override
    protected void process(String topic, MqttMessage mqttMessage) throws Exception {
        String payload = new String(mqttMessage.getPayload());

        // TODO: 采用分布式锁避免消息重复消费

        // 首先校验消息来源是否在平台注册
        String cardNo = payload.split(",")[0];
        boolean validated = validateMessage(cardNo);
        if (!validated) {
            log.info("卡号 [{}] 的单灯控制器未在平台注册", cardNo);
            return;
        }

        // 消息事件派发
        AbstractMqttMsgCenter msgCenter = null;
        if (topic.startsWith("Alive")) {               // 心跳事件
            msgCenter = new HeartbeatReceiveEvent(this).setTopic(topic);
        } else if (topic.contains("Login")) {        // 上机事件
            msgCenter = new LoginReceiveEvent(this).setPayload(payload);
        } else if (topic.contains("warning")) {        // 报警事件
            msgCenter = new WarningReceiveEvent(this).setTopic(topic).setPayload(payload);
        } else if (topic.contains("LightCtrl")) {      // 亮度控制回复
            msgCenter = new LightCtrlReplyEvent(this);
        } else if (topic.contains("ElcData")) {        // 电能信息回复
            msgCenter = new ElcDataReplyEvent(this).setPayload(payload);
        } else if (topic.contains("AutoSend")) {       // 自动发送回复
            msgCenter = new AutoSendReplyEvent(this);
        } else if (topic.contains("ClearElc")) {       // 清除电量回复
            msgCenter = new ClearElcReplyEvent(this);
        } else if (topic.contains("GnssData")) {       // 定位信息回复
            msgCenter = new GnssDataReplyEvent(this).setPayload(payload);
        } else if (topic.contains("McuRst")) {         // 灯控器重启前回复
            msgCenter = new McuRstReplyEvent(this);
        } else if (topic.contains("ClearWarning")) {   // 报警清除回复
            msgCenter = new ClearWarningReplyEvent(this);
        } else if (topic.contains("DefaultRst")) {     // 默认配置设置回复
            msgCenter = new DefaultRstReplyEvent(this).setPayload(payload);
        } else if (topic.contains("AlarmSet")) {       // 时钟配置设置回复
            msgCenter = new AlarmSetReplyEvent(this).setPayload(payload);
        }

        if (msgCenter != null) {
            publisher.publishEvent(msgCenter);
        }
    }

    private boolean validateMessage(String cardNo) {
        // 从缓存中查询平台已注册的卡号
        List<String> allCardNo;

        if (redisService.hasKey(Constants.ALL_CARD_NO_KEY)) {
            allCardNo = redisService.getCacheListAll(Constants.ALL_CARD_NO_KEY);
        } else {
            // 为命中则从数据库查询
            allCardNo = lampMapper.getAllCardNo();
            if (allCardNo == null) {
                return false;
            }
            // 缓存到redis服务中
            redisService.setCacheList(Constants.ALL_CARD_NO_KEY, allCardNo);
            redisService.expire(Constants.ALL_CARD_NO_KEY, 60, TimeUnit.SECONDS);
        }

        return allCardNo.contains(cardNo);
    }
}
