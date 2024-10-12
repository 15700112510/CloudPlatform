package com.cloud.api.factory;

import com.cloud.api.clients.LampClient;
import com.cloud.api.model.Lamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 路灯模块降级处理
 *
 * @author cloud
 */
@Component
public class LampClientFallbackFactory implements FallbackFactory<LampClient> {

    private static final Logger logger = LoggerFactory.getLogger(LampClientFallbackFactory.class);

    @Override
    public LampClient create(Throwable cause) {
        logger.error("路灯服务调用失败：{}", cause.getMessage());

        return new LampClient() {
            @Override
            public int getCardNoCount(String cardNo) {
                return -1;
            }

            @Override
            public int getTotalNum() {
                return -1;
            }

            @Override
            public int addLamp(Lamp lamp, String opsBy) {
                return 0;
            }

            @Override
            public boolean subscribe(String cardNo) {
                return false;
            }
        };
    }
}
