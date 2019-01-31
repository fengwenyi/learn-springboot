package com.fengwenyi.springbootrediscacheannotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringbootRedisCacheAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRedisCacheAnnotationApplication.class, args);
    }

}

