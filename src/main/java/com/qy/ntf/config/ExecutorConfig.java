package com.qy.ntf.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

// @Configuration
// @EnableAsync
public class ExecutorConfig {

  private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);
  // 这里在配置文件（application.yml）中进行配置
  // 本例中为了方便将配置写死了
  private int corePoolSize = 10;

  private int maxPoolSize = 10;

  private int queueCapacity = 99999;

  private String namePrefix = "ocr-Thread-";

  @Bean(name = "asyncServiceExecutor")
  public ThreadPoolTaskExecutor asyncServiceExecutor() {
    logger.info("start asyncServiceExecutor");
    // ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor(); // 用自定义的子类，可以展示线程池的状况
    // 配置核心线程数
    executor.setCorePoolSize(corePoolSize);
    // 配置最大线程数
    executor.setMaxPoolSize(maxPoolSize);
    // 配置队列大小
    executor.setQueueCapacity(queueCapacity);
    // 配置线程池中的线程的名称前缀
    executor.setThreadNamePrefix(namePrefix);

    // rejection-policy：当pool已经达到max size的时候，如何处理新任务
    // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    // 执行初始化
    executor.initialize();
    return executor;
  }
}
