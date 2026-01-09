package com.zzq.system.mapper;

import com.zzq.framework.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 10:54
 * @Author : ZZQ
 * @Desc : 系统用户 数据层
 */
@Mapper
public interface SysUserMapper {
    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return {@link SysUser} 用户对象信息
     */
    public SysUser selectUserByUserName(String username);

    /**
     * 更新用户登录信息（IP和登录时间）
     *
     * @param userId 用户ID
     * @param loginIp 登录IP地址
     * @return 结果
     */
    public int updateLoginInfo(@Param("userId") Long userId, @Param("loginIp") String loginIp);
}
