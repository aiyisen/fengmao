package com.qy.ntf.controller;

import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.OrderProductAddDto;
import com.qy.ntf.service.OrderProductService;
import com.qy.ntf.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@Api(tags = {"H5支付api接口"})
@RequestMapping("/h5/pay")
@Controller
@Slf4j
public class H5payController extends BaseController {
  @Autowired private OrderProductService orderProductService;
  @Autowired private RedissonClient redissonClient;

  @ApiOperation(value = "APP - " + "权益商城商品下单", notes = "下单,返回支付相关参数")
  @PostMapping("/add")
  @ResponseBody
  //  @RedisLockTestAnnotation(redisKey = RedisConfig.REDIS_TEST + "#0")
  public ApiResponse<Object> add(
      @RequestBody @Valid OrderProductAddDto dto, BindingResult bindingResult) {
    try {
      log.info(dto.getSysAddressId() + "请求");
      RLock rLock = redissonClient.getLock("LOCK_Tre" + dto.getProductId());
      if (rLock.tryLock(0, 3, TimeUnit.SECONDS)) {
        log.info(dto.getSysAddressId() + "LOCK_Tre" + dto.getProductId() + "得到锁 执行");
        Thread.sleep(10 * 1000);
        rLock.unlock();
        log.info(dto.getSysAddressId() + "LOCK_Tre" + dto.getProductId() + "锁释放");
      }

    } catch (Exception e) {
      log.info(dto.getSysAddressId() + "异常了");
      throw new RuntimeException(e);
    }
    log.info(dto.getSysAddressId() + "走了");
    return ApiResponse.success();
  }
}
