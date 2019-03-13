package com.fengwenyi.springbootelasticsearch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wenyi Feng
 * @since 2019-03-04
 */
@Data
@Accessors(chain = true)
@Document(indexName = "blog", type = "java")
public class BlogModel implements Serializable {

    private static final long serialVersionUID = 6320548148250372657L;

    @Id
    private String id;

    private String title;

    //@Field(type = FieldType.Date, format = DateFormat.basic_date)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date time;
}
