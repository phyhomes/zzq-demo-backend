package com.zzq.framework.service;

import com.zzq.common.constant.Constants;
import com.zzq.common.utils.StringUtils;
import com.zzq.framework.utils.SecurityUtils;
import com.zzq.framework.domain.dto.LoginUserDTO;
import com.zzq.framework.domain.dto.SysRoleDTO;
import com.zzq.framework.security.context.PermissionContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * RuoYi首创 自定义权限实现，ss取自SpringSecurity首字母
 * 
 * @author ruoyi
 */
// 指定了 Bean 的名称为 "ss"
@Service("ss")
public class PermissionService {
    /**
     * 验证用户是否具备某权限
     * 
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        if (StringUtils.isBlank(permission)) {
            return false;
        }
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        // 保存到上下文中
        PermissionContextHolder.setContext(permission);
        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermi(String permission) {
        return !hasPermi(permission);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 PERMISSION_DELIMITER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(String permissions)
    {
        if (StringUtils.isBlank(permissions)) {
            return false;
        }
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        PermissionContextHolder.setContext(permissions);
        // 获取登录用户具备的权限字符串集合
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(Constants.PERMISSION_DELIMITER)) {
            if (hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission)
    {
        return permissions.contains(Constants.ALL_PERMISSION)
                || permissions.contains(StringUtils.trim(permission));
    }

    /**
     * 判断用户是否拥有某个角色
     * 
     * @param role 角色键值 roleKey
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role) {
        if (StringUtils.isEmpty(role)) {
            return false;
        }
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        return hasRole(role, loginUser);
    }

    /**
     *
     * @param role 角色键值 roleKey
     * @param loginUser 登录用户对象
     * @return 用户是否含有对应角色
     */
    private boolean hasRole(String role, LoginUserDTO loginUser){

        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getRoles())) {
            return false;
        }
        for (SysRoleDTO sysRole : loginUser.getRoles()) {
            String roleKey = sysRole.getRoleKey();
            if (sysRole.isAdmin() || roleKey.equals(StringUtils.trim(role))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证用户是否不具备某角色，与 hasRole逻辑相反。
     *
     * @param role 角色键值 roleKey
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String role)
    {
        return !hasRole(role);
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 以 ROLE_DELIMITER = ',' 为分隔符的角色键值列表 roleKey
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String roles)
    {
        if (StringUtils.isEmpty(roles)) {
            return false;
        }
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getRoles())) {
            return false;
        }
        for (String role : roles.split(Constants.ROLE_DELIMITER)) {
            if (hasRole(role, loginUser)) {
                return true;
            }
        }
        return false;
    }


}
