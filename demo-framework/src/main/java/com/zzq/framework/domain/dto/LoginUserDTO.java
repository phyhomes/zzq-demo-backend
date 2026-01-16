package com.zzq.framework.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zzq.framework.domain.entity.SysDept;
import com.zzq.framework.domain.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 10:20
 * @Author : ZZQ
 * @Desc : 登录用户信息
 */
public class LoginUserDTO implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String uuid;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String location;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 用户信息
     */
    private SysUser user;

    /**
     * 用户部门
     */
    private SysDept dept;

    /**
     * 用户角色列表
     */
    private List<SysRoleDTO> roles;

    public LoginUserDTO() {
    }

    public LoginUserDTO(SysUser user, Set<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUserDTO(SysUser user, SysDept dept, List<SysRoleDTO> roles, Set<String> permissions) {
        this.user = user;
        this.dept = dept;
        this.roles = roles;
        this.permissions = permissions;
    }

    public boolean isAdmin() {
        if (user.isAdmin()) {
            return true;
        }

        if (!CollectionUtils.isEmpty(roles)) {
            for (SysRoleDTO role : roles) {
                if (role.isAdmin()) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getUserId() {
        return user.getId();
    }

    public Long getPersonId() {
        return user.getPersonId();
    }

    public Long getDeptId() {
        return user.getDeptId();
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    /**
     * 获取创建时间
     * 测试：
     *  - 对象存储到redis时，会序列化所有get方法
     *  - LocalDateTime的同样要加@Json
     * @return 创建时间
     */
    /*public LocalDateTime getCreateTime() {
        return user.getCreateTime();
    }*/

    /**
     * 获取姓名
     * 测试：对象存储到redis时，会序列化所有get方法
     * @return 用户名
     */
    /*
    public String getName () {
        return user.getName();
    }*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public void setRoles(List<SysRoleDTO> roles) {
        this.roles = roles;
    }

    public List<SysRoleDTO> getRoles() {
        return roles;
    }
}
