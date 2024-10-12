package com.cloud.server.user.mapper;

import com.cloud.server.user.domain.User;
import com.cloud.server.user.dto.UpdateNicknameDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    // 该用户名是否存在
    int usernameExistCount(String username);

    // 手机号是否已注册过用户
    int phoneNumExistCount(String phoneNum);

    // 登陆时用户名或手机号是否已存在
    int rootExistCount(String root);

    // 昵称是否已存在
    int nicknameExistCount(String nickname);

    // 增加用户
    int insertUser(User user);

    // 分页查询用户
    List<User> getPageUsers(String wrapper);

    // 登录
    int login(Long userId);

    // 退出登录
    int logout(String username);

    // 获取用户登录状态
    char getFlag(String root);

    // 根据手机号查询用户名
    String queryUsernameByPhoneNum(String phoneNum);

    // 根据用户名或手机号查询User
    User queryUserByRoot(String root);

    // 根据id获取用户信息
    User queryUserById(Long userId);

    // 修改密码
    int updatePassword(User user);

    // 修改昵称
    int updateNickname(UpdateNicknameDto dto);
}
