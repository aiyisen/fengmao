package com.qy.ntf.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.customResult.AfterSaleResult;
import com.qy.ntf.bean.customResult.JsonRootBean;
import com.qy.ntf.bean.customResult.SaleAfterOrder;
import com.qy.ntf.bean.dto.OrderProductAddDto;
import com.qy.ntf.bean.dto.OrderProductDto;
import com.qy.ntf.bean.dto.OrderProductSelectDto;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.entity.OrderProduct;
import com.qy.ntf.bean.entity.SysAddress;
import com.qy.ntf.bean.param.AdminAfterSales;
import com.qy.ntf.bean.param.AfterSalesParam;
import com.qy.ntf.bean.param.UpdateOrderParam;
import com.qy.ntf.service.OrderProductService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 实物商品订单 控制层接口
 */
@Api(tags = {"实物商品订单 api接口"})
@RestController
@Slf4j
// @CrossOrigin(origins = "*")
@RequestMapping("/orderProduct")
public class OrderProductController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  @Autowired private RedissonClient redissonClient;
  /** 实物商品订单 服务 */
  @Autowired private OrderProductService orderProductService;

  @ApiOperation(value = "APP - " + "权益商城商品下单", notes = "下单,返回支付相关参数")
  @PostMapping("/add")
  public ApiResponse<Object> add(
      @RequestBody @Valid OrderProductAddDto dto, BindingResult bindingResult) {
    log.info("权益商城下单请求参数:{}", JSONObject.toJSONString(dto));
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), -1);
    } else {
      UserDto userData = getUserData();
      dto.setUserId(userData.getId());
      RLock rLock = redissonClient.getLock("LOCK_pro_" + dto.getProductId());
      try {
        if (rLock.tryLock(0, 3, TimeUnit.SECONDS)) {
          Object res = orderProductService.save(dto, getUserData());
          if (rLock.isLocked()) {
            if (rLock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
              rLock.unlock(); // 释放锁
            }
          }
          if (res.toString().equals("积分不足")) {
            return ApiResponse.fail("积分不足", -1);
          }
          log.info("权益商城下单返回参数：{}", JSONObject.toJSONString(res));
          return ApiResponse.success(res);
        }
      } catch (Exception e) {
        if (rLock.isLocked()) {
          if (rLock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
            rLock.unlock(); // 释放锁
          }
        }
        throw new RuntimeException(e);
      }
      log.info("权益商城下单请求结束：tePoId:" + dto.getProductId() + " userId: " + userData.getId());
      return ApiResponse.fail("权益商城下单请求异常");
    }
  }

  @ApiOperation(value = "APP - " + "权益商城商品获取订单价格", notes = "下单,返回支付相关参数")
  @PostMapping("/getOrderPrice")
  public ApiResponse<Object> getOrderPrice(@RequestBody OrderProductAddDto dto) {
    log.info("权益商城商品获取订单价格:{}", JSONObject.toJSONString(dto));
    UserDto userData = getUserData();
    dto.setUserId(userData.getId());
    Object res = orderProductService.getOrderPrice(dto);
    log.info("权益商城下单返回参数：{}", JSONObject.toJSONString(res));
    return ApiResponse.success(res);
  }

  @ApiOperation(value = "APP - " + "实物订单订单退款orderFlag=1 2 3", notes = "根据订单id退款")
  @PutMapping("/backOrder/{id}")
  public ApiResponse<String> backOrder(@PathVariable("id") Long id) {
    String dto = orderProductService.backOrder(id, getUserData());
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "ADMIN - " + "实物订单发货", notes = "发货")
  @PutMapping("/sendProduct/{id}/{orderNum}")
  public ApiResponse<OrderProduct> sendProduct(
      @PathVariable("id") Long id, @PathVariable("orderNum") String orderNum) {
    OrderProduct dto = orderProductService.sendProduct(id, getAdminUserData(), orderNum);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "APP - " + "实物商品订单分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<OrderProductDto>> listByPage(
      @RequestBody PageSelectParam<OrderProductSelectDto> param) {
    // 组合查询条件
    IPage<OrderProductDto> page =
        orderProductService.listByPage(
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            getUserData().getId(),
            param);
    List<Long> collect =
        page.getRecords().stream()
            .map(OrderProductDto::getSysAddressId)
            .collect(Collectors.toList());
    List<SysAddress> addresses = orderProductService.getAddressByIds(collect);
    Map<Long, SysAddress> collect1 =
        addresses.stream().collect(Collectors.toMap(SysAddress::getId, o -> o));
    for (OrderProductDto record : page.getRecords()) {
      record.setSysAddress(collect1.get(record.getSysAddressId()));
    }
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "实物商品订单分页列表")
  @PostMapping("/adminListByPage")
  public ApiResponse<IPage<OrderProductDto>> adminListByPage(
      @RequestBody PageSelectParam<OrderProductSelectDto> param) {
    // 组合查询条件
    IPage<OrderProductDto> page =
        orderProductService.listByPage(
            param.getPageNum(), Long.parseLong(param.getPageSize().toString()), null, param);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "APP - " + "操作修改订单,type:0去支付1取消订单2确认收货")
  @PostMapping("/updateOrderProduct")
  public ApiResponse<Object> updateOrderProduct(@RequestBody UpdateOrderParam param) {
    param.setUserData(getUserData());
    return ApiResponse.success(orderProductService.updateOrderProduct(param));
  }

  /** 三种售后方式： 用户-type :0仅退款1退货退款2换货 用户再次确认收货 平台-type:同意、驳回退款；同意驳回退货退款；再次发货（即代表已收到货） */
  @ApiOperation(value = "APP - " + "用户发起售后")
  @PostMapping("/saleAfterActive")
  public ApiResponse<Object> saleAfterActive(@RequestBody AfterSalesParam param) {
    return ApiResponse.success(orderProductService.saleAfterActive(getUserData(), param));
  }

  @ApiOperation(value = "GENERAL - " + "用户售后订单列表")
  @PostMapping("/saleAfterOrders")
  public ApiResponse<IPage<SaleAfterOrder>> saleAfterOrders(
      @RequestBody PageSelectParam<Integer> param) {
    return ApiResponse.success(
        orderProductService.saleAfterOrders(getUserData(), getAdminUserData(), param));
  }
  // 用户售后-用户发货
  @ApiOperation(value = "APP - " + "用户售后-发货")
  @PostMapping("/userSendBack")
  public ApiResponse<Object> userSendBack(@RequestBody AfterSalesParam param) {
    return ApiResponse.success(orderProductService.userSendBack(getUserData(), param));
  }
  //  用户售后收货--换货逻辑
  @ApiOperation(value = "APP - " + "用户售后-确认收货")
  @PostMapping("/confirmSaleAfterReceive")
  public ApiResponse<Object> confirmSaleAfterReceive(@RequestBody AfterSalesParam param) {
    return ApiResponse.success(orderProductService.confirmSaleAfterReceive(getUserData(), param));
  }
  // 用户撤销售后
  @ApiOperation(value = "APP - " + "用户售后-撤销")
  @PostMapping("/cancelOrderAfter")
  public ApiResponse<Object> cancelOrderAfter(@RequestBody AfterSalesParam param) {
    return ApiResponse.success(orderProductService.cancelOrderAfter(getUserData(), param));
  }

  // 售后订单详情
  @ApiOperation(value = "GENERAL - " + "售后订单详情")
  @GetMapping("/afterOrderDetail/{id}")
  public ApiResponse<AfterSaleResult> afterOrderDetail(@PathVariable("id") Long orderProductId) {
    return ApiResponse.success(orderProductService.afterOrderDetail(orderProductId));
  }

  @ApiOperation(value = "App - " + "获取用户默认收获地址")
  @GetMapping("/getUserDefaultAddress")
  public ApiResponse<SysAddress> getUserDefaultAddress() {
    return ApiResponse.success(orderProductService.getUserDefaultAddress(getUserData()));
  }

  @ApiOperation(value = "App - " + "获取快递信息")
  @GetMapping("/getTransInfo/{transNum}")
  public ApiResponse<JsonRootBean> getTransInfo(@PathVariable("transNum") String transNum) {
    return ApiResponse.success(orderProductService.getTransInfo(transNum));
  }

  @ApiOperation(value = "ADMIN - " + "平台处理售后")
  @PostMapping("/saleAfterDone")
  public ApiResponse<Object> saleAfterDone(@RequestBody AdminAfterSales param) {
    return ApiResponse.success(orderProductService.saleAfterDone(getAdminUserData(), param));
  }
}
