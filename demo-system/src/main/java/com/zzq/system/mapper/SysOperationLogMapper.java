package com.zzq.system.mapper;

import com.zzq.framework.domain.entity.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 20:27
 * @Author : ZZQ
 * @Desc : 系统操作日志 数据层
 */
@Mapper
public interface SysOperationLogMapper {
    /**
     * 新增操作日志
     *
     * @param operationLog 操作日志对象
     */
    public void insertOperationLog(SysOperationLog operationLog);
}
