package com.iot.lamp.service.impl;

import com.cloud.common.core.R;
import com.cloud.common.core.enums.ErrorEnum;
import com.cloud.common.core.utils.StringUtil;
import com.iot.lamp.domain.ClockConfig;
import com.iot.lamp.domain.DefaultConfig;
import com.iot.lamp.dto.BrightnessControlDto;
import com.iot.lamp.dto.ControlDto;
import com.iot.lamp.service.LampService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/31 09:41
 */
@Service
public class LampServiceImpl implements LampService {

    private final Logger logger = LoggerFactory.getLogger(LampServiceImpl.class);
    private static final String DEFAULT_GROUP_NO = "YZDX0001";
    private static final String BROADCAST = "BROADCAST";
    @Autowired
    private MqttClient mqttClient;

    @Override
    public R<?> brightnessControl(BrightnessControlDto dto) {
        // 取出参数
        Integer brightness = dto.getBrightness();    // 亮度
        Character flag = dto.getFlag();    // 开关：0开1关
        Character type = dto.getType();    // 控制类型：0单灯1组播
        String cardNo = dto.getCardNo();    // 单灯卡号
        String groupNo = dto.getGroupNo();    // 组播群号
        // 单灯时卡号为空
        if (type == '0' && StringUtil.isNullOrEmpty(cardNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_CARD_NO);
        }
        // 组播时群号为空
        if (type == '1' && StringUtil.isNullOrEmpty(groupNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_GROUP_NO);
        }
        // 亮度不符合规则
        if (brightness < 0 || brightness > 100) {
            return R.fail(ErrorEnum.NOT_MATCH_RULES_BRIGHTNESS);
        }
        // 将亮度转为十六进制
        String hexBrightness = Integer.toHexString(brightness < 15 ? 15 : brightness);
        // 若十六进制数不足两位则补0
        hexBrightness = hexBrightness.length() == 1 ? "0" + hexBrightness : hexBrightness;
        // payload and topic
        String topic = String.format("%s/%s", type == '0' ? DEFAULT_GROUP_NO : groupNo, type == '0' ? cardNo : BROADCAST);
        String payload = String.format("LightCtrl,%s%s", flag == '0' ? "01" : "00", hexBrightness);
        // 向主题推送数据
        try {
            mqttClient.publish(topic, new MqttMessage(payload.getBytes(StandardCharsets.UTF_8)));
            // 发布成功，返回ok
            return R.ok();
        } catch (Exception e) {
            // 出现异常发布失败，返回发布失败消息
            logger.error("消息发往MQTT服务器失败");
            return R.fail(ErrorEnum.FAIL_PUBLISH);
        }
    }

    @Override
    public R<?> restartControl(ControlDto dto) {
        // 取出参数
        Character type = dto.getType();    // 控制类型：0单播1组播
        String cardNo = dto.getCardNo();    // 单灯卡号
        String groupNo = dto.getGroupNo();    // 组播群号
        // 单灯时卡号为空
        if (type == '0' && StringUtil.isNullOrEmpty(cardNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_CARD_NO);
        }
        // 组播时群号为空
        if (type == '1' && StringUtil.isNullOrEmpty(groupNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_GROUP_NO);
        }
        // payload and topic
        String topic = String.format("%s/%s", type == '0' ? DEFAULT_GROUP_NO : groupNo, type == '0' ? cardNo : BROADCAST);
        String payload = String.format("McuRst,%s%s", "0", "1");
        // 发布控制消息
        try {
            mqttClient.publish(topic, new MqttMessage(payload.getBytes(StandardCharsets.UTF_8)));
            // 发布成功，返回ok
            return R.ok();
        } catch (Exception e) {
            // 出现异常发布失败，返回发布失败消息
            logger.error("消息发往MQTT服务器失败");
            return R.fail(ErrorEnum.FAIL_PUBLISH);
        }
    }

    @Override
    public R<?> defaultConfig(DefaultConfig config) {
        // 卡号为空直接返回错误信息
        String cardNo = config.getCardNo();
        if (StringUtil.isNullOrEmpty(cardNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_CARD_NO);
        }
        // 其余项为空则用系统默认作为替代
        String autoSend = StringUtil.isNullOrEmpty(config.getAutoSend()) ? "0102" : config.getAutoSend();
        String alive = StringUtil.isNullOrEmpty(config.getAlive()) ? "0103" : config.getAlive();
        String selfCheck = StringUtil.isNullOrEmpty(config.getSelfCheck()) ? "0101" : config.getSelfCheck();
        String locate = StringUtil.isNullOrEmpty(config.getLocate()) ? "0106" : config.getLocate();
        String rtc = StringUtil.isNullOrEmpty(config.getRtc()) ? "0124" : config.getRtc();
        String userId = config.getUserId();
        // 生成主题和消息体
        String topic = String.format("%s/%s", "config", cardNo);
        String payload = String.format("%s,%s,%s,%s,%s,%s,%s,%s", "DefaultRst", DEFAULT_GROUP_NO, autoSend, alive, selfCheck, locate, rtc, userId);
        // 发布控制消息
        try {
            mqttClient.publish(topic, new MqttMessage(payload.getBytes(StandardCharsets.UTF_8)));
            // 发布成功，返回ok
            return R.ok();
        } catch (Exception e) {
            // 出现异常发布失败，返回发布失败消息
            logger.error("消息发往MQTT服务器失败");
            e.printStackTrace();
            return R.fail(ErrorEnum.FAIL_PUBLISH);
        }
    }

    @Override
    public R<?> clockConfig(ClockConfig config) {
        // 卡号为空直接返回错误信息
        String cardNo = config.getCardNo();
        if (StringUtil.isNullOrEmpty(cardNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_CARD_NO);
        }
        // 闹钟类型为空直接返回错误信息
        String clockNo = config.getClockNo();
        String clockTime = config.getClockTime();
        String clockTask = config.getClockTask();
        String userId = config.getUserId();
        // 判断闹钟配置参数是否有为空
        if (StringArrayExistNullOrEmpty(clockNo, clockTime, clockTask)) {
            return R.fail(ErrorEnum.IS_EMPTY_CLOCK_CONFIG);
        }
        // 生成主题和消息体
        String topic = String.format("%s/%s", "config", cardNo);
        String payload = String.format("%s,%s,%s,%s,%s", "SetAlarm", clockNo, clockTime, clockTask, userId);
        // 发布控制消息
        try {
            mqttClient.publish(topic, new MqttMessage(payload.getBytes(StandardCharsets.UTF_8)));
            // 发布成功，返回ok
            return R.ok();
        } catch (Exception e) {
            // 出现异常发布失败，返回发布失败消息
            logger.error("消息发往MQTT服务器失败");
            return R.fail(ErrorEnum.FAIL_PUBLISH);
        }
    }

    /**
     * 判断多个字符串中是否存在空字符串
     *
     * @param strings 多个字符串
     * @return 只要存在一个空字符串则返回true，否则为false
     */
    private boolean StringArrayExistNullOrEmpty(String... strings) {
        for (String str : strings) {
            if (StringUtil.isNullOrEmpty(str)) {
                return true;
            }
        }
        return false;
    }
}
