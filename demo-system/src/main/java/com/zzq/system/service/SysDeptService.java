package com.zzq.system.service;

import com.zzq.framework.domain.entity.SysDept;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 22:50
 * @Author : ZZQ
 * @Desc : 部门服务层接口
 */
public interface SysDeptService {
    public SysDept selectDeptById(Long deptId);
}
