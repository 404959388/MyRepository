package com.yellowriver.redis626.service.impl;

import com.yellowriver.redis626.annotation.RedisLock;
import com.yellowriver.redis626.service.RedisService;
import com.yellowriver.redis626.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author huanghe
 * @create 2022/2/18 3:11 PM
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

  @RedisLock
  @Override
  public void doSomething(String keyLock) {
    Random random = new Random();
    Integer anInt = random.nextInt(5000);
    Tools.sleep(anInt.longValue());
    log.info("{} ms 后 我已经处理完毕,你们去拿锁吧", anInt);
  }
}
