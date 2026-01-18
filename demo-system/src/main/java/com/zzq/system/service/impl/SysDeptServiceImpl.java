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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
        if (dept.getParentId() == null) {
            dept.setParentId(oldDept.getParentId());
        } else {
            SysDept parentDept = sysDeptMapper.selectDeptById(dept.getParentId());
            checkParentDept(parentDept);
            // 父部门不能是子部门
            List<SysDept> children = sysDeptMapper.selectDeptsByLevel(oldDept.getLevel());
            if (children.stream().anyMatch(child -> child.getId().equals(dept.getParentId()))) {
                throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.PARA_ERROR, "父部门不能是当前部门的子部门！");
            }
        }
        // 校验部门名称，父部门相同的情况下，部门名称不能重复
        checkDeptNameUnique(deptId, dept.getParentId(), dept.getName());

        SysDept sysDept = converter.convert(dept, SysDept.class);
        sysDept.setUpdateBy(SecurityUtils.getUsername());
        sysDept.setLevel(oldDept.getLevel());

        // 判断要更新的内容是否影响部门层级变化
        if (Objects.equals(sysDept.getSeq(), oldDept.getSeq())
            && Objects.equals(sysDept.getParentId(), oldDept.getParentId())) {
            // 不影响部门层级变化，直接更新
            return update(sysDept);
        }

        // String oldLevel = oldDept.getLevel();
        SysDept parentDept = sysDeptMapper.selectDeptById(sysDept.getParentId());
        // 处理这个要更新的部门和同级之间的关系
        List<SysDept> siblingDepts = sysDeptMapper.selectDeptsByParentId(sysDept.getParentId());
        if (CollectionUtils.isEmpty(siblingDepts)) {
            // 设置level后直接更新
            String level = parentDept.getLevel() + ".1";
            sysDept.setLevel(level);
            return update(sysDept);
        }
        // 只有一个同级
        if (siblingDepts.size() == 1) {
            if (siblingDepts.getFirst().getId().equals(sysDept.getId())) {
                // 同级部门就是自己，直接更新
                return update(sysDept);
            } else {
                // 同级部门不是自己，加自己
                siblingDepts.add(sysDept);
            }
        } else {
            // 有多个同级
            // 过滤掉同级中的自己（可能存在也可能不存在）
            siblingDepts = siblingDepts.stream()
                    // 过滤，满足条件的才会被留下
                    .filter(siblingDept -> !siblingDept.getId().equals(sysDept.getId()))
                    .collect(Collectors.toList());
            // 加自己
            siblingDepts.add(sysDept);
        }

        // 排序
        siblingDepts.sort(Comparator.comparing(SysDept::getSeq,
                        Comparator.nullsLast(Comparator.naturalOrder())));
        List<String> oldLevelList = new ArrayList<>();
        for (int i = 0; i < siblingDepts.size(); i++) {
            String oldLevel = siblingDepts.get(i).getLevel();
            oldLevelList.add(oldLevel);
            String newLevel = parentDept == null ? String.valueOf(i + 1) : parentDept.getLevel() + "." + (i + 1);
            SysDept siblingDept = siblingDepts.get(i);
            if (!Objects.equals(oldLevel, newLevel)) {
                siblingDept.setUpdateBy(SecurityUtils.getUsername());
                siblingDept.setLevel(newLevel);
                // 处理各自的下级
                // 获取全部下级部门
                List<SysDept> childrenDepts = sysDeptMapper.selectDeptsByLevel(oldLevel + ".");
                siblingDept.setChildren(childrenDepts);
            }
        }

        List<SysDept> allList = new ArrayList<>();
        for (int i = 0; i < siblingDepts.size(); i++) {
            String oldLevel = oldLevelList.get(i);
            SysDept siblingDept = siblingDepts.get(i);
            String newLevel = siblingDept.getLevel();
            if (!oldLevel.equals(newLevel)) {
                List<SysDept> childrenDepts = siblingDept.getChildren();
                for (SysDept childrenDept : childrenDepts) {
                    String level = newLevel + childrenDept.getLevel().substring(oldLevel.length());
                    childrenDept.setUpdateBy(SecurityUtils.getUsername());
                    childrenDept.setLevel(level);
                }
                allList.add(siblingDept);
                allList.addAll(childrenDepts);
            }
        }

        sysDeptMapper.updateLevelInBatch(allList);
        // 最后更新其它字段
        return update(sysDept);
    }

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     * @return 结果
     */
    private AjaxResult update(SysDept sysDept) {
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
        String level = siblingDepts == null ? "1" : siblingDepts.size() + 1 + "";
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
