package com.yellowriver.redis626.service;

import lombok.NonNull;

/**
 * @author huanghe
 * @create 2022/2/18 3:10 PM
 */
public interface RedisService {
  /**
   * doSomething
   *
   * @param keyLock 使用keyLock 获取分布式锁,最终会被释放
   */
  void doSomething(@NonNull String keyLock);
}
