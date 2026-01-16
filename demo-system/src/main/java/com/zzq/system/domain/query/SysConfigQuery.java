package com.zzq.system.domain.query;

import com.zzq.system.domain.entity.SysConfig;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-13 23:19
 * @Author : ZZQ
 * @Desc : {@link SysConfig} 的请求参数对象
 */

public class SysConfigQuery {
    /** 参数名称 */
    private String name;

    /** 参数键名 */
    private String configKey;

    /** 参数键值 */
    private String configValue;

    public SysConfigQuery() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("configKey", configKey)
                .append("configValue", configValue)
                .toString();
    }
}
