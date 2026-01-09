package com.zzq.framework.filter;

import com.zzq.common.exception.BaseException;
import com.zzq.framework.domain.dto.LoginUserDTO;
import com.zzq.framework.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-09 09:31
 * @Author : ZZQ
 * @Desc : JWT认证的过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private TokenService tokenService;

    // 定义不需要 JWT 认证的路径（可配置化）
    private static final String[] EXCLUDE_PATHS = {
            "/login",
            "/register",
            "/captchaImage",
            // 其他公开接口...
    };

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private boolean shouldSkip(String path) {
        return Arrays.stream(EXCLUDE_PATHS)
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (shouldSkip(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        LoginUserDTO user = tokenService.getLoginUser(request);
        // 一旦redis的缓存被清除，找不到user，就会认证失败
        if (user == null) {
            // 直接继续执行下一个过滤器
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // 封装成authentication对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            // 设置详细信息可以获取到请求的详细信息，如ip地址、请求的会话ID等
            // 帮助记录日志、进行安全审计和监控等操作
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            // 抛出没有预知的异常
            log.error("Authentication对象封装失败：{}", e.getMessage());
            throw new BaseException("auth", e.getMessage());
        }
        // 执行完毕，继续执行下一个过滤器
        filterChain.doFilter(request, response);
    }

}
