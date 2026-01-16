package com.zzq.system.mapper;

import com.zzq.common.annotation.DataScope;
import com.zzq.framework.domain.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-16 11:30
 * @Author : ZZQ
 * @Desc : todo
 */
@Mapper
public interface SysDeptMapper {
    @DataScope(deptAlias = "d")
    public List<SysDept> listDept(SysDept dept);

    Long selectOneIdByNameAndParentId(String name, Long parentId);

    int update(SysDept sysDept);

    List<SysDept> selectDeptsByParentId(Long parentId);

    SysDept selectDeptById(Long id);
}
