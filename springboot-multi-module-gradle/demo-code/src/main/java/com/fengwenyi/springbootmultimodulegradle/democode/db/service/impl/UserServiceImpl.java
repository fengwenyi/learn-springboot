package com.fengwenyi.springbootmultimodulegradle.democode.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengwenyi.springbootmultimodulegradle.demomodel.model.UserModel;
import com.fengwenyi.springbootmultimodulegradle.democode.db.dao.UserDao;
import com.fengwenyi.springbootmultimodulegradle.democode.db.service.MPUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wenyi Feng
 * @since 2019-02-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserModel> implements MPUserService {

}
