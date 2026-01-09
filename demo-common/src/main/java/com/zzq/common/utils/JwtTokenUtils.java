package com.zzq.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-19 14:46
 * @Author : ZZQ
 * @Desc : JWT Token 工具类
 */
public class JwtTokenUtils {
    /**
     * 密钥长度要求：用于 HS256 的密钥长度不得小于 256 位（32 字节）
     */
    private static final int SECRET_LENGTH = 32;

    /**
     * 自定义密钥，用于弥补密钥长度
     */
    private static final String SECRET = "Tiger$1998&WelcomeToZayton-QuanZhou";

    /**
     * 处理密钥：防止密钥长度过短
     *
     * @param secret 密钥
     * @return 密钥
     */
    private static String getSecret(String secret) {
        // 密钥长度要求大于32个字节
        // utf-8编码：一个ASCII是一个字节，一个中文字符是三个字节
        // 默认密钥是ASCII编码
        System.out.println(SECRET.length());
        return secret + SECRET.substring(0, SECRET_LENGTH - secret.length());
    }

    /**
     * 获取JWT Token
     *
     * @param secret 密钥
     * @param claims Token中的载荷
     * @param expireTime 过期时间（数字）
     * @param timeUnit 时间单位
     * @return {@link String} Token
     */
    public static String generateToken(String secret, Map<String, Object> claims, long expireTime, TimeUnit timeUnit) {
        secret = secret.length() > SECRET_LENGTH ? secret : getSecret(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        // Generate JWT token here
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey, Jwts.SIG.HS256)
                .expiration(new Date(System.currentTimeMillis() + expireTime * timeUnit.toMillis(1)))
                .compact();
    }

    /**
     * 解析JWT Token
     *
     * @param token    JWT
     * @param secret   密钥
     * @return {@link Claims} Token中的载荷
     */
    public static Map<String, Object> parseToken(String token, String secret) {
        secret = secret.length() > SECRET_LENGTH ? secret : getSecret(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static void main(String[] args) {
        String token = generateToken("secret", null, 1, TimeUnit.DAYS);
        Claims claims = (Claims) parseToken(token, "secret");
        System.out.println(claims);
    }





}
