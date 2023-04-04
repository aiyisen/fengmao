package com.qy.ntf.config;

import com.qy.ntf.bean.entity.OrderTreasurePool;
import com.qy.ntf.dao.SysDictonaryDao;
import com.qy.ntf.service.*;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/** spring-boot项目启动完成运行 */
@Log4j2
@Component
public class AppStartupRunner implements CommandLineRunner {

  @Autowired private RedissonClient redissonClient;
  @Autowired private OrderProductService orderProductService;
  @Autowired private OrderTreasurePoolService orderTreasurePoolService;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private OrderVipService orderVipService;
  @Autowired private SysDictonaryDao sysDictonaryDao;
  @Autowired private StoreTreasureService storeTreasureService;
  @Autowired private SysUserService sysUserService;

  @Override
  public void run(String... args) {
    new Thread(
            () -> {
              RBlockingQueue<Object> blockingFairQueue =
                  redissonClient.getBlockingQueue("delay_queue_call");
              // 开启客户端监听（必须调用），否者系统重启时拿不到已过期数据，要等到系统第一次调用getDelayedQueue方法时才能开启监听
              redissonClient.getDelayedQueue(blockingFairQueue);
              while (true) {
                Object dto = null;
                try {
                  dto = blockingFairQueue.take();
                } catch (Exception e) {
                  continue;
                }
                if (Objects.isNull(dto)) {
                  continue;
                }
                RDelayedQueue<Object> delayedQueue =
                    redissonClient.getDelayedQueue(blockingFairQueue);
                try {
                  String key = dto.toString();
                  log.info("redis过期通知,key：" + key);
                  if (key.startsWith("orderProduct_")) {
                    Long orderID = Long.valueOf(key.replace("orderProduct_", ""));
                    orderProductService.orderOutDate(orderID);
                  } else if (key.startsWith("orderTreasurePool_")) {
                    Long orderID = Long.valueOf(key.replace("orderTreasurePool_", ""));
                    orderTreasurePoolService.orderOutDate(orderID);
                  } else if (key.startsWith("orderAutoConfirm_")) {
                    Long orderID = Long.valueOf(key.replace("orderAutoConfirm_", ""));
                    orderProductService.autoConfirm(orderID);
                  } else if (key.startsWith("afterOrderSend_")) {
                    Long orderID = Long.valueOf(key.replace("afterOrderSend_", ""));
                    orderProductService.saleAfterAutoConfirm(orderID);
                  } else if (key.startsWith("vip_order_")) {
                    Long orderID = Long.valueOf(key.replace("vip_order_", ""));
                    orderVipService.setFail(orderID);
                  } else if (key.startsWith("storeTreasureCheck_")) {
                    Long treasureId = Long.valueOf(key.replace("storeTreasureCheck_", ""));
                    orderTreasurePoolService.checkTreasure(treasureId);
                  } else if (key.startsWith("BSN_DDC_ASYNC_")) {
                    String tmpRes = key.replace("BSN_DDC_ASYNC_", "");
                    log.info("生成ddc延迟消息：" + key);
                    String[] s = tmpRes.split("_");
                    if (s[2].startsWith("h")) {
                      orderTreasurePoolService.executeAsync(s[0], Long.valueOf(s[1]), s[2]);
                    } else {
                      OrderTreasurePool orderProduct =
                          orderTreasurePoolService.selectById(Long.valueOf(s[2]));

                      orderTreasurePoolService.executeAsync(
                          s[0],
                          Long.valueOf(s[1]),
                          sysDictonaryDao.selectByAlias("ddc_url")
                              + orderProduct.getOrderFingerprint());
                    }
                  } else if (key.startsWith("blodJoin_")) {
                    //                    log.info("上链交易异步通知开始");
                    //                    String[] s = key.split("_");
                    //                    storeTreasureService.joinBlodCheck(s[1]);
                  } else if (key.startsWith("user_approve_")) {
                    String userId = new String(key).replace("user_approve_", "");
                    log.info("用户授权异步消息,userId: " + userId);
                    sysUserService.approve(userId);
                    delayedQueue.offer("check_approve_" + userId, 60000, TimeUnit.MILLISECONDS);
                  } else if (key.startsWith("check_approve_")) {
                    String userId = new String(key).replace("check_approve_", "");
                    log.info("用户授权检查，userId： " + userId);
                    sysUserService.checkApprove(userId);
                  } else if (key.startsWith("order_trea_copy_collect")) {
                    orderTreasurePoolService.orderTreaCopyCollectRedisDalyed();
                  } else if (key.startsWith("add_gas_")) {
                    //                    String userId = new String(key).replace("add_gas_", "");
                    //                    log.info("用户充值能量，userIds:" + userId);
                    //                    SysUser sysUser =
                    // sysUserService.selectDataById(Long.parseLong(userId));
                    //                    WenchangDDC bsnUtil = new WenchangDDC();
                    //                    SysUser sysUser1 =
                    // bsnUtil.userAddGas(sysUser.getLinkAddress(), sysUser);
                    //                    if (sysUser1 != null) {
                    //                      sysUserService.updateDataById(sysUser1);
                    //                      delayedQueue.offer("add_gas_" + sysUser.getId(), 1,
                    // TimeUnit.MINUTES);
                    //                    }
                  } else {
                    throw new RuntimeException("过期消费异常重新添加回队列中: " + key);
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                  log.error("订单过期消费异常：重新添加回队列中：orderId " + dto.toString());
                  Integer time = 15 * 60000;
                  if (dto.toString().startsWith("blodJoin_")) {
                    time = 10000;
                  } else if (dto.toString().startsWith("user_approve_")) {
                    time = 3600000 * 24;
                  }

                  delayedQueue.offer(dto.toString(), time, TimeUnit.MILLISECONDS);
                  e.printStackTrace();
                }
              }
            })
        .start();
  }
}
