package com.zzq.common.exception;

import com.zzq.common.constant.HttpStatus;
import com.zzq.common.utils.MessageUtils;
import com.zzq.common.utils.StringUtils;

import java.io.Serial;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-04 17:41
 * @Author : ZZQ
 * @Desc : 基础异常
 */
public class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private String module;

    /**
     * ajax状态码
     */
    private int code;

    /**
     * i18n消息键
     */
    private String msgKey;

    /**
     * i18n中对应的参数
     */
    private Object[] args;

    /**
     * 错误消息
     */
    private String defaultMessage;

    public BaseException(String module, int code, String msgKey, Object[] args, String defaultMessage)
    {
        this.module = module;
        this.code = code;
        this.msgKey = msgKey;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public BaseException(String module, int code, String msgKey, Object[] args)
    {
        this(module, code, msgKey, args, null);
    }

    public BaseException(String module, int code, String msgKey)
    {
        this(module, code, msgKey, null, null);
    }

    public BaseException(String module, String defaultMessage)
    {
        this(module, HttpStatus.ERROR, null, null, defaultMessage);
    }

    public BaseException(String msgKey, Object[] args)
    {
        this(null, HttpStatus.ERROR, msgKey, args, null);
    }

    public BaseException(String defaultMessage)
    {
        this(null, HttpStatus.ERROR, null, null, defaultMessage);
    }

    @Override
    public String getMessage()
    {
        String message = null;
        if (!StringUtils.isBlank(msgKey))
        {
            // 当msgKey为i18n消息键时，获取对应的消息，否则直接返回
            message = MessageUtils.message(msgKey, args);

        }
        if (message == null)
        {
            message = defaultMessage;
        }
        return message;
    }

    public String getModule()
    {
        return module;
    }

    public String getMsgKey()
    {
        return msgKey;
    }

    public int getCode()
    {
        return code;
    }

    public Object[] getArgs()
    {
        return args;
    }

    public String getDefaultMessage()
    {
        return defaultMessage;
    }

}
