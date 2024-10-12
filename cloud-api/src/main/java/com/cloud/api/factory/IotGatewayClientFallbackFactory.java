package com.cloud.api.factory;

import com.cloud.api.clients.IotGatewayClient;
import com.cloud.api.model.GatewayInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/9 14:58
 */
@Component
public class IotGatewayClientFallbackFactory
        implements IotGatewayClient, FallbackFactory<IotGatewayClient> {

    private final Logger logger = LoggerFactory.getLogger(IotGatewayClientFallbackFactory.class);

    @Override
    public boolean subscribe(String serialNum) {
        return false;
    }

    @Override
    public int addGateway(GatewayInfo info) {
        return 0;
    }

    @Override
    public int getSerialNum(String serial) {
        return -1;
    }

    @Override
    public IotGatewayClient create(Throwable cause) {
        logger.error("物联网网关服务调用失败", cause);
        return this;
    }
}
