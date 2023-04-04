package com.qy.ntf.config;

public interface RedisConfig {

  String REDIS_LOCK = "'REDIS_LOCK_'";

  String REDIS_TEST = "REDIS_TEST_";

  long REDIS_EXPIRE_TIME = 60L * 1;
}
