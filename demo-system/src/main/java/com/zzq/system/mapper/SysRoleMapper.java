package com.zzq.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-20 13:07
 * @Author : ZZQ
 * @Desc : 系统角色 数据层
 */
@Mapper
public interface SysRoleMapper {
    /**
     * 根据用户ID获取权限列表
     *
     * @param id 用户ID
     * @return 权限列表
     */
    Set<String> selectRoleKeysByUserId(Long id);

    /**
     * 根据用户ID获取角色ID列表
     *
     * @param id 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(Long id);

    Set<String> selectPermissionsByRoleIds(List<Long> roleIds);


//    public List<SysRoleDTO> selectRolesByUserId(Long userId);
}
