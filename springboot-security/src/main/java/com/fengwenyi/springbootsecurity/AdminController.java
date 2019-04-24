package com.fengwenyi.springbootsecurity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Erwin Feng
 * @since 2019-04-23 10:37
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/index")
    public String index() {
        return "Admin Index";
    }

}
