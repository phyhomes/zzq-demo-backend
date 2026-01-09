package com.zzq.system.service.impl;

import com.zzq.common.core.redis.RedisCache;
import com.zzq.framework.domain.entity.SysUser;
import com.zzq.system.mapper.SysUserMapper;
import com.zzq.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void updateLoginInfo(Long userId, String loginIp) {
        sysUserMapper.updateLoginInfo(userId, loginIp);
    }

    @Override
    public SysUser selectUserByUserName(String username) {
        return sysUserMapper.selectUserByUserName(username);
    }
}
