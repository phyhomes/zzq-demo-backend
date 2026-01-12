package com.zzq.system.service;


import com.zzq.common.core.domain.AjaxResult;

/**
 * @Project : zzq-demo-backend
 * @Time    : 2026-01-10 22:43
 * @Author  : ZZQ
 * @Desc    : 针对表【sys_menu(菜单权限表)】的数据库操作Service
 */
public interface SysMenuService {

    AjaxResult getRouters();
}
