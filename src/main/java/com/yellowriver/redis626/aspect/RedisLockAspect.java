package com.yellowriver.redis626.aspect;

import com.yellowriver.redis626.RedissonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

/**
 * @author huanghe
 * @create 2022/2/18 12:38 PM
 */
@Aspect
@Component
public class RedisLockAspect {

  private final RedissonUtil redissonUtil;

  public RedisLockAspect(RedissonUtil redissonUtil) {
    this.redissonUtil = redissonUtil;
  }

  @Pointcut("@annotation(com.yellowriver.redis626.annotation.RedisLock)")
  public void lockPointcut() {}

  @Around("lockPointcut()")
  public Object invokeWithRedisRLock(ProceedingJoinPoint joinPoint) throws Throwable {
    /** 锁在redis中的key */
    String keyLock = joinPoint.getArgs()[0].toString();
    RLock lock = redissonUtil.lock(keyLock);
    Object result = null;
    try {
      result = joinPoint.proceed();
    } catch (Throwable ex) {
      throw ex;
    } finally {
      lock.unlock();
    }
    return result;
  }
}
