package com.zzq.system.domain.entity;

import com.zzq.common.core.domain.BaseEntity;
import com.zzq.system.domain.SysConfigVO;
import io.github.linpeilie.annotations.AutoMapper;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-17 19:50
 * @Author : ZZQ
 * @Desc : 参数配置表 sys_config
 */
@AutoMapper(target = SysConfigVO.class)
public class SysConfig extends BaseEntity {
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

    /** 系统内置（Y是 N否） */
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("configId", getId())
                .append("configName", getName())
                .append("configKey", getConfigKey())
                .append("configValue", getConfigValue())
                .append("configType", getType())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
