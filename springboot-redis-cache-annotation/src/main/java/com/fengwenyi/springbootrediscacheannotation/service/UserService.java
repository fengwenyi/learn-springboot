package com.fengwenyi.springbootrediscacheannotation.service;

import com.fengwenyi.springbootrediscacheannotation.model.UserModel;

import java.util.List;

/**
 * @author Wenyi Feng
 * @since 2019-01-31
 */
public interface UserService {

    /**
     * 查询所有
     * @return
     */
    List<UserModel> findList();

    /**
     * 通过ID查询
     * @param id
     * @return
     */
    UserModel findById(String id);

    /**
     * 新增
     * @param model
     * @return
     */
    UserModel add(UserModel model);

    /**
     * 通过ID修改
     * @param userModel
     * @return
     */
    UserModel updateById(UserModel userModel);

    /**
     * 通过ID删除
     * @param id
     * @return
     */
    boolean deleteById(String id);
}
