package com.qy.ntf.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.common.utils.HttpHeaders;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.customResult.OrderVipResult;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.param.*;
import com.qy.ntf.config.AccessLimit;
import com.qy.ntf.service.OrderTreasurePoolService;
import com.qy.ntf.service.SysUserService;
import com.qy.ntf.util.*;
import com.qy.ntf.util.wxPay.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池藏品订单 控制层接口
 */
@Api(tags = {"聚合池藏品订单 api接口"})
@RestController
@Slf4j
@RequestMapping("/orderTreasurePool")
public class OrderTreasurePoolController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  @Autowired private RedissonClient redissonClient;

  /** 聚合池藏品订单 服务 */
  @Autowired private OrderTreasurePoolService orderTreasurePoolService;

  @Autowired private SysUserService sysUserService;

  @ApiOperation(value = "APP - " + "藏品首发，聚合池 下单", notes = "添加")
  @PostMapping("/add")
  public OrderApiResponse<Object> add(
      @RequestBody OrderTreasurePoolAddDto dto,
      BindingResult bindingResult,
      HttpServletRequest request) {

    log.info("藏品首发，聚合池 下单:{}", JSONObject.toJSONString(dto));
    if (bindingResult.hasErrors()) {
      return OrderApiResponse.fail(
          Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), -1);
    } else {
      RLock rLock = redissonClient.getLock("LOCK_tre_" + dto.getTeaPoId());
      UserDto userData = getUserData();
      try {
        if (rLock.tryLock(0, 5, TimeUnit.SECONDS)) {
          dto.setUserId(userData.getId());
          if (dto.getPayType() == 2) {
            SysUser sysUser = sysUserService.selectDataById(userData.getId());
            if (sysUser.getIsOpening() != 1) {
              throw new RuntimeException("对不起用户暂未完成开户");
            }
          }
          OrderApiResponse<Object> res =
              orderTreasurePoolService.save(dto, WXPayUtil.getIpAddress(request));
          if (rLock.isLocked()) {
            if (rLock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
              rLock.unlock(); // 释放锁
            }
          }
          return res;
        }
      } catch (Exception e) {
        if (rLock.isLocked()) {
          if (rLock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
            rLock.unlock(); // 释放锁
          }
        }
        e.printStackTrace();
        return OrderApiResponse.fail(e.getMessage());
      }
      log.info("藏品下单请求结束：tePoId:" + dto.getTeaPoId() + " userId: " + userData.getId());
      return OrderApiResponse.fail("服务器爆满请稍后再试");
    }
  }

  @ApiOperation(value = "APP - " + "购买会员", notes = "购买会员")
  @PostMapping("/buyVip")
  public ApiResponse<Object> buyVip(@RequestBody BuyVipParam dto) {
    log.info("购买会员：" + JSONObject.toJSONString(dto));
    UserDto userData = getUserData();
    Object res = orderTreasurePoolService.buyVip(dto, userData);
    return ApiResponse.success(res);
  }

  @ApiOperation(value = "APP - " + "超前申购参加申购抽签", notes = "参加抽签")
  @PostMapping("/addJoin")
  public ApiResponse<Object> addJoin(
      @RequestBody OrderTreasurePoolAddDto dto, BindingResult bindingResult) {

    log.info("藏品首发，聚合池 下单:{}", JSONObject.toJSONString(dto));
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), -1);
    } else {
      UserDto userData = getUserData();
      dto.setUserId(userData.getId());
      dto.setIsJoin(1);
      Object res = orderTreasurePoolService.addJoin(dto);
      log.info("藏品首发，聚合池 下单", JSONObject.toJSONString(res));
      if (res != null && res.equals("积分不足")) {
        return ApiResponse.fail("积分不足", -1);
      }
      return ApiResponse.success(res);
    }
  }

  @ApiOperation(value = "盲盒订单交易结果查询", notes = "盲盒订单交易结果查询")
  @GetMapping("/bindBoxOrderCheck/{id}")
  public ApiResponse<BindBoxOrderCheckParam> bindBoxOrderCheck(@PathVariable("id") Long id) {
    BindBoxOrderCheckParam dto = orderTreasurePoolService.bindBoxOrderCheck(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "聚合池藏品订单详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<OrderTreasurePoolDto> detail(@PathVariable("id") Long id) {
    OrderTreasurePoolDto dto = orderTreasurePoolService.getOrderTreasurePoolById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "VIP订单记录", notes = "根据时间查询Vip購買記錄")
  @PostMapping("/orderCollectData")
  public ApiResponse<OrderVipResult> orderCollectData(
      @RequestBody PageSelectParam<OrderVipSelectParam> param) {
    OrderVipResult dto = orderTreasurePoolService.orderCollectData(param);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "聚合池藏品订单修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody OrderTreasurePoolUpdateDto dto) {
    setUpdateUserInfo(dto);

    orderTreasurePoolService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "聚合池藏品订单修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody OrderTreasurePoolDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderTreasurePoolDto dto = new OrderTreasurePoolDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    orderTreasurePoolService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "聚合池藏品订单删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    orderTreasurePoolService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "APP - " + "聚合池藏品订单分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<OrderTreasurePoolDto>> listByPage(
      @RequestBody PageSelectParam<OrderTreasurePoolSelectDto> param) {
    // 组合查询条件
    IPage<OrderTreasurePoolDto> page =
        orderTreasurePoolService.listByPage(
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            getUserData().getId(),
            param.getSelectParam().getOrderFlag(),
            param.getSelectParam().getItemType());
    page = orderTreasurePoolService.updateSaleFlag(page, getUserData());
    if (param.getSelectParam().getOrderFlag() != null
        && param.getSelectParam().getOrderFlag() == 1) {
      List<OrderTreasurePoolDto> collect =
          page.getRecords().stream()
              .filter(o -> o.getIsSaleIng() == 1)
              .collect(Collectors.toList());
      page.setRecords(collect);
    }
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "APP - " + "我的申购")
  @PostMapping("/myJoin")
  public ApiResponse<IPage<StoreTreasureDto>> myJoin(@RequestBody PageSelectParam<Object> param) {
    // 组合查询条件
    IPage<StoreTreasureDto> page =
        orderTreasurePoolService.myJoin(
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            getUserData().getId());

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "聚合池藏品订单分页列表")
  @PostMapping("/adminOrderPpage")
  public ApiResponse<IPage<OrderTreasurePoolDto>> adminOrderPage(
      @RequestBody PageSelectParam<OrderTreasurePoolSelectDto> param) {
    // 组合查询条件
    IPage<OrderTreasurePoolDto> page =
        orderTreasurePoolService.adminOrderPage(
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            param.getSelectParam().getOrderFlag());
    //    page = orderTreasurePoolService.updateSaleFlag(page);
    for (OrderTreasurePoolDto o : page.getRecords()) {
      if (o.getItemType() == 3) {
        o.setItemType(4);
      }
    }
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "APP - " + "操作修改订单,type: 0取消订单1支付订单2收货/修改价格")
  @PostMapping("/updateOrderProduct")
  public OrderApiResponse<Object> updateOrderProduct(
      @RequestBody UpdateOrderParam param, HttpServletRequest request) {
    param.setUserData(getUserData());
    OrderApiResponse o =
        orderTreasurePoolService.updateOrderProduct(param, WXPayUtil.getIpAddress(request));
    return o;
  }

  @ApiOperation(value = "聚合池藏品订单列表")
  @PostMapping("/list")
  public ApiResponse<List<OrderTreasurePoolDto>> listByPage(@RequestBody OrderTreasurePoolDto dto) {
    return ApiResponse.success(orderTreasurePoolService.list(dto));
  }

  @ApiOperation(value = "APP - " + "消费排行榜 0日榜1周榜2月榜3总榜")
  @PostMapping("/consumptionCollect")
  public JSONObject consumptionCollect(@RequestBody PageSelectParam<Integer> param) {
    // 组合查询条件
    IPage<TopItem> page =
        orderTreasurePoolService.consumptionCollect(
            new Page(param.getPageNum(), param.getPageSize()), param.getSelectParam());
    TopResponse<IPage<TopItem>> success = TopResponse.success(page);
    success =
        orderTreasurePoolService.updateConsumptionTopAndTotal(
            success, getUserData(), param.getSelectParam());
    JSONObject resu = JSONObject.parseObject(JSONObject.toJSONString(success));
    resu.getJSONObject("data").put("topNum", success.getTopNum());
    resu.getJSONObject("data").put("totalCol", success.getTotal());
    return resu;
  }

  @ApiOperation(value = "APP - " + "消费排行榜 0日榜1周榜2月榜3总榜")
  @PostMapping("/inviteCollect")
  public JSONObject inviteCollect(@RequestBody PageSelectParam<Integer> param) {
    // 组合查询条件
    IPage<TopItem> page =
        orderTreasurePoolService.inviteCollect(
            new Page(param.getPageNum(), param.getPageSize()), param.getSelectParam());
    TopResponse<IPage<TopItem>> success = TopResponse.success(page);
    success =
        orderTreasurePoolService.updateInviteCollectTopAndTotal(
            success, getUserData(), param.getSelectParam());
    JSONObject resu = JSONObject.parseObject(JSONObject.toJSONString(success));
    resu.getJSONObject("data").put("topNum", success.getTopNum());
    resu.getJSONObject("data").put("totalCol", success.getTotal());
    return resu;
  }

  @ApiOperation(value = "APP - " + "藏品用户快照")
  @PostMapping("/orderTreaCopy")
  public ApiResponse<IPage<SysUser>> orderTreaCopy(
      @RequestBody PageSelectParam<OrderTreaCopyParam> param) {
    // 组合查询条件
    IPage<SysUser> page =
        orderTreasurePoolService.orderTreaCopy(
            new Page(param.getPageNum(), param.getPageSize()), param.getSelectParam());
    return ApiResponse.success(page);
  }

  @AccessLimit(times = 1, second = 600)
  @ApiOperation(value = "APP - " + "藏品用户快照统计")
  @PostMapping("/orderTreaCopyCollect")
  public ApiResponse orderTreaCopyCollect() {
    orderTreasurePoolService.orderTreaCopyCollect();
    return ApiResponse.success();
  }

  @ApiOperation(value = "APP - " + "藏品用户快照导出列表")
  @PostMapping("/orderTreaCopyExport")
  public void exportConsumList(
      @RequestBody OrderTreaCopyParam param, HttpServletResponse response) {
    /** 下面给 exportList 赋值 */
    /** 下面就是具体的导出方法 */
    IPage<SysUser> page = orderTreasurePoolService.orderTreaCopy(new Page(1, 999999999), param);
    exportNewModel(page.getRecords(), response);
    response.setHeader("Content-Disposition", "attachment;filename=藏品用户快照导出列表.xls");
  }

  private void exportNewModel(List<SysUser> exportList, HttpServletResponse response) {
    OutputStream bos = null;
    String fileName = "藏品用户快照导出列表.xls";
    try {
      bos = new BufferedOutputStream(response.getOutputStream());
      response.setHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel");
      /** 这里的表头,需要与前面定义的 ExcelConsumListResp 类中的属性名保持一致,并且顺序和数量也需要一致 */
      String[] atrArray = {"手机号", "持有数量"};
      // 调用工具类中的方法,进行导出
      ExcelExportUtils.exportExcelList(fileName, atrArray, exportList, bos, "yyy-MM-dd");
      // 写出流
      ResponseEntity.ok().body(bos);
      bos.close();
    } catch (Exception e) {
      e.getMessage();
    }
  }
}
