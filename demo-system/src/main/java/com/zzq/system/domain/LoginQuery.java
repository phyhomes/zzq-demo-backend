package com.zzq.system.domain;

import com.zzq.common.annotation.Xss;
import com.zzq.system.config.LoginConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-04 17:12
 * @Author : ZZQ
 * @Desc : 用户登录请求对象
 */
public class LoginQuery {
    /**
     * 用户名
     */
    @Xss(message = "user.xss.forbidden")
    @NotBlank(message = "user.username.not.blank")
    @Size(min = LoginConfig.USERNAME_MIN_LENGTH,
            max = LoginConfig.USERNAME_MAX_LENGTH,
            message = "user.username.size")
    private String username;

    /**
     * 用户密码
     */
    @Xss(message = "user.xss.forbidden")
    @NotBlank(message = "user.password.not.blank")
    @Size(min = LoginConfig.PASSWORD_MIN_LENGTH,
            max = LoginConfig.PASSWORD_MAX_LENGTH,
            message = "user.password.size")
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "user.code.not.blank")
    private String code;

    /**
     * 验证码的唯一标识
     */
    @NotBlank(message = "user.uuid.not.blank")
    private String uuid;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "LoginQuery{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
