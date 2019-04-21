package com.fengwenyi.springbootjenkinsdocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootJenkinsDockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJenkinsDockerApplication.class, args);
    }

    @RequestMapping("/")
    public String index() {
        return "Spring Boot + Jenkins + Docker 自动化部署";
    }
}
