package com.fengwenyi.springbootrediscacheannotation.aop.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

/**
 * 先将缓存中对应的数据删除，再添加。
 * 如果用在List上的，可能会给Redis造成很大压力。
 * @author Wenyi Feng
 * @since 2019-01-31
 */
@Caching(put = {
        @CachePut(value = "springboot-redis-cache-user", key = "#model.id"),
        @CachePut(value = "springboot-redis-cache-user", key = "#model.name")
})
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface UserAddCache {
}
