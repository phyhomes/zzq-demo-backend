package com.zzq.common.utils;

import com.zzq.common.constant.Constants;
import org.apache.commons.lang3.Strings;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-04 16:43
 * @Author : ZZQ
 * @Desc : 字符串工具类
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final String SEPARATOR = ",";
    public static final String STAR = "*";
    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     ** @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     *
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个字符串是否为空串（null、空）
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || str.isEmpty();
    }

    /**
     * * 判断一个字符串是否为空串（null、空或仅包含空白字符）
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isBlank(String str) {
        return isNull(str) || str.isEmpty() || str.trim().isEmpty();
    }

    /**
     * * 判断一个字符串是否非空串（非null、非空、非仅包含空白字符）
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    /**
     * 去空格 null安全的
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }


    public static String defaultString(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value;
    }

    /**
     * 是否包含字符串
     *
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 数字左边补齐0，使之达到指定长度。注意，如果数字转换为字符串后，长度大于size，则只保留 最后size个字符。
     *
     * @param num 数字对象
     * @param size 字符串指定长度
     * @return 返回数字的字符串格式，该字符串为指定长度。
     */
    public static final String padl(final Number num, final int size)
    {
        return padl(num.toString(), size, '0');
    }

    /**
     * 字符串左补齐。如果原始字符串s长度大于size，则只保留最后size个字符。
     *
     * @param s 原始字符串
     * @param size 字符串指定长度
     * @param c 用于补齐的字符
     * @return 返回指定长度的字符串，由原字符串左补齐或截取得到。
     */
    public static final String padl(final String s, final int size, final char c)
    {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null)
        {
            final int len = s.length();
            if (s.length() <= size)
            {
                for (int i = size - len; i > 0; i--)
                {
                    sb.append(c);
                }
                sb.append(s);
            }
            else
            {
                return s.substring(len - size, len);
            }
        }
        else
        {
            for (int i = size; i > 0; i--)
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 是否为http(s)://开头
     *
     * @param link 链接
     * @return 结果
     */
    public static boolean isHttp(String link)
    {
        // CS: 不忽略大小写
        return Strings.CS.startsWithAny(link, Constants.HTTP, Constants.HTTPS);

    }

    /**
     * 判断给定的collection列表中是否包含数组array 判断给定的数组array中是否包含给定的元素value
     *
     * @param collection 给定的集合
     * @param array 给定的数组
     * @return boolean 结果
     */
    public static boolean containsAny(Collection<String> collection, String... array)
    {
        if (!CollectionUtils.isEmpty(collection) && !isEmpty(array)) {
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
    public static String toUnderScoreCase(String str)
    {
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
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
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
