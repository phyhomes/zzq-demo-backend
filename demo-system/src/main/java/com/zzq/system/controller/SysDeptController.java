package com.zzq.system.controller;

import com.zzq.common.annotation.Log;
import com.zzq.common.constant.Constants;
import com.zzq.common.constant.HttpStatus;
import com.zzq.common.constant.ModuleConstants;
import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.enums.BusinessType;
import com.zzq.common.exception.BaseException;
import com.zzq.system.domain.dto.SysDeptDTO;
import com.zzq.system.service.SysDeptService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

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

    @PostMapping
    @Log(module = "部门管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    public AjaxResult insert(@Valid @RequestBody SysDeptDTO dept) {
        log.info("新增部门：{}", dept);
        // 校验部门状态值
        // checkDeptStatus(dept.getStatus());
        // 检验部门名称
        if (StringUtils.isBlank(dept.getName())) {
            return AjaxResult.error(HttpStatus.PARA_ERROR, "dept.name.not.blank");
        }
        // 要求有父部门ID
        if (dept.getParentId() == null) {
            return AjaxResult.error("dept.parent.id.not.blank");
        }
        return sysDeptService.insert(dept);

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
    public AjaxResult update(@RequestBody SysDeptDTO dept) {
        log.info("修改部门：{}", dept);
        Long deptId = dept.getId();
        if (deptId == null) {
            return AjaxResult.error("dept.id.not.blank");
        }
        // 校验部门状态值
        // checkDeptStatus(dept.getStatus());
        // 校验父部门
        if (deptId.equals(dept.getParentId())) {
            return AjaxResult.error("dept.parent.not.self");
        }
        if (StringUtils.isBlank(dept.getName())) {
            dept.setName(null);
        }

        return sysDeptService.update(dept);
    }

    public void checkDeptStatus(Integer status) {
        // 校验状态值
        if (status == null) {
            return;
        }
        Set<Integer> validStatus = Set.of(Constants.NORMAL, Constants.DISABLE);
        if (validStatus.contains(status)){
            return;
        }
        throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.PARA_ERROR, "dept.status.error");
    }
}
