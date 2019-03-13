package com.fengwenyi.springbootelasticsearch.repository;

import com.fengwenyi.springbootelasticsearch.model.CountModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Wenyi Feng
 * @since 2019-03-12
 */
public interface CountRepository extends ElasticsearchRepository<CountModel, String> {
}
