package com.iot.gateway.mapper;

import com.cloud.api.model.GatewayInfo;
import com.iot.gateway.domain.gateway.ElectricParam;
import com.iot.gateway.domain.register.CommonConf;
import com.iot.gateway.domain.register.MqttConf;
import com.iot.gateway.domain.register.Traffic;
import com.iot.gateway.domain.register.WifiConf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/30 16:26
 */
@Mapper
public interface GatewayMapper {

    int getGatewayNum(String serialNum);

    int insertGatewayInfo(GatewayInfo info);

    long getDeviceId(String serialNum);

    long getGatewayIdBySerialNum(String serialNum);

    long getGatewayId(long deviceId);

    String getSerialNum(long gatewayId);

    /**
     * 获取所有网关设备
     */
    List<GatewayInfo> queryAllGateway();

    /**
     * 插入电能信息
     */
    int insertElectricParam(ElectricParam param);

    /**
     * 获取所有网关设备序列号
     */
    List<String> getAllSerialNum();

    /**
     * 获取所有网关设备设备号
     */
    List<Long> getAllDeviceId();

    /**
     * 获取所有网关id
     */
    List<Long> getAllGatewayId();

    /**
     * 获取所有在线网关id
     */
    List<Long> getAllOnlineGatewayId();

    /**
     * 根据serial_num查询gateway_id
     */
    long queryGatewayId(String serialNum);

    /**
     * 更新一条MQTT配置信息
     */
    int updateMqttConf(@Param("id") long gatewayId, @Param("conf") MqttConf conf);

    /**
     * 插入一条初始化MQTT配置信息
     */
    int insertMqttConf(long gatewayId);

    /**
     * 更新一条Common配置信息
     */
    int updateCommonConf(@Param("id") long gatewayId, @Param("conf") CommonConf conf);

    /**
     * 插入一条初始化Common配置信息
     */
    int insertCommonConf(long gatewayId);

    /**
     * 更新一条Wifi配置信息
     */
    int updateWifiConf(@Param("id") long gatewayId, @Param("conf") WifiConf conf);

    /**
     * 插入一条初始化Wifi配置信息
     */
    int insertWifiConf(long gatewayId);

    /**
     * 查看某网关中流量表最近一条时间
     */
    Timestamp getRecentTime(long gatewayId);

    /**
     * 更新一条Traffic信息
     */
    int updateTraffic(@Param("id") long gatewayId, @Param("recent") Timestamp recentTime, @Param("traffic") Traffic traffic);

    /**
     * 新增一条Traffic信息
     */
    int insertTraffic(@Param("id") long gatewayId, @Param("traffic") Traffic traffic);

    /**
     * 插入一条初始化Traffic信息
     */
    int insertInitTraffic(long gatewayId);

    /**
     * 获取某网关的MQTT配置
     */
    MqttConf queryMqttConf(long gatewayId);

    /**
     * 获取某网关的Common配置
     */
    CommonConf queryCommonConf(long gatewayId);

    /**
     * 获取某网关的Wifi配置
     */
    WifiConf queryWifiConf(long gatewayId);

    /**
     * 获取某网关的当天流量信息
     */
    Traffic queryTraffic(long gatewayId);

    /**
     * 更新网关信息
     */
    int updateGatewayInfo(long gatewayId, char signalType, char moduleStatus);
}
