package com.zzq.framework.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzq.common.core.domain.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 20:09
 * @Author : ZZQ
 * @Desc : 系统操作日志记录表 sys_operation_log
 */
public class SysOperationLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 日志主键 */
    private Long id;

    /** 操作模块 */
    private String module;

    /** 业务类型（0其它 1新增 2修改 3删除） */
    private Integer businessType;

    /** 业务类型数组 */
    private Integer[] businessTypes;

    /** 请求方法 */
    private String method;

    /** 请求方式 */
    private String requestMethod;

    /** 操作类别（0其它 1后台用户 2手机端用户） */
    private Integer operatorType;

    /** 操作人员 */
    private String username;

    /** 请求url */
    private String url;

    /** 操作地址 */
    private String ipaddr;

    /** 操作地点 */
    private String location;

    /** 请求参数 */
    private String param;

    /** 返回参数 */
    private String jsonResult;

    /** 操作状态（0异常 1正常） */
    private Integer status;

    /** 错误消息 */
    private String errorMsg;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    /** 消耗时间 */
    private Long costTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getModule()
    {
        return module;
    }

    public void setModule(String title)
    {
        this.module = module;
    }

    public Integer getBusinessType()
    {
        return businessType;
    }

    public void setBusinessType(Integer businessType)
    {
        this.businessType = businessType;
    }

    public Integer[] getBusinessTypes()
    {
        return businessTypes;
    }

    public void setBusinessTypes(Integer[] businessTypes)
    {
        this.businessTypes = businessTypes;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public Integer getOperatorType()
    {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType)
    {
        this.operatorType = operatorType;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getParam()
    {
        return param;
    }

    public void setParam(String param)
    {
        this.param = param;
    }

    public String getJsonResult()
    {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult)
    {
        this.jsonResult = jsonResult;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public LocalDateTime getOperationTime()
    {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime)
    {
        this.operationTime = operationTime;
    }

    public Long getCostTime()
    {
        return costTime;
    }

    public void setCostTime(Long costTime)
    {
        this.costTime = costTime;
    }
}
