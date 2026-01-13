package com.zzq.system.controller;

import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.core.domain.PageData;
import com.zzq.common.utils.PageUtils;
import com.zzq.system.domain.SysConfigQuery;
import com.zzq.system.domain.SysConfigVO;
import com.zzq.system.domain.entity.SysConfig;
import com.zzq.system.service.SysConfigService;
import io.github.linpeilie.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-13 11:30
 * @Author : ZZQ
 * @Desc : todo
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController {

    @Autowired
    private Converter converter;

    @Autowired
    private SysConfigService sysConfigService;


    /**
     * 获取参数配置列表
     */
    @GetMapping("/list")
    public AjaxResult list(SysConfigQuery sysConfigQuery) {
        SysConfig sysConfig = converter.convert(sysConfigQuery, SysConfig.class);
        return sysConfigService.listSysConfig(sysConfig);
    }

}
