package com.qy.ntf.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/** Created by yanshao on 2018/9/30. */
@Component
@Slf4j
public class RedisLock {

  @Autowired StringRedisTemplate redisTemplate;

  private static final long EXPIRE = 5 * 1000L;

  private static final long TIMEOUT = 10 * 1000L;

  public boolean lock(String key, String value) {
    log.info("获取锁 kye:{},value:{}", key, value);
    // 请求锁时间
    long requestTime = System.currentTimeMillis();
    while (true) {
      // 等待锁时间
      long watiTime = System.currentTimeMillis() - requestTime;
      // 如果等待锁时间超过10s，加锁失败
      if (watiTime > TIMEOUT) {
        log.info("等待锁超时 kye:{},value:{}", key, value);
        return false;
      }

      if (redisTemplate
          .opsForValue()
          .setIfAbsent(key, String.valueOf(System.currentTimeMillis()))) {
        // 获取锁成功
        log.info("获取锁成功 kye:{},value:{}", key, value);
        // 设置超时时间，防止解锁失败，导致死锁
        redisTemplate.expire(key, EXPIRE, TimeUnit.MILLISECONDS);
        return true;
      }

      String valueTime = redisTemplate.opsForValue().get(key);
      if (!StringUtils.isEmpty(valueTime)
          && System.currentTimeMillis() - Long.parseLong(valueTime) > EXPIRE) {
        // 加锁时间超过过期时间，删除key，防止死锁
        log.info("锁超时, key:{}, value:{}", key, value);
        try {
          redisTemplate.opsForValue().getOperations().delete(key);
        } catch (Exception e) {
          log.info("删除锁异常 key:{}, value:{}", key, value);
          e.printStackTrace();
        }
        return false;
      }

      // 获取锁失败，等待20毫秒继续请求
      try {
        log.info("等待20 nanoSeconds key:{},value:{}", key, value);
        TimeUnit.NANOSECONDS.sleep(20000);
      } catch (InterruptedException e) {
        log.info("等待20 nanoSeconds 异常 key:{},value:{}", key, value);
        e.printStackTrace();
      }
    }
  }
  /**
   * 分布式加锁
   *
   * @param key
   * @param value
   * @return
   */
  public boolean secKilllock(String key, String value) {
    /**
     * setIfAbsent就是setnx 将key设置值为value，如果key不存在，这种情况下等同SET命令。 当key存在时，什么也不做。SETNX是”SET if Not
     * eXists”的简写
     */
    if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
      // 加锁成功返回true
      return true;
    }
    String currentValue = redisTemplate.opsForValue().get(key);

    /** 下面这几行代码的作用： 1、防止死锁 2、防止多线程抢锁 */
    if (!StringUtils.isEmpty(currentValue)
        && Long.parseLong(currentValue) < System.currentTimeMillis()) {
      String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
      if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
        return true;
      }
    }
    return false;
  }
  /**
   * 解锁
   *
   * @param key
   * @param value
   */
  public void unlock(String key, String value) {
    try {
      redisTemplate.opsForValue().getOperations().delete(key);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
