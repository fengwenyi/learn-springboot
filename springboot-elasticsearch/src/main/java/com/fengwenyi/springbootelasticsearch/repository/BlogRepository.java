package com.fengwenyi.springbootelasticsearch.repository;

import com.fengwenyi.springbootelasticsearch.model.BlogModel;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Wenyi Feng
 * @since 2019-03-04
 */
public interface BlogRepository extends ElasticsearchRepository<BlogModel, String> {

    List<BlogModel> findByTitleLike(String keyword);

    @Query("{\"match_phrase\":{\"title\":\"?0\"}}")
    List<BlogModel> findByTitleCustom(String keyword);

}
