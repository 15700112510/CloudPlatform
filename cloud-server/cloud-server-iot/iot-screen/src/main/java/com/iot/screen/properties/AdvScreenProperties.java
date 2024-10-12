package com.iot.screen.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AdvScreenProperties {
    /**
     * 第三方平台url地址
     */
    @Value("${screen.apiUrl}")
    private String apiUrl;

    /**
     * 第三方平台密钥
     */
    @Value("${screen.appKey}")
    private String appKey;

    /**
     * 广告屏编号，可数组
     */
    @Value("${screen.screens}")
    private String screens;
}
