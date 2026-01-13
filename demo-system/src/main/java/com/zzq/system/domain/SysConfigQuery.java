package com.zzq.system.domain;

import com.zzq.system.domain.entity.SysConfig;
import io.github.linpeilie.annotations.AutoMapper;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-13 23:19
 * @Author : ZZQ
 * @Desc : {@link SysConfig} 的请求参数对象
 */
@AutoMapper(target = SysConfig.class)
public class SysConfigQuery {
    /** 参数名称 */
    private String name;

    /** 参数键名 */
    private String key;

    /** 参数键值 */
    private String value;

    public SysConfigQuery() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
