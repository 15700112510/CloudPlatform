package com.iot.gateway.job;

import com.cloud.common.redis.service.RedisService;
import com.iot.gateway.common.Constants;
import com.iot.gateway.domain.register.CommonConf;
import com.iot.gateway.domain.register.MqttConf;
import com.iot.gateway.domain.register.Traffic;
import com.iot.gateway.domain.register.WifiConf;
import com.iot.gateway.mapper.GatewayMapper;
import com.iot.gateway.service.getter.GatewayGetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/3 15:56
 */
@Component
public class DataPuller {

    @Autowired
    private GatewayGetterService implV2;
    @Autowired
    private GatewayMapper gatewayMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 每60s拉取一次MQTT配置信息并入库
     * A0～A4寄存器
     */
    @Scheduled(initialDelay = 3000L, fixedRate = 60000L)
    public void pullMqttConf() {
        List<Long> onlineGatewayId = getAllOnlineGatewayId();
        for (Long gatewayId : onlineGatewayId) {
            MqttConf conf = implV2.getMqttConf(gatewayId);
            gatewayMapper.updateMqttConf(gatewayId, conf);
        }
    }

    /**
     * 每60s拉取一次Common配置信息并入库
     * A5～A9寄存器
     */
    @Scheduled(initialDelay = 3000L, fixedRate = 60000L)
    public void pullCommonConf() {
        List<Long> onlineGatewayId = getAllOnlineGatewayId();
        for (Long gatewayId : onlineGatewayId) {
            CommonConf conf = implV2.getCommonConf(gatewayId);
            gatewayMapper.updateCommonConf(gatewayId, conf);
        }
    }

    /**
     * 每60s拉取一次Wifi配置信息并入库
     * B0～B4寄存器
     */
    @Scheduled(initialDelay = 3000L, fixedRate = 60000L)
    public void pullWifiConf() {
        List<Long> onlineGatewayId = getAllOnlineGatewayId();
        for (Long gatewayId : onlineGatewayId) {
            WifiConf conf = implV2.getWifiConf(gatewayId);
            gatewayMapper.updateWifiConf(gatewayId, conf);
        }
    }

    /**
     * 每30s拉取一次Traffic信息并入库
     * B5～B8寄存器
     */
    @Scheduled(initialDelay = 3000L, fixedRate = 30000L)
    public void pullTraffic() {
        List<Long> onlineGatewayId = getAllOnlineGatewayId();
        for (Long gatewayId : onlineGatewayId) {
            Traffic traffic = implV2.getTraffic(gatewayId);
            Timestamp recentTime = gatewayMapper.getRecentTime(gatewayId);

            Calendar recent = Calendar.getInstance();
            Calendar now = Calendar.getInstance();

            recent.setTimeInMillis(recentTime.getTime());
            now.setTimeInMillis(System.currentTimeMillis());
            if (recent.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
                // 若是同一天，则仅进行更新
                gatewayMapper.updateTraffic(gatewayId, recentTime, traffic);
            } else {
                // 若到了第二天，则新增一条
                gatewayMapper.insertTraffic(gatewayId, traffic);
            }
        }
    }

    private List<Long> getAllOnlineGatewayId() {
        List<Long> gatewayIds;
        if (redisService.hasKey(Constants.ALL_GATEWAY_ID_KEY)) {
            gatewayIds = redisService.getCacheListAll(Constants.ALL_GATEWAY_ID_KEY);
        } else {
            gatewayIds = gatewayMapper.getAllOnlineGatewayId();

            if (gatewayIds == null) {
                gatewayIds = new ArrayList<>();
                return gatewayIds;
            }

            redisService.setCacheList(Constants.ALL_GATEWAY_ID_KEY, gatewayIds);
            redisService.expire(Constants.ALL_GATEWAY_ID_KEY, 10, TimeUnit.MINUTES);
        }
        return gatewayIds;
    }
}
