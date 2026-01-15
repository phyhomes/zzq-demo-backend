package com.zzq.common.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-14 12:28
 * @Author : ZZQ
 * @Desc : 统一分页数据结构
 */
public class PageData<E> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 数量 */
    private Long total;

    /** 页码 */
    private Integer pageSize;

    /** 每页数据大小 */
    private Integer pageNum;

    /** 记录 */
    private List<E> records;

    public PageData() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public List<E> getRecords() {
        return records;
    }

    public void setRecords(List<E> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("total", getTotal())
                .append("pageSize", getPageSize())
                .append("pageNum", getPageNum())
                .append("records", getRecords())
                .toString();
    }
}
