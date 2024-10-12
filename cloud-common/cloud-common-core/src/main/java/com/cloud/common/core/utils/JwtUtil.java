package com.cloud.common.core.utils;

import com.cloud.common.core.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @param ttlMillis token过期毫秒值
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims, Integer ttlMillis) {
        JwtBuilder builder = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, JwtConstant.SECRET);
        if (ttlMillis != null) {
            // ttl不为空则设置token过期时间
            builder.setExpiration(new Date(Calendar.getInstance().getTimeInMillis() + ttlMillis));
        }
        return builder.compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(JwtConstant.SECRET).parseClaimsJws(token).getBody();
    }
}
