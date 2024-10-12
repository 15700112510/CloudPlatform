package com.iot.lamp.mqtt.listener;

import com.cloud.api.clients.DeviceClient;
import com.cloud.common.redis.service.RedisService;
import com.iot.lamp.common.Constants;
import com.iot.lamp.domain.ClockConfig;
import com.iot.lamp.domain.DefaultConfig;
import com.iot.lamp.dto.AddLampAlarmDto;
import com.iot.lamp.dto.LampParamDto;
import com.iot.lamp.mapper.LampAlarmMapper;
import com.iot.lamp.mapper.LampLocationMapper;
import com.iot.lamp.mapper.LampMapper;
import com.iot.lamp.mapper.LampParamMapper;
import com.iot.lamp.mqtt.event.AbstractCallReplyEvent;
import com.iot.lamp.mqtt.event.AbstractMqttMsgCenter;
import com.iot.lamp.mqtt.event.AbstractMsgReceiveEvent;
import com.iot.lamp.mqtt.event.receive.HeartbeatReceiveEvent;
import com.iot.lamp.mqtt.event.receive.LoginReceiveEvent;
import com.iot.lamp.mqtt.event.receive.WarningReceiveEvent;
import com.iot.lamp.mqtt.event.reply.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * TODO: MQTT事件监听及消费中心
 *
 * @author likain
 * @since 2023/6/10 17:25
 */
@SuppressWarnings("all")
@Slf4j
@Component
public class MqttMsgEventListener implements ApplicationListener<AbstractMqttMsgCenter> {

    @Autowired
    private RedisService redisService;
    @Autowired
    private LampMapper lampMapper;
    @Autowired
    private LampLocationMapper lampLocationMapper;
    @Autowired
    private LampParamMapper lampParamMapper;
    @Autowired
    private LampAlarmMapper lampAlarmMapper;
    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private DeviceClient deviceClient;

    @Override
    public void onApplicationEvent(AbstractMqttMsgCenter event) {
        if (event instanceof AbstractMsgReceiveEvent) {
            AbstractMsgReceiveEvent msgReceiveEvent = (AbstractMsgReceiveEvent) event;
            try {
                dealMsgReceiveEvent(msgReceiveEvent);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        } else if (event instanceof AbstractCallReplyEvent) {
            AbstractCallReplyEvent callReplyEvent = (AbstractCallReplyEvent) event;
            dealCallReplyEvent(callReplyEvent);
        }
    }

    /**
     * 处理消息接受事件
     *
     * @param event AbstractMsgReceiveEvent
     */
    private void dealMsgReceiveEvent(AbstractMsgReceiveEvent event) throws MqttException {
        if (event instanceof HeartbeatReceiveEvent) {
            invokeHeartbeatEvent(event.getTopic());
        } else if (event instanceof LoginReceiveEvent) {
            invokeLoginEvent(event.getPayload());
        } else if (event instanceof WarningReceiveEvent) {
            invokeWarningEvent(event.getTopic(), event.getPayload());
        }
    }

    /**
     * 处理命令回复事件
     *
     * @param event AbstractCallReplyEvent
     */
    private void dealCallReplyEvent(AbstractCallReplyEvent event) {
        String payload = event.getPayload();
        if (event instanceof LightCtrlReplyEvent) {
            log.info("light ctrl reply...");
        } else if (event instanceof ElcDataReplyEvent) {
            invokeElcDataReplyEvent(payload);
        } else if (event instanceof AutoSendReplyEvent) {
            log.info("auto send reply...");
        } else if (event instanceof ClearElcReplyEvent) {
            log.info("clear elc reply...");
        } else if (event instanceof GnssDataReplyEvent) {
            invokeGnssDataReplyEvent(payload);
        } else if (event instanceof McuRstReplyEvent) {
            log.info("mcu rst reply...");
        } else if (event instanceof ClearWarningReplyEvent) {
            log.info("clear warning reply...");
        } else if (event instanceof DefaultRstReplyEvent) {
            invokeDefaultRstReplyEvent(event.getPayload());
        } else if (event instanceof AlarmSetReplyEvent) {
            invokeAlarmSetReplyEvent(event.getPayload());
        }
    }

    /**
     * 处理心跳事件
     */
    private void invokeHeartbeatEvent(String topic) {
        // 从心跳主题中获取此次心跳的单灯控制器卡号
        String cardNo = topic.split("/")[1];
        Long deviceId = lampMapper.getDeviceId(cardNo);
        // 更新心跳时间为当前毫秒数
        redisService.setCacheObject(String.valueOf(deviceId), System.currentTimeMillis());
    }

    /**
     * 处理登陆事件
     */
    private void invokeLoginEvent(String payload) throws MqttException {
        String cardNo = payload.split(",")[0];
        // 收到登陆消息后进行回复
        String replyTopic = Constants.PUB_TOPIC_PREFIX + cardNo;
        String replyPayload = cardNo + ",1111";
        MqttMessage mqttMessage = new MqttMessage(replyPayload.getBytes(StandardCharsets.UTF_8));
        mqttClient.publish(replyTopic, mqttMessage);

        // 调用远程设备服务上线设备
        Long deviceId = lampMapper.getDeviceId(cardNo);
        deviceClient.deviceLogin(deviceId);
    }

    /**
     * 处理报警事件
     */
    private void invokeWarningEvent(String topic, String payload) {
        String type;
        // 分类报警信息
        switch (topic.split("/")[2]) {
            case "PowerH":
                type = "功率过高";
                break;
            case "PowerL":
                type = "功率过低";
                break;
            case "PfL":
                type = "功率因数过低";
                break;
            default:
                type = "未知的警报项";
                break;
        }
        // 解析报警次数及数值
        String[] strings = payload.split(",");
        int num = Integer.parseInt(strings[1]);
        double value = Double.parseDouble(strings[2]) * 0.01;
        // 远程调用报警模块新增报警信息
        lampAlarmMapper.addAlarm(new AddLampAlarmDto(strings[0], type, String.valueOf(value), num));
    }

    /**
     * 处理ElcData事件
     */
    private void invokeElcDataReplyEvent(String payload) {
        String[] strings = payload.split(",");
        // 取出所有参数
        String cardNo = strings[0];
        int brightness = brightnessConvert(strings[1]);
        double voltage = Double.parseDouble(strings[2]) * 0.01;
        double current = Double.parseDouble(strings[3]) * 0.01;
        double power = Double.parseDouble(strings[4]) * 0.01;
        double electricity = Double.parseDouble(strings[5]) * 0.0001;
        double factory = Double.parseDouble(strings[6]) * 0.01;
        // 写入数据库
        lampParamMapper.addElcData(
                new LampParamDto(cardNo, brightness, current, voltage, power, factory, electricity, null));
    }

    /**
     * 处理GnssData事件
     */
    private void invokeGnssDataReplyEvent(String payload) {
        String[] strings = payload.split(",");
        // A代表此次经纬度信息有效，否则无效不存储
        if ("A".equals(strings[1])) {
            String cardNo = strings[0];
            Long lampId = lampMapper.getLampId(cardNo);

            if (lampId != null) {
                // 更新定位
                lampLocationMapper.updateLocation(lampId, strings[2], strings[4]);
            }
        }
    }

    /**
     * 处理DefaultRst回复
     */
    private void invokeDefaultRstReplyEvent(String payload) {
        String[] strings = payload.split(",");
        // TODO: 需记录操作者，应该采用等待读模式
        DefaultConfig config = new DefaultConfig(
                strings[0], strings[2], strings[3], strings[4], strings[5], strings[6], "83027453");
        // 更新失败，说明该卡号的灯暂无默认配置
        if (lampMapper.updateDefaultConfig(config) == 0) {
            // 执行插入默认配置
            lampMapper.addDefaultConfig(config);
        }
    }

    /**
     * 处理AlarmSet回复
     */
    private void invokeAlarmSetReplyEvent(String payload) {
        String[] strings = payload.split(",");
        // 遍历回复的时钟信息
        for (int i = 1; i < 5; i++) {
            String clockNo = "Alarm" + i;
            String clockTime = strings[i].substring(0, 4);
            String clockTask = strings[i].substring(4, 8);
            // 有效的配置信息检索
            if (!"ffffffff".equals(strings[i])) {
                // 判断是否已存在且为发生变化，发生变化时才更新
                if (!strings[i].substring(0, 8).equals(
                        lampMapper.getClockTime(clockNo) + lampMapper.getClockTask(clockNo))) {
                    ClockConfig config = new ClockConfig(strings[0], clockNo, clockTime, clockTask, "83027453");
                    // 更新失败，说明暂无该类型的时钟配置
                    if (lampMapper.updateClockConfig(config) == 0) {
                        // 执行插入时钟配置
                        lampMapper.addClockConfig(config);
                    }
                }
            }
        }
    }

    /**
     * 十六进制亮度转十进制
     *
     * @param hexBrightness 十六进制亮度数值
     * @return 十进制数值
     */
    private static int brightnessConvert(String hexBrightness) {
        // 前两位，开区间
        String str = hexBrightness.substring(0, 2);
        if ("00".equals(str)) {
            return 0;
        }
        // 后两位，开区间
        return Integer.parseInt(hexBrightness.substring(2, 4), 16);
    }
}
