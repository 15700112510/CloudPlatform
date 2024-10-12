package com.cloud.server.user.controller;

import com.cloud.common.core.R;
import com.cloud.common.security.annotation.RequiredOrigin;
import com.cloud.server.user.dto.*;
import com.cloud.server.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@RequiredOrigin("gateway")
public class WebUserController {

    @Autowired
    private UserService userService;

    // 账号密码登录
    @PostMapping("/login/password")
    public R<?> loginByPassword(HttpServletRequest request, @RequestBody LoginByPasswordDto dto) {
        return userService.loginByPassword(request, dto);
    }

    // 验证码登录
    @PostMapping("/login/code")
    public R<?> loginByCode(HttpServletRequest request, @RequestBody LoginByCodeDto dto) {
        return userService.loginByCode(request, dto);
    }

    // 用户注册
    @PostMapping("/register")
    public R<?> register(@RequestBody RegisterDto dto) {
        return userService.register(dto);
    }

    // 分页获取用户
    @GetMapping("/query/page/users")
    public R<?> getPageUsers(@RequestParam Integer page, @RequestParam Integer limit, @RequestParam String wrapper) {
        return userService.getPageUsers(page, limit, wrapper);
    }

    // 修改密码
    @PutMapping("/update/password")
    public R<?> updatePassword(@RequestBody UpdatePasswordDto dto) {
        return userService.updatePassword(dto);
    }

    // 修改昵称
    @PutMapping("/update/nickname")
    public R<?> updateNickname(@RequestBody UpdateNicknameDto dto) {
        return userService.updateNickname(dto);
    }

    // 获取并发送验证码
    @PostMapping("/code/message")
    public R<?> getCode(@RequestBody CodeDto dto) {
        return userService.getCode(dto);
    }

    // 退出登录
    @GetMapping("/logout/{username}")
    public R<?> logout(@PathVariable String username) {
        return userService.logout(username);
    }

    // 根据id获取用户信息
    @GetMapping("/query/user/{userId}")
    public R<?> queryUserById(@PathVariable Long userId) {
        return userService.queryUserById(userId);
    }
}
