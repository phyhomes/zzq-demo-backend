package com.zzq.system.mapper;

import com.zzq.system.domain.vo.SysConfigVO;
import com.zzq.system.domain.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-12 09:51
 * @Author : ZZQ
 * @Desc : 系统配置信息Mapper
 */
@Mapper
public interface SysConfigMapper {
    SysConfig selectConfigByIdOrKey(Integer id, String configKey);

    List<SysConfigVO> listSysConfig(SysConfig sysConfig);
}
