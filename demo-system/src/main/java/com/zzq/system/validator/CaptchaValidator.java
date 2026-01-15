package com.zzq.system.validator;

import com.zzq.common.utils.StringUtils;
import com.zzq.system.annotation.CaptchaRequired;
import com.zzq.system.service.SysConfigService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-15 19:23
 * @Author : ZZQ
 * @Desc : 验证码参数校验逻辑
 */
public class CaptchaValidator implements ConstraintValidator<CaptchaRequired, String> {
    @Autowired
    private SysConfigService sysConfigService;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 从配置信息中获取验证码开关，默认开启
        boolean captchaEnabled = sysConfigService.selectCaptchaEnabled();
        if (captchaEnabled) {
            return !StringUtils.isBlank(s);
        }
        return true;
    }
}
