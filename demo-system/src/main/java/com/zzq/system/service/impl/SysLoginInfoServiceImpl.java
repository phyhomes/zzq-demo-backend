package com.zzq.system.service.impl;

import com.zzq.framework.domain.entity.SysLoginInfo;
import com.zzq.system.mapper.SysLoginInfoMapper;
import com.zzq.system.service.SysLoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 18:40
 * @Author : ZZQ
 * @Desc : 系统登录日志情况信息 服务层实现类
 */
@Service
public class SysLoginInfoServiceImpl implements SysLoginInfoService {
    @Autowired
    private SysLoginInfoMapper sysLoginInfoMapper;
    @Override
    public void insertLoginInfo(SysLoginInfo sysLoginInfo) {
        sysLoginInfoMapper.insertLoginInfo(sysLoginInfo);
    }
}
