package com.cloud.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description:
 *
 * @author likain
 * @since 2024/1/2 14:02
 */
@Component
public class FlowLimitFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return null;
    }

    private boolean hasRemaining() {
        return false;
    }
}
