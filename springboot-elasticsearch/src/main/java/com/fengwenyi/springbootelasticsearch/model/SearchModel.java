package com.fengwenyi.springbootelasticsearch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Wenyi Feng
 * @since 2019-03-13
 */
@Data
@Accessors(chain = true)
@Document(indexName = "search", type = "com.fengwenyi.springbootelasticsearch.model.SearchModel")
public class SearchModel {

    /** ID */
    @Id
    private String id;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 权重 */
    private Integer weight;

    /** 创建时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
