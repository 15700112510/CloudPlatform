package com.cloud.server.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.cloud.common.redis", "com.cloud.common.security", "com.cloud.server.user","com.cloud.common.datasource"
})
@MapperScan(
        {"com.cloud.common.datasource.mapper", "com.cloud.server.user.mapper" }
)
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
