package com.zzq.system.service;

import com.zzq.framework.domain.entity.SysLoginInfo;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 18:29
 * @Author : ZZQ
 * @Desc : 系统登录日志情况信息 服务层
 */
public interface SysLoginInfoService {
    /**
     * 新增系统登录日志
     *
     * @param sysLoginInfo 访问日志对象
     */
    public void insertLoginInfo(SysLoginInfo sysLoginInfo);
}
