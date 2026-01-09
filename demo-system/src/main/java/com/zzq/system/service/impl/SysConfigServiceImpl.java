package com.zzq.system.service.impl;

import com.zzq.system.domain.entity.SysConfig;
import com.zzq.system.service.SysConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-17 19:49
 * @Author : ZZQ
 * @Desc : 系统参数配置服务层实现
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {
    @Override
    public SysConfig selectConfigById(Long configId) {
        return null;
    }

    @Override
    public String selectConfigByKey(String configKey) {
        return "";
    }

    @Override
    public boolean selectCaptchaEnabled() {
        return false;
    }

    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        return List.of();
    }

    @Override
    public int insertConfig(SysConfig config) {
        return 0;
    }

    @Override
    public int updateConfig(SysConfig config) {
        return 0;
    }

    @Override
    public void deleteConfigByIds(Long[] configIds) {

    }

    @Override
    public void loadingConfigCache() {

    }

    @Override
    public void clearConfigCache() {

    }

    @Override
    public void resetConfigCache() {

    }

    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        return false;
    }
}
