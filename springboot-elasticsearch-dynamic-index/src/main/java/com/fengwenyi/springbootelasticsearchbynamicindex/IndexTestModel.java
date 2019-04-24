package com.fengwenyi.springbootelasticsearchbynamicindex;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author Erwin Feng
 * @since 2019-04-24 11:14
 */
@Data
@Accessors(chain = true)
@Document(indexName = ".erwin-index-test-#{indexCreateBean.index}", type = "com.fengwenyi.springbootelasticsearchbynamicindex.IndexTestModel")
public class IndexTestModel {

    @Id
    private String id;

    private String test;

}
