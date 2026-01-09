package com.zzq.framework.security.handler;

import com.zzq.common.constant.HttpStatus;
import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.utils.ServletUtils;
import com.zzq.framework.config.SecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-17 11:37
 * @Author : ZZQ
 * @Desc : 认证失败处理类 返回未授权
 *  {@link SecurityConfig}
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationEntryPointImpl.class);
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {
        log.info("未认证访问: {}", authException.getMessage());

        AjaxResult result = AjaxResult.error(HttpStatus.UNAUTHORIZED, "未授权访问");
        ServletUtils.renderAjaxResult(response, result);
    }
}
