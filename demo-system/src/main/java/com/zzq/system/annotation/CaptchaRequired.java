package com.zzq.system.annotation;

import com.zzq.system.validator.CaptchaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-15 19:10
 * @Author : ZZQ
 * @Desc : 自定义校验注解 - 验证码
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { CaptchaValidator.class })
public @interface CaptchaRequired {
    String message()

            default "验证码不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
