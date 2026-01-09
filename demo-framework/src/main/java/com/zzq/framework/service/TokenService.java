package com.zzq.framework.service;

import com.zzq.framework.domain.dto.LoginUserDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 11:19
 * @Author : ZZQ
 * @Desc : token验证处理 服务层接口
 */
public interface TokenService {
    /**
     * 创建令牌
     *
     * @param loginUser 登录信息
     * @return 令牌
     */
    Map<String, String> createToken(LoginUserDTO loginUser);

    /**
     * 解析令牌
     *
     * @param token 令牌
     * @return 登录信息
     */
    Map<String, String> parseToken(String token);

    /**
     * 从请求头中获取登录用户信息
     *
     * @param request HTTP请求
     * @return 登录信息
     */
    LoginUserDTO getLoginUser(HttpServletRequest request);

    /**
     * 删除用户在Redis中的缓存记录
     *
     * @param uuid 用户ID
     */
    void delLoginUser(String uuid);


}
