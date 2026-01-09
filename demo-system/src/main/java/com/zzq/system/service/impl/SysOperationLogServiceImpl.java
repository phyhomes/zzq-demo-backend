package com.zzq.system.service.impl;

import com.zzq.framework.domain.entity.SysOperationLog;
import com.zzq.system.mapper.SysOperationLogMapper;
import com.zzq.system.service.SysOperationLogService;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 20:26
 * @Author : ZZQ
 * @Desc : 系统操作日志记录 服务层实现类
 */
public class SysOperationLogServiceImpl implements SysOperationLogService {
    private SysOperationLogMapper operationLogMapper;
    /**
     * 新增操作日志
     *
     * @param operationLog 操作日志对象
     */
    @Override
    public void insertOperationLog(SysOperationLog operationLog) {

    }
}
