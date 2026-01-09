package com.zzq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication(exclude = {SqlInitializationAutoConfiguration.class}, scanBasePackages = "com.zzq")
@SpringBootApplication()
// @ComponentScan(basePackages = {"com.zzq", "com.zzq.system"})
// @MapperScan(basePackages = {"com.zzq.system.mapper", "com.zzq.framework.mapper"})
public class DemoSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSystemApplication.class, args);
    }

}
