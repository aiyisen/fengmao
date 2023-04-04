package com.qy.ntf.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RedisLockTestAspectUtils {

  @Autowired RedisLock redisLockUtil;

  @Around("@annotation(redisLock)")
  public Object redisLockTest(ProceedingJoinPoint point, RedisLockTestAnnotation redisLock) {

    String lockKey = null;
    boolean flag = false;
    try {
      // 根据
      String paramterIndex = redisLock.redisKey().substring(redisLock.redisKey().indexOf("#") + 1);
      int index = Integer.parseInt(paramterIndex);
      // 获取添加注解方法中的参数列表
      Object[] args = point.getArgs();
      // 生成redis的key：
      // TODO: 2018/11/23 根据固定为：REDIS_TEST_#数字，必须是参数列表对应的下表，从0开始，并且小于参数列表的长度
      lockKey = redisLock.redisKey().replace("#" + paramterIndex, args[index].toString());
      log.info("redis key:{}", lockKey);
      // set到redis
      flag = redisLockUtil.lock(lockKey, args[index].toString());
      log.info("redis save result:{}", flag);
      // 执行添加了注解的方法并返回
      if (flag) {
        Object result = point.proceed();
        return result;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    } finally {
      // 最后在finally中删除
      if (flag) {
        redisLockUtil.unlock(lockKey, "");
      }
    }
    return null;
  }
}
