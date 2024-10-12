package com.iot.gateway.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.iot.gateway.domain.register.CommonConf;
import com.iot.gateway.domain.register.MqttConf;
import com.iot.gateway.domain.register.Traffic;
import com.iot.gateway.domain.register.WifiConf;
import com.iot.gateway.exceptions.MessageFormatException;
import com.iot.gateway.exceptions.MessageParseException;
import com.iot.gateway.exceptions.UnknownClassException;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/4 21:03
 */
public interface RegisterEntityParser {

    /**
     * 从整个数据报中取出消息体
     *
     * @param info 原数据报
     * @return 去掉头部的消息体
     */
    default String getBody(String info) {
        String start = info.split("\n")[0];
        if (start.length() == info.length()) {
            throw new MessageFormatException("An error message arrived, for it has no head.");
        }
        return info.substring(start.length() + 1);
    }

    /**
     * 从字符串中解析出实体类对象
     *
     * @param body  消息体JSON字符串
     * @param clazz 解析的类型
     * @return 实体类对象
     */
    default <T> T parseEntity(String body, Class<T> clazz) {
        try {
            JSONObject obj = JSON.parseObject(body);
            if (clazz.equals(MqttConf.class)) {
                return clazz.cast(new MqttConf(obj.getString("A0"),
                        Integer.parseInt(obj.getString("A1")), obj.getString("A2"),
                        obj.getString("A3"), Integer.parseInt(obj.getString("A4")), null));
            } else if (clazz.equals(CommonConf.class)) {
                return clazz.cast(new CommonConf(Integer.parseInt(obj.getString("A5")),
                        obj.getString("A6"), Integer.parseInt(obj.getString("A7")),
                        obj.getString("A8"), obj.getString("AA"), null));
            } else if (clazz.equals(WifiConf.class)) {
                return clazz.cast(new WifiConf(Integer.parseInt(obj.getString("B0")),
                        obj.getString("B1"), Integer.parseInt(obj.getString("B2")),
                        obj.getString("B3"), Integer.parseInt(obj.getString("B4")), null));
            } else if (clazz.equals(Traffic.class)) {
                return clazz.cast(new Traffic(obj.getString("B5"), obj.getString("B6"),
                        obj.getString("B7"), obj.getString("B8"), null));
            }
        } catch (Exception e) {
            throw new MessageParseException("Message error", e);
        }

        throw new UnknownClassException("Cannot parse class: " + clazz.getSimpleName());
    }
}
