package com.cloud.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 注入请求来源头
 *
 * @author likain
 * @since 2023/6/7 22:35
 */
@Component
public class OriginInjectionFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        request.mutate().header("origin", "gateway");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
