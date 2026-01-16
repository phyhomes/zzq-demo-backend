package com.zzq.system.controller;

import com.zzq.common.annotation.Log;
import com.zzq.common.constant.Constants;
import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.enums.BusinessType;
import com.zzq.system.domain.dto.SysDeptDTO;
import com.zzq.system.service.SysDeptService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-16 11:13
 * @Author : ZZQ
 * @Desc : 部门相关Controller层
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController {
    private static final Logger log = LoggerFactory.getLogger(SysDeptController.class);

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 获取部门列表
     *
     * @return 部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.wrapList(sysDeptService.listDept());
    }

    /**
     * 修改部门
     *
     * @param dept 部门对象
     * @return 修改结果
     */
    @PutMapping
    @Log(module = "部门管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    public AjaxResult update(@Valid @RequestBody SysDeptDTO dept) {
        log.info("修改部门：{}", dept);
        Long deptId = dept.getId();
        if (deptId == null) {
            return AjaxResult.error("dept.id.not.blank");
        }
        // 校验状态值
        if (dept.getStatus() != null) {
            if (!dept.getStatus().equals(Constants.NORMAL)
                    && !dept.getStatus().equals(Constants.DISABLE)){
                return AjaxResult.error("dept.status.error");
            }
        }
        // 校验数据权限
        sysDeptService.checkDeptDataScope(deptId);
        // 校验部门名称，父部门相同的情况下，部门名称不能重复
        sysDeptService.checkDeptNameUnique(deptId, dept.getParentId(), dept.getName());
        if (deptId.equals(dept.getParentId())) {
            return AjaxResult.error("dept.parent.not.self");
        }

        return sysDeptService.update(dept);
    }
}
