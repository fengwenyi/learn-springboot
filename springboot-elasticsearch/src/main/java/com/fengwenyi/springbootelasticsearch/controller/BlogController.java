package com.fengwenyi.springbootelasticsearch.controller;

import com.fengwenyi.javalib.result.Result;
import com.fengwenyi.springbootelasticsearch.model.BlogModel;
import com.fengwenyi.springbootelasticsearch.repository.BlogRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * @author Wenyi Feng
 * @since 2019-03-04
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @PostMapping("/add")
    public Result add(@RequestBody BlogModel blogModel) {
        blogRepository.save(blogModel);
        return Result.success();
    }

    @GetMapping("/get/{id}")
    public Result getById(@PathVariable String id) {
        if (StringUtils.isEmpty(id))
            return Result.error();
        Optional<BlogModel> blogModelOptional = blogRepository.findById(id);
        if (blogModelOptional.isPresent()) {
            BlogModel blogModel = blogModelOptional.get();
            return Result.success(blogModel);
        }
        return Result.error();
    }

    @GetMapping("/get")
    public Result getAll() {
        Iterable<BlogModel> iterable = blogRepository.findAll();
        List<BlogModel> list = new ArrayList<>();
        iterable.forEach(list::add);
        return Result.success(list);
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody BlogModel blogModel) {
        String id = blogModel.getId();
        if (StringUtils.isEmpty(id))
            return Result.error();
        blogRepository.save(blogModel);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable String id) {
        if (StringUtils.isEmpty(id))
            return Result.error();
        blogRepository.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result deleteById() {
        blogRepository.deleteAll();
        return Result.success();
    }

    @GetMapping("/rep/search/title")
    public Result repSearchTitle(String keyword) {
        if (StringUtils.isEmpty(keyword))
            return Result.error();
        return Result.success(blogRepository.findByTitleLike(keyword));
    }

    @GetMapping("/rep/search/title/custom")
    public Result repSearchTitleCustom(String keyword) {
        if (StringUtils.isEmpty(keyword))
            return Result.error();
        return Result.success(blogRepository.findByTitleCustom(keyword));
    }

    @GetMapping("/search/title")
    public Result searchTitle(String keyword) {
        if (StringUtils.isEmpty(keyword))
            return Result.error();
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryStringQuery(keyword))
                .build();
        List<BlogModel> list = elasticsearchTemplate.queryForList(searchQuery, BlogModel.class);
        return Result.success(list);
    }

}
