package com.zzq.common.utils;

import com.zzq.common.pojo.ua.Browser;
import com.zzq.common.pojo.ua.Engine;
import com.zzq.common.pojo.ua.OS;
import com.zzq.common.pojo.ua.Platform;
import com.zzq.common.pojo.ua.UserAgent;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 01:25
 * @Author : ZZQ
 * @Desc : User-Agent解析器工具类
 */
public class UserAgentUtils {
    /**
     * 解析User-Agent
     *
     * @param userAgentString User-Agent字符串
     * @return {@link UserAgent}
     */
    public static UserAgent parse(String userAgentString) {
        if(StringUtils.isBlank(userAgentString)){
            return null;
        }
        final UserAgent userAgent = new UserAgent();

        // 浏览器
        final Browser browser = parseBrowser(userAgentString);
        userAgent.setBrowser(browser);
        userAgent.setVersion(browser.getVersion(userAgentString));

        // 浏览器引擎
        final Engine engine = parseEngine(userAgentString);
        userAgent.setEngine(engine);
        userAgent.setEngineVersion(engine.getVersion(userAgentString));

        // 操作系统
        final OS os = parseOS(userAgentString);
        userAgent.setOs(os);
        userAgent.setOsVersion(os.getVersion(userAgentString));

        // 平台
        final Platform platform = parsePlatform(userAgentString);
        userAgent.setPlatform(platform);

        // issue#IA74K2 MACOS下的微信不属于移动平台
        if(platform.isMobile() || browser.isMobile()){
            if(!os.isMacOS()){
                userAgent.setMobile(true);
            }
        }


        return userAgent;
    }

    /**
     * 解析浏览器类型
     *
     * @param userAgentString User-Agent字符串
     * @return 浏览器类型
     */
    private static Browser parseBrowser(String userAgentString) {
        for (Browser browser : Browser.browsers) {
            if (browser.isMatch(userAgentString)) {
                return browser;
            }
        }
        return Browser.Unknown;
    }

    /**
     * 解析引擎类型
     *
     * @param userAgentString User-Agent字符串
     * @return 引擎类型
     */
    private static Engine parseEngine(String userAgentString) {
        for (Engine engine : Engine.engines) {
            if (engine.isMatch(userAgentString)) {
                return engine;
            }
        }
        return Engine.Unknown;
    }

    /**
     * 解析系统类型
     *
     * @param userAgentString User-Agent字符串
     * @return 系统类型
     */
    private static OS parseOS(String userAgentString) {
        for (OS os : OS.oses) {
            if (os.isMatch(userAgentString)) {
                return os;
            }
        }
        return OS.Unknown;
    }

    /**
     * 解析平台类型
     *
     * @param userAgentString User-Agent字符串
     * @return 平台类型
     */
    private static Platform parsePlatform(String userAgentString) {
        for (Platform platform : Platform.platforms) {
            if (platform.isMatch(userAgentString)) {
                return platform;
            }
        }
        return Platform.Unknown;
    }

    public static void main(String[] args) {
        // UserAgentUtils.parse("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0");
//        UserAgent ua = UserAgentUtils.parse("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36");
        UserAgent ua = UserAgentUtils.parse("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        System.out.println(ua.getOs().toString());
    }
}
