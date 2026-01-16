package com.zzq.common.core.domain;

import com.zzq.common.utils.LocalStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-13 16:43
 * @Author : ZZQ
 * @Desc : 分页相关的请求参数
 */
public class PageQuery {
    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;

    /** 排序列 */
    private String orderByColumn;

    /** 排序的方向desc或者asc，默认使用asc（升序） */
    private String sortDirection = "asc";

    /** 分页参数合理化，默认不需要合理化 */
    private Boolean reasonable = false;

    /** 无参构造 */
    public PageQuery() {}

    public String getOrderBy() {
        if (StringUtils.isEmpty(orderByColumn)) {
            return "";
        }
        return LocalStringUtils.toUnderScoreCase(orderByColumn) + " " + sortDirection;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        if (StringUtils.isNotBlank(sortDirection)) {
            sortDirection = sortDirection.toLowerCase();
            // 兼容前端排序类型，要使用升序、降序一对一对的形式
            List<String> directions = Stream.of("asc", "desc", "ascending", "descending").toList();
            if (directions.contains(sortDirection.toLowerCase())) {
                this.sortDirection = directions.get(directions.indexOf(sortDirection) % 2);
            }
        }
    }

    /** 获取合理化分页参数，默认不需要合理化 */
    public Boolean getReasonable() {
        return Objects.requireNonNullElse(reasonable, Boolean.FALSE);
    }

    public void setReasonable(Boolean reasonable) {
        this.reasonable = reasonable;
    }


}
