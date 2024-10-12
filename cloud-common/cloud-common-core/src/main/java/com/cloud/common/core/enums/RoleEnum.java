package com.cloud.common.core.enums;

public enum RoleEnum {

    SYS_ADMIN('1',"系统管理员"),
    SYS_OPERATOR('2',"操作工"),
    ;
    private final char role;
    private final String info;

    RoleEnum(char role, String info) {
        this.role = role;
        this.info = info;
    }

    public char getRole() {
        return role;
    }

    public String getInfo() {
        return info;
    }
}
