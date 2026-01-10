package com.zzq.system.service.impl;

import com.zzq.common.constant.Constants;
import com.zzq.common.constant.HttpStatus;
import com.zzq.common.constant.ModuleConstants;
import com.zzq.common.exception.BaseException;
import com.zzq.common.utils.MessageUtils;
import com.zzq.common.utils.ip.IpUtils;
import com.zzq.framework.security.context.AuthenticationContextHolder;
import com.zzq.framework.service.impl.UserDetailsServiceImpl;
import com.zzq.framework.async.SysAsyncFactory;
import com.zzq.common.constant.CacheConstants;
import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.core.redis.RedisCache;
import com.zzq.framework.manager.AsyncManager;
import com.zzq.system.config.LoginConfig;
import com.zzq.system.domain.LoginQuery;
import com.zzq.framework.domain.dto.LoginUserDTO;
import com.zzq.system.service.SysConfigService;
import com.zzq.system.service.SysLoginService;
import com.zzq.common.utils.StringUtils;
import com.zzq.system.service.SysUserService;
import com.zzq.framework.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-17 19:39
 * @Author : ZZQ
 * @Desc : 系统登录服务层实现
 */
@Service
public class SysLoginServiceImpl implements SysLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private TokenService tokenService;

    @Override
    public AjaxResult login(LoginQuery loginQuery) {
        String username = loginQuery.getUsername();
        String password = loginQuery.getPassword();
        String code = loginQuery.getCode();
        String uuid = loginQuery.getUuid();

        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);

        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 这里设置成上下文的意图是在身份验证过程中，
            // 其它组件或方法可以获取到该对象，以便进行相关操作
            // 如记录登录日志，获取用户信息等
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用 UserDetailsServiceImpl.loadUserByUsername
            /** {@link UserDetailsServiceImpl} */
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        // 这里捕捉到的异常会交给GlobalExceptionHandler处理
        catch (Exception e)
        {
            // 判断错误类型，记录对应日志
            // UserNotFoundException 会被 BadCredentialsException 所捕捉
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(SysAsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new BaseException(ModuleConstants.USER, HttpStatus.UNAUTHORIZED, "user.password.not.match");
            }
            else
            {
                AsyncManager.me().execute(SysAsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new BaseException(ModuleConstants.USER, e.getMessage());
            }
        }
        finally
        {
            // 最后，在所有操作完成后，清理AuthenticationContextHolder（认证上下文）
            AuthenticationContextHolder.clearContext();
        }
        // 记录登录成功的日志
        AsyncManager.me().execute(SysAsyncFactory.recordLoginInfo(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        // 获取自定义封装的用户信息
        LoginUserDTO loginUser = (LoginUserDTO) authentication.getPrincipal();
        // 将登录信息存入到数据库
        recordLoginInfo(loginUser.getUserId());
        // 生成token并返回
        return AjaxResult.success(tokenService.createToken(loginUser));
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 无
     */
    public void validateCaptcha(String username, String code, String uuid) {
        // 从配置信息中获取验证码开关，默认开启
        boolean captchaEnabled = sysConfigService.selectCaptchaEnabled();
        if (captchaEnabled) {
            // 拼接验证码的key，对传入的uuid进行防null处理，设置默认值
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
            // 从redis缓存中获取验证码
            String captcha = redisCache.getCacheObject(verifyKey, String.class);
            // 验证码不存在，记录日志，抛出验证码失效异常
            if (captcha == null)
            {
                AsyncManager.me().execute(SysAsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new BaseException(ModuleConstants.USER, HttpStatus.NOT_FOUND, "user.jcaptcha.expire");
            }
            // 从redis缓存中删除验证码
            redisCache.deleteObject(verifyKey);
            // 验证码验证错误，记录日志，抛出验证码错误异常
            if (!code.equalsIgnoreCase(captcha))
            {
                AsyncManager.me().execute(SysAsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new BaseException(ModuleConstants.USER, HttpStatus.PARA_ERROR, "user.jcaptcha.expire");
            }


        }


    }

    /**
     * 登录前置校验
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {

        // 用户名或密码为空 错误
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password))
        {
            AsyncManager.me().execute(SysAsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.blank")));
            throw new BaseException(ModuleConstants.USER, HttpStatus.PARA_ERROR, "user.not.blank");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < LoginConfig.PASSWORD_MIN_LENGTH
                || password.length() > LoginConfig.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(SysAsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new BaseException(ModuleConstants.USER, HttpStatus.PARA_ERROR, "user.password.not.match");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < LoginConfig.USERNAME_MIN_LENGTH
                || username.length() > LoginConfig.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(SysAsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new BaseException(ModuleConstants.USER, HttpStatus.PARA_ERROR, "user.password.not.match");
        }
        // IP黑名单校验
        String blackStr = sysConfigService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            AsyncManager.me().execute(SysAsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BaseException(ModuleConstants.USER, HttpStatus.FORBIDDEN, "login.blocked");
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId){

        sysUserService.updateLoginInfo(userId, IpUtils.getIpAddr());
    }



}
