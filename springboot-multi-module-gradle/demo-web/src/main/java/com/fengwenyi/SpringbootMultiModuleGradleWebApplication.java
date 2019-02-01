package com.fengwenyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fengwenyi.springbootmultimodulegradle.democode.db.dao")
public class SpringbootMultiModuleGradleWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMultiModuleGradleWebApplication.class, args);
    }

}

