package com.fengwenyi.springbootelasticsearch.controller;

import com.fengwenyi.springbootelasticsearch.model.CountModel;
import com.fengwenyi.springbootelasticsearch.repository.CountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author Wenyi Feng
 * @since 2019-03-12
 */
@RestController
public class LongTestController {

    @Autowired
    private CountRepository countRepository;

    @PostConstruct
    public void add() {
        countRepository.save(new CountModel().setCount(1234567890123456789L));
    }

}
