package com.cloud.common.core.constant;

/**
 * 认证授权相关通用常量
 */
public class AuthConstant {
    /**
     * Token
     */
    public static final String HEADER_TOKEN = "Token";

    /**
     * 客户端类型
     */
    public static final String DEVICE_KIND = "DEVICE_KIND";

    /**
     * 移动端令牌ttl
     */
    public static final int MOBILE_TOKEN_TTL = 7 * 24 * 60 * 60 * 1000;

    /**
     * 浏览器端令牌ttl
     */
    public static final int BROWSER_TOKEN_TTL = 2000 * 1000;

    /**
     * 用户名最大长度
     */
    public static final int MAX_USERNAME_LENGTH = 15;

    /**
     * 昵称最大长度
     */
    public static final int MAX_NICKNAME_LENGTH = 10;

    /**
     * 电话号码最大长度
     */
    public static final int PHONE_NUM_LENGTH = 11;

    /**
     * 验证码最大长度
     */
    public static final int CODE_NUM_LENGTH = 6;

    /**
     * 验证码每一位的范围（0~10）
     */
    public static final int CODE_RANGE = 10;

    /**
     * UserService业务缓存键前缀
     */
    public static final String USER_PREFIX = "cloud:user:";

    /**
     * 验证码ttl（分钟）
     */
    public static final int CODE_TTL = 3;
}
