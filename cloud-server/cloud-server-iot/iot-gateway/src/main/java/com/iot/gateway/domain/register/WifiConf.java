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
public class WifiConf {
    /**
     * WIFI Mode
     */
    @BasicValueCheck
    private Integer mode;
    /**
     * 读取网络名称
     */
    @StringValueCheck
    private String name;
    /**
     * 读取加密标志
     */
    @BasicValueCheck
    private Integer encryption;
    /**
     * 读取网络密码
     */
    @StringValueCheck
    private String password;
    /**
     * 读取网络是否使能
     */
    @BasicValueCheck
    private Integer enable;
    /**
     * 最近更新时间
     */
    private Timestamp updateTime;
}
