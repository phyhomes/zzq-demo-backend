package com.zzq.framework.service.impl;

import com.zzq.common.constant.CacheConstants;
import com.zzq.common.constant.Constants;
import com.zzq.common.core.redis.RedisCache;
import com.zzq.common.exception.BaseException;
import com.zzq.common.pojo.ua.UserAgent;
import com.zzq.common.utils.JwtTokenUtils;
import com.zzq.common.utils.ServletUtils;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.UserAgentUtils;
import com.zzq.common.utils.ip.AddressUtils;
import com.zzq.common.utils.ip.IpUtils;
import com.zzq.framework.domain.dto.LoginUserDTO;
import com.zzq.framework.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 11:19
 * @Author : ZZQ
 * @Desc : token验证处理 服务层实现
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private RedisCache redisCache;

    private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Value("${token.header}")
    private String header;

    @Value("${token.prefix}")
    private String tokenPrefix;

    /** 密钥 */
    @Value("${token.secret}")
    private String secret;

    /**
     * 令牌有效时间（单位默认为分钟）
     */
    @Value("${token.expireTime}")
    private int expireTime;

    /**
     * 刷新令牌有效期（单位默认为分钟）
     */
    @Value("${token.refreshExpireTime}")
    private int refreshExpireTime;

    @Override
    public Map<String, String> createToken(LoginUserDTO loginUser) {
        // log.info("createToken: {}", loginUser);
        String uuid = UUID.randomUUID().toString();
        loginUser.setUuid(uuid);
        loginUser.setLoginTime(System.currentTimeMillis());
        // 有效期：令牌有效期 + 刷新令牌有效期
        loginUser.setExpireTime(
                expireTime*TimeUnit.MINUTES.toMillis(1) +
                        refreshExpireTime*TimeUnit.MINUTES.toMillis(1)
        );
        setUserAgent(loginUser);
        saveUuid(loginUser);

        // Generate JWT token here
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, uuid);
        claims.put(Constants.JWT_USERNAME, loginUser.getUsername());
        String token = JwtTokenUtils.generateToken(secret, claims, expireTime, TimeUnit.MINUTES);
        String refreshToken = JwtTokenUtils.generateToken(secret, claims, refreshExpireTime, TimeUnit.MINUTES);
        return Map.of("token", token, "refreshToken", refreshToken);
    }

    /**
     * 解析令牌
     *
     * @param token 令牌
     * @return token中的登录信息
     */
    @Override
    public Map<String, String> parseToken(String token) {
        Map<String, Object> claims =  JwtTokenUtils.parseToken(token, secret);
        return Map.of(
                // uuid
                Constants.LOGIN_USER_KEY, (String) claims.get(Constants.LOGIN_USER_KEY),
                // username
                Constants.JWT_USERNAME, (String) claims.get(Constants.JWT_USERNAME)
                );
    }

    /**
     * 解析令牌
     *
     * @param request HTTP请求
     * @return 登录信息
     */
    private Map<String, String> parseToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(header);
        if (StringUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith(tokenPrefix + " ")){
            // throw new BaseException("auth", HttpStatus.UNAUTHORIZED, "auth.failed");
            return null;
        }
        String token = authorizationHeader.substring(tokenPrefix.length() + 1);
        if (StringUtils.isBlank(token)){
            // throw new BaseException("auth", HttpStatus.UNAUTHORIZED, "auth.failed");
            return null;
        }

        return parseToken(token);
    }

    @Override
    public LoginUserDTO getLoginUser(HttpServletRequest request) {
        // 解析JWT token
        Map<String, String> claims = parseToken(request);
        if (claims == null){
            return null;
        }
        String userKey = getUserKey(claims.get(Constants.LOGIN_USER_KEY));
        // 从Token中获取登录用户信息，封装为LoginUser对象
        return redisCache.getCacheObject(userKey, LoginUserDTO.class);
    }

    @Override
    public void delLoginUser(String uuid) {
        if (uuid == null) {
            throw new BaseException("logout", "系统出错，退出登录失败");
        }
        redisCache.deleteObject(getUserKey(uuid));
    }

    private void saveUuid(LoginUserDTO loginUser) {
        // log.info("saveUuid: {}", loginUser);
        String uuid = loginUser.getUuid();
        String key = getUserKey(uuid);
        // 保存时的有效期：登录时间 + 有效期 - 当前时间
        Long expireTime = loginUser.getLoginTime() + loginUser.getExpireTime() - System.currentTimeMillis();
        // 缓存用户信息
        redisCache.setCacheObject(key, loginUser, expireTime, TimeUnit.MILLISECONDS);
    }

    private Long getExpireTime(Long startTime) {
        // 过期的时间点：开始的时间点 + 刷新令牌的有效时间长 + 令牌的有效时间长
        long endTime = startTime
                + expireTime* TimeUnit.MINUTES.toMillis(1)
                + refreshExpireTime* TimeUnit.MINUTES.toMillis(1);
        return endTime - System.currentTimeMillis();
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    private void setUserAgent(LoginUserDTO loginUser)
    {
        // 调用浏览器解析工具UserAgentUtils获取userAgent
        UserAgent userAgent = UserAgentUtils.parse(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr();
        loginUser.setIpaddr(ip);
        loginUser.setLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOs().getName());
    }

    /**
     * 获取用户缓存key
     *
     * @param uuid 用户ID
     * @return 缓存key
     */
    private String getUserKey(String uuid)
    {
        return CacheConstants.LOGIN_USER_KEY + uuid;
    }
}
