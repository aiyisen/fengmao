package com.qy.ntf.config;

import com.qy.ntf.bean.entity.OrderTreasurePool;
import com.qy.ntf.dao.SysDictonaryDao;
import com.qy.ntf.service.OrderProductService;
import com.qy.ntf.service.OrderTreasurePoolService;
import com.qy.ntf.service.OrderVipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/** 主要作用就是:接收过期的redis消息,获取到key,key就是订单号,然后去更新订单号的状态(说明一下:用户30分钟不支付的话取消用户的订单) */
@Transactional
@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
  @Autowired private OrderProductService orderProductService;
  @Autowired private OrderTreasurePoolService orderTreasurePoolService;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private OrderVipService orderVipService;
  @Autowired private SysDictonaryDao sysDictonaryDao;

  public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
    super(listenerContainer);
  }

  /**
   * @param message redis key
   * @param pattern
   */
  @Override
  public void onMessage(Message message, byte[] pattern) {

    try {
      String key = message.toString();
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
          OrderTreasurePool orderProduct = orderTreasurePoolService.selectById(Long.valueOf(s[2]));

          orderTreasurePoolService.executeAsync(
              s[0],
              Long.valueOf(s[1]),
              sysDictonaryDao.selectByAlias("ddc_url") + orderProduct.getOrderFingerprint());
        }
      }
    } catch (Exception e) {
      log.error("订单过期消费异常：重新添加回队列中：orderId " + message.toString());
      redisTemplate
          .opsForValue()
          .set(message.toString(), System.currentTimeMillis() + "", 15, TimeUnit.MINUTES);
    }
  }
}
