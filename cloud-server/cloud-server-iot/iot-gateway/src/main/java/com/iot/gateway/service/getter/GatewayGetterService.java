package com.iot.gateway.service.getter;

import com.cloud.api.model.GatewayInfo;
import com.iot.gateway.domain.register.CommonConf;
import com.iot.gateway.domain.register.MqttConf;
import com.iot.gateway.domain.register.Traffic;
import com.iot.gateway.domain.register.WifiConf;

import java.util.List;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/29 10:55
 */
public interface GatewayGetterService {

    List<GatewayInfo> getAllGateway();

    MqttConf getMqttConf(long gatewayId);

    CommonConf getCommonConf(long gatewayId);

    WifiConf getWifiConf(long gatewayId);

    Traffic getTraffic(long gatewayId);
}
