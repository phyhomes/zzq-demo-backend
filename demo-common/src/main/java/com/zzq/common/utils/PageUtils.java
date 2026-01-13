package com.zzq.common.utils;

import com.github.pagehelper.PageHelper;
import com.zzq.common.core.domain.PageQuery;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-13 16:28
 * @Author : ZZQ
 * @Desc : 分页工具类
 */
public class PageUtils extends PageHelper {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc"
     */
    public static final String SORT_DIRECTION = "sortDirection";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /** 从请求参数中获取分页相关的参数
     * @return 分页请求对象
     */
    public static PageQuery getPageQuery() {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(ConvertUtils.toInt(ServletUtils.getParameter(PAGE_NUM), 1));
        pageQuery.setPageSize(ConvertUtils.toInt(ServletUtils.getParameter(PAGE_SIZE), 10));
        pageQuery.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        pageQuery.setSortDirection(ServletUtils.getParameter(SORT_DIRECTION));
        pageQuery.setReasonable(ServletUtils.getParameterToBool(REASONABLE));
        return pageQuery;
    }

    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        PageQuery pageQuery = getPageQuery();
        Integer pageNum = pageQuery.getPageNum();
        Integer pageSize = pageQuery.getPageSize();
        String orderBy = SqlUtils.escapeOrderBySql(pageQuery.getOrderBy());
        Boolean reasonable = pageQuery.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    public static void setDefaultOrder(boolean asc) {
        
        String orderBy = asc ? "create_time asc": "create_time desc";
        PageHelper.orderBy(orderBy);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }

}
