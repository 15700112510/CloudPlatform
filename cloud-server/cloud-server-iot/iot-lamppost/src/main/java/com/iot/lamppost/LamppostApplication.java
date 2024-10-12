package com.iot.lamppost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.iot.lamppost", "com.cloud.common.security", "com.cloud.common.redis"
})
public class LamppostApplication {

    public static void main(String[] args) {
        SpringApplication.run(LamppostApplication.class, args);
    }
}
