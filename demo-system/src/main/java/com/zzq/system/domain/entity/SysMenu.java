package com.zzq.system.domain.entity;

import com.zzq.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * 菜单权限表
 * TableName  sys_menu
 */
public class SysMenu extends BaseEntity {
    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer seq;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 路由名称，默认和路由地址相同的驼峰格式（注意：因为vue3版本的router会删除名称相同路由，为避免名字的冲突，特殊情况可以自定义）
     */
    private String routeName;

    /**
     * 是否为外链（1是 0否）
     */
    private Integer frameFlag;

    /**
     * 是否缓存（1是，0否）
     */
    private Integer cacheFlag;

    /**
     * 菜单类型（C目录 M菜单 B按钮）
     */
    private String type;

    /**
     * 菜单状态（1显示 0隐藏）
     */
    private Integer visible;

    /**
     * 菜单状态（1正常 0停用）
     */
    private Integer status;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;



    /**
     * 菜单ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 菜单ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 菜单名称
     */
    public String getName() {
        return name;
    }

    /**
     * 菜单名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 父菜单ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 父菜单ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 显示顺序
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 显示顺序
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    /**
     * 路由地址
     */
    public String getPath() {
        return path;
    }

    /**
     * 路由地址
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 组件路径
     */
    public String getComponent() {
        return component;
    }

    /**
     * 组件路径
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * 路由参数
     */
    public String getQuery() {
        return query;
    }

    /**
     * 路由参数
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * 路由名称
     */
    public String getRouteName() {
        return routeName;
    }

    /**
     * 路由名称
     */
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    /**
     * 是否为外链（1是 0否）
     */
    public Integer getFrameFlag() {
        return frameFlag;
    }

    /**
     * 是否为外链（1是 0否）
     */
    public void setFrameFlag(Integer frameFlag) {
        this.frameFlag = frameFlag;
    }

    /**
     * 是否缓存（1是，0否）
     */
    public Integer getCacheFlag() {
        return cacheFlag;
    }

    /**
     * 是否缓存（1是，0否）
     */
    public void setCacheFlag(Integer cacheFlag) {
        this.cacheFlag = cacheFlag;
    }

    /**
     * 菜单类型（C目录 M菜单 B按钮）
     */
    public String getType() {
        return type;
    }

    /**
     * 菜单类型（C目录 M菜单 B按钮）
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 菜单状态（1显示 0隐藏）
     */
    public Integer getVisible() {
        return visible;
    }

    /**
     * 菜单状态（1显示 0隐藏）
     */
    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    /**
     * 菜单状态（1正常 0停用）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 菜单状态（1正常 0停用）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 权限标识
     */
    public String getPerms() {
        return perms;
    }

    /**
     * 权限标识
     */
    public void setPerms(String perms) {
        this.perms = perms;
    }

    /**
     * 菜单图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 菜单图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("parentId", parentId)
                .append("seq", seq)
                .append("path", path)
                .append("component", component)
                .append("query", query)
                .append("routeName", routeName)
                .append("frameFlag", frameFlag)
                .append("cacheFlag", cacheFlag)
                .append("type", type)
                .append("visible", visible)
                .append("status", status)
                .append("perms", perms)
                .append("icon", icon)
                .append("remark", super.getRemark())
                .toString();
    }
}