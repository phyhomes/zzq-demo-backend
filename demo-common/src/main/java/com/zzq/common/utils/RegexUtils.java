package com.zzq.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 00:21
 * @Author : ZZQ
 * @Desc : 正则相关工具类
 */
public class RegexUtils {
    /**
     * 指定内容中是否有表达式匹配的内容
     *
     * @param pattern 编译后的正则模式
     * @param content 被查找的内容
     * @return 指定内容中是否有表达式匹配的内容
     */
    public static boolean contains(Pattern pattern, CharSequence content) {
        if (null == pattern || null == content) {
            return false;
        }
        return pattern.matcher(content).find();
    }

    /**
     * 获得匹配的字符串，获得正则中分组1的内容
     *
     * @param pattern 编译后的正则模式
     * @param content 被匹配的内容
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String getGroup1(Pattern pattern, CharSequence content) {
        return get(pattern, content, 1);
    }

    /**
     * 获得匹配的字符串，对应分组0表示整个匹配内容，1表示第一个括号分组内容，依次类推
     *
     * @param pattern    编译后的正则模式
     * @param content    被匹配的内容
     * @param groupIndex 匹配正则的分组序号，0表示整个匹配内容，1表示第一个括号分组内容，依次类推
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String get(Pattern pattern, CharSequence content, int groupIndex) {
        if (null == content || null == pattern) {
            return null;
        }
        final List<String> result = new ArrayList<>();
        get(pattern, content, matcher -> result.add(matcher.group(groupIndex)));
        return result.getFirst();
    }

    /**
     * 在给定字符串中查找给定规则的字符，如果找到则使用{@link Consumer}处理之<br>
     * 如果内容中有多个匹配项，则只处理找到的第一个结果。
     *
     * @param pattern  匹配的正则
     * @param content  被匹配的内容
     * @param consumer 匹配到的内容处理器
     */
    public static void get(Pattern pattern, CharSequence content, Consumer<Matcher> consumer) {
        if (null == content || null == pattern || null == consumer) {
            return;
        }
        final Matcher m = pattern.matcher(content);
        if (m.find()) {
            consumer.accept(m);
        }
    }
}
