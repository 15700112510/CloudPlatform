package com.iot.device;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
        "com.cloud.common.redis", "com.cloud.common.security", "com.iot.device", "com.cloud.api.factory", "com.cloud.common.datasource.mapper"
})
@EnableFeignClients("com.cloud.api.clients")
public class DeviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class, args);
    }
}