package com.fengwenyi.springbootrediscacheannotation.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Wenyi Feng
 * @since 2019-01-31
 */
@Data
@Accessors(chain = true)
public class UserModel implements Serializable {

    private String id;

    private String name;

    private Integer age;

}
