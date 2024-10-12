package com.iot.gateway.service.setter;

import com.iot.gateway.domain.register.CommonConf;
import com.iot.gateway.domain.register.MqttConf;
import com.iot.gateway.domain.register.WifiConf;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/5 17:01
 */
public interface GatewaySetterService {

    void setMqttConf(long gatewayId, MqttConf conf);

    void setCommonConf(long gatewayId, CommonConf conf);

    void setSubscribe(long gatewayId, String sub);

    void setReboot(long gatewayId);

    void setWifiConf(long gatewayId, WifiConf conf);
}
