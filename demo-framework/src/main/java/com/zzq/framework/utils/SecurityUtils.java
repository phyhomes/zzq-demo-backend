package com.zzq.framework.utils;

import com.zzq.common.constant.Constants;
import com.zzq.common.constant.HttpStatus;
import com.zzq.common.constant.ModuleConstants;
import com.zzq.common.exception.BaseException;
import com.zzq.framework.domain.dto.LoginUserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.PatternMatchUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 22:20
 * @Author : ZZQ
 * @Desc : SpringSecurity 相关的工具类
 */
public class SecurityUtils {
    /**
     * 用户ID
     * @return 用户ID
     **/
    public static Long getUserId()
    {
        try
        {
            return getLoginUser().getUserId();
        }
        catch (Exception e)
        {
            throw new BaseException(ModuleConstants.AUTH, HttpStatus.UNAUTHORIZED, "获取用户ID异常：" + e.getMessage());
        }
    }

    /**
     * 获取部门ID
     * @return 部门ID
     **/
    public static Long getDeptId()
    {
        try
        {
            return getLoginUser().getDeptId();
        }
        catch (Exception e)
        {
            throw new BaseException(ModuleConstants.AUTH, HttpStatus.UNAUTHORIZED, "获取部门ID异常：" + e.getMessage());
        }
    }

    /**
     * 获取用户的用户名
     * @return 用户名
     **/
    public static String getUsername()
    {
        try
        {
            return getLoginUser().getUsername();
        }
        catch (Exception e)
        {
            throw new BaseException(ModuleConstants.AUTH, HttpStatus.UNAUTHORIZED, "获取用户名异常：" + e.getMessage());
        }
    }

    /**
     * 获取登录用户 保证不会返回Null
     * @return 登录用户对象
     **/
    public static LoginUserDTO getLoginUser()
    {
        try
        {
            LoginUserDTO loginUserDTO = (LoginUserDTO) getAuthentication().getPrincipal();
            if (loginUserDTO == null) {
                throw new BaseException(ModuleConstants.AUTH, HttpStatus.UNAUTHORIZED, "获取登录对象异常：登录用户不存在");
            }
            return loginUserDTO;
        }
        catch (Exception e)
        {
            throw new BaseException(ModuleConstants.AUTH, HttpStatus.UNAUTHORIZED, "获取登录对象异常：" + e.getMessage());
        }
    }




    /**
     * 获取Authentication
     * SecurityContextHolder是Spring Security存储当前安全上下文详细信息的地方
     * 从这里获取当前（线程）认证的用户
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermission(Collection<String> authorities, String permission)
    {
        return authorities.stream().filter(StringUtils::isNotBlank)
                .anyMatch(x -> Constants.ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
    }


    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role 角色
     * @return 用户是否具备某角色权限
     */
    public static boolean hasRole(Collection<String> roles, String role)
    {
        return roles.stream().filter(StringUtils::isNotBlank)
                .anyMatch(x -> Constants.SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }
}
