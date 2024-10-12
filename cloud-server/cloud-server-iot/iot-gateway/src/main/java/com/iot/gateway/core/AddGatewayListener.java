package com.iot.gateway.core;

import com.cloud.api.model.GatewayInfo;
import com.cloud.common.redis.service.RedisService;
import com.iot.gateway.mapper.GatewayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/9 19:43
 */
@Component
public class AddGatewayListener implements ApplicationListener<AddGatewayEvent> {

    @Autowired
    private GatewayMapper gatewayMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public void onApplicationEvent(AddGatewayEvent event) {
        GatewayInfo info = event.getInfo();
        gatewayMapper.insertGatewayInfo(info);

        // 初始化相关数据库表项
        Long deviceId = info.getDeviceId();
        long gatewayId = gatewayMapper.getGatewayId(deviceId);
        gatewayMapper.insertMqttConf(gatewayId);
        gatewayMapper.insertCommonConf(gatewayId);
        gatewayMapper.insertWifiConf(gatewayId);
        gatewayMapper.insertInitTraffic(gatewayId);

        // 添加心跳信息
        redisService.setCacheObject(String.valueOf(deviceId), System.currentTimeMillis());
    }
}
