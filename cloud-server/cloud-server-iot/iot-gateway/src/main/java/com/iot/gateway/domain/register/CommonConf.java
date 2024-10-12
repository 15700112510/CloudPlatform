package com.iot.gateway.domain.register;

import com.cloud.common.security.annotation.BasicValueCheck;
import com.cloud.common.security.annotation.StringValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonConf {
    /**
     * 读数据报上传的时间间隔
     */
    @BasicValueCheck
    private Integer reportInterval;
    /**
     * 读 DNS Server
     */
    @StringValueCheck
    private String dnsServer;
    /**
     * 读取定义设备与云平台连接丢失使用的超时时间
     */
    @BasicValueCheck
    private Integer timeout;
    /**
     * 读取系统时间
     */
    private String systemTime;
    /**
     * 读取软件版本
     */
    @StringValueCheck
    private String softwareVersion;
    /**
     * 最近更新时间
     */
    private Timestamp updateTime;
}
