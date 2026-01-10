package com.zzq.system.service.impl;

import com.zzq.common.constant.Constants;
import com.zzq.framework.domain.dto.SysRoleDTO;
import com.zzq.framework.domain.entity.SysRole;
import com.zzq.framework.domain.entity.SysUser;
import com.zzq.system.mapper.SysRoleMapper;
import com.zzq.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-20 13:04
 * @Author : ZZQ
 * @Desc : 系统角色服务层 实现类
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Override
    public List<SysRoleDTO> selectRolesByUserId(Long userId) {
//        return sysRoleMapper.selectRolesByUserId(userId);
        return null;
    }


    @Override
    public Set<String> getRoleKeysByUserId(Long id) {
        return sysRoleMapper.selectRoleKeysByUserId(id);
    }

    @Override
    public Set<String> getPermissionsByUserId(Long id) {
        // 判断用户是不是Admin
        if (SysUser.isAdmin(id)) {
            return Set.of(Constants.ALL_PERMISSION);
        }
        List<Long> roleIds = sysRoleMapper.selectRoleIdsByUserId(id);
        if (roleIds != null && !roleIds.isEmpty()) {
            // 判断角色是不是Admin
            if (roleIds.contains(1L)) {
                return Set.of(Constants.ALL_PERMISSION);
            } else {
                return sysRoleMapper.selectPermissionsByRoleIds(roleIds);
            }
        }
        // 没有角色，就没有权限
        return Set.of();

    }
}
