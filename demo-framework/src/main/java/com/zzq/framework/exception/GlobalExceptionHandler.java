package com.zzq.framework.exception;

import com.zzq.common.constant.HttpStatus;
import com.zzq.common.core.domain.AjaxResult;
import com.zzq.common.exception.BaseException;
import com.zzq.common.utils.ConvertUtils;
import com.zzq.common.utils.EscapeUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-17 12:46
 * @Author : ZZQ
 * @Desc : 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.warn("请求路径中缺少必需的路径变量'{}'，异常信息：'{}'。", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.PARA_ERROR, String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String value = ConvertUtils.toStr(e.getValue());
        if (!StringUtils.isBlank(value))
        {
            value = EscapeUtils.clean(value);
        }
        log.warn("请求参数类型不匹配'{}'，异常信息：'{}'。", requestURI, e.getMessage());
        return AjaxResult.error(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), value));
    }

    /**
     * 请求参数验证失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.warn("请求参数验证失败'{}'，异常信息：'{}'。", requestURI, e.getMessage());
        String message = e.getBindingResult().getFieldError().getDefaultMessage();

        return AjaxResult.error(HttpStatus.PARA_ERROR, message);
    }

    /**
     * 请求参数验证失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public AjaxResult handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.warn("参数验证失败'{}'，异常信息：'{}'。", requestURI, e.getMessage());
        String message = e.getConstraintViolations().iterator().next().getMessage();
        return AjaxResult.error(HttpStatus.PARA_ERROR, message);
    }

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.warn("请求地址'{}'，权限校验失败。异常信息：'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "permission.denied");
    }

    /**
     * 自定义BaseException异常
     */
    @ExceptionHandler(BaseException.class)
    public AjaxResult handleBaseException(BaseException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.warn("请求地址'{}'，发生系统异常.", requestURI, e);
        return AjaxResult.error(e.getCode(), e.getMessage());
    }

    /**
     * JWT Token异常
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public AjaxResult handleJwtException(ExpiredJwtException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.warn("请求地址'{}'，JWT校验发生异常。异常信息：{}", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}'，发生系统异常，信息：", requestURI, e);
        return AjaxResult.error(HttpStatus.ERROR, e.getMessage());
    }

}
