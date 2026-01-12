package com.zzq.system.domain;

import com.zzq.common.core.domain.TreeNode;
import com.zzq.system.domain.entity.SysMenu;

import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-12 15:53
 * @Author : ZZQ
 * @Desc :
 */
public class SysMenuTree extends SysMenu implements TreeNode<SysMenuTree> {
    private List<SysMenuTree> children;
    public SysMenuTree() {}

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public Long getParentId() {
        return super.getParentId();
    }

    public List<SysMenuTree> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<SysMenuTree> children) {
        this.children = children;
    }
}
