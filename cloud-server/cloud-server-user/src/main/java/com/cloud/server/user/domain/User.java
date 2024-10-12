package com.cloud.server.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //唯一标识id
    private Long userId;
    //用户名
    private String username;
    //密码
    private String password;
    //昵称
    private String nickname;
    //电话号码
    private String phoneNum;
    //角色：1为超级管理员，2为普通管理员
    private Character role;
    //创建时间
    private Date createTime;
    //创建者
    private String createBy;
    //更新时间
    private Date updateTime;
    //更新者
    private String updateBy;
    //启用状态：0启用，1停用
    private Character status;
    //登录标志
    private Character flag;

    //注册所用构造方法
    public User(String username, String password, String nickname, String phoneNum, Character role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.role = role;
    }

    //修改密码所用构造方法
    public User(String password, String phoneNum, String updateBy) {
        this.password = password;
        this.phoneNum = phoneNum;
        this.updateBy = updateBy;
    }

    //修改昵称所需构造方法
    public User(String nickname, String updateBy) {
        this.nickname = nickname;
        this.updateBy = updateBy;
    }
}
