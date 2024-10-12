package com.iot.gateway.common;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/10 13:37
 */
public class Constants {

    /**
     * 阻塞等待回复最大超时时间
     */
    public static final long MAX_TIMEOUT_MILLIS = 5000;
    /**
     * 最大允许的心跳间隔
     */
    public static final long MAX_HEARTBEAT_INTERVAL = 90000L;

    /**
     * 写入寄存器主题前缀
     */
    public static final String WRITE_REGISTER_TOPIC_PREFIX = "WEGW2/WriteIn/";
    /**
     * 读取寄存器主题前缀
     */
    public static final String READ_REGISTER_TOPIC_PREFIX = "WEGW2/ReadIn/";

    /**
     * 缓存网关设备id的redis键
     */
    public static final String ALL_DEVICE_ID_KEY = "all:deviceId:gateway";
    /**
     * 缓存网关id的redis键
     */
    public static final String ALL_GATEWAY_ID_KEY = "all:gatewayId";
    /**
     * 网关设备所需订阅的主题前缀
     */
    public static final String[] TOPIC_PREFIX = new String[]
            {"WEGW2/WriteOut/", "WEGW2/ReadOut/", "WEGW2/Login/", "WEGW2/DataReport/", "WEGW2/HeartBeat/"};

    /**
     * 用于消息准确回复的消息id
     */
    public static final Map<String, Object> MESSAGE_IDS = new HashMap<>(1024);
    /**
     * 准确回复的消息体
     */
    public static final Map<String, String> MESSAGE_REPLY = new HashMap<>(1024);
}
