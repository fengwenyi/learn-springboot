package com.fengwenyi.springbootrediscacheannotation.aop.cache;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

/**
 * @author Wenyi Feng
 * @since 2019-01-31
 */
@Cacheable(value = "springboot-redis-cache-user", key = "#id")
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface UserFindIdCache {
}
