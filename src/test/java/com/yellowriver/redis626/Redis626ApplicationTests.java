package com.yellowriver.redis626;

import com.yellowriver.redis626.annotation.RedisLock;
import com.yellowriver.redis626.db.core.User;
import com.yellowriver.redis626.db.redis.RedissonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Redis626ApplicationTests {
  private User user = new User("yellow-river", "Ali@2019", 24);
  // @Autowired RedisUtil redisUtil;
  @Autowired
  RedissonUtil redissonUtil;

  @Test
  void contextLoads() {
    //    boolean setOk = this.redisUtil.hset("users", user.getUsername(), user);
    //    if (setOk) {
    //      User getUser = (User) this.redisUtil.hget("users", user.getUsername());
    //      System.out.println(getUser.toString());
    //    }
  }

  @Test
  void redissonTest() {
    redissonUtil.set("demo", "redis-demo", 60);
    Object demo = redissonUtil.get("demo");
    System.out.println(demo);
    //    redissonUtil.set("user",user,60);
    //    User user = (User) redissonUtil.get("user");
    //    System.out.println(user.getUsername()+" "+user.getPassword());
  }

  @Test
  void lockTest() {
    Thread[] threads = new Thread[10];
    for (int i = 0; i < 10; i++) {
      threads[i] =
          new Thread(
              new Runnable() {
                @Override
                public void run() {
                  doSomething(user);
                }
              });
    }
    for (Thread thread : threads) {
      thread.start();
    }
  }

  @RedisLock()
  private void doSomething(User user) {
    System.out.println("啦啦啦德玛西亚...");
  }
}
