package com.zzq.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-04 16:43
 * @Author : ZZQ
 * @Desc : 字符串工具类
 */
public class LocalStringUtils {
    public static final String SEPARATOR = ",";
    public static final String STAR = "*";
    public static final String UNDERLINE = "_";


    /**
     * 去空格 null安全的
     * trim变成strip(比trim更智能，能处理Unicode空白字符)
     */
    public static String strip(String str) {
        return str == null ? StringUtils.EMPTY : str.strip();
    }


    /**
     * 是否包含字符串
     *
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.strip().equalsIgnoreCase(strip(s))) {
                    return true;
                }
            }
        }
        return false;
    }




    /**
     * 判断给定的collection列表中是否包含数组array 判断给定的数组array中是否包含给定的元素value
     *
     * @param collection 给定的集合
     * @param array 给定的数组
     * @return boolean 结果
     */
    public static boolean containsAny(Collection<String> collection, String... array) {
        if (!CollectionUtils.isEmpty(collection) && ArrayUtils.isNotEmpty(array)) {
            for (String str : array) {
                if (collection.contains(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(UNDERLINE);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(UNDERLINE);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }


    /**
     * 将字符串中间部分替换为星号（*）
     * 例如: mask("551234567868", 2, 2) → "55********68"
     *
     * @param str       原字符串
     * @param headLen   保留头部长度
     * @param tailLen   保留尾部长度
     * @return 掩码后的字符串
     */
    public static String mask(String str, int headLen, int tailLen) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        int len = str.length();
        if (headLen + tailLen >= len) {
            // 如果保留长度超过总长度，返回全星号或原串（按需调整）
            return StringUtils.repeat(STAR, len);
        }
        String head = StringUtils.left(str, headLen);
        String tail = StringUtils.right(str, tailLen);
        String middle = StringUtils.repeat(STAR, len - headLen - tailLen);
        return head + middle + tail;
    }


}
