package com.zzq.system.service.impl;

import com.zzq.common.annotation.DataScope;
import com.zzq.common.constant.Constants;
import com.zzq.common.constant.HttpStatus;
import com.zzq.common.constant.ModuleConstants;
import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.exception.BaseException;
import com.zzq.common.utils.SpringUtils;
import com.zzq.framework.domain.dto.LoginUserDTO;
import com.zzq.framework.domain.entity.SysDept;
import com.zzq.framework.utils.SecurityUtils;
import com.zzq.system.domain.dto.SysDeptDTO;
import com.zzq.system.mapper.SysDeptMapper;
import com.zzq.system.service.SysDeptService;
import io.github.linpeilie.Converter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 22:51
 * @Author : ZZQ
 * @Desc : 部门服务层接口实现
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {
    @Autowired
    private Converter converter;

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Override
    public SysDept selectDeptById(Long deptId) {
        return null;
    }

    @Override
    public List<SysDept> listDept() {
        // 获取登录用户
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        List<SysDept> depts = sysDeptMapper.listDept(loginUser.getDept());

        return depts;
    }

    public void checkDeptDataScope(Long deptId) {
        // 获取登录用户
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        if (loginUser.isAdmin()) {
            return;
        }
        // 约定根部门只能由超级管理员添加
        if (deptId == 0L) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.FORBIDDEN, "没有权限添加此部门！");
        }
        List<SysDept> depts = sysDeptMapper.listDept(loginUser.getDept());
        if (depts.stream().noneMatch(dept -> dept.getId().equals(deptId))) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.FORBIDDEN, "没有权限访问部门数据！");
        }
    }

    /**
     * 校验部门名称唯一性
     * <br/>父部门相同情况下，部门名称不能重复
     *
     * @param deptId 部门ID
     * @param parentId 父部门ID
     * @param name 部门名称
     */
    public void checkDeptNameUnique(Long deptId, Long parentId, String name) {
        if (name == null) {
            return;
        }
        if (name.length() > 30) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.PARA_ERROR, "部门名称长度不能超过30个字符！");
        }
        deptId = deptId == null ? -1L : deptId;
        Long id = sysDeptMapper.selectOneIdByNameAndParentId(name, parentId);
        if (id != null && !id.equals(deptId)) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.PARA_ERROR, "部门名称已存在！");
        }
    }

    /**
     * 校验父部门
     * <br/>父部门不能为空（当parentId != 0时）
     * <br/>父部门不能为删除状态
     * <br/>父部门不能为停用状态
     *
     * @param parentDept 父部门
     */
    public void checkParentDept(SysDept parentDept) {
        if (parentDept == null) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.PARA_ERROR, "父部门不存在！");
        }
        if (parentDept.getDelFlag().equals(Constants.DELETED)) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.PARA_ERROR, "父部门不存在！");
        }
        // 当父节点为停用状态，不允许添加子节点
        if (parentDept.getStatus().equals(Constants.DISABLE)) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.FORBIDDEN, "父部门已停用！");
        }
    }

    /**
     * 更新部门
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public AjaxResult update(SysDeptDTO dept) {
        Long deptId = dept.getId();

        // 校验数据权限
        checkDeptDataScope(deptId);
        // 获取数据库中存储的原始部门信息
        SysDept oldDept = sysDeptMapper.selectDeptById(deptId);
        if (oldDept == null || Constants.DELETED.equals(oldDept.getDelFlag())) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.FORBIDDEN, "找不到要更新的部门！");
        }
        // 校验父部门
        if (dept.getParentId() == null || Objects.equals(dept.getParentId(), oldDept.getParentId())) {
            // 父部门不变，直接更新
            // 校验部门名称，父部门相同的情况下，部门名称不能重复
            checkDeptNameUnique(deptId, oldDept.getParentId(), dept.getName());
            SysDept sysDept = converter.convert(dept, SysDept.class);
            return update(sysDept);
        }

        // 父部门变了
        // 获取父部门信息
        SysDept parentDept = null;
        List<SysDept> children = sysDeptMapper.selectDeptsByLevel(oldDept.getLevel() + ".");
        // 判断父部门id不为0的情况
        if (dept.getParentId() != 0L) {
            parentDept = sysDeptMapper.selectDeptById(dept.getParentId());
            // 校验父部门
            checkParentDept(parentDept);
            // 父部门不能是子部门
            if (children.stream().anyMatch(child -> child.getId().equals(dept.getParentId()))) {
                throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.PARA_ERROR, "父部门不能是当前部门的子部门！");
            }
        }
        // 校验部门名称，父部门相同的情况下，部门名称不能重复
        checkDeptNameUnique(deptId, dept.getParentId(), dept.getName());

        SysDept sysDept = converter.convert(dept, SysDept.class);
        // 处理这个要更新的部门和同级之间的关系
        List<SysDept> siblingDepts = sysDeptMapper.selectDeptsByParentId(sysDept.getParentId());
        int uniqueLevel = getUniqueLevel(siblingDepts);
        String oldLevel = oldDept.getLevel();
        String newLevel = parentDept == null ? String.valueOf(uniqueLevel) : parentDept.getLevel() + "." + uniqueLevel;
        children.forEach(child -> {
            String level = newLevel + child.getLevel().substring(oldLevel.length());
            child.setLevel(level);
            child.setUpdateBy(SecurityUtils.getUsername());
        });
        // 批量更新所有子部门
        sysDeptMapper.updateLevelInBatch(children);
        // 更新自己
        sysDept.setLevel(newLevel);
        return update(sysDept);
    }

    /**
     * 获取唯一的部门层级数
     *
     * @param siblingDepts 同级部门列表
     * @return 层级
     */
    private int getUniqueLevel(List<SysDept> siblingDepts) {
        if (CollectionUtils.isEmpty(siblingDepts)) {
            return 1;
        }
        int[] levels = siblingDepts.stream()
                .map(SysDept::getLevel)  // 获取level字段
                .map(level -> level.split("\\."))  // 按.分割
                .map(parts -> parts[parts.length - 1])  // 取最后一个部分
                .mapToInt(Integer::parseInt)  // 转为整数
                .toArray();
        Arrays.sort(levels);

        // 从2开始查找第一个不在数组中的数
        int candidate = 2;
        for (int level : levels) {
            if (candidate < level) {
                // 找到了不在数组中的最小数
                break;
            } else if (candidate == level) {
                // 如果相等，候选数递增
                candidate++;
            }
            // 如果candidate > level，继续循环
        }

        return candidate;
    }

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     * @return 结果
     */
    private AjaxResult update(SysDept sysDept) {
        sysDept.setUpdateBy(SecurityUtils.getUsername());
        int rows = sysDeptMapper.update(sysDept);
        return AjaxResult.toAjaxResult(rows, "put.success", "put.failed");
    }

    /**
     * 新增部门
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public AjaxResult insert(SysDeptDTO dept) {
        SysDept sysDept = converter.convert(dept, SysDept.class);
        Long parentId = sysDept.getParentId();
        // 校验部门名称，父部门相同的情况下，部门名称不能重复
        checkDeptNameUnique(null, parentId, sysDept.getName());
        // 检查权限
        checkDeptDataScope(parentId);
        // 处理同级部门之间的关系
        List<SysDept> siblingDepts = sysDeptMapper.selectDeptsByParentId(parentId);
        int uniqueLevel = getUniqueLevel(siblingDepts);
        String level = String.valueOf(uniqueLevel);
        if (parentId != 0) {
            SysDept parentDept = sysDeptMapper.selectDeptById(parentId);
            checkParentDept(parentDept);

            level = parentDept.getLevel() + "." + level;
        }
        sysDept.setLevel(level);
        sysDept.setCreateBy(SecurityUtils.getUsername());
        int rows = sysDeptMapper.insert(sysDept);
        return AjaxResult.toAjaxResult(rows, "post.success", "post.failed");
    }


}
