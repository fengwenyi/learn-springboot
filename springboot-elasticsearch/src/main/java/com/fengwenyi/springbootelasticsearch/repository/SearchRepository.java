package com.fengwenyi.springbootelasticsearch.repository;

import com.fengwenyi.springbootelasticsearch.model.SearchModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Wenyi Feng
 * @since 2019-03-13
 */
public interface SearchRepository extends ElasticsearchRepository<SearchModel, String> {
}
