package com.fengwenyi.springbootelasticsearch.controller;

import com.fengwenyi.javalib.constant.DateTimeFormat;
import com.fengwenyi.javalib.result.Result;
import com.fengwenyi.javalib.util.DateTimeUtil;
import com.fengwenyi.springbootelasticsearch.model.SearchModel;
import com.fengwenyi.springbootelasticsearch.repository.SearchRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * @author Wenyi Feng
 * @since 2019-03-13
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 初始化
     * 检查是否存在数据，如果不存在，则增加
     */
    @PostConstruct
    public void init() {
        //只初始化一次
        Iterable<SearchModel> iterable = searchRepository.findAll();
        if (iterable.iterator().hasNext()) {
            return;
        }
        for (int i = 0; i < getTitles().size(); i++) {
            searchRepository.save(
                    new SearchModel()
                            .setTitle(this.getTitles().get(i))
                            .setContent(this.getContents().get(i))
                            .setWeight(this.getWeights().get(i))
                            .setCreateTime(this.getCreateTimes().get(i)));
        }
    }

    private List<String> getTitles() {
        List<String> list = new ArrayList<>();
        list.add("Java SE Development Kit 8 Downloads");
        list.add("Java 教程");
        list.add("Java （计算机编程语言）");
        list.add("java吧");
        list.add("Java SE Downloads");
        list.add("ImportNew - 专注Java &amp; Android 技术分享");
        list.add("Java - 开源软件 - 开源中国");
//        list.add("");

        return list;
    }

    private List<String> getContents() {
        List<String> list = new ArrayList<>();
        list.add("Thank you for downloading this release of the Java™ Platform, Standard Edition Development Kit (JDK™). The JDK is a development environment for building applications, applets, and components using the Java programming language.");
        list.add("Java 是由Sun Microsystems公司于1995年5月推出的高级程序设计语言。\n" +
                "\n" +
                "Java可运行于多个平台，如Windows, Mac OS，及其他多种UNIX版本的系统。\n" +
                "\n" +
                "本教程通过简单的实例将让大家更好的了解JAVA编程语言。");
        list.add("Java是一门面向对象编程语言，不仅吸收了C++语言的各种优点，还摒弃了C++里难以理解的多继承、指针等概念，因此Java语言具有功能强大和简单易用两个特征。Java语言作为静态面向对象编程语言的代表，极好地实现了面向对象理论，允许程序员以优雅的思维方式进行复杂的编程 [1]  。\n" +
                "Java具有简单性、面向对象、分布式、健壮性、安全性、平台独立与可移植性、多线程、动态性等特点 [2]  。Java可以编写桌面应用程序、Web应用程序、分布式系统和嵌入式系统应用程序等 [3]  。");
        list.add("有问题为什么不先问问隔壁C++吧呢？");
        list.add("Looking for Oracle OpenJDK builds?\n" +
                "\n" +
                "Oracle Customers and ISVs targeting Oracle LTS releases: Oracle JDK is Oracle's supported Java SE version for customers and for developing, testing, prototyping or demonstrating your Java applications.\n" +
                "End users and developers looking for free JDK versions: Oracle OpenJDK offers the same features and performance as Oracle JDK under the GPL license.\n" +
                "To Learn more about these options visit Oracle JDK Releases for Java 11 and Later");
        list.add("ImportNew 是一个专注于 Java &amp; Android 技术分享的博客，为Java 和 Android开发者提供有价值的内容。包括：Android开发与快讯、Java Web开发和其他的Java技术相关的分享。");
        list.add("开源中国 www.oschina.net 是目前中国最大的开源技术社区。我们传播开源的理念，推广开源项目，为 IT 开发者提供了一个发现、使用、并交流开源技术的平台。目前开源中国社区已收录近五万款开源软件。");

        return list;
    }

    private List<Integer> getWeights() {
        List<Integer> list = new ArrayList<>();
        list.add(100);
        list.add(99);
        list.add(96);
        list.add(95);
        list.add(96);
        list.add(80);
        list.add(40);

        return list;
    }

    private List<Date> getCreateTimes() {
        List<Date> list = new ArrayList<>();
        try {
            list.add(DateTimeUtil.stringToDate("2019-03-13 11:46:09", DateTimeFormat.yyyy_MM_dd_HH_mm_ss));
            list.add(DateTimeUtil.stringToDate("2019-03-12 10:08:20", DateTimeFormat.yyyy_MM_dd_HH_mm_ss));
            list.add(DateTimeUtil.stringToDate("2019-03-10 08:00:09", DateTimeFormat.yyyy_MM_dd_HH_mm_ss));
            list.add(DateTimeUtil.stringToDate("2019-03-01 08:00:09", DateTimeFormat.yyyy_MM_dd_HH_mm_ss));
            list.add(DateTimeUtil.stringToDate("2019-02-01 16:00:09", DateTimeFormat.yyyy_MM_dd_HH_mm_ss));
            list.add(DateTimeUtil.stringToDate("2018-02-01 16:00:09", DateTimeFormat.yyyy_MM_dd_HH_mm_ss));
            list.add(DateTimeUtil.stringToDate("2017-02-01 16:00:09", DateTimeFormat.yyyy_MM_dd_HH_mm_ss));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 关键字全文搜索
     * @param keyword
     * @param pageable
     * @return
     */
    @GetMapping("/full")
    public Result full(String keyword, @PageableDefault Pageable pageable) {
        //Pageable pageable2 = PageRequest.of(0, 10, Sort.Direction.DESC, "keywords");
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(keyword))
                .withPageable(pageable)
                .build()
                ;
        List<SearchModel> list = elasticsearchTemplate.queryForList(searchQuery, SearchModel.class);
        return Result.success(list);
    }

    /**
     * 对某个字段的关键字进行搜索
     * @param field 字段
     * @param keyword 关键字
     * @param pageable
     * @return
     */
    @GetMapping("/match")
    public Result match(String field, String keyword, @PageableDefault Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(field, keyword))
                .withPageable(pageable)
                .build()
                ;
        List<SearchModel> list = elasticsearchTemplate.queryForList(searchQuery, SearchModel.class);
        return Result.success(list);
    }

    /**
     * 短语匹配
     * @param field 字段
     * @param keyword 关键字
     * @param pageable
     * @return
     */
    @GetMapping("/match-phrase")
    public Result matchPhrase(String field, String keyword, @PageableDefault Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchPhraseQuery(field, keyword))
                .withPageable(pageable)
                .build()
                ;
        List<SearchModel> list = elasticsearchTemplate.queryForList(searchQuery, SearchModel.class);
        return Result.success(list);
    }

    /**
     * 条件筛选
     * @param field 字段
     * @param keyword 关键字
     * @param pageable
     * @return
     */
    @GetMapping("/term")
    public Result term(String field, String keyword, @PageableDefault Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termQuery(field, keyword))
                .withPageable(pageable)
                .build()
                ;
        List<SearchModel> list = elasticsearchTemplate.queryForList(searchQuery, SearchModel.class);
        return Result.success(list);
    }

    /**
     * 多字段查询
     * @param keyword 关键字
     * @param pageable
     * @param fields 字段
     * @return
     */
    @GetMapping("/multi-match")
    public Result multiMatch(String keyword, @PageableDefault Pageable pageable, String ... fields) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, fields))
                .withPageable(pageable)
                .build()
                ;
        List<SearchModel> list = elasticsearchTemplate.queryForList(searchQuery, SearchModel.class);
        return Result.success(list);
    }

}
