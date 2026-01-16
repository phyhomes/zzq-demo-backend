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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
    public void checkDeptDataScope(Long deptId) {
        // 获取登录用户
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        if (loginUser.isAdmin()) {
            return;
        }
        List<SysDept> depts = sysDeptMapper.listDept(loginUser.getDept());
        if (depts.stream().noneMatch(dept -> dept.getId().equals(deptId))) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.FORBIDDEN, "没有权限访问部门数据！");
        }

    }

    @Override
    public void checkDeptNameUnique(Long deptId, Long parentId, String name) {
        deptId = deptId == null ? -1L : deptId;
        Long id = sysDeptMapper.selectOneIdByNameAndParentId(name, parentId);
        if (id != null && !id.equals(deptId)) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.PARA_ERROR, "部门名称已存在！");
        }
    }

    @Override
    public AjaxResult update(SysDeptDTO dept) {
        SysDept sysDept = converter.convert(dept, SysDept.class);
        sysDept.setUpdateBy(SecurityUtils.getUsername());

        SysDept parentDept = sysDeptMapper.selectDeptById(sysDept.getParentId());
        SysDept oldDept = sysDeptMapper.selectDeptById(sysDept.getId());
        if (Constants.DELETED.equals(oldDept.getDelFlag())) {
            throw new BaseException(ModuleConstants.SYSTEM, HttpStatus.FORBIDDEN, "要更新的部门已删除！");
        }
        // 判断要更新的内容是否影响部门层级变化
        if (Objects.equals(sysDept.getSeq(), oldDept.getSeq())
            && Objects.equals(sysDept.getParentId(), oldDept.getParentId())) {
            // 不影响部门层级变化，直接更新
            return update(sysDept);
        }

        // 处理这个要更新的部门和同级之间的关系
        List<SysDept> siblingDepts = sysDeptMapper.selectDeptsByParentId(sysDept.getParentId());
        if (CollectionUtils.isEmpty(siblingDepts)) {
            // 没有同级，更新level
            String level = parentDept.getLevel() + ".1";
            sysDept.setLevel(level);
            return update(sysDept);
        }
        // 有一个同级，且同级部门就是自己
        if (siblingDepts.size() == 1 && siblingDepts.getFirst().getId().equals(sysDept.getId())) {
            // 直接更新
            return update(sysDept);
        }
        // 有多个同级
        // 过滤掉同级中的自己（可能存在也可能不存在）
        siblingDepts = siblingDepts.stream()
                // 过滤，满足条件的才会被留下
                .filter(siblingDept -> !siblingDept.getId().equals(sysDept.getId()))
                .collect(Collectors.toList());
        // 加自己
        siblingDepts.add(sysDept);
        // 排序
        siblingDepts.sort(Comparator.comparing(SysDept::getSeq,
                        Comparator.nullsLast(Comparator.reverseOrder())));
        for (int i = 1; i < siblingDepts.size(); i++) {
            String level = parentDept.getLevel() + "." + (i + 1);
            siblingDepts.get(i).setUpdateBy(SecurityUtils.getUsername());
            siblingDepts.get(i).setLevel(level);
            sysDeptMapper.update(siblingDepts.get(i));
        }

        return AjaxResult.success("put.success");
    }

    private AjaxResult update(SysDept sysDept) {
        int rows = sysDeptMapper.update(sysDept);
        return AjaxResult.toAjaxResult(rows, "put.success", "put.failed");
    }
}
