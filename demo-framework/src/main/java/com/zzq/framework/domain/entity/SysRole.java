package com.zzq.framework.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zzq.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-20 11:38
 * @Author : ZZQ
 * @Desc : sys_role 系统角色表
 */
public class SysRole extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色键值
     */
    private String roleKey;

    /**
     * 角色排序
     */
    private Integer seq;

    /**
     * 数据权限（1全部，2自定数据权限，3本部门，4本部门及以下）
     */
    private Integer dataScope;

    /**
     * 菜单树选项是否关联勾选（1是，0否）
     */
    private Integer menuAssociateFlag;

    /**
     * 部门树选项是否关联勾选（1是，0否）
     */
    private Integer deptAssociateFlag;

    /**
     * 角色状态（1正常，0停用）
     */
    private Integer status;

    /**
     * 是否已删除（1是，0否）
     */
    @JsonIgnore
    private Integer delFlag;

    public SysRole() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank(message = "role.name.not.blank")
    @Size(max = 30, message = "role.name.size")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @NotBlank(message = "role.key.not.blank")
    @Size(max = 100, message = "role.key.size")
    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @NotNull(message = "role.data.scope.not.null")
    @Size(max = 4, message = "role.data.scope.size")
    public Integer getDataScope() {
        return dataScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }

    public Integer getMenuAssociateFlag() {
        return menuAssociateFlag;
    }

    public void setMenuAssociateFlag(Integer menuAssociateFlag) {
        this.menuAssociateFlag = menuAssociateFlag;
    }

    public Integer getDeptAssociateFlag() {
        return deptAssociateFlag;
    }

    public void setDeptAssociateFlag(Integer deptAssociateFlag) {
        this.deptAssociateFlag = deptAssociateFlag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("seq", getSeq())
            .append("dataScope", getDataScope())
            .append("menuAssociateFlag", getMenuAssociateFlag())
            .append("deptAssociateFlag", getDeptAssociateFlag())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
