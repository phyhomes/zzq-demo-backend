package com.zzq.system.service;

import com.zzq.framework.domain.dto.SysRoleDTO;

import java.util.List;

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
}
