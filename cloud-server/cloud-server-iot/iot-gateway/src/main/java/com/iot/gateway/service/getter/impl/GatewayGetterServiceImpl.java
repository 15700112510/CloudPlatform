package com.iot.gateway.service.getter.impl;

import com.cloud.api.model.GatewayInfo;
import com.iot.gateway.domain.register.CommonConf;
import com.iot.gateway.domain.register.MqttConf;
import com.iot.gateway.domain.register.Traffic;
import com.iot.gateway.domain.register.WifiConf;
import com.iot.gateway.mapper.GatewayMapper;
import com.iot.gateway.service.getter.GatewayGetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: 获取数据库数据
 *
 * @author likain
 * @since 2023/6/4 20:28
 */
@Service("impl")
public class GatewayGetterServiceImpl implements GatewayGetterService {

    @Autowired
    private GatewayMapper gatewayMapper;

    public List<GatewayInfo> getAllGateway() {
        return gatewayMapper.queryAllGateway();
    }

    public MqttConf getMqttConf(long gatewayId) {
        return gatewayMapper.queryMqttConf(gatewayId);
    }

    public CommonConf getCommonConf(long gatewayId) {
        return gatewayMapper.queryCommonConf(gatewayId);
    }

    public WifiConf getWifiConf(long gatewayId) {
        return gatewayMapper.queryWifiConf(gatewayId);
    }

    public Traffic getTraffic(long gatewayId) {
        return gatewayMapper.queryTraffic(gatewayId);
    }
}
