package com.zzq.system.service;

import com.zzq.framework.domain.entity.SysOperationLog;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 20:24
 * @Author : ZZQ
 * @Desc : 系统操作日志服务层 接口
 */
public interface SysOperationLogService {
    /**
     * 新增操作日志
     *
     * @param operationLog 操作日志对象
     */
    public void insertOperationLog(SysOperationLog operationLog);
}
