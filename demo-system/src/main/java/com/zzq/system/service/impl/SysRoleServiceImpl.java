package com.zzq.system.service.impl;

import com.zzq.framework.domain.dto.SysRoleDTO;
import com.zzq.system.mapper.SysRoleMapper;
import com.zzq.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
