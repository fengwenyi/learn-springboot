package com.fengwenyi.springbootrediscacheannotation.controller;

import com.fengwenyi.springbootrediscacheannotation.model.UserModel;
import com.fengwenyi.springbootrediscacheannotation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author Wenyi Feng
 * @since 2019-01-31
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public void add() {
        int i = 10;
        for (int j = 0; j < 10; j++)
            userService.add(
                    new UserModel()
                            .setId(UUID.randomUUID().toString())
                            .setName("name : " + j)
                            .setAge(i + j));
    }

    @RequestMapping("/all")
    public List<UserModel> all() {
         return userService.findList();
    }

    @RequestMapping("/get/{id}")
    public UserModel get(@PathVariable("id") String id) {
        return userService.findById(id);
    }

    @RequestMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") String id) {
        return userService.deleteById(id);
    }

}
