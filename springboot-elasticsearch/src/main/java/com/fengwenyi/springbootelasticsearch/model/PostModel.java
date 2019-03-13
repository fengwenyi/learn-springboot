package com.fengwenyi.springbootelasticsearch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author Wenyi Feng
 * @since 2019-02-25
 */
@Data
@Document(indexName="projectname",type="post")
public class PostModel {

    @Id
    private String id;

    private String title;

    private String content;

    private Integer userId;

    private Integer weight;
}
