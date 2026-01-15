package com.zzq.framework.security.context;

import com.zzq.common.utils.ConvertUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 权限信息上下文管理工具
 * 实现在代码中的不同地方便捷获取当前请求的权限信息
 * 
 * @author ruoyi
 */
public class PermissionContextHolder
{
    private static final String PERMISSION_CONTEXT_ATTRIBUTES = "PERMISSION_CONTEXT";

    /**
     * 设置上下文
     * @param permission 权限字符串
     */
    public static void setContext(String permission)
    {
        // RequestContextHolder 是 Spring 框架中核心的请求上下文管理工具类
        RequestContextHolder
                // 获取当前请求上下文
                .currentRequestAttributes()
                // 设置指定作用域的属性值
                // SCOPE_REQUEST：请求级作用域
                .setAttribute(PERMISSION_CONTEXT_ATTRIBUTES, permission, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 获取上下文
     * @return 权限字符串
     */
    public static String getContext()
    {
        return ConvertUtils.toStr(
                RequestContextHolder
                        .currentRequestAttributes()
                        // 获取指定作用域的属性值
                        .getAttribute(PERMISSION_CONTEXT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST)
        );
    }
}
