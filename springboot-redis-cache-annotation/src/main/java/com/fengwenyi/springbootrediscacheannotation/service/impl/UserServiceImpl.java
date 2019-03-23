package com.fengwenyi.springbootrediscacheannotation.service.impl;

import com.fengwenyi.springbootrediscacheannotation.aop.cache.UserDeleteIdCache;
import com.fengwenyi.springbootrediscacheannotation.aop.cache.UserFindIdCache;
import com.fengwenyi.springbootrediscacheannotation.model.UserModel;
import com.fengwenyi.springbootrediscacheannotation.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wenyi Feng
 * @since 2019-01-31
 */
@Service
public class UserServiceImpl implements UserService {

    private List<UserModel> list = new ArrayList<>();

    @Override
    public List<UserModel> findList() {
        return list;
    }

    @Override
    @UserFindIdCache
    public UserModel findById(String id) {
        System.out.println("请求数据库");
        Assert.notNull(id, "id must not null.");
        for (UserModel model : list) {
            if (id.equals(model.getId()))
                return model;
        }
        return null;
    }

    @Override
    //@UserAddCache
    public UserModel add(UserModel model) {
        Assert.notNull(model, "UserModel must not null.");
        list.add(model);
        return model;
    }

    @Override
    public UserModel updateById(UserModel userModel) {
        Assert.notNull(userModel, "UserModel must not null.");
        for (UserModel model : list) {
            if (userModel.getId().equals(model.getId())) {
                list.remove(model);
                list.add(userModel);
                return userModel;
            }
        }
        return null;
    }

    @Override
    @UserDeleteIdCache
    public boolean deleteById(String id) {
        Assert.notNull(id, "Id must not null.");
        for (UserModel model : list) {
            if (id.equals(model.getId())) {
                list.remove(model);
                return true;
            }
        }
        return false;
    }
}
