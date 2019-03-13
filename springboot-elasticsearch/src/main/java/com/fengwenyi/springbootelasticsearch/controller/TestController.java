package com.fengwenyi.springbootelasticsearch.controller;

import com.fengwenyi.springbootelasticsearch.model.PostModel;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author Wenyi Feng
 * @since 2019-02-25
 */
@RestController
public class TestController {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 单字符串模糊查询，默认排序。将从所有字段中查找包含传来的word分词后字符串的数据集
     */
    @GetMapping("/singleWord")
    public Object singleTitle(String word, @PageableDefault Pageable pageable) {
        //使用queryStringQuery完成单字符串查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryStringQuery(word)).withPageable(pageable).build();
        return elasticsearchTemplate.queryForList(searchQuery, PostModel.class);
    }

    /**
     * 多字段匹配
     */
    @RequestMapping("/multiMatch")
    public Object singleUserId(String title, @PageableDefault(sort = "weight", direction = Sort.Direction.DESC) Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQuery(title, "title", "content")).withPageable(pageable).build();
        return elasticsearchTemplate.queryForList(searchQuery, PostModel.class);
    }

    /**
     * 单字段包含所有输入
     */
    @RequestMapping("/contain1")
    public Object contain1(String title) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("title", title).operator(Operator.AND)).build();
        return elasticsearchTemplate.queryForList(searchQuery, PostModel.class);
    }

    /**
     * 单字段包含所有输入(按比例包含)
     */
    @RequestMapping("/contain")
    public Object contain(String title) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("title", title).operator(Operator.AND).minimumShouldMatch("75%")).build();
        return elasticsearchTemplate.queryForList(searchQuery, PostModel.class);
    }

    /**
     * 多字段合并查询
     */
    @RequestMapping("/bool")
    public Object bool(String title, Integer userId, Integer weight) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQuery().must(termQuery("userId", userId))
                .should(rangeQuery("weight").lt(weight)).must(matchQuery("title", title))).build();
        return elasticsearchTemplate.queryForList(searchQuery, PostModel.class);
    }

}
