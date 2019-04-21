package com.fengwenyi.springbootapidate;

import com.fengwenyi.javalib.constant.DateTimeFormat;
import com.fengwenyi.javalib.util.DateTimeUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@SpringBootApplication
@RestController
public class SpringbootApiDateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApiDateApplication.class, args);
    }

    @PostMapping("/add")
    public UserVO add(@RequestBody UserVO userVO) {
        Date time = userVO.getTime();
        System.out.println(DateTimeUtil.toString(time, DateTimeFormat.yyyy_MM_dd_HH_mm_ss));
        return userVO;
    }
}
