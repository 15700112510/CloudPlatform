package com.iot.lamp.common;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/10 16:40
 */
public class Constants {

    /**
     * 缓存单灯控制器设备id的redis键
     */
    public static final String ALL_DEVICE_ID_KEY = "all:deviceId:lamp";
    /**
     * 缓存单灯控制器id的redis键
     */
    public static final String ALL_LAMP_ID_KEY = "all:lampId";
    /**
     * 缓存单灯控制器卡号的redis键
     */
    public static final String ALL_CARD_NO_KEY = "all:cardNo";
    /**
     * 缓存在线单灯控制器卡号的redis键
     */
    public static final String ALL_ONLINE_CARD_NO_KEY = "all:online:cardNo";

    /**
     * 单灯控制器设备所需订阅的主题前缀
     */
    public static final String[] TOPIC_PREFIX = new String[]{"Alive/"};
    /**
     * 服务启动时需订阅的主题
     */
    public static final String[] INIT_TOPIC = new String[]
            {"YZDX0001/Login", "YZDX0001/reply/#", "YZDX0001/warning/#", "config/reply/#"};
    /**
     * 发送消息主题前缀
     */
    public static final String PUB_TOPIC_PREFIX = "YZDX0001/";

    /**
     * 最大允许的心跳间隔
     */
    public static final long MAX_HEARTBEAT_INTERVAL = 60000L;
}
