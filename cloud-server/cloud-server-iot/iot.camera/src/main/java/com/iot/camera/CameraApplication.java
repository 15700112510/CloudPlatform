package com.iot.camera;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.cloud.common.datasource","com.iot.camera","com.cloud.common.redis", "com.cloud.common.security",
       "com.cloud.api.factory"},
        exclude = {DataSourceAutoConfiguration.class})
@MapperScan(
        {"com.cloud.common.datasource.mapper", "com.iot.camera.mapper" }
)
@EnableFeignClients("com.cloud.api.clients")
public class CameraApplication {
    public static void main(String[] args) {
        SpringApplication.run(CameraApplication.class, args);
    }
}