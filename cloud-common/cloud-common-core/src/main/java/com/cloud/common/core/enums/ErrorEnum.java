package com.cloud.common.core.enums;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    //通用
    IS_EMPTY_OPS_BY(100, "操作者为空"),
    IS_EMPTY_PAGE_NUM(101, "分页页码为空"),
    IS_EMPTY_PAGE_SIZE(102, "分页数量为空"),
    IS_ILLEGAL_PAGE_PARAM(103, "分页参数非法"),
    SYSTEM_ERROR(500, "系统出错"),
    SERVICE_FALLBACK(104, "相关服务失效"),
    ILLEGAL_PARAM(105, "非法参数"),
    REMOTE_CALL_EXCEPTION(555, "远程调用失败"),
    //登录
    NOT_EXIST_ROOT(2000, "用户不存在"),
    ERROR_PASSWORD(2001, "密码错误"),
    ROOT_STATUS_DISABLE(2002, "该用户已被停用"),
    IS_EMPTY_USER_ID(2003, "用户ID为空"),
    ALREADY_LOGOUT(2004, "重复登出"),
    ALREADY_LOGIN(2005, "该用户已登录"),
    //注册及修改密码
    IS_EXIST_USERNAME(3000, "该用户名已存在"),
    IS_EXIST_PHONE_NUM(3001, "该手机号已注册用户"),
    IS_EMPTY_USERNAME(3002, "用户名为空"),
    IS_EMPTY_PASSWORD(3003, "密码为空"),
    IS_EMPTY_PHONE_NUM(3004, "手机号为空"),
    BEYOND_MAX_LENGTH_USERNAME(3005, "用户名超过最大长度"),
    BEYOND_MAX_LENGTH_NICKNAME(3006, "昵称超过最大长度"),
    CONTAIN_ILLEGAL_CHARACTER(3007, "包含非法字符"),
    IS_EMPTY_ROLE(3008, "用户角色为空"),
    NOT_MATCH_ROLE_RULE(3009, "角色信息不符合规则"),
    IS_EMPTY_NICKNAME(3010, "昵称为空"),
    ALREADY_EXIST_NICKNAME(3011, "昵称已存在"),
    //验证码服务
    PHONE_NUM_NOT_MATCH_RULE(4000, "手机号格式不正确"),
    CODE_NOT_MATCH(4001, "验证码错误"),
    DISABLED_CODE(4002, "无效的验证码"),
    IS_EMPTY_CODE(4003, "验证码为空"),
    IS_EMPTY_CODE_TYPE(4004, "验证码类型为空"),
    //灯杆业务
    IS_EMPTY_LAMPPOST_NAME(5000, "灯杆名为空"),
    IS_EMPTY_GROUP_NO(5001, "群号为空"),
    ALREADY_EXIST_NAME(5002, "该群已有此灯杆名"),
    NOT_EXIST_LAMPPOST(5003, "此灯杆不存在"),
    BEYOND_MAX_LENGTH_LAMPPOST_NAME(5004, "灯杆名称超过最大长度"),
    //设备业务
    IS_EMPTY_LAMPPOST_ID(6000, "灯杆id为空"),
    IS_EMPTY_TYPE(6001, "设备类型为空"),
    IS_EMPTY_DETAILS(6002, "设备详细参数为空"),
    TYPE_NOT_MATCH(6003, "设备类型格式错误"),
    ALREADY_MOUNTED(6004, "该设备已挂载"),
    ALREADY_EXIST_LAMP(6005, "该灯杆已挂载了照明设备"),
    //设备参数业务
    IS_EMPTY_CARD_NO(7000, "卡号为空"),
    NOT_EXIST_CARD_NO(7001, "卡号不存在"),
    ALREADY_EXIST_CARD_NO(7002, "卡号已存在"),
    IS_EMPTY_SERIAL_NUM(7003, "序列号为空"),
    ALREADY_EXIST_SERIAL_NUM(7004, "序列号已存在"),
    IS_EMPTY_TIME_INTERVAL(7005, "起始或终止时间为空"),
    //设备报警业务
    IS_EMPTY_ALARM_ID(8000, "警报项id为空"),
    NOT_EXIST_ALARM_ID(8001, "该报警项不存在"),
    ALREADY_REPAIRED(8002, "该报警项已被修复"),
    //MQTT业务
    NOT_MATCH_RULES_BRIGHTNESS(9000, "亮度格式不符合规则"),
    IS_EMPTY_FLAG(9001, "开关信息为空"),
    IS_EMPTY_CONTROL_TYPE(9002, "控制类型为空"),
    FAIL_PUBLISH(9003, "消息发布失败"),
    NOT_MATCH_RULES_INTERVAL(9004, "时间间隔格式不符合规则"),
    IS_EMPTY_CLOCK_CONFIG(9005, "闹钟配置存在为空"),
    //用户认证
    TOKEN_EMPTY(10001, "登录令牌为空"),
    TOKEN_EXPIRED(10002, "登录令牌过期"),
    TOKEN_INVALID(10003, "登录令牌失效"),
    SIGNATURE_ERROR(10004, "登录令牌签名异常"),
    FORM_ERROR(10005, "登录令牌格式异常"),
    PARSE_ERROR(10006, "登录令牌解析出错"),
    //物联网网关
    TIMEOUT_ERROR(20001, "命令响应超时"),
    MQTT_ERROR(20002, "MQTT处理错误"),
    ;

    private final int code;
    private final String msg;

    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
