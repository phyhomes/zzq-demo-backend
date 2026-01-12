package com.zzq.system.service.impl;

import com.zzq.common.constant.CacheConstants;
import com.zzq.common.constant.Constants;
import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.core.redis.RedisCache;
import com.zzq.common.utils.ConvertUtils;
import com.zzq.framework.domain.dto.LoginUserDTO;
import com.zzq.framework.domain.entity.SysUser;
import com.zzq.framework.service.impl.TokenServiceImpl;
import com.zzq.framework.utils.SecurityUtils;
import com.zzq.system.mapper.SysUserMapper;
import com.zzq.system.service.SysConfigService;
import com.zzq.system.service.SysRoleService;
import com.zzq.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 10:52
 * @Author : ZZQ
 * @Desc : 系统用户服务层实现
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public void updateLoginInfo(Long userId, String loginIp) {
        sysUserMapper.updateLoginInfo(userId, loginIp);
    }

    @Override
    public SysUser selectUserByUserName(String username) {
        return sysUserMapper.selectUserByUserName(username);
    }

    @Override
    public AjaxResult getUserInfo() {
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        // 角色集合（roleKey）
        Set<String> roleKeys = new HashSet<>();
        if (user.isAdmin()) {
            roleKeys.add(Constants.SUPER_ADMIN);
        } else {
            roleKeys.addAll(sysRoleService.getRoleKeysByUserId(user.getId()));
        }
        // 权限集合（权限字符串）
        Set<String> permissions = sysRoleService.getPermissionsByUserId(user.getId());
        if (!loginUser.getPermissions().equals(permissions))
        {
            loginUser.setPermissions(permissions);

            /** 和{@link TokenServiceImpl.saveUuid() }一样 */
            String uuid = loginUser.getUuid();
            String key = getUserKey(uuid);
            // 保存时的有效期：登录时间 + 有效期 - 当前时间
            Long expireTime = loginUser.getLoginTime() + loginUser.getExpireTime() - System.currentTimeMillis();
            // 缓存用户信息
            redisCache.setCacheObject(key, loginUser, expireTime, TimeUnit.MILLISECONDS);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("permissions", permissions);
        result.put("roleKeys", roleKeys);
        LocalDateTime pwdUpdateTime = user.getPwdUpdateTime();
        if (pwdUpdateTime == null) {
            result.put("isDefaultModifyPwd", initPasswordIsModify());
        } else {
            result.put("isDefaultModifyPwd", false);
        }
        result.put("isPasswordExpire", isPasswordExpire(pwdUpdateTime));
        return AjaxResult.success(result);
    }

    /**
     * 是否提醒用户修改初始密码
     *
     * @return true/false
     */
    private boolean initPasswordIsModify() {
        // 获取配置信息 - 是否要提醒用户修改密码
        Integer initPasswordModify = ConvertUtils.toInt(sysConfigService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify == 1;
    }

    /**
     * 是否提醒用户密码过期
     *
     * @param pwdUpdateTime 密码更新时间
     * @return true/false
     */
    private boolean isPasswordExpire(LocalDateTime pwdUpdateTime) {
        // 获取密码过期时间
        Integer passwordValidateDays = ConvertUtils.toInt(sysConfigService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays != null && passwordValidateDays > 0) {
            if (pwdUpdateTime == null) {
                // 如果从未修改过初始密码，则直接返回过期
                return true;
            }
            // 密码过期时间
            LocalDateTime expireTime = pwdUpdateTime.plusDays(passwordValidateDays);
            // 判断当前时间是否超过密码过期时间
            return LocalDateTime.now().isAfter(expireTime);
        }
        return false;
    }

    private String getUserKey(String uuid)
    {
        return CacheConstants.LOGIN_USER_KEY + uuid;
    }
}
