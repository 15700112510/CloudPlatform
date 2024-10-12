package com.iot.gateway.service.setter.impl;

import com.cloud.common.security.exception.IllegalParamException;
import com.iot.gateway.core.RegisterManager;
import com.iot.gateway.domain.register.CommonConf;
import com.iot.gateway.domain.register.MqttConf;
import com.iot.gateway.domain.register.WifiConf;
import com.iot.gateway.mapper.GatewayMapper;
import com.iot.gateway.service.setter.GatewaySetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/5 17:08
 */
@Service("setter")
public class GatewaySetterServiceImpl implements GatewaySetterService {

    @Autowired
    private RegisterManager registerManager;
    @Autowired
    private GatewayMapper gatewayMapper;

    /**
     * 设置MQTT配置
     *
     * @param gatewayId 网关id
     * @param conf      配置实体类对象
     */
    @Override
    public void setMqttConf(long gatewayId, MqttConf conf) {
        String serialNum = gatewayMapper.getSerialNum(gatewayId);
        paramCheck(serialNum);
        registerManager.setRegister(serialNum, new String[]{"A0", "A1", "A2", "A3", "A4"},
                new String[]{conf.getHostname(), String.valueOf(conf.getPort()),
                        conf.getUsername(), conf.getPassword(), String.valueOf(conf.getKeepalive())});
    }

    /**
     * 设置Common配置
     *
     * @param gatewayId 网关id
     * @param conf      配置实体类对象
     */
    @Override
    public void setCommonConf(long gatewayId, CommonConf conf) {
        String serialNum = gatewayMapper.getSerialNum(gatewayId);
        paramCheck(serialNum);
        registerManager.setRegister(serialNum, new String[]{"A5", "A6", "A7", "AA"},
                new String[]{String.valueOf(conf.getReportInterval()),
                        conf.getDnsServer(), String.valueOf(conf.getTimeout()), conf.getSoftwareVersion()});
    }

    /**
     * 设置网关订阅的主题
     *
     * @param gatewayId 网关id
     * @param sub       订阅的主题
     */
    @Override
    public void setSubscribe(long gatewayId, String sub) {
        String serialNum = gatewayMapper.getSerialNum(gatewayId);
        paramCheck(serialNum);
        registerManager.setRegister(serialNum, new String[]{"A9"}, new String[]{sub});
    }

    /**
     * 网关重启
     *
     * @param gatewayId 网关id
     */
    @Override
    public void setReboot(long gatewayId) {
        String serialNum = gatewayMapper.getSerialNum(gatewayId);
        paramCheck(serialNum);
        registerManager.setRegister(serialNum, new String[]{"AB"}, new String[]{"1"});
    }

    /**
     * 设置Wifi配置
     *
     * @param gatewayId 数据库主键id
     * @param conf      配置实体类对象
     */
    @Override
    public void setWifiConf(long gatewayId, WifiConf conf) {
        String serialNum = gatewayMapper.getSerialNum(gatewayId);
        paramCheck(serialNum);
        registerManager.setRegister(serialNum, new String[]{"B0", "B1", "B2", "B3", "B4"},
                new String[]{String.valueOf(conf.getMode()), conf.getName(),
                        String.valueOf(conf.getEncryption()), conf.getPassword(), String.valueOf(conf.getEnable())});
    }

    /**
     * 检查参数是否非法
     *
     * @param obj 待检测的参数
     */
    private void paramCheck(Object obj) {
        if (obj == null) {
            throw new IllegalParamException();
        }
    }
}
