package com.zzq.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-16 19:14
 * @Author : ZZQ
 * @Desc : 错误信息处理工具
 */
public class ExceptionUtils {
    public static String getExceptionMessage(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }
}
