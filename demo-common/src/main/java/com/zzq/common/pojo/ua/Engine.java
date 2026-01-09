package com.zzq.common.pojo.ua;

import com.zzq.common.utils.RegexUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 01:18
 * @Author : ZZQ
 * @Desc : 引擎对象
 */
public class Engine extends UserAgentInfo {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 未知 */
    public static final Engine Unknown = new Engine(NameUnknown, null);

    /**
     * 支持的引擎类型
     */
    public static final List<Engine> engines = new ArrayList<>();
    static {
        Collections.addAll(engines,
                new Engine("Trident", "trident"),
                new Engine("Webkit", "webkit"),
                new Engine("Chrome", "chrome"),
                new Engine("Opera", "opera"),
                new Engine("Presto", "presto"),
                new Engine("Gecko", "gecko"),
                new Engine("KHTML", "khtml"),
                new Engine("Konqueror", "konqueror"),
                new Engine("MIDP", "MIDP")
        );
    }


    private final Pattern versionPattern;

    /**
     * 构造
     *
     * @param name 引擎名称
     * @param regex 关键字或表达式
     */
    public Engine(String name, String regex) {
        super(name, regex);
        this.versionPattern = Pattern.compile(name + "[/\\- ]([\\d\\w.\\-]+)", Pattern.CASE_INSENSITIVE);
    }

    /**
     * 获取引擎版本
     *
     * @param userAgentString User-Agent字符串
     * @return 版本
     * @since 5.7.4
     */
    public String getVersion(String userAgentString) {
        if(isUnknown()){
            return null;
        }
        return RegexUtils.getGroup1(this.versionPattern, userAgentString);
    }

}
