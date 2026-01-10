package com.zzq.system.service;

import com.zzq.framework.domain.dto.SysRoleDTO;

import java.util.List;
import java.util.Set;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-20 13:03
 * @Author : ZZQ
 * @Desc : 系统角色服务层接口
 */
public interface SysRoleService {
    /**
     * 根据用户ID获取角色列表
     *
     * @return 角色列表
     */
    public List<SysRoleDTO> selectRolesByUserId(Long userId);

    /**
     * 根据用户ID获取角色键值列表
     *
     * @return 角色键值列表
     */
    Set<String> getRoleKeysByUserId(Long id);

    Set<String> getPermissionsByUserId(Long id);
}
