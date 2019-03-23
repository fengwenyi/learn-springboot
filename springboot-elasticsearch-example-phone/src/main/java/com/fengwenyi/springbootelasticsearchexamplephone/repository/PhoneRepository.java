package com.fengwenyi.springbootelasticsearchexamplephone.repository;

import com.fengwenyi.springbootelasticsearchexamplephone.model.PhoneModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Erwin
 * @date 2019-03-17 02:38
 */
public interface PhoneRepository extends ElasticsearchRepository<PhoneModel, String> {
}
