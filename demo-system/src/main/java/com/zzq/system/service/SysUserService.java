package com.zzq.system.service;

import com.zzq.common.core.domain.AjaxResult;
import com.zzq.framework.domain.entity.SysUser;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 10:51
 * @Author : ZZQ
 * @Desc : 系统用户服务层接口
 */
public interface SysUserService {
    /**
     * 更新用户登录信息（IP和登录时间）
     *
     * @param userId 用户ID
     * @param loginIp 登录IP地址
     */
    public void updateLoginInfo(Long userId, String loginIp);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String username);

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    AjaxResult getUserInfo();
}
