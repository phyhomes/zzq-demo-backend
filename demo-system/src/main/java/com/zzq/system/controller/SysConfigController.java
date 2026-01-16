package com.zzq.system.controller;

import com.zzq.common.core.domain.AjaxResult;
import com.zzq.system.domain.query.SysConfigQuery;
import com.zzq.system.domain.entity.SysConfig;
import com.zzq.system.service.SysConfigService;
import io.github.linpeilie.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-13 11:30
 * @Author : ZZQ
 * @Desc : 配置参数 - Controller层
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController {
    private static final Logger log = LoggerFactory.getLogger(SysConfigController.class);

    @Autowired
    private Converter converter;

    @Autowired
    private SysConfigService sysConfigService;


    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public AjaxResult list(SysConfigQuery sysConfigQuery) {
        log.info("获取参数配置列表，请求参数：{}", sysConfigQuery);
        SysConfig sysConfig = converter.convert(sysConfigQuery, SysConfig.class);
        return sysConfigService.listSysConfig(sysConfig);
    }

}
