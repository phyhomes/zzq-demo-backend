package com.zzq.system.service.impl;

import com.zzq.common.constant.CacheConstants;
import com.zzq.common.core.redis.RedisCache;
import com.zzq.common.utils.StringUtils;
import com.zzq.system.domain.entity.SysConfig;
import com.zzq.system.mapper.SysConfigMapper;
import com.zzq.system.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    public SysConfigServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public SysConfig selectConfigById(Long configId) {
        return null;
    }

    @Override
    public String selectConfigByKey(String configKey) {
        // 先从Redis中查询键值
        String configValue = redisCache.getCacheObject(getCacheKey(configKey), String.class);
        if (!StringUtils.isBlank(configValue)){
            return configValue;
        }
        // Redis中没有，从数据库中查询
        SysConfig config = sysConfigMapper.selectConfigByIdOrKey(null, configKey);
        if (config != null) {
            redisCache.setCacheObject(getCacheKey(configKey), config.getValue());
            return config.getValue();
        }

        // 都没有，返回空
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

    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
