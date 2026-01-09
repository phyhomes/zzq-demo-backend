package com.zzq.framework.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zzq.common.core.domain.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @Project : zzq-demo-backend
 * @Time    : 2025-12-18 16:59
 * @Author  : ZZQ
 * @Desc    : 数据库sys_login_info表，系统访问记录表
 */
public class SysLoginInfo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 登录IP地址 */
    private String ipaddr;

    /** 登录地点 */
    private String location;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 登录状态 0失败 1成功 */
    private String status;

    /** 提示消息 */
    private String msg;

    /** 登录时间 */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime loginTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public LocalDateTime getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime)
    {
        this.loginTime = loginTime;
    }
}
