package com.iot.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.cloud.common.security", "com.cloud.common.redis", "com.iot.gateway"
})
@EnableFeignClients(basePackages = "com.cloud.api.clients")
@EnableScheduling
public class IotGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotGatewayApplication.class, args);
    }
}
