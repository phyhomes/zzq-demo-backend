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
        // 当msgKey为i18n消息键时，获取对应的消息，否则直接返回
        if (isMsgKey(msgKey)) {
            MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
            return messageSource.getMessage(msgKey, args, LocaleContextHolder.getLocale());
        } else {
            return msgKey;
        }

    }

    /**
     * 判断msgKey是否为i18n消息键
     * <p>正则表达式：^[a-zA-Z]+(\.[a-zA-Z]+)+$<p/>
     * <p>^：字符串开始<p/>
     * <p>[a-zA-Z]+：第一段，一个或多个字母<p/>
     * <p>(\.[a-zA-Z]+)+：一个或多个 ( . + 字母段 )，确保至少再出现一次 .xxx，从而保证至少两个段<p/>
     * <p>$：字符串结束<p/>
     * <p>这个正则确保：<p/>
     * <p>1.不以 . 开头/结尾<p/>
     * <p>2.没有连续 .（因为每段都要求 [a-zA-Z]+）<p/>
     * <p>3.所有非 . 字符都是字母<p/>
     * <p>4.至少有一个 .（即至少两段）<p/>
     * @param msgKey 消息键
     * @return true/false
     */
    private static boolean isMsgKey(String msgKey) {
        String pattern = "^[a-zA-Z]+(\\.[a-zA-Z]+)+$";
        return msgKey.matches(pattern);
    }
}
