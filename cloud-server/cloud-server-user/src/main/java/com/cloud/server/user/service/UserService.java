package com.cloud.server.user.service;

import com.cloud.common.core.R;
import com.cloud.server.user.dto.*;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    // 账号密码登录
    R<?> loginByPassword(HttpServletRequest request, LoginByPasswordDto dto);

    // 验证码登录
    R<?> loginByCode(HttpServletRequest request, LoginByCodeDto dto);

    // 用户注册
    R<?> register(RegisterDto dto);

    // 分页获取用户
    R<?> getPageUsers(Integer pageNum, Integer pageSize, String wrapper);

    // 修改密码
    R<?> updatePassword(UpdatePasswordDto dto);

    // 修改昵称
    R<?> updateNickname(UpdateNicknameDto dto);

    // 生成验证码并发送短信至手机号
    R<?> getCode(CodeDto dto);

    // 退出登录
    R<?> logout(String username);

    // 根据id获取用户信息
    R<?> queryUserById(Long userId);
}
