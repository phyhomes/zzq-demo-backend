package com.zzq.common.core.domain;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-13 11:39
 * @Author : ZZQ
 * @Desc : 统一分页数据
 */
public class PageData<E> extends HashMap<String, Object> {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 数量 */
    public static final String TOTAL = "total";

    /** 页码 */
    public static final String PAGE_NUM = "pageNum";

    /** 每页数据大小 */
    public static final String PAGE_SIZE = "pageSize";

    /** 记录 */
    public static final String RECORDS = "records";

    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private List<E> records;

    public PageData(){

    }

    public PageData(Long total, Integer pageNum, Integer pageSize, List<E> records){
        super.put(TOTAL, total);
        super.put(PAGE_NUM, pageNum);
        super.put(PAGE_SIZE, pageSize);
        super.put(RECORDS, records);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
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

    public List<E> getRecords() {
        return records;
    }

    public void setRecords(List<E> records) {
        this.records = records;
    }

    /**
     * 方便链式调用
     *
     * @param key 键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public PageData<E> put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
