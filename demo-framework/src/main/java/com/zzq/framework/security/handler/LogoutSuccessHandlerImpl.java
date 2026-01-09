package com.zzq.framework.security.handler;

import com.zzq.common.constant.Constants;
import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.utils.MessageUtils;
import com.zzq.common.utils.ServletUtils;
import com.zzq.framework.async.SysAsyncFactory;
import com.zzq.framework.domain.dto.LoginUserDTO;
import com.zzq.framework.manager.AsyncManager;
import com.zzq.framework.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 * 
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     *
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUserDTO loginUser = tokenService.getLoginUser(request);
        if (loginUser != null)
        {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getUuid());
            // 记录用户退出日志
            AsyncManager.me().execute(SysAsyncFactory.recordLoginInfo(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        // 返回信息交给前端渲染
        AjaxResult ajaxResult = AjaxResult.success(MessageUtils.message("user.logout.success"));
        ServletUtils.renderAjaxResult(response, ajaxResult);
    }

}
