package com.zzq.system.config;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 21:14
 * @Author : ZZQ
 * @Desc : 登录的相关配置信息
 */
public class LoginConfig {
    /**
     * 用户名最小长度
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    /**
     * 用户名最大长度
     */
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码最小长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 6;
    /**
     * 密码最大长度限制
     */
    public static final int PASSWORD_MAX_LENGTH = 20;

    /**
     * 用户昵称最小长度限制
     */
    public static final int NAME_MIN_LENGTH = 2;

    /**
     * 用户昵称最大长度限制
     */
    public static final int NAME_MAX_LENGTH = 20;
}
