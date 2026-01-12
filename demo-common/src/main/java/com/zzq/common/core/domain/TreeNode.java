package com.zzq.common.core.domain;

import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-12 15:51
 * @Author : ZZQ
 * @Desc : 树节点的接口
 */
public interface TreeNode<T> {
    /**
     * 获取节点ID
     *
     * @return 节点ID
     */
    Long getId();

    /**
     * 获取父节点ID
     *
     * @return 父节点ID
     */
    Long getParentId();

    /**
     * 设置子节点列表
     *
     * @param children 子节点列表
     */
    void setChildren(List<T> children);
}
