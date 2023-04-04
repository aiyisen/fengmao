package com.qy.ntf.config;

import com.qy.ntf.bean.entity.SysCollect;
import com.qy.ntf.dao.SysCollectDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Slf4j
@Component
public class Collect {
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private SysCollectDao sysCollectDao;
  private static String COUNT = "cpp_bank_list_total_size_today";

  private static String TOTAL_COUNT = "cpp_bank_list_total_size";

  private static String TOTAL_ID = "0";

  @Scheduled(cron = "0 0 0 * * ?")
  public void saveUserAccessLog() {
    HyperLogLogOperations<String, String> hyperlog = redisTemplate.opsForHyperLogLog();
    int count = hyperlog.size(COUNT).intValue();
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    SysCollect sysCollect = new SysCollect();
    sysCollect.setDate(calendar.getTime());
    sysCollect.setTotalCount(count);
    sysCollectDao.insert(sysCollect);
    log.info("日活量信息入库，昨日数据:[{}]，", count);
    // 删除today中的数据
    hyperlog.delete(COUNT);
  }
}
