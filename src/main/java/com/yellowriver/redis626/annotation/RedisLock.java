package com.yellowriver.redis626.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用 Redis 分布式锁,注意 .... AOP 生效必须是spring管理的类调用才行
 *
 * @author huanghe
 * @create 2022/2/18 12:44 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisLock {}
