package com.zzq.framework.service.impl;

import com.zzq.common.constant.CacheConstants;
import com.zzq.common.constant.Constants;
import com.zzq.common.core.redis.RedisCache;
import com.zzq.framework.utils.SecurityUtils;
import com.zzq.framework.security.context.AuthenticationContextHolder;
import com.zzq.framework.service.SysService;
import com.zzq.framework.domain.dto.LoginUserDTO;
import com.zzq.framework.domain.dto.SysRoleDTO;
import com.zzq.framework.domain.entity.SysDept;
import com.zzq.framework.domain.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 21:19
 * @Author : ZZQ
 * @Desc : 用户验证处理 实现security提供的抽象类
 */

/*
 *   实现 UserDetailsService 接口中的 loadUserByUsername 去校验用户名
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    public static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private RedisCache redisCache;


    @Autowired
    private SysService sysService;

    @Value(value = "${user.maxLoginFailCount}")
    private int maxLoginFailCount;

    @Value(value = "${user.lockTime}")
    private int lockTime;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
         * 在这里面抛出的所有 RunTimeException 都会被 Spring Security 捕获，并重新处理
         * 定义的错误码不会被传递下去
         */
        // 通过用户名从数据库中查询相关用户数据
        // SysUser user = sysUserService.selectUserByUserName(username);
        SysUser user = sysService.selectUserByUserName(username);
        if (user == null) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("msg");
            // throw new BaseException(ModuleConstants.USER, HttpStatus.NOT_FOUND, "user.not.exists");
        }
        if (user.getDelFlag() == 1) {
            log.info("登录用户：{} 已被删除.", username);
            throw new UsernameNotFoundException("msg");
            // throw new BaseException(ModuleConstants.USER, HttpStatus.NOT_FOUND, "user.deleted");
        }
        if (user.getStatus() == 0) {
            log.info("登录用户：{} 已被停用.", username);
            throw new UsernameNotFoundException("msg");
            // throw new BaseException(ModuleConstants.USER, HttpStatus.FORBIDDEN, "user.blocked");
        }

        validatePassword(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        // 根据用户的deptId查询部门信息
        // SysDept dept = sysDeptService.selectDeptById(user.getDeptId());
        SysDept dept = sysService.selectDeptById(user.getDeptId());

        // 根据用户的id查询角色列表
        // List<SysRoleDTO> roles = sysRoleService.selectRolesByUserId(user.getId());
        List<SysRoleDTO> roles = sysService.selectRolesByUserId(user.getId());
        Set<String> permissions = getUserPermissions(roles);
        if (CollectionUtils.isEmpty(permissions)) {
            // 判断是不是该用户是不是Admin
            if (user.isAdmin()) {
                permissions.add("*:*:*");
            } else {
                // 根据用户id获取用户权限
                permissions = sysService.selectPermsByUserId(user.getId());
            }
        }
        return new LoginUserDTO(user, dept, roles, permissions);
    }

    public Set<String> getUserPermissions(List<SysRoleDTO> roles) {
        // 登录用户可访问菜单，set集合可以自动去重
        Set<String> perms = new HashSet<String>();
        // 判断roles是否为null或空集合（不包含任何元素）
        //
        if (CollectionUtils.isEmpty(roles)) {
            return perms;
        }

        // 多角色设置permissions属性，以便数据权限匹配权限
        for (SysRoleDTO role : roles) {
            // role 非null，状态正常，非超级管理员
            if (role != null
                    && Objects.equals(role.getStatus(), Constants.NORMAL)
                    && !role.isAdmin() ) {
                // 获取角色权限
                perms.addAll(role.getPermissions());
            }
        }
        return perms;
    }

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    public String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validatePassword(SysUser sysUser){
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        Integer loginFailCount = redisCache.getCacheObject(getCacheKey(username), Integer.class);
        // 第一次登录，初始化缓存
        if (loginFailCount == null) {
            loginFailCount = 0;
        }
        // 密码输入错误次数超过最大次数则锁定
        if (loginFailCount >= maxLoginFailCount)
        {
            throw new UsernameNotFoundException("msg");
            /*throw new BaseException(ModuleConstants.USER,
                    HttpStatus.FORBIDDEN,
                    "user.password.retry.limit.exceed",
                    new Object[] { maxLoginFailCount, lockTime }
            );*/
        }
        // 校验密码正确性
        if (!matches(password, sysUser.getPassword())) {
            loginFailCount++;
            // 将错误次数存入redis缓存中，设置过期时间
            redisCache.setCacheObject(getCacheKey(username), loginFailCount, (long) lockTime, TimeUnit.HOURS);
            // throw new BaseException(ModuleConstants.USER, HttpStatus.PARA_ERROR, "user.password.not.match");
            throw new UsernameNotFoundException("msg");
        }

        // 密码校验正确，清除redis缓存中的登录错误次数记录
        clearLoginRecordCache(username);



    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param password 加密后字符
     * @return 结果
     */
    private boolean matches(String rawPassword, String password)
    {
        // user是数据库中查到的用户信息，rawPassword是前端传来的密码（用户输入的）
        return SecurityUtils.matchesPassword(rawPassword, password);
    }

    private void clearLoginRecordCache(String username)
    {
        if (redisCache.hasKey(getCacheKey(username)))
        {
            redisCache.deleteObject(getCacheKey(username));
        }
    }
}
