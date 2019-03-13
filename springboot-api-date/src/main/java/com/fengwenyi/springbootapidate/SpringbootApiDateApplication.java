package com.fengwenyi.springbootapidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootApiDateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApiDateApplication.class, args);
    }

    @PostMapping("/add")
    public UserVO add(@RequestBody UserVO userVO) {
        return userVO;
    }
}
