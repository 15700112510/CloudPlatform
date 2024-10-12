package com.cloud.server.user.service.impl;

import com.alibaba.fastjson2.JSON;
import com.cloud.common.core.R;
import com.cloud.common.core.constant.AuthConstant;
import com.cloud.common.core.enums.ErrorEnum;
import com.cloud.common.core.utils.JwtUtil;
import com.cloud.common.core.utils.StrTypeUtil;
import com.cloud.common.core.utils.StringUtil;
import com.cloud.common.datasource.mbtspls.DataSourceService;
import com.cloud.common.redis.service.RedisService;
import com.cloud.server.user.domain.User;
import com.cloud.server.user.dto.*;
import com.cloud.server.user.mapper.UserMapper;
import com.cloud.server.user.service.UserService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 *
 * @author cloud
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final RedisService redisService;

    @Autowired
    private DataSourceService dataSourceService;

    public UserServiceImpl(UserMapper userMapper, RedisService redisService) {
        this.userMapper = userMapper;
        this.redisService = redisService;
    }

    /**
     * Login by root and password.
     *
     * @param request HttpServletRequest获取请求头信息
     * @param dto     密码登录所需参数
     * @return 统一结果类R
     */
    @Override
    public R<?> loginByPassword(HttpServletRequest request, LoginByPasswordDto dto) {
        // 取出登录参数
        String root = dto.getRoot();
        String password = dto.getPassword();

        // 用户是否存在
        if (userMapper.rootExistCount(root) == 0) {
            return R.fail(ErrorEnum.NOT_EXIST_ROOT);
        }
        // 根据用户名或手机号查询用户
        User user = userMapper.queryUserByRoot(root);
        // 用户是否被停用
        if (user.getStatus() == 1) {
            return R.fail(ErrorEnum.ROOT_STATUS_DISABLE);
        }
        // 密码是否匹配
        if (!user.getPassword().equals(password)) {
            return R.fail(ErrorEnum.ERROR_PASSWORD);
        }

        // 改变登录标志
        userMapper.login(user.getUserId());

        // 生成token，并设置ttl为2000秒
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", user.getUsername());
        map.put("role", user.getRole());
        String token = JwtUtil.createToken(map, AuthConstant.BROWSER_TOKEN_TTL);

        // 若为移动端登录，设置为七天后过期
        if (Objects.equals(request.getHeader("DEVICE_KIND"), "MOBILE")) {
            token = JwtUtil.createToken(map, AuthConstant.MOBILE_TOKEN_TTL);
        }

        // 登录信息存入redis
        redisService.setCacheObject("JWT_" + user.getUsername(), token,
                2 * AuthConstant.BROWSER_TOKEN_TTL, TimeUnit.MILLISECONDS);


        // 将token和userId传给客户端
        return R.ok(new LoginOkBackDto(token, String.valueOf(user.getUserId()), user.getCreateBy()));
    }

    /**
     * 账号验证码登录
     *
     * @param request HttpServletRequest获取请求头信息
     * @param dto     验证码登录所需参数
     * @return 统一结果类R
     */
    @Override
    public R<?> loginByCode(HttpServletRequest request, LoginByCodeDto dto) {
        // 取出登录参数
        String phoneNum = dto.getPhoneNum();
        String code = dto.getCode();

        // 用户是否存在
        if (userMapper.phoneNumExistCount(phoneNum) == 0) {
            return R.fail(ErrorEnum.NOT_EXIST_ROOT);
        }
        // 查询用户
        User user = userMapper.queryUserByRoot(phoneNum);
        // 用户是否被停用
        if (user.getStatus() == 1) {
            return R.fail(ErrorEnum.ROOT_STATUS_DISABLE);
        }
        // 比对验证码
        String realCode = redisService.getCacheObject(AuthConstant.USER_PREFIX + "code:L:" + phoneNum);
        // 前后手机号不一致，或者验证码过期
        if (StringUtil.isNullOrEmpty(realCode)) {
            return R.fail(ErrorEnum.DISABLED_CODE);
        }
        // 验证码错误
        if (!code.equals(realCode)) {
            return R.fail(ErrorEnum.CODE_NOT_MATCH);
        }

        // 改变登录标志
        userMapper.login(user.getUserId());

        // 生成token，并设置ttl为2000秒
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", user.getUsername());
        map.put("role", user.getRole());
        String token = JwtUtil.createToken(map, AuthConstant.BROWSER_TOKEN_TTL);

        // 若为移动端登录，设置为七天后过期
        if (Objects.equals(request.getHeader("DEVICE_KIND"), "MOBILE")) {
            token = JwtUtil.createToken(map, AuthConstant.MOBILE_TOKEN_TTL);
        }

        // 登录信息存入redis
        redisService.setCacheObject(
                "JWT_" + user.getUsername(), token, 2 * AuthConstant.BROWSER_TOKEN_TTL, TimeUnit.MILLISECONDS);

        // 将token和userId传给客户端
        return R.ok(new LoginOkBackDto(token, user.getUserId() + "", user.getCreateBy())+"");
    }

    /**
     * 用户注册
     *
     * @param dto 注册所需参数
     * @return 统一结果类R
     */
    @Override
    public R<?> register(RegisterDto dto) {
        // 取出注册参数
        String username = dto.getUsername();
        String password = dto.getPassword();
        Character role = dto.getRole();
        String nickname = dto.getNickname();
        String phoneNum = dto.getPhoneNum();
        String code = dto.getCode();
        // 用户名包含汉字或特殊字符
        if (StrTypeUtil.containChinese(username) || StrTypeUtil.containSpecialChar(username)) {
            return R.fail(ErrorEnum.CONTAIN_ILLEGAL_CHARACTER);
        }
        // 用户名长度超过最大长度
        if (username.length() > AuthConstant.MAX_USERNAME_LENGTH) {
            return R.fail(ErrorEnum.BEYOND_MAX_LENGTH_USERNAME);
        }
        // 角色信息不符合规则
        if (!(role.equals('1') || role.equals('2'))) {
            return R.fail(ErrorEnum.NOT_MATCH_ROLE_RULE);
        }
        // 昵称超过最大长度
        if (nickname.length() > AuthConstant.MAX_NICKNAME_LENGTH) {
            return R.fail(ErrorEnum.BEYOND_MAX_LENGTH_NICKNAME);
        }
        // 昵称已被占用
        if (userMapper.nicknameExistCount(nickname) > 0) {
            return R.fail(ErrorEnum.ALREADY_EXIST_NICKNAME);
        }
        // 用户名是否已存在
        if (userMapper.usernameExistCount(username) > 0) {
            return R.fail(ErrorEnum.IS_EXIST_USERNAME);
        }
        // 手机号是否已注册
        if (userMapper.phoneNumExistCount(phoneNum) > 0) {
            return R.fail(ErrorEnum.IS_EXIST_PHONE_NUM);
        }
        // 比对验证码
        String realCode = redisService.getCacheObject(AuthConstant.USER_PREFIX + "code:R:" + phoneNum);
        // 前后手机号不一致，或者验证码过期
        if (StringUtil.isNullOrEmpty(realCode)) {
            return R.fail(ErrorEnum.DISABLED_CODE);
        }
        // 验证码错误
        if (!code.equals(realCode)) {
            return R.fail(ErrorEnum.CODE_NOT_MATCH);
        }
        User user = new User(username, password, nickname, phoneNum, role);
        dataSourceService.init(nickname,nickname);
        // 插入数据库
        userMapper.insertUser(user);
        return R.ok("注册成功！");
    }

    /**
     * 分页模糊获取用户
     *
     * @param pageNum  分页页码
     * @param pageSize 页大小
     * @param wrapper  模糊查询条件
     * @return 统一结果类R
     */
    @Override
    public R<?> getPageUsers(Integer pageNum, Integer pageSize, String wrapper) {
        // 查询条件为空才缓存，否则不缓存
        if (wrapper.length() > 0) {
            // 开始分页
            PageHelper.startPage(pageNum, pageSize);
            // PageHelper拦截到查询所有并将其转为分页请求
            List<User> userList = userMapper.getPageUsers("%" + wrapper + "%");
            return R.ok(userList);
        }
        // 查看redis是否有缓存数据
        if (Boolean.TRUE.equals(redisService.hasKey(AuthConstant.USER_PREFIX))) {
            // 若有则返回
            List<User> pageUser = redisService.getCacheList(
                    AuthConstant.USER_PREFIX, (long) (pageNum - 1) * pageSize, (long) pageNum * pageSize - 1);
            return R.ok(pageUser);
        }
        // 若没有，从数据库分页查询
        PageHelper.startPage(pageNum, pageSize);
        // PageHelper拦截到查询所有并将其转为分页请求
        List<User> userList = userMapper.getPageUsers(wrapper);
        // 将所有灯杆数据写入缓存
        if (userList.size() > 0) {
            redisService.setCacheList(AuthConstant.USER_PREFIX, userList);
            redisService.expire(AuthConstant.USER_PREFIX, 3, TimeUnit.MINUTES);
        }
        return R.ok(userList);
    }

    /**
     * 修改密码
     *
     * @param dto 修改密码所需参数
     * @return 统一结果类R
     */
    @Override
    public R<?> updatePassword(UpdatePasswordDto dto) {
        // 取出修改密码的参数
        String password = dto.getPassword();
        String phoneNum = dto.getPhoneNum();
        String code = dto.getCode();
        String updateBy = dto.getUpdateBy();

        // 用户不存在
        if (userMapper.phoneNumExistCount(phoneNum) == 0) {
            return R.fail(ErrorEnum.NOT_EXIST_ROOT);
        }
        // 比对验证码
        String realCode = redisService.getCacheObject(AuthConstant.USER_PREFIX + "code:U:" + phoneNum);
        // 前后手机号不一致，或者验证码过期
        if (StringUtil.isNullOrEmpty(realCode)) {
            return R.fail(ErrorEnum.DISABLED_CODE);
        }
        // 验证码错误
        if (!code.equals(realCode)) {
            return R.fail(ErrorEnum.CODE_NOT_MATCH);
        }
        // 参数装载进实体插入数据库
        User user = new User(password, phoneNum, updateBy);
        // 更新数据库
        userMapper.updatePassword(user);
        // 删除缓存
        redisService.deleteObject(
                AuthConstant.USER_PREFIX + "info:" + userMapper.queryUserByRoot(phoneNum).getUserId());
        return R.ok();
    }

    /**
     * 修改昵称
     *
     * @param dto 修改昵称所需参数
     * @return 统一结果类R
     */
    @Override
    public R<?> updateNickname(UpdateNicknameDto dto) {
        // 取出参数
        Long userId = dto.getUserId();
        String nickname = dto.getNickname();

        // 昵称超过最大长度
        if (nickname.length() > AuthConstant.MAX_NICKNAME_LENGTH) {
            return R.fail(ErrorEnum.BEYOND_MAX_LENGTH_NICKNAME);
        }
        // 昵称已被占用
        if (userMapper.nicknameExistCount(nickname) > 0) {
            return R.fail(ErrorEnum.ALREADY_EXIST_NICKNAME);
        }
        // 更新数据库
        userMapper.updateNickname(dto);
        // 删除缓存
        redisService.deleteObject(AuthConstant.USER_PREFIX + "info:" + userId);
        return R.ok();
    }

    /**
     * 生成验证码并发短信
     *
     * @param dto 生成验证码所需参数
     * @return 统一结果类R
     */
    @Override
    public R<?> getCode(CodeDto dto) {
        // 取出手机号和业务类型
        String phoneNum = dto.getPhoneNum();
        String type = dto.getType();
        // 手机号格式不正确
        if (phoneNum.length() != AuthConstant.PHONE_NUM_LENGTH || !phoneNum.startsWith("1")) {
            return R.fail(ErrorEnum.PHONE_NUM_NOT_MATCH_RULE);
        }
        // 生成6位随机数
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < AuthConstant.CODE_NUM_LENGTH; i++) {
            builder.append(random.nextInt(AuthConstant.CODE_RANGE));
        }
        String code = builder.toString();
        switch (type) {
            // 登录
            case "L":
                // 存入redis用以比对，3分钟内有效，键中包含手机号，以防获取验证码与注册的手机号前后不一致
                redisService.setCacheObject(
                        AuthConstant.USER_PREFIX + "code:L:" + phoneNum, code, AuthConstant.CODE_TTL, TimeUnit.MINUTES);
                break;
            // 注册
            case "R":
                // 存入redis用以比对，3分钟内有效，键中包含手机号，以防获取验证码与注册的手机号前后不一致
                redisService.setCacheObject(
                        AuthConstant.USER_PREFIX + "code:R:" + phoneNum, code, AuthConstant.CODE_TTL, TimeUnit.MINUTES);
                break;
            // 改密码
            case "U":
                // 存入redis用以比对，3分钟内有效，键中包含手机号，以防获取验证码与修改密码的手机号前后不一致
                redisService.setCacheObject(
                        AuthConstant.USER_PREFIX + "code:U:" + phoneNum, code, AuthConstant.CODE_TTL, TimeUnit.MINUTES);
                break;
            default:
                break;
        }
        // 发送短信，此处用返回参数的方式模拟
        return R.ok(code);
    }

    /**
     * 退出登录
     *
     * @param username 用户名
     * @return 统一结果类R
     */
    @Override
    public R<?> logout(String username) {
        // 生成一个无效令牌
        String invalidToken = JwtUtil.createToken(null, 0);
        // 删除redis中的备份token
        redisService.deleteObject("JWT_" + username);
        // 改变用户登录标志
        userMapper.logout(username);
        // 无效令牌返回
        return R.ok(invalidToken);
    }

    /**
     * 根据id获取用户信息
     *
     * @param userId 用户id
     * @return 统一结果类R
     */
    @Override
    public R<?> queryUserById(Long userId) {
        // 创建user对象
        User user;
        // 是否有用户缓存信息
        if (Boolean.TRUE.equals(redisService.hasKey(AuthConstant.USER_PREFIX + "info:" + userId))) {
            // 有缓存则取出并装载进user对象
            String obj = redisService.getCacheObject(AuthConstant.USER_PREFIX + "info:" + userId);
            user = JSON.parseObject(obj, User.class);
            // 将user对象返回
            return R.ok(user);
        }
        // 查询数据库获取用户信息
        user = userMapper.queryUserById(userId);
        return R.ok(user);
    }
}
