package com.zzq.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-04 19:58
 * @Author : ZZQ
 * @Desc : 获取i18n资源文件
 */
public class MessageUtils {
    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param msgKey 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String msgKey, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(msgKey, args, LocaleContextHolder.getLocale());
    }
}
