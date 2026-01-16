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
public class PageDataBack<E> extends HashMap<String, Object> {
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

    public PageDataBack(){

    }

    public PageDataBack(Long total, Integer pageNum, Integer pageSize, List<E> records){
        super.put(TOTAL, total);
        super.put(PAGE_NUM, pageNum);
        super.put(PAGE_SIZE, pageSize);
        super.put(RECORDS, records);
    }

    public Long getTotal() {
        return (Long) super.get(TOTAL);
    }

    public void setTotal(Long total) {
        super.put(TOTAL, total);
    }

    public Integer getPageNum() {
        return (Integer) super.get(PAGE_NUM);
    }

    public void setPageNum(Integer pageNum) {
        super.put(PAGE_NUM, pageNum);
    }

    public Integer getPageSize() {
        return (Integer) super.get(PAGE_SIZE);
    }

    public void setPageSize(Integer pageSize) {
        super.put(PAGE_SIZE, pageSize);
    }

    // public List<E> getRecords() {
    //     if(super.get(RECORDS) instanceof List<E> )
    //     return (List<E>) super.get(RECORDS);
    // }

    public void setRecords(List<E> records) {
        super.put(RECORDS, records);
    }

    /**
     * 方便链式调用
     *
     * @param key 键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public PageDataBack<E> put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
