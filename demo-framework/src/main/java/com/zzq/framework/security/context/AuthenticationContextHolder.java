package com.zzq.framework.security.context;

import org.springframework.security.core.Authentication;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 22:22
 * @Author : ZZQ
 * @Desc : 身份验证信息
 */
public class AuthenticationContextHolder {
    private static final ThreadLocal<Authentication> contextHolder = new ThreadLocal<>();

    public static Authentication getContext()
    {
        return contextHolder.get();
    }

    public static void setContext(Authentication context)
    {
        contextHolder.set(context);
    }

    public static void clearContext()
    {
        contextHolder.remove();
    }
}
