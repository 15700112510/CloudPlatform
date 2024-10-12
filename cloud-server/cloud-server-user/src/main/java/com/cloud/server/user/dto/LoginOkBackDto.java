package com.cloud.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录成功返回结构
 *
 * @author cloud
 */
@Data
@AllArgsConstructor
public class LoginOkBackDto {
    /**
     * token
     */
    private String token;

    /**
     * userId
     */
    private String userId;

    /**
     * createBy
     */
    private String createBy;
}
