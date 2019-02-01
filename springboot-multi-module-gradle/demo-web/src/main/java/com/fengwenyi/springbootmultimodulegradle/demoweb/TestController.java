package com.fengwenyi.springbootmultimodulegradle.demoweb;

import com.fengwenyi.springbootmultimodulegradle.democode.db.service.MPUserService;
import com.fengwenyi.springbootmultimodulegradle.demomodel.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wenyi Feng
 * @since 2019-02-01
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MPUserService mpUserService;

    @PostMapping("/add")
    public void add() {
        mpUserService.save(new UserModel().setName("张三").setAge(20));
    }

}
