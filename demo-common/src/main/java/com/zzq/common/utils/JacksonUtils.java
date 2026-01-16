package com.zzq.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @Project : zzq-demo-backend
 * @Time : 2026-01-16 19:33
 * @Author : ZZQ
 * @Desc : Jackson工具类
 */
@Component
public class JacksonUtils {
    private static ObjectMapper objectMapper;

    public JacksonUtils(@Qualifier("objectMapper") ObjectMapper objectMapper) {
        JacksonUtils.objectMapper = objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
