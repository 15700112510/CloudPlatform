package com.cloud.api.model;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/30 15:04
 */
public class GatewayInfo {
    /**
     * 主键Id
     */
    private Long gatewayId;
    /**
     * 连表deviceId
     */
    private Long deviceId;
    /**
     * 序列号
     */
    private String serialNum;
    /**
     * 信号类型：0是有线，1是4G
     */
    private Character signalType;
    /**
     * 模块状态：0是正常，1是异常
     */
    private Character moduleStatus;
    /**
     * 协议版本
     */
    private String protocolVersion;
    /**
     * 软件版本
     */
    private String softwareVersion;
    /**
     * 硬件版本
     */
    private String hardwareVersion;
    /**
     * 在线状态
     */
    private Character status;

    public Long getGatewayId() {
        return gatewayId;
    }

    public GatewayInfo setGatewayId(Long gatewayId) {
        this.gatewayId = gatewayId;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public GatewayInfo setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public GatewayInfo setSerialNum(String serialNum) {
        this.serialNum = serialNum;
        return this;
    }

    public Character getSignalType() {
        return signalType;
    }

    public GatewayInfo setSignalType(Character signalType) {
        this.signalType = signalType;
        return this;
    }

    public Character getModuleStatus() {
        return moduleStatus;
    }

    public GatewayInfo setModuleStatus(Character moduleStatus) {
        this.moduleStatus = moduleStatus;
        return this;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public GatewayInfo setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public GatewayInfo setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
        return this;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public GatewayInfo setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
        return this;
    }

    public Character getStatus() {
        return status;
    }

    public GatewayInfo setStatus(Character status) {
        this.status = status;
        return this;
    }
}
