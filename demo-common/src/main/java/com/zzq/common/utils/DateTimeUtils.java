package com.zzq.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 12:29
 * @Author : ZZQ
 * @Desc : 日期、时间相关工具类
 */
public class DateTimeUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String getDateTimeNow() {
        return getDateTimeNow(YYYYMMDDHHMMSS);
    }

    public static String getDateTimeNow(String format) {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(format));
    }

    public static void main(String[] args) {
        System.out.println(getDateTimeNow());
    }
}
