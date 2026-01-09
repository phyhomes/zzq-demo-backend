package com.zzq.framework.service;

import com.zzq.framework.domain.dto.SysRoleDTO;
import com.zzq.framework.domain.entity.SysDept;
import com.zzq.framework.domain.entity.SysLoginInfo;
import com.zzq.framework.domain.entity.SysOperationLog;
import com.zzq.framework.domain.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-06 09:06
 * @Author : ZZQ
 * @Desc : 系统相关服务层接口，用于登录
 *
 */
public interface SysService {
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    SysUser selectUserByUserName(String username);

    /**
     * 根据部门id查询部门信息
     * @param deptId 部门id
     * @return 部门信息
     */
    SysDept selectDeptById(Long deptId);

    /**
     * 根据用户id查询角色列表
     * @param id 用户id
     * @return 角色列表
     */
    List<SysRoleDTO> selectRolesByUserId(Long id);

    /**
     * 根据用户id查询权限列表
     * @param id 用户id
     * @return 权限列表
     */
    Set<String> selectPermsByUserId(Long id);


    /**
     * 新增系统登录日志
     *
     * @param sysLoginInfo 访问日志对象
     */
    void insertLoginInfo(SysLoginInfo sysLoginInfo);


    /**
     * 新增操作日志
     *
     * @param operationLog 操作日志对象
     */
    void insertOperationLog(SysOperationLog operationLog);
}
