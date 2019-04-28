package com.fengwenyi.springbootkafkademo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Erwin Feng
 * @since 2019-04-28 09:35
 */
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1338206557636133557L;

    private Long id;

    private String msg;

    /** 发送时间 */
    private Date time;
}
