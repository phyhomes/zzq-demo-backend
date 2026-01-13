package com.zzq.system.domain;

import com.zzq.system.domain.entity.SysConfig;

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
    private String key;

    /** 参数键值 */
    private String value;

    /** 备注 */
    private String remark;


}
