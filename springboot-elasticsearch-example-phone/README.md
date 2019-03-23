
# SpringBoot Elasticsearch Example Phone

当前Spring Boot很是流行，包括我自己，也是在用Spring Boot集成其他框架进行项目开发，所以这一节，我们一起来探讨Spring Boot整合ElasticSearch的问题。

本文主要讲以下内容：

第一部分，通读文档

第二部分，Spring Boot整合ElasticSearch

第三部分，基本的CRUD操作

第四部分，搜索

第五部分，例子

还没有学过Elasticsearch的朋友，可以先学这个系列的第一节（这个系列共三节），如果你有不明白或者不正确的地方，可以给我评论、留言或者私信。

## 第一步，通读文档

[Spring Data Elasticsearch 官方文档](https://docs.spring.io/spring-data/elasticsearch/docs/3.1.5.RELEASE/reference/html/)，这是当前最新的文档。

#### 关于repository

文档一开始就介绍 `CrudRepository` ，比如，继承 `Repository`，其他比如 `JpaRepository`、`MongoRepository`是继承`CrudRepository`。也对其中的方法做了简单说明，我们一起来看一下：

```
public interface CrudRepository<T, ID extends Serializable>
  extends Repository<T, ID> {

// Saves the given entity.
  <S extends T> S save(S entity);      

// Returns the entity identified by the given ID.
  Optional<T> findById(ID primaryKey); 

// Returns all entities.
  Iterable<T> findAll();               

// Returns the number of entities.
  long count();                        

// Deletes the given entity.
  void delete(T entity);               

// Indicates whether an entity with the given ID exists.
  boolean existsById(ID primaryKey);   

  // … more functionality omitted.
}
```

好了，下面我们看一下今天的主角 `ElasticsearchRepository` 他是怎样的吧。

![ElasticsearchRepository继承图](https://upload-images.jianshu.io/upload_images/5805596-7d08db984ae32d65.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这说明什么？

- 用法和JPA一样；

- 再这他除了有CRUD的基本功能之外，还有分页和排序。

清楚了这之后，是不是应该考虑该如何使用了呢？

#### 如何用？

没错，接下来，开始说如何用，也写了很多示例代码。相对来说，还是比较简单，这里就贴一下代码就行了吧。

```java
interface PersonRepository extends Repository<User, Long> {

  List<Person> findByEmailAddressAndLastname(EmailAddress emailAddress, String lastname);

  // Enables the distinct flag for the query
  List<Person> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);
  List<Person> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);

  // Enabling ignoring case for an individual property
  List<Person> findByLastnameIgnoreCase(String lastname);
  // Enabling ignoring case for all suitable properties
  List<Person> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);

  // Enabling static ORDER BY for a query
  List<Person> findByLastnameOrderByFirstnameAsc(String lastname);
  List<Person> findByLastnameOrderByFirstnameDesc(String lastname);
}
```

是不是这样，就可以正常使用了呢？

#### 问题

当然可以，但是如果错了问题怎么办呢，官网写了一个常见的问题，比如包扫描问题，没有你要的方法。

```java
interface HumanRepository {
  void someHumanMethod(User user);
}

class HumanRepositoryImpl implements HumanRepository {

  public void someHumanMethod(User user) {
    // Your custom implementation
  }
}

interface ContactRepository {

  void someContactMethod(User user);

  User anotherContactMethod(User user);
}

class ContactRepositoryImpl implements ContactRepository {

  public void someContactMethod(User user) {
    // Your custom implementation
  }

  public User anotherContactMethod(User user) {
    // Your custom implementation
  }
}
```

你也可以自己写接口，并且去实现它。

说完理论，作为我，应该在实际的代码中如何运用呢？

#### 示例

官方也提供了很多示例代码，我们一起来看看。

```java
@Controller
class PersonController {

  @Autowired PersonRepository repository;

  @RequestMapping(value = "/persons", method = RequestMethod.GET)
  HttpEntity<PagedResources<Person>> persons(Pageable pageable,
    PagedResourcesAssembler assembler) {

    Page<Person> persons = repository.findAll(pageable);
    return new ResponseEntity<>(assembler.toResources(persons), HttpStatus.OK);
  }
}
```

这段代码相对来说还是十分经典的，我相信很多人都看到别人的代码，可能都会问，它为什么会这么用呢，答案或许就在这里吧。

当然，这是以前的代码，或许现在用不一定合适。

#### 高级搜索

终于到高潮了！

学完我的第一节，你应该已经发现了，Elasticsearch搜索是一件十分复杂的事，为了用好它，我们不得不学好它。一起加油。

到这里，官方文档我们算是过了一遍了，大致明白了，他要告诉我们什么。其实，文档还有很多内容，可能你遇到的问题都能在里面找到答案。

最后，我们继续看一下官网写的一段处理得十分优秀的一段代码吧：

```java
SearchQuery searchQuery = new NativeSearchQueryBuilder()
    .withQuery(matchAllQuery())
    .withIndices(INDEX_NAME)
    .withTypes(TYPE_NAME)
    .withFields("message")
    .withPageable(PageRequest.of(0, 10))
    .build();

CloseableIterator<SampleEntity> stream = elasticsearchTemplate.stream(searchQuery, SampleEntity.class);

List<SampleEntity> sampleEntities = new ArrayList<>();
while (stream.hasNext()) {
    sampleEntities.add(stream.next());
}
```

## 第二部分，Spring Boot整合ElasticSearch

#### 添加依赖

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-elasticsearch'
```

#### 添加配置

```yaml
spring:
  data:
    elasticsearch:
      cluster-nodes: localhost:9300
      cluster-name: es-wyf
```

这样就完成了整合，接下来我们用两种方式操作。

#### Model

我们先写一个的实体类，借助这个实体类呢来完成基础的CRUD功能。

```java
@Data
@Accessors(chain = true)
@Document(indexName = "blog", type = "java")
public class BlogModel implements Serializable {

    private static final long serialVersionUID = 6320548148250372657L;

    @Id
    private String id;

    private String title;

    //@Field(type = FieldType.Date, format = DateFormat.basic_date)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date time;
}
```

注意id字段是必须的，可以不写注解@Id。

#### BlogRepository

```java
public interface BlogRepository extends ElasticsearchRepository<BlogModel, String> {
}
```

## 第三部分，CRUD

基础操作的代码，都是在 `BlogController` 里面写。

```java
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogRepository blogRepository;
}
```

#### 添加

```java
@PostMapping("/add")
public Result add(@RequestBody BlogModel blogModel) {
    blogRepository.save(blogModel);
    return Result.success();
}
```

我们添加一条数据，标题是：Elasticsearch实战篇：Spring Boot整合ElasticSearch，时间是：2019-03-06。我们来测试，看一下成不成功。

POST http://localhost:8080/blog/add

```json
{
	"title":"Elasticsearch实战篇：Spring Boot整合ElasticSearch",
	"time":"2019-05-06"
}
```

得到响应：

```json
{
    "code": 0,
    "msg": "Success"
}
```

嘿，成功了。那接下来，我们一下查询方法测试一下。

#### 查询

- 根据ID查询

```java
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
```

测试一下：

![测试根据ID查询](https://upload-images.jianshu.io/upload_images/5805596-95e0ac47daebb7a0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

ok，没问题。

- 查询所有

```java
@GetMapping("/get")
public Result getAll() {
    Iterable<BlogModel> iterable = blogRepository.findAll();
    List<BlogModel> list = new ArrayList<>();
    iterable.forEach(list::add);
    return Result.success(list);
}
```

测试一下：

GET http://localhost:8080/blog/get

结果：

```json
{
    "code": 0,
    "msg": "Success",
    "data": [
        {
            "id": "fFXTTmkBTzBv3AXCweFS",
            "title": "Elasticsearch实战篇：Spring Boot整合ElasticSearch",
            "time": "2019-05-06"
        }
    ]
}
``` 

#### 根据ID修改

```java
@PostMapping("/update")
public Result updateById(@RequestBody BlogModel blogModel) {
    String id = blogModel.getId();
    if (StringUtils.isEmpty(id))
        return Result.error();
    blogRepository.save(blogModel);
    return Result.success();
}
```

测试：

POST http://localhost:8080/blog/update

```json
{
	"id":"fFXTTmkBTzBv3AXCweFS",
	"title":"Elasticsearch入门篇",
	"time":"2019-05-01"
}
```

响应：

```json
{
    "code": 0,
    "msg": "Success"
}
```

查询一下：

![修改数据成功](https://upload-images.jianshu.io/upload_images/5805596-44762b9282f2afbc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

ok，成功！

#### 删除

- 根据ID删除

```java
@DeleteMapping("/delete/{id}")
public Result deleteById(@PathVariable String id) {
    if (StringUtils.isEmpty(id))
        return Result.error();
    blogRepository.deleteById(id);
    return Result.success();
}
```

测试：

DELETE  http://localhost:8080/blog/delete/fFXTTmkBTzBv3AXCweFS

响应：

```json
{
    "code": 0,
    "msg": "Success"
}
```

我们再查一下：

![删除数据成功](https://upload-images.jianshu.io/upload_images/5805596-31f49c418faeb701.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 删除所有数据

```java
@DeleteMapping("/delete")
public Result deleteById() {
    blogRepository.deleteAll();
    return Result.success();
}
```

## 第四部分，搜索

#### 构造数据

为了方便测试，我们先构造数据

![构造查询数据](https://upload-images.jianshu.io/upload_images/5805596-3313589ddca0e1b6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### Repository查询操作

**搜索标题中的关键字**

`BlogRepository`

```java
List<BlogModel> findByTitleLike(String keyword);
```

`BlogController`

```java
@GetMapping("/rep/search/title")
public Result repSearchTitle(String keyword) {
    if (StringUtils.isEmpty(keyword))
        return Result.error();
    return Result.success(blogRepository.findByTitleLike(keyword));
}
```

我们来测试一下。

POST http://localhost:8080/blog/rep/search/title?keyword=java

结果：
```json
{
    "code": 0,
    "msg": "Success",
    "data": [
        {
            "id": "f1XrTmkBTzBv3AXCeeFA",
            "title": "java实战",
            "time": "2018-03-01"
        },
        {
            "id": "fVXrTmkBTzBv3AXCHuGH",
            "title": "java入门",
            "time": "2018-01-01"
        },
        {
            "id": "flXrTmkBTzBv3AXCUOHj",
            "title": "java基础",
            "time": "2018-02-01"
        },
        {
            "id": "gFXrTmkBTzBv3AXCn-Eb",
            "title": "java web",
            "time": "2018-04-01"
        },
        {
            "id": "gVXrTmkBTzBv3AXCzuGh",
            "title": "java ee",
            "time": "2018-04-10"
        }
    ]
}
```

继续搜索：

GET http://localhost:8080/blog/rep/search/title?keyword=入门

结果：

```json
{
    "code": 0,
    "msg": "Success",
    "data": [
        {
            "id": "hFXsTmkBTzBv3AXCtOE6",
            "title": "Elasticsearch入门",
            "time": "2019-01-20"
        },
        {
            "id": "fVXrTmkBTzBv3AXCHuGH",
            "title": "java入门",
            "time": "2018-01-01"
        },
        {
            "id": "glXsTmkBTzBv3AXCBeH_",
            "title": "php入门",
            "time": "2018-05-10"
        }
    ]
}
```

为了验证，我们再换一个关键字搜索：

GET http://localhost:8080/blog/rep/search/title?keyword=java入门

```json
{
    "code": 0,
    "msg": "Success",
    "data": [
        {
            "id": "fVXrTmkBTzBv3AXCHuGH",
            "title": "java入门",
            "time": "2018-01-01"
        },
        {
            "id": "hFXsTmkBTzBv3AXCtOE6",
            "title": "Elasticsearch入门",
            "time": "2019-01-20"
        },
        {
            "id": "glXsTmkBTzBv3AXCBeH_",
            "title": "php入门",
            "time": "2018-05-10"
        },
        {
            "id": "gFXrTmkBTzBv3AXCn-Eb",
            "title": "java web",
            "time": "2018-04-01"
        },
        {
            "id": "gVXrTmkBTzBv3AXCzuGh",
            "title": "java ee",
            "time": "2018-04-10"
        },
        {
            "id": "f1XrTmkBTzBv3AXCeeFA",
            "title": "java实战",
            "time": "2018-03-01"
        },
        {
            "id": "flXrTmkBTzBv3AXCUOHj",
            "title": "java基础",
            "time": "2018-02-01"
        }
    ]
}
```

哈哈，有没有觉得很眼熟。

那根据上次的经验，我们正好换一种方式解决这个问题。

```java
@Query("{\"match_phrase\":{\"title\":\"?0\"}}")
List<BlogModel> findByTitleCustom(String keyword);
```

值得一提的是，官方文档示例代码可能是为了好看，出现问题。

官网文档给的错误示例：

![官网文档错误](https://upload-images.jianshu.io/upload_images/5805596-dcc56e96ef91cc47.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

官网示例代码：

![官方示例代码](https://upload-images.jianshu.io/upload_images/5805596-f52fb1f0e4441826.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

[官方示例代码](https://github.com/spring-projects/spring-data-elasticsearch/blob/416daef2f92734d0d1b5681265d719b9f1c347d5/src/test/java/org/springframework/data/elasticsearch/repositories/custom/SampleCustomMethodRepository.java)

另外，`?0` 代指变量的意思。


```java
@GetMapping("/rep/search/title/custom")
public Result repSearchTitleCustom(String keyword) {
    if (StringUtils.isEmpty(keyword))
        return Result.error();
    return Result.success(blogRepository.findByTitleCustom(keyword));
}
```

测试一下：

![测试成功示例](https://upload-images.jianshu.io/upload_images/5805596-193d186464764674.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

ok，没有问题。

#### ElasticsearchTemplate

```java
@Autowired
private ElasticsearchTemplate elasticsearchTemplate;

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
```

测试：

POST http://localhost:8080/blog/search/title?keyword=java入门

结果：

```json
{
    "code": 0,
    "msg": "Success",
    "data": [
        {
            "id": "fVXrTmkBTzBv3AXCHuGH",
            "title": "java入门",
            "time": "2018-01-01"
        },
        {
            "id": "hFXsTmkBTzBv3AXCtOE6",
            "title": "Elasticsearch入门",
            "time": "2019-01-20"
        },
        {
            "id": "glXsTmkBTzBv3AXCBeH_",
            "title": "php入门",
            "time": "2018-05-10"
        },
        {
            "id": "gFXrTmkBTzBv3AXCn-Eb",
            "title": "java web",
            "time": "2018-04-01"
        },
        {
            "id": "gVXrTmkBTzBv3AXCzuGh",
            "title": "java ee",
            "time": "2018-04-10"
        },
        {
            "id": "f1XrTmkBTzBv3AXCeeFA",
            "title": "java实战",
            "time": "2018-03-01"
        },
        {
            "id": "flXrTmkBTzBv3AXCUOHj",
            "title": "java基础",
            "time": "2018-02-01"
        }
    ]
}
```

OK，暂时先到这里，关于搜索，我们后面会专门开一个专题，学习搜索。

## 第五部分，例子

我们写个什么例子，想了很久，那就写一个搜索手机的例子吧！

#### 界面截图

我们先看下最后实现的效果吧

主页效果：

![主页效果](https://upload-images.jianshu.io/upload_images/5805596-f1bbd5b82c575182.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

分页效果：

![分页效果](https://upload-images.jianshu.io/upload_images/5805596-43a9a6e744f382f7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们搜索 “小米”：

![全文搜索 - “小米”](https://upload-images.jianshu.io/upload_images/5805596-86d72ee0b097f7b4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们搜索 “1999”：

![全文搜索 - “1999”](https://upload-images.jianshu.io/upload_images/5805596-8c098a7f74fdbf4b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们搜索 “黑色”：

![全文搜索 - “黑色”](https://upload-images.jianshu.io/upload_images/5805596-e18eb9531b6e5a21.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

高级搜索页面：

![高级搜索 - "主页面"](https://upload-images.jianshu.io/upload_images/5805596-78444e87955a4313.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们使用高级搜索，搜索：“小米”、“1999”：

![高级搜索 - “小米 1999”](https://upload-images.jianshu.io/upload_images/5805596-7b38b4b0860e516a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

高级搜索 “小米”、“1999” 结果：

![高级搜索 - “小米 1999” - 结果](https://upload-images.jianshu.io/upload_images/5805596-e9d4801973bce1bb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上面的并且关系生效了吗？我们试一下搜索 “华为”，“1999”：

![高级搜索 - “华为 1999” - 结果](https://upload-images.jianshu.io/upload_images/5805596-4926eb83bacef927.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

最后，我们尝试搜索时间段：

![高级搜索 - “2019-03-19 01:44:53 ~ 2019-03-19 01:44:55”](https://upload-images.jianshu.io/upload_images/5805596-61be4ca8339f5915.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

看一下，搜索结果吧：

![高级搜索 - “2019-03-19 01:44:53 ~ 2019-03-19 01:44:55” - 结果](https://upload-images.jianshu.io/upload_images/5805596-71550e6f9058fd26.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

说实话，这个时间搜索结果，我不是很满意，ES 的时间问题，我打算在后面花一些时间去研究下。

#### 搭建项目

基于Gradle搭建Spring Boot项目，把我折腾的受不了（如果哪位这方面有经验，可以给我指点指点），这个demo写了很久，那天都跑的好好的，今早上起来，就跑步起来了，一气之下，就改成Maven了。

下面看一下我的依赖和配置

**pom.xml** 片段

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.3.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!--
    添加 JavaLib 支持
    用于接口返回
     -->
    <dependency>
        <groupId>com.github.fengwenyi</groupId>
        <artifactId>JavaLib</artifactId>
        <version>1.0.7.RELEASE</version>
    </dependency>

    <!--
    添加 webflux 支持
    用于编写非阻塞接口
     -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>

    <!--
    添加 fastjson 的支持
    用于处理JSON格式数据
     -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.56</version>
    </dependency>

    <!--
    添加 Httpclient 的支持
    用于网络请求
     -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.7</version>
    </dependency>

    <!--
    添加 jsoup 的支持
    用于解析网页内容
     -->
    <dependency>
        <groupId>org.jsoup</groupId>
        <artifactId>jsoup</artifactId>
        <version>1.10.2</version>
    </dependency>
</dependencies>
```
**application.yml**

```yaml
server:
  port: 9090

spring:
  data:
    elasticsearch:
      cluster-nodes: localhost:9300
      cluster-name: es-wyf
      repositories:
        enabled: true
```

**PhoneModel**

```java
@Data
@Accessors(chain = true)
@Document(indexName = "springboot_elasticsearch_example_phone", type = "com.fengwenyi.springbootelasticsearchexamplephone.model.PhoneModel")
public class PhoneModel implements Serializable {
    private static final long serialVersionUID = -5087658155687251393L;

    /* ID */
    @Id
    private String id;

    /* 名称 */
    private String name;

    /* 颜色，用英文分号(;)分隔 */
    private String colors;

    /* 卖点，用英文分号(;)分隔 */
    private String sellingPoints;

    /* 价格 */
    private String price;

    /* 产量 */
    private Long yield;

    /* 销售量 */
    private Long sale;

    /* 上市时间 */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date marketTime;

    /* 数据抓取时间 */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
```

**PhoneRepository**

```java
public interface PhoneRepository extends ElasticsearchRepository<PhoneModel, String> {
}
```

**PhoneController**

```java
@RestController
@RequestMapping(value = "/phone")
@CrossOrigin
public class PhoneController {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

}
```

后面接口，都会在这里写。

#### 构造数据

我的数据是抓的 “华为” 和 “小米” 官网

首先使用 `httpclient` 下载html，然后使用 `jsoup` 进行解析。

以 **华为** 为例：

```java
private void huawei() throws IOException {
    CloseableHttpClient httpclient = HttpClients.createDefault(); // 创建httpclient实例
    HttpGet httpget = new HttpGet("https://consumer.huawei.com/cn/phones/?ic_medium=hwdc&ic_source=corp_header_consumer"); // 创建httpget实例

    CloseableHttpResponse response = httpclient.execute(httpget); // 执行get请求
    HttpEntity entity=response.getEntity(); // 获取返回实体
    //System.out.println("网页内容："+ EntityUtils.toString(entity, "utf-8")); // 指定编码打印网页内容
    String content = EntityUtils.toString(entity, "utf-8");
    response.close(); // 关闭流和释放系统资源

//        System.out.println(content);

    Document document = Jsoup.parse(content);
    Elements elements = document.select("#content-v3-plp #pagehidedata .plphidedata");
    for (Element element : elements) {
//            System.out.println(element.text());
        String jsonStr = element.text();
        List<HuaWeiPhoneBean> list = JSON.parseArray(jsonStr, HuaWeiPhoneBean.class);
        for (HuaWeiPhoneBean bean : list) {
            String productName = bean.getProductName();
            List<ColorModeBean> colorsItemModeList = bean.getColorsItemMode();

            StringBuilder colors = new StringBuilder();
            for (ColorModeBean colorModeBean : colorsItemModeList) {
                String colorName = colorModeBean.getColorName();
                colors.append(colorName).append(";");
            }

            List<String> sellingPointList = bean.getSellingPoints();
            StringBuilder sellingPoints = new StringBuilder();
            for (String sellingPoint : sellingPointList) {
                sellingPoints.append(sellingPoint).append(";");
            }

//                System.out.println("产品名：" + productName);
//                System.out.println("颜  色：" + color);
//                System.out.println("买  点：" + sellingPoint);
//                System.out.println("-----------------------------------");
            PhoneModel phoneModel = new PhoneModel()
                    .setName(productName)
                    .setColors(colors.substring(0, colors.length() - 1))
                    .setSellingPoints(sellingPoints.substring(0, sellingPoints.length() - 1))
                    .setCreateTime(new Date());
            phoneRepository.save(phoneModel);
        }
    }
}
```

#### 全文搜索

全文搜索来说，还是相对来说，比较简单，直接贴代码吧：

```java
/**
 * 全文搜索
 * @param keyword 关键字
 * @param page 当前页，从0开始
 * @param size 每页大小
 * @return {@link Result} 接收到的数据格式为json
 */
@GetMapping("/full")
public Mono<Result> full(String keyword, int page, int size) {
    // System.out.println(new Date() + " => " + keyword);

    // 校验参数
    if (StringUtils.isEmpty(page))
        page = 0; // if page is null, page = 0
    
    if (StringUtils.isEmpty(size))
        size = 10; // if size is null, size default 10
    
    // 构造分页类
    Pageable pageable = PageRequest.of(page, size);
    
    // 构造查询 NativeSearchQueryBuilder
    NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
            .withPageable(pageable)
            ;
    if (!StringUtils.isEmpty(keyword)) {
        // keyword must not null
        searchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(keyword));
    }
    
    /*
    SearchQuery
    这个很关键，这是搜索条件的入口，
    elasticsearchTemplate 会 使用它 进行搜索
     */
    SearchQuery searchQuery = searchQueryBuilder.build();

    // page search
    Page<PhoneModel> phoneModelPage = elasticsearchTemplate.queryForPage(searchQuery, PhoneModel.class);
    
    // return
    return Mono.just(Result.success(phoneModelPage));
}
```

官网文档也是这么用的，所以相对来说，这还是很简单的，不过拆词 和 搜索策略  搜索速度 可能在实际使用中要考虑。

#### 高级搜索

先看代码，后面我们再来分析：

```java
/**
 * 高级搜索，根据字段进行搜索
 * @param name 名称
 * @param color 颜色
 * @param sellingPoint 卖点
 * @param price 价格
 * @param start 开始时间(格式：yyyy-MM-dd HH:mm:ss)
 * @param end 结束时间(格式：yyyy-MM-dd HH:mm:ss)
 * @param page 当前页，从0开始
 * @param size 每页大小
 * @return {@link Result}
 */
@GetMapping("/_search")
public Mono<Result> search(String name, String color, String sellingPoint, String price, String start, String end, int page, int size) {

    // 校验参数
    if (StringUtils.isEmpty(page) || page < 0)
        page = 0; // if page is null, page = 0

    if (StringUtils.isEmpty(size) || size < 0)
        size = 10; // if size is null, size default 10
    
    // 构造分页对象
    Pageable pageable = PageRequest.of(page, size);

    // BoolQueryBuilder (Elasticsearch Query)
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    if (!StringUtils.isEmpty(name)) {
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", name));
    }

    if (!StringUtils.isEmpty(color)) {
        boolQueryBuilder.must(QueryBuilders.matchQuery("colors", color));
    }

    if (!StringUtils.isEmpty(color)) {
        boolQueryBuilder.must(QueryBuilders.matchQuery("sellingPoints", sellingPoint));
    }

    if (!StringUtils.isEmpty(price)) {
        boolQueryBuilder.must(QueryBuilders.matchQuery("price", price));
    }

    if (!StringUtils.isEmpty(start)) {
        Date startTime = null;
        try {
            startTime = DateTimeUtil.stringToDate(start, DateTimeFormat.yyyy_MM_dd_HH_mm_ss);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").gt(startTime.getTime()));
    }

    if (!StringUtils.isEmpty(end)) {
        Date endTime = null;
        try {
            endTime = DateTimeUtil.stringToDate(end, DateTimeFormat.yyyy_MM_dd_HH_mm_ss);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").lt(endTime.getTime()));
    }

    // BoolQueryBuilder (Spring Query)
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withPageable(pageable)
            .withQuery(boolQueryBuilder)
            .build()
            ;

    // page search
    Page<PhoneModel> phoneModelPage = elasticsearchTemplate.queryForPage(searchQuery, PhoneModel.class);
    
    // return
    return Mono.just(Result.success(phoneModelPage));
}
```

不管spring如何封装，查询方式都一样，如下图：

![es 搜索 语句](https://upload-images.jianshu.io/upload_images/5805596-d7322863b51032db.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

好吧，我们怀着这样的心态去看下源码。

```
org.springframework.data.elasticsearch.core.query.SearchQuery
```

这个是我们搜索需要用到对象

```java
    public NativeSearchQueryBuilder withQuery(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
        return this;
    }
```

OK，根据源码，我们需要构造这个 QueryBuilder，那么问题来了，这个是个什么东西，我们要如何构造，继续看：

```
org.elasticsearch.index.query.QueryBuilder
```

注意包名。

啥，怎么又跑到 elasticsearch。

你想啊，你写的东西，会让别人直接操作吗？

答案是不会的，我们只会提供API，所有，不管Spring如何封装，也只会通过API去调用。

![query 包下得类](https://upload-images.jianshu.io/upload_images/5805596-0b74c55a67992f32.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

好吧，今天先到这里，下一个专题，我们再讨论关于搜索问题。

## 链接

- [ElasticSearch入门](https://www.imooc.com/learn/889)

- [Elastic官网](https://www.elastic.co/)

- [ElasticSearch](https://www.elastic.co/cn/products/elasticsearch)

- [ElasticSearch Docs](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)

- [ElasticSearch Head](https://github.com/mobz/elasticsearch-head)

- [搜索软件Elastic上市：市值近50亿美元 是开源项目商业化范本 ](http://www.sohu.com/a/257956489_430392)


## ElasticSearch 学习系列

- [Elasticsearch入门篇——基础知识](https://www.jianshu.com/p/7ea5f6fa5d66)

- [Elasticsearch实战篇——Spring Boot整合ElasticSearch](https://www.jianshu.com/p/bd2da1cde6f5)

- [Elasticsearch专题篇——搜索](https://www.jianshu.com/p/69dc8ff24ecc)

## 代码

[Spring Boot整合Elasticsearch](https://github.com/fengwenyi/learn-springboot/tree/master/springboot-elasticsearch)

[Spring Boot结合Elasticsearch，实现手机信息搜索小例子](https://github.com/fengwenyi/learn-springboot/tree/master/springboot-elasticsearch-example-phone)

## 演示视频

<iframe height=498 width=510 src='http://player.youku.com/embed/XNDEwNzg4NzMwOA==' frameborder=0 'allowfullscreen'></iframe>

如果无法播放，请点击[这里](http://player.youku.com/embed/XNDEwNzg4NzMwOA==)