package com.zzq.common.utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzq.common.constant.HttpStatus;
import com.zzq.common.constant.ModuleConstants;
import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.core.domain.PageData;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.exception.BaseException;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Stream;

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

    /**
     * 从HTTP请求中获取分页参数
     * @return 分页索引值
     */
    private static Integer getPageNum() {
        return ConvertUtils.toInt(ServletUtils.getParameter(PAGE_NUM), 1);
    }

    /**
     * 从HTTP请求中获取分页参数
     * @return 分页大小
     */
    private static Integer getPageSize() {
        return ConvertUtils.toInt(ServletUtils.getParameter(PAGE_SIZE), 10);
    }
    /** 从请求参数中获取分页相关的参数
     * @return 分页请求对象
     */
    private static PageQuery getPageQuery() {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(getPageNum());
        pageQuery.setPageSize(getPageSize());
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

    /**
     * 检查分页参数是否合理，只能是10，20，50
     */
    public static void checkPageSize() {
        Integer pageSize = getPageSize();
        List<Integer> validPageSize = Stream.of(10, 20, 50).toList();
        if (!validPageSize.contains(pageSize)) {
            throw new BaseException(ModuleConstants.QUERY, HttpStatus.PARA_ERROR, "param.page.size");
        }
    }

    public static void setDefaultOrder(boolean asc) {
        
        String orderBy = asc ? "create_time asc": "create_time desc";
        PageHelper.orderBy(orderBy);
    }

    /**
     * 获取数据总量大小
     * @param list 数据列表
     * @return 总量大小
     */
    public static Long getTotal(List<?> list){
        return new PageInfo<>(list).getTotal();
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }

    /**
     * 获取分页形式的数据
     * @param list 数据
     * @return 分页形式的数据 {@link PageData}
     * @param <E> 列表的类型
     */
    private static <E> PageData<E> getData(List<E> list) {
        PageData<E> data = new PageData<>();
        data.setPageNum(getPageNum());
        data.setPageSize(getPageSize());
        data.setRecords(list);
        data.setTotal(getTotal(list));
        return data;
    }

    public static <E> AjaxResult getAjaxResult(List<E> list) {
        if (CollectionUtils.isEmpty(list)) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "get.empty");
        } else {
            return AjaxResult.success(getData(list));
        }
    }
}
