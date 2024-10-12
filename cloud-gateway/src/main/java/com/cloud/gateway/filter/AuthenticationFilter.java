package com.cloud.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.cloud.common.core.R;
import com.cloud.common.core.constant.AuthConstant;
import com.cloud.common.core.enums.ErrorEnum;
import com.cloud.common.core.utils.JwtUtil;
import com.cloud.common.redis.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.netty.util.internal.StringUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * 用户认证过滤器
 *
 * @author likain
 */
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    /**
     * 直接放行的接口
     */
    private final String[] ignoreFilterList = new String[]{"ws", "login", "register", "update/password", "code/message","camera"};

    @Resource
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 若为登录相关请求，则直接放行
        ServerHttpRequest request = exchange.getRequest();
        for (String str : ignoreFilterList) {
            if (request.getURI().getPath().contains(str)) {
                return chain.filter(exchange);
            }
        }

        // 从请求头中尝试获取token
        String token = request.getHeaders().getFirst(AuthConstant.HEADER_TOKEN);
        ServerHttpResponse response = exchange.getResponse();

        if (StringUtil.isNullOrEmpty(token)) {
            // 令牌为空
            return unAuthorizedResponse(response, JSON.toJSONString(R.fail(ErrorEnum.TOKEN_EMPTY)));
        }

        Claims claims;
        try {
            claims = JwtUtil.parseToken(token);
        } catch (MalformedJwtException e) {
            // token格式异常
            return unAuthorizedResponse(response, JSON.toJSONString(R.fail(ErrorEnum.FORM_ERROR)));
        } catch (SignatureException e) {
            // 签名异常
            return unAuthorizedResponse(response, JSON.toJSONString(R.fail(ErrorEnum.SIGNATURE_ERROR)));
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
            // claims为空，用户已登出
            if (claims == null) {
                return unAuthorizedResponse(response, JSON.toJSONString(R.fail(ErrorEnum.TOKEN_INVALID)));
            }
            // 用户登录信息过期
            String backupToken = redisService.getCacheObject("JWT_" + claims.get("username"));
            // 备份令牌为过期则续期令牌
            if (token.equals(backupToken)) {
                // 根据移动端和浏览器端分别设置不同的过期时间
                String mobileTag = "MOBILE";
                int refreshTokenExpireMillis = Objects.equals(request.getHeaders().getFirst(AuthConstant.DEVICE_KIND), mobileTag) ? AuthConstant.MOBILE_TOKEN_TTL : AuthConstant.BROWSER_TOKEN_TTL;
                String refreshToken = JwtUtil.createToken(claims, refreshTokenExpireMillis);

                // 续期的新令牌写回redis并覆盖原备份令牌
                redisService.setCacheObject("JWT_" + claims.get("username"), refreshToken, 2 * AuthConstant.BROWSER_TOKEN_TTL, TimeUnit.MILLISECONDS);
                // 返回刷新的token
                return refreshTokenResponse(exchange, chain, refreshToken);
            }
            // 未携带新令牌导致与备份令牌不一致或备份令牌过期为空，均给予拦截
            return unAuthorizedResponse(response, JSON.toJSONString(R.fail(ErrorEnum.TOKEN_EXPIRED)));
        } catch (Exception e) {
            // 解析出错
            return unAuthorizedResponse(response, JSON.toJSONString(R.fail(ErrorEnum.PARSE_ERROR)));
        }

        // ServletHttpRequest是只读的，无法直接setHeader或addHeader
        // 此处request.mutate()返回值为ServletHttpRequest的Builder，相当于重构请求，可在此处设置请求头
        request.mutate().header("role", (String) claims.get("role"));
        // 放行
        return chain.filter(exchange);
    }

    /**
     * 未认证的返回信息
     *
     * @param response response
     * @param errorMsg 错误信息：未认证或过期
     * @return 错误消息写回
     */
    private Mono<Void> unAuthorizedResponse(ServerHttpResponse response, String errorMsg) {
        // 未认证401状态码
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        // 错误信息回写
        DataBuffer buffer = response.bufferFactory().wrap(errorMsg.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 刷新令牌的响应
     *
     * @param exchange     上下文
     * @param chain        过滤器链
     * @param refreshToken 新token
     * @return 新token回写
     */
    private Mono<Void> refreshTokenResponse(ServerWebExchange exchange, GatewayFilterChain chain, String refreshToken) {
        ServerHttpResponse response = exchange.getResponse();

        // 返回200状态码及刷新的token
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set("Token", refreshToken);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
