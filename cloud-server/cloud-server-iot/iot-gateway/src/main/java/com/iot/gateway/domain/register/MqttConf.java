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
public class MqttConf {
    /**
     * 服务器名称
     */
    @StringValueCheck
    private String hostname;
    /**
     * 服务器端口号
     */
    @BasicValueCheck
    private Integer port;
    /**
     * 服务器用户名
     */
    @StringValueCheck
    private String username;
    /**
     * 服务器密码
     */
    @StringValueCheck
    private String password;
    /**
     * MQTT心跳
     */
    @BasicValueCheck
    private Integer keepalive;
    /**
     * 最近更新时间
     */
    private Timestamp updateTime;
}
