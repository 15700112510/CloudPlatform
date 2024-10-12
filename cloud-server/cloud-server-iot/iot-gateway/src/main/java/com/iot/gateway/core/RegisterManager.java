package com.iot.gateway.core;

import com.alibaba.fastjson2.JSONObject;
import com.cloud.api.clients.DeviceClient;
import com.cloud.api.model.GatewayInfo;
import com.cloud.common.redis.service.RedisService;
import com.iot.gateway.common.Constants;
import com.iot.gateway.domain.gateway.ElectricParam;
import com.iot.gateway.exceptions.GatewayMqttException;
import com.iot.gateway.exceptions.ReplyTimeoutException;
import com.iot.gateway.mapper.GatewayMapper;
import com.iot.gateway.mqtt.event.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/4 20:32
 */
@Component
public class RegisterManager implements ApplicationListener<MqttMsgEvent> {

    private final Logger logger = LoggerFactory.getLogger(RegisterManager.class);

    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private DeviceClient deviceClient;
    @Autowired
    private GatewayMapper gatewayMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 设置寄存器
     *
     * @param serialNum  网关设备ID
     * @param registerId 设置寄存器的ID
     * @param content    设置寄存器的内容
     */
    public void setRegister(String serialNum, String[] registerId, String[] content) {
        if (registerId.length != content.length) {
            throw new RuntimeException("Illegal array length");
        }

        String msgID = getRandomMessageID();
        Constants.MESSAGE_IDS.put(msgID, msgID);

        String topic = Constants.WRITE_REGISTER_TOPIC_PREFIX + serialNum;
        String cmd = formatWriteRegisterCmd(msgID, registerId, content);
        MqttMessage message = new MqttMessage(cmd.getBytes(StandardCharsets.UTF_8));
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            throw new GatewayMqttException(e);
        }

        // 同步等待设置寄存器的结果
        synchronized (Constants.MESSAGE_IDS.get(msgID)) {
            try {
                Constants.MESSAGE_IDS.get(msgID).wait(Constants.MAX_TIMEOUT_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException("unknown exception");
            }
        }

        // 判断是超时还是正常被唤醒
        if (Constants.MESSAGE_REPLY.get(msgID) != null) {
            Constants.MESSAGE_REPLY.remove(msgID);
            Constants.MESSAGE_IDS.remove(msgID);
        } else {
            throw new ReplyTimeoutException("Set register [" + Arrays.toString(registerId) + "] timeout!");
        }
    }

    /**
     * 拉取指定寄存器的内容
     *
     * @param serialNum  网关设备ID
     * @param registerId 读取的寄存器ID
     * @param number     读取的寄存器数量
     * @return 返回的寄存器内的值
     */
    public String fetchRegisterInfo(String serialNum, String registerId, int number) {
        String msgID = getRandomMessageID();
        Constants.MESSAGE_IDS.put(msgID, msgID);

        String topic = Constants.READ_REGISTER_TOPIC_PREFIX + serialNum;
        String cmd = formatReadRegisterCmd(msgID, registerId, number);
        MqttMessage message = new MqttMessage(cmd.getBytes(StandardCharsets.UTF_8));
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            throw new GatewayMqttException(e);
        }

        // 同步等待设置寄存器的结果
        synchronized (Constants.MESSAGE_IDS.get(msgID)) {
            try {
                Constants.MESSAGE_IDS.get(msgID).wait(Constants.MAX_TIMEOUT_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException("unknown exception");
            }
        }

        // 判断是超时还是正常被唤醒
        String reply = Constants.MESSAGE_REPLY.get(msgID);
        if (reply != null) {
            Constants.MESSAGE_REPLY.remove(msgID);
            Constants.MESSAGE_IDS.remove(msgID);
            return reply;
        } else {
            throw new ReplyTimeoutException("Fetch register info [" + registerId + "] timeout!");
        }
    }

    @Override
    public void onApplicationEvent(MqttMsgEvent event) {
        if (event instanceof AbstractReplyEvent) {
            AbstractReplyEvent replyEvent = (AbstractReplyEvent) event;
            String msgID = replyEvent.getMsgID();
            String reply = replyEvent.getReply();

            if (Constants.MESSAGE_IDS.get(msgID) != null) {
                synchronized (Constants.MESSAGE_IDS.get(msgID)) {
                    Constants.MESSAGE_REPLY.put(msgID, reply);
                    Constants.MESSAGE_IDS.get(msgID).notifyAll();
                }
            }
            return;
        }

        if (event instanceof AbstractSendEvent) {
            // 登陆事件
            if (event instanceof LoginEvent) {
                LoginEvent loginEvent = (LoginEvent) event;
                String message = loginEvent.getMessage();
                String[] msgArr = message.split(",");
                if (msgArr.length <= 1) {
                    logger.error("Payload error: [{}]", message);
                    return;
                }
                // 回复已收到注册登陆消息
                try {
                    mqttClient.publish("WEGW2/LoginOut/" + msgArr[0], new MqttMessage("1".getBytes()));
                } catch (MqttException e) {
                    throw new GatewayMqttException(e);
                }

                // 做登陆操作
                long deviceId = gatewayMapper.getDeviceId(msgArr[0]);
                // 远程调用device服务登陆网关设备
                deviceClient.deviceLogin(deviceId);
                // 更新心跳信息
                redisService.setCacheObject(String.valueOf(deviceId), System.currentTimeMillis());
                return;
            }

            // 心跳事件
            if (event instanceof HeartbeatEvent) {
                HeartbeatEvent heartbeatEvent = (HeartbeatEvent) event;
                String serialNum = heartbeatEvent.getMessage();
                // 更新心跳信息
                long deviceId = gatewayMapper.getDeviceId(serialNum);
                redisService.setCacheObject(String.valueOf(deviceId), System.currentTimeMillis());
            }

            // 实时数据报事件
            if (event instanceof DataReportEvent) {
                DataReportEvent dataReportEvent = (DataReportEvent) event;
                String message = dataReportEvent.getMessage();
                String[] msgArr = message.split(",");
                if (msgArr.length <= 1) {
                    logger.error("Payload error: [{}]", message);
                    return;
                }

                switch (msgArr[0]) {
                    case "01":  // RealTime Report 实时数据报
                        String serialNum = msgArr[0];
                        long id = gatewayMapper.getGatewayIdBySerialNum(serialNum);
                        char moduleSignal = msgArr[7].toCharArray()[1];
                        char moduleStatus = msgArr[8].toCharArray()[1];
                        gatewayMapper.updateGatewayInfo(id, moduleSignal, moduleStatus);
                        break;
                    case "03":  // Power Data Report 电力数据报格式
                        ElectricParam param = parseElectricParam(message);
                        gatewayMapper.insertElectricParam(param);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 格式化设置寄存器的命令
     *
     * @param msgId      唯一消息ID
     * @param registerId 设置寄存器的ID
     * @param content    设置寄存器的内容
     * @return 格式化的设置命令
     */
    private String formatWriteRegisterCmd(String msgId, String[] registerId, String[] content) {
        JSONObject obj = new JSONObject();
        for (int i = 0; i < registerId.length; i++) {
            obj.put(registerId[i], content[i]);
        }
        return msgId + " 01 01 " + obj;
    }

    /**
     * 格式化读取寄存器的命令
     *
     * @param msgId      唯一消息ID
     * @param registerId 读取寄存器的ID
     * @param number     读取寄存器的数量
     * @return 格式化的读取命令
     */
    private String formatReadRegisterCmd(String msgId, String registerId, int number) {
        if (number > 99) {
            throw new RuntimeException("Illegal register number!");
        }
        return msgId + " 01 00 " + registerId + " " + (number > 9 ? number : "0" + number);
    }

    /**
     * 获取8位随机消息ID
     *
     * @return 8位随机消息ID
     */
    private String getRandomMessageID() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 解析 GatewayInfo
     *
     * @param message 消息体
     * @return GatewayInfo对象
     */
    private GatewayInfo parseGatewayInfo(String message) {
        String[] msgArr = message.split(",");
        return new GatewayInfo()
                .setSerialNum(msgArr[0])
                .setSignalType(msgArr[4].toCharArray()[0])
                .setModuleStatus(msgArr[5].toCharArray()[0])
                .setProtocolVersion(msgArr[7])
                .setSoftwareVersion(msgArr[8])
                .setHardwareVersion(msgArr[9]);
    }

    /**
     * 解析 ElectricParam
     *
     * @param message 消息体
     * @return ElectricParam对象
     */
    private ElectricParam parseElectricParam(String message) {
        String[] msgArr = message.split(",");
        return new ElectricParam()
                .setGatewayId(244L)
                .setSystemTime(msgArr[1])
                .setStatus(msgArr[2].toCharArray()[0])
                .setGridVoltage(msgArr[3])
                .setGridCurrent(msgArr[4])
                .setGridPower(msgArr[5])
                .setGridFrequency(msgArr[6])
                .setGridElectricity(msgArr[7])
                .setAStatus(msgArr[8].toCharArray()[0])
                .setAVoltage(msgArr[9])
                .setACurrent(msgArr[10])
                .setBStatus(msgArr[11].toCharArray()[0])
                .setBVoltage(msgArr[12])
                .setBCurrent(msgArr[13])
                .setCStatus(msgArr[14].toCharArray()[0])
                .setCVoltage(msgArr[15])
                .setCCurrent(msgArr[16])
                .setAlternatingOneStatus(msgArr[17].toCharArray()[0])
                .setAlternatingTwoStatus(msgArr[18].toCharArray()[0])
                .setAlternatingThreeStatus(msgArr[19].toCharArray()[0])
                .setDirectOneStatus(msgArr[20].toCharArray()[0])
                .setDirectTwoStatus(msgArr[21].toCharArray()[0])
                .setDirectThreeStatus(msgArr[22].toCharArray()[0]);
    }
}
