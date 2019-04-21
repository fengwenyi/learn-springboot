package com.fengwenyi.springbootelasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Erwin Feng
 * @since 2019-04-02 12:19
 */
public interface UserRepository extends ElasticsearchRepository<UserModel, String> {
}
