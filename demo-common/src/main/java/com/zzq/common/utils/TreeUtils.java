package com.zzq.common.utils;

import com.zzq.common.core.domain.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-12 15:24
 * @Author : ZZQ
 * @Desc : 树形结构工具类
 */
public class TreeUtils {

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 列表
     * @param rootId 传入的根节点ID
     * @return String
     */
    public static <T extends TreeNode<T>> List<T> listToTree(List<T> list, Long rootId) {
        List<T> returnList = new ArrayList<T>();
        for (T node : list) {
            // 根据传入的某个父节点ID，遍历该父节点的所有子节点
            if (Objects.equals(node.getParentId(), rootId)) {
                recursionFn(list, node);
                returnList.add(node);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 列表
     * @param t 子节点
     */
    private static <T extends TreeNode<T>> void recursionFn(List<T> list, T t)
    {
        // 得到子节点列表
        List<T> childList = getChildList(list, t);
        t.setChildren(childList);
        for (T tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private static <T extends TreeNode<T>> List<T> getChildList(List<T> list, T t)
    {
        List<T> tlist = new ArrayList<T>();
        for (T value : list) {
            if (Objects.equals(value.getParentId(), t.getId())) {
                tlist.add(value);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private static <T extends TreeNode<T>> boolean hasChild(List<T> list, T t)
    {
        for (T value : list) {
            if (Objects.equals(value.getParentId(), t.getId())) {
                return true;
            }
        }
        return false;
    }



}
