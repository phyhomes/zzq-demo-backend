package com.zzq.system.mapper;

import com.zzq.framework.domain.entity.SysLoginInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 18:45
 * @Author : ZZQ
 * @Desc : 系统登录日志情况信息 数据层
 */
@Mapper
public interface SysLoginInfoMapper {
    /**
     * 新增系统登录日志
     *
     * @param sysLoginInfo 登录日志对象
     */
    public void insertLoginInfo(SysLoginInfo sysLoginInfo);


}
