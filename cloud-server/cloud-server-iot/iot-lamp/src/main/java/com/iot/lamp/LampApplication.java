package com.iot.lamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.cloud.common.security", "com.cloud.common.redis", "com.iot.lamp", "com.cloud.api.factory"
})
@EnableFeignClients("com.cloud.api.clients")
@EnableScheduling
public class LampApplication {
    public static void main(String[] args) {
        SpringApplication.run(LampApplication.class, args);
    }
}
