package com.fengwenyi.springbootapireactiveweb;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Wenyi Feng
 * @since 2019-03-12
 */
@Data
@Accessors(chain = true)
public class UserModel {

    private String name;

    private Integer age;

}
