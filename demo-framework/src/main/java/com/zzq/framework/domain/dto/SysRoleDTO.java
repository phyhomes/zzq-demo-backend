package com.zzq.framework.domain.dto;

import com.zzq.framework.domain.entity.SysDept;
import com.zzq.framework.domain.entity.SysRole;

import java.io.Serial;
import java.util.List;
import java.util.Set;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-20 12:42
 * @Author : ZZQ
 * @Desc : 角色信息
 */
public class SysRoleDTO extends SysRole {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 菜单组 */
    // private Long[] menuIds;
    private List<Long> menuIds;

    /** 部门组（数据权限） */
    // private Long[] deptIds;
    private List<SysDept> depts;

    /** 角色权限集合 */
    private Set<String> permissions;


    public SysRoleDTO() {
    }

    /**
     * 是否为超级管理员，role_id为1就是超级管理员
     *
     * @return 结果
     */
    public boolean isAdmin()
    {
        return isAdmin(super.getId());
    }

    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public List<SysDept> getDepts() {
        return depts;
    }

    public void setDeptIds(List<SysDept> depts) {
        this.depts = depts;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
