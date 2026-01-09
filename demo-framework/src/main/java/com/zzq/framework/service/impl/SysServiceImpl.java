package com.zzq.framework.service.impl;

import com.zzq.framework.domain.dto.SysRoleDTO;
import com.zzq.framework.domain.entity.SysDept;
import com.zzq.framework.domain.entity.SysLoginInfo;
import com.zzq.framework.domain.entity.SysOperationLog;
import com.zzq.framework.domain.entity.SysUser;
import com.zzq.framework.mapper.SysMapper;
import com.zzq.framework.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-06 09:30
 * @Author : ZZQ
 * @Desc : 系统相关服务层接口实现类，用于登录
 */
@Service
public class SysServiceImpl implements SysService {
    @Autowired
    private SysMapper sysMapper;
    @Override
    public SysUser selectUserByUserName(String username) {
        return sysMapper.selectUserByUserName(username);
    }

    @Override
    public SysDept selectDeptById(Long deptId) {
        return sysMapper.selectDeptById(deptId);
    }

    @Override
    public List<SysRoleDTO> selectRolesByUserId(Long userId) {
        return sysMapper.selectRolesByUserId(userId);
    }

    @Override
    public Set<String> selectPermsByUserId(Long id) {
        return sysMapper.selectPermsByUserId(id);
    }

    @Override
    public void insertLoginInfo(SysLoginInfo sysLoginInfo) {
        sysMapper.insertLoginInfo(sysLoginInfo);
    }

    @Override
    public void insertOperationLog(SysOperationLog operationLog) {

    }
}
