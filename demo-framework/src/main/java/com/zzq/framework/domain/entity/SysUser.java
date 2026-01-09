package com.zzq.framework.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zzq.common.annotation.Xss;
import com.zzq.common.core.domain.BaseEntity;
import com.zzq.framework.config.LoginConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 08:36
 * @Author : ZZQ
 * @Desc : 用户表 sys_user
 */
public class SysUser extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 人员id
     */
    private Long personId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 最近登录ip
     */
    private String loginIp;

    /**
     * 最近登录时间
     */
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

    /**
     * 密码最近更新时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pwdUpdateTime;

    /**
     * 是否允许登录（1允许，0禁止）
     */
    private Integer allowed;

    /**
     * 用户状态（1正常，0停用）
     */
    private Integer status;

    /**
     * 是否（1是，0否）
     */
    private Integer delFlag;

    public SysUser() {
    }
    public SysUser(Long id) {
        this.id = id;
    }


    /**
     * 根据id判断是否是超级管理员Admin
     */
    public boolean isAdmin()
    {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long id)
    {
        return id != null && 1L == id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Xss(message = "user.xss.forbidden")
    @NotBlank(message = "user.username.not.blank")
    @Size(min = LoginConfig.USERNAME_MIN_LENGTH,
            max = LoginConfig.USERNAME_MAX_LENGTH,
            message = "user.username.size")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Xss(message = "user.xss.forbidden")
    @NotBlank(message = "user.password.not.blank")
    @Size(min = LoginConfig.PASSWORD_MIN_LENGTH,
            max = LoginConfig.PASSWORD_MAX_LENGTH,
            message = "user.password.size")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Xss(message = "user.name.xss.forbidden")
    @NotBlank(message = "user.name.not.blank")
    @Size(min = LoginConfig.NAME_MIN_LENGTH,
            max = LoginConfig.NAME_MAX_LENGTH,
            message = "user.name.size")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getPwdUpdateTime() {
        return pwdUpdateTime;
    }

    public void setPwdUpdateTime(LocalDateTime pwdUpdateTime) {
        this.pwdUpdateTime = pwdUpdateTime;
    }

    public Integer getAllowed() {
        return allowed;
    }

    public void setAllowed(Integer allowed) {
        this.allowed = allowed;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("personId", getPersonId())
                .append("deptId", getDeptId())
                .append("username", getUsername())
                .append("password", getPassword())
                .append("name", getName())
                .append("avatar", getAvatar())
                .append("loginIp", getLoginIp())
                .append("loginTime", getLoginTime())
                .append("pwdUpdateTime", getPwdUpdateTime())
                .append("allowed", getAllowed())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
