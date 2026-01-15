package com.zzq.system.domain;

import com.zzq.system.annotation.CaptchaRequired;
import com.zzq.common.annotation.Xss;
import com.zzq.common.utils.StringUtils;
import com.zzq.system.config.LoginConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Random;

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
    @CaptchaRequired(message = "user.code.not.blank")
    private String code;

    /**
     * 验证码的唯一标识
     */
    @CaptchaRequired(message = "user.code.not.blank")
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

    public String getMaskedPassword() {
        Random random = new Random();
        int repeatLeft = random.nextInt(10) + 1;
        int repeatRight = random.nextInt(10) + 1;
        return StringUtils.repeat(StringUtils.STAR, repeatLeft)
                + StringUtils.mask(password, 0, 0)
                + StringUtils.repeat(StringUtils.STAR, repeatRight);
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
        return new ToStringBuilder(this)
                .append("username", username)
                .append("password", getMaskedPassword())
                .append("code", code)
                .append("uuid", uuid)
                .toString();
    }
}
