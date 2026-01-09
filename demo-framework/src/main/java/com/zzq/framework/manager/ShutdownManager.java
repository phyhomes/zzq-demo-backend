package com.zzq.framework.manager;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-17 22:58
 * @Author : ZZQ
 * @Desc : 确保应用退出时能关闭后台线程
 */
@Component
public class ShutdownManager {
    public static final Logger log = LoggerFactory.getLogger("sys-user");
    @PreDestroy
    public void destroy()
    {
        shutdownAsyncManager();
    }

    /**
     * 停止异步执行任务
     */
    private void shutdownAsyncManager()
    {
        try
        {
            log.info("====关闭后台任务任务线程池====");
            AsyncManager.me().shutdown();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }
}
