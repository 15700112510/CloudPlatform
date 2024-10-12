package com.iot.gateway.core;

import com.cloud.api.model.GatewayInfo;
import org.springframework.context.ApplicationEvent;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/9 19:42
 */
public class AddGatewayEvent extends ApplicationEvent {

    private final GatewayInfo info;

    public AddGatewayEvent(Object source, GatewayInfo info) {
        super(source);
        this.info = info;
    }

    public GatewayInfo getInfo() {
        return info;
    }
}
