package com.zzq.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-09 17:42
 * @Author : ZZQ
 * @Desc : CORS 策略配置
 *  <p>跨源资源共享（CORS，Cross-Origin Resource Sharing）是一种基于HTTP头的机制。
 *  它允许服务器标示除了它自己以外的其他源（域、协议或端口），使得浏览器允许这些源访问加载自己的资源。<p/>
 *  <p>CORS通过在请求和响应中添加特定的HTTP头来实现跨源访问控制，从而使跨源数据传输得以安全进行。<p/>
 */
@Configuration
public class CorsConfig {

    /**
     * 跨域过滤器 自动注入到SecurityConfig中
     * {@link SecurityConfig}
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // config.setAllowedOriginPatterns(List.of("*"));
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // config.setAllowedHeaders(List.of("*"));
        // 设置访问源请求方法
        // config.addAllowedMethod("*");
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

}
