package com.iot.gateway.service.getter.impl.v2;

import com.cloud.api.model.GatewayInfo;
import com.iot.gateway.core.RegisterEntityParser;
import com.iot.gateway.core.RegisterManager;
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
 * TODO: 获取强实时性数据
 *
 * @author likain
 * @since 2023/5/29 10:56
 */
@Service("implV2")
public class GatewayGetterServiceImplV2 implements GatewayGetterService, RegisterEntityParser {

    @Autowired
    private RegisterManager registerManager;
    @Autowired
    private GatewayMapper gatewayMapper;

    @Override
    public List<GatewayInfo> getAllGateway() {
        return gatewayMapper.queryAllGateway();
    }

    @Override
    public MqttConf getMqttConf(long gatewayId) {
        String serialNum = gatewayMapper.getSerialNum(gatewayId);
        if (serialNum == null || serialNum.length() == 0) {
            throw new RuntimeException("gatewayId " + gatewayId + "not exist");
        }
        String info = registerManager.fetchRegisterInfo(serialNum, "A0", 5);
        return parseEntity(getBody(info), MqttConf.class);
    }

    @Override
    public CommonConf getCommonConf(long gatewayId) {
        String serialNum = gatewayMapper.getSerialNum(gatewayId);
        if (serialNum == null || serialNum.length() == 0) {
            throw new RuntimeException("gatewayId " + gatewayId + "not exist");
        }
        String info = registerManager.fetchRegisterInfo(serialNum, "A5", 6);
        return parseEntity(getBody(info), CommonConf.class);
    }

    @Override
    public WifiConf getWifiConf(long gatewayId) {
        String serialNum = gatewayMapper.getSerialNum(gatewayId);
        if (serialNum == null || serialNum.length() == 0) {
            throw new RuntimeException("gatewayId " + gatewayId + "not exist");
        }
        String info = registerManager.fetchRegisterInfo(serialNum, "B0", 5);
        return parseEntity(getBody(info), WifiConf.class);
    }

    @Override
    public Traffic getTraffic(long gatewayId) {
        String serialNum = gatewayMapper.getSerialNum(gatewayId);
        if (serialNum == null || serialNum.length() == 0) {
            throw new RuntimeException("gatewayId " + gatewayId + "not exist");
        }
        String info = registerManager.fetchRegisterInfo(serialNum, "B5", 4);
        return parseEntity(getBody(info), Traffic.class);
    }
}
