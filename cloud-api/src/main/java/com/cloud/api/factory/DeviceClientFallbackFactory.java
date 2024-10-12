package com.cloud.api.factory;

import com.cloud.api.clients.DeviceClient;
import com.cloud.api.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 设备服务降级处理
 *
 * @author cloud
 */
@Component
public class DeviceClientFallbackFactory implements FallbackFactory<DeviceClient> {
    /**
     * 日志打印
     */
    private static final Logger logger = LoggerFactory.getLogger(DeviceClientFallbackFactory.class);

    @Override
    public DeviceClient create(Throwable cause) {
        logger.error("设备服务调用失败：{}", cause.getMessage());

        return new DeviceClient() {
            @Override
            public int deviceLogin(Long deviceId) {
                return 0;
            }

            @Override
            public int deviceLogout(Long deviceId) {
                return 0;
            }

            @Override
            public char deviceStatus(Long deviceId) {
                return 'f';
            }

            @Override
            public long addDevice(Device device) {
                return 0;
            }
        };
    }
}
