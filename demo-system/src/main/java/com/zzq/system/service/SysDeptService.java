package com.zzq.system.service;

import com.zzq.common.core.domain.AjaxResult;
import com.zzq.framework.domain.entity.SysDept;
import com.zzq.system.domain.dto.SysDeptDTO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 22:50
 * @Author : ZZQ
 * @Desc : 部门服务层接口
 */
public interface SysDeptService {
    public SysDept selectDeptById(Long deptId);

    List<SysDept> listDept();

    AjaxResult update(SysDeptDTO dept);

    AjaxResult insert(SysDeptDTO dept);
}
