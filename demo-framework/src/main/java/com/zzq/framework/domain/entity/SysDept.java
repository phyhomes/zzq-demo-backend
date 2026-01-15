package com.zzq.framework.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zzq.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 09:55
 * @Author : ZZQ
 * @Desc : 部门表 sys_dept
 */
public class SysDept extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    private Long id;

    /** 部门名称 */
    private String name;

    /** 父部门ID */
    private Long parentId;

    /** 部门级别字符串，以.分隔 */
    private String level;

    /** 部门本级排序 */
    private Integer seq;

    /** 全局序号，由程序自动生成 */
    private Integer seqGlobal;

    /** 部门状态（1正常 0停用） */
    private Integer status;

    /** 是否删除（1是，0否） */
    @JsonIgnore
    private Integer delFlag;

    public SysDept() {
    }

    public SysDept(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank(message = "dept.name.not.blank")
    @Size(max = 30, message = "dept.name.size")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getSeqGlobal() {
        return seqGlobal;
    }

    public void setSeqGlobal(Integer seqGlobal) {
        this.seqGlobal = seqGlobal;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("parentId", getParentId())
            .append("level", getLevel())
            .append("seq", getSeq())
            .append("seqGlobal", getSeqGlobal())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
