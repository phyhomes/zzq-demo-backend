package com.zzq.system.controller;

import com.zzq.common.core.domain.AjaxResult;
import com.zzq.system.domain.query.LoginQuery;
import com.zzq.system.service.SysLoginService;
import com.zzq.system.service.SysMenuService;
import com.zzq.system.service.SysUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-04 16:27
 * @Author : ZZQ
 * @Desc : 登录验证
 */
@RestController
public class SysLoginController {
    private static final Logger log = LoggerFactory.getLogger(SysLoginController.class);

    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 登录方法
     *
     * @param loginQuery 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@Valid @RequestBody LoginQuery loginQuery) {
        log.info("用户登录，登录信息：{}", loginQuery);
        return sysLoginService.login(loginQuery);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        return sysUserService.getUserInfo();
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        return sysMenuService.getRouters();
    }

}
