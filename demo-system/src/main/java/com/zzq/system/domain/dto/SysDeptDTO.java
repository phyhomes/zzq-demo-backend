package com.zzq.system.domain.dto;

import com.zzq.framework.domain.entity.SysDept;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-16 18:49
 * @Author : ZZQ
 * @Desc : {@link SysDept}
 */
@AutoMapper(target = SysDept.class)
public class SysDeptDTO {
    /** 部门ID */
    private Long id;

    /** 部门名称 */
    @NotBlank(message = "dept.name.not.blank")
    @Size(max = 30, message = "dept.name.size")
    private String name;

    /** 父部门ID */
    @NotNull(message = "dept.parent.id.not.blank")
    private Long parentId;

    /** 部门本级排序 */
    private Integer seq;


    /** 部门状态（1正常 0停用） */
    private Integer status;

    /** 备注信息 */
    private String remark;

    /** 子部门 */
    private List<SysDeptDTO> children;

    public SysDeptDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SysDeptDTO> getChildren() {
        return children;
    }

    public void setChildren(List<SysDeptDTO> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("parentId", parentId)
                .append("seq", seq)
                .append("status", status)
                .append("remark", remark)
                .append("children", children)
                .toString();
    }
}
