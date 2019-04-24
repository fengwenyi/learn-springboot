package com.fengwenyi.springbootelasticsearchbynamicindex;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Erwin Feng
 * @since 2019-04-24 11:16
 */
@Repository
public interface IndexTestRepository extends ElasticsearchRepository<IndexTestModel, String> {
}
