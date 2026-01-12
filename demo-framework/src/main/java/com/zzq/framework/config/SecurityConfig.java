package com.zzq.framework.config;

import com.zzq.framework.filter.JwtAuthenticationFilter;
import com.zzq.framework.security.handler.AuthenticationEntryPointImpl;
import com.zzq.framework.security.handler.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.util.List;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-20 19:19
 * @Author : ZZQ
 * @Desc : Spring Security 配置类
 */
@Configuration
public class SecurityConfig {
    /*@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    AuthenticationManager authenticationManager;*/


    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * JWT Token认证过滤器
     */
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * 身份验证管理器 - 手动配置方式
     */
    /*@Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(provider);
    }*/

    /**
     * 身份验证管理器 - 自动配置方式
     * Spring Security 推荐的标准方式
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * <p>anyRequest          |   匹配所有请求路径<p/>
     * <p>access              |   SpringEl表达式结果为true时可以访问<p/>
     * <p>anonymous           |   匿名可以访问<p/>
     * <p>denyAll             |   用户不能访问<p/>
     * <p>fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）<p/>
     * <p>hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问<p/>
     * <p>hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问<p/>
     * <p>hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问<p/>
     * <p>hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问<p/>
     * <p>hasRole             |   如果有参数，参数表示角色，则其角色可以访问<p/>
     * <p>permitAll           |   用户可以任意访问<p/>
     * <p>rememberMe          |   允许通过remember-me登录的用户访问<p/>
     * <p>authenticated       |   用户登录后可访问<p/>
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        // authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        // authenticationManager = authenticationManagerBuilder.build();

        http
            .csrf(AbstractHttpConfigurer::disable)
            // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 禁用HTTP响应标头
            .headers(headersCustomizer -> headersCustomizer
                    .cacheControl(HeadersConfigurer.CacheControlConfig::disable)
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            // 基于token，所以不需要session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 注解标记允许匿名访问的url
            .authorizeHttpRequests(auth -> auth
                //.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // 对于登录login 注册register 验证码captchaImage 允许匿名访问
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated())
            // 自定义认证失败响应
            .exceptionHandling(e -> e
                    .authenticationEntryPoint(unauthorizedHandler))
            // 禁用 Spring Security 自带的表单登录
            .formLogin(AbstractHttpConfigurer::disable)
            // 添加Logout filter
            .logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler))
            // 添加JWT token认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // 添加CORS filter
            .addFilterBefore(corsFilter, JwtAuthenticationFilter.class)
            .addFilterBefore(corsFilter, LogoutFilter.class)
            .build();


        /*http.formLogin(formLogin -> {
            formLogin.loginProcessingUrl("/login")
                    .successHandler((request, response, authentication) -> {
                        response.setContentType("application/json;charset=UTF-8");
                        ObjectMapper mapper = new ObjectMapper();
                        AjaxResult result = AjaxResult.success("登录成功");
                        response.getWriter().write(mapper.writeValueAsString(result));
                    })
                    .failureHandler((request, response, exception) -> {
                        response.setContentType("application/json;charset=UTF-8");
                    });
        });*/

        // 换成getOrBuild 解决 http Build重复的问题
        return http.getOrBuild();
    }


    /**
     * 强散列哈希加密实现 用于加密密码
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /** 跨域配置，使用{@link CorsConfig}做了替代 */
    /*@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/




}
