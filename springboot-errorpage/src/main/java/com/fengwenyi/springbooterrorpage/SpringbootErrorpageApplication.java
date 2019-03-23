package com.fengwenyi.springbooterrorpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class SpringbootErrorpageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootErrorpageApplication.class, args);
        /*new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(SpringbootErrorpageApplication.class)
                .run(args);*/
    }

    @RequestMapping("/index/error")
    public String error() {
        return "error";
    }

}
