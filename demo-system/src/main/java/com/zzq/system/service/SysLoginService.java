package com.zzq.system.service;

import com.zzq.common.core.domain.AjaxResult;
import com.zzq.system.domain.query.LoginQuery;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-17 19:37
 * @Author : ZZQ
 * @Desc : 系统登录服务层
 */
public interface SysLoginService {
    AjaxResult login(LoginQuery loginQuery);
}
