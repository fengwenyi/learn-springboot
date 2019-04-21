package com.fengwenyi.springbootelasticsearchexamplephone.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Erwin
 * @date 2019-03-17 02:29
 */
@Data
@Accessors(chain = true)
@Document(indexName = "springboot_elasticsearch_example_phone", type = "com.fengwenyi.springbootelasticsearchexamplephone.model.PhoneModel")
public class PhoneModel implements Serializable {
    private static final long serialVersionUID = -5087658155687251393L;

    /* ID */
    @Id
    private String id;

    /* 名称 */
    private String name;

    /* 颜色，用英文分号(;)分隔 */
    private String colors;

    /* 卖点，用英文分号(;)分隔 */
    private String sellingPoints;

    /* 价格 */
    private String price;

    /* 产量 */
    private Long yield;

    /* 销售量 */
    private Long sale;

    /* 上市时间 */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date marketTime;

    /* 数据抓取时间 */
    //@Field(type = )
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
