package com.zzq.framework.mapper;

import com.zzq.framework.domain.dto.SysRoleDTO;
import com.zzq.framework.domain.entity.SysDept;
import com.zzq.framework.domain.entity.SysLoginInfo;
import com.zzq.framework.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-06 09:52
 * @Author : ZZQ
 * @Desc : 系统相关 数据层接口
 */
@Mapper
public interface SysMapper {
    SysUser selectUserByUserName(String username);

    SysDept selectDeptById(Long deptId);

    List<SysRoleDTO> selectRolesByUserId(Long userId);

    Long selectMenuIdsByRoleId(Long roleId);

    Set<String> selectPermsByUserId(Long userId);

    void insertLoginInfo(SysLoginInfo sysLoginInfo);

    SysRoleDTO selectRoleByRoleId(Long roleId);
}
