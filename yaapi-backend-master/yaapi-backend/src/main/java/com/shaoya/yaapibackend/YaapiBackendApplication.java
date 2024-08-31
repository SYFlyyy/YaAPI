package com.shaoya.yaapibackend;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shaoya.yaapibackend.mapper")
@EnableDubbo
public class YaapiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YaapiBackendApplication.class, args);
    }

}
