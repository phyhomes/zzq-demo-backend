package com.zzq.framework.async;

import com.zzq.common.constant.Constants;
import com.zzq.common.pojo.ua.UserAgent;
import com.zzq.common.utils.ServletUtils;
import com.zzq.common.utils.SpringUtils;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.UserAgentUtils;
import com.zzq.common.utils.ip.AddressUtils;
import com.zzq.common.utils.ip.IpUtils;
import com.zzq.framework.domain.entity.SysLoginInfo;
import com.zzq.framework.domain.entity.SysOperationLog;
import com.zzq.framework.service.SysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.TimerTask;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-17 23:15
 * @Author : ZZQ
 * @Desc : 系统操作的异步工厂：记录登录日志、操作日志
 */
public class SysAsyncFactory {
    public static final Logger log = LoggerFactory.getLogger("sys-user");
    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLoginInfo(final String username, final String status, final String message,
                                             final Object... args)
    {
        final UserAgent userAgent = UserAgentUtils.parse(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                String s = "[" + StringUtils.defaultString(ip, "") + "]" +
                        address +
                        "[" + StringUtils.defaultString(username, "") + "]" +
                        "[" + StringUtils.defaultString(status, "") + "]" +
                        "[" + StringUtils.defaultString(message, "") + "]";
                // 打印信息到日志
                log.info(s, args);
                // 获取客户端操作系统
                String os = userAgent.getOs().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLoginInfo loginInfo = new SysLoginInfo();
                loginInfo.setUsername(username);
                loginInfo.setIpaddr(ip);
                loginInfo.setLocation(address);
                loginInfo.setBrowser(browser);
                loginInfo.setOs(os);
                loginInfo.setMsg(message);
                // 日志状态
                if (Arrays.asList(Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER).contains(status))
                {
                    loginInfo.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    loginInfo.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(SysService.class).insertLoginInfo(loginInfo);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operationLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOperation(final SysOperationLog operationLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operationLog.setLocation(AddressUtils.getRealAddressByIP(operationLog.getIpaddr()));
                SpringUtils.getBean(SysService.class).insertOperationLog(operationLog);
            }
        };
    }
}
