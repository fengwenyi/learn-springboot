package com.fengwenyi.springbootelasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author Erwin Feng
 * @since 2019-04-02 12:17
 */
@Data
@Document(indexName = "js-test-api", type = "com.fengwenyi.springbootelasticsearch.UserModel")
public class UserModel {

    @Id
    private String id;

    private String name;

}
