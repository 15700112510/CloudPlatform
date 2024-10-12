package com.iot.lamp.job;

import com.cloud.api.clients.DeviceClient;
import com.cloud.common.redis.service.RedisService;
import com.iot.lamp.common.Constants;
import com.iot.lamp.mapper.LampMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO: 单灯控制器心跳探针
 *
 * @author likain
 * @since 2023/6/10 16:36
 */
@Component
public class HeartbeatProbe {

    @Autowired
    private RedisService redisService;
    @Autowired
    private LampMapper lampMapper;
    @Autowired
    private DeviceClient deviceClient;

    @Scheduled(initialDelay = 3000L, fixedRate = 10000L)
    public void probe() {
        List<Long> deviceIds;

        if (redisService.hasKey(Constants.ALL_DEVICE_ID_KEY)) {
            deviceIds = redisService.getCacheListAll(Constants.ALL_DEVICE_ID_KEY);
        } else {
            deviceIds = lampMapper.getAllDeviceId();
            if (deviceIds == null) {
                return;
            }
            redisService.setCacheList(Constants.ALL_DEVICE_ID_KEY, deviceIds);
            redisService.expire(Constants.ALL_DEVICE_ID_KEY, 10, TimeUnit.MINUTES);
        }

        for (Long deviceId : deviceIds) {
            Long ttl = redisService.getCacheObject(String.valueOf(deviceId));
            if (ttl == null) {
                return;
            }

            char status = deviceClient.deviceStatus(deviceId);
            if (System.currentTimeMillis() - ttl > Constants.MAX_HEARTBEAT_INTERVAL) {
                if (status == '0') {
                    deviceClient.deviceLogout(deviceId);
                }
            } else {
                if (status == '1') {
                    deviceClient.deviceLogin(deviceId);
                }
            }
        }
    }
}
