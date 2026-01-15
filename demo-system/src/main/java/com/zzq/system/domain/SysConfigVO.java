package com.zzq.system.domain;

import com.zzq.system.domain.entity.SysConfig;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-13 21:01
 * @Author : ZZQ
 * @Desc : {@link SysConfig} 前端页面展示列表
 */
public class SysConfigVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 参数主键 */
    private Long id;

    /** 参数名称 */
    private String name;

    /** 参数键名 */
    private String configKey;

    /** 参数键值 */
    private String configValue;

    /** 备注 */
    private String remark;

    public SysConfigVO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("configKey", configKey)
                .append("configValue", configValue)
                .append("remark", remark)
                .toString();
    }
}
