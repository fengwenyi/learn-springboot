package com.fengwenyi.springbootelasticsearch.repository;

import com.fengwenyi.springbootelasticsearch.model.PostModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Wenyi Feng
 * @since 2019-02-25
 */
public interface PostRepository extends ElasticsearchRepository<PostModel, String> {
}
