package com.qy.ntf.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.google.gson.Gson;
import com.kuaidi100.sdk.api.AutoNum;
import com.kuaidi100.sdk.api.QueryTrack;
import com.kuaidi100.sdk.core.IBaseClient;
import com.kuaidi100.sdk.request.AutoNumReq;
import com.kuaidi100.sdk.request.QueryTrackParam;
import com.kuaidi100.sdk.request.QueryTrackReq;
import com.kuaidi100.sdk.utils.SignUtils;
import com.lianpay.api.util.TraderRSAUtil;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.customResult.*;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.*;
import com.qy.ntf.bean.param.*;
import com.qy.ntf.config.AccessLimit;
import com.qy.ntf.dao.*;
import com.qy.ntf.service.OrderProductService;
import com.qy.ntf.service.StoreProPoolService;
import com.qy.ntf.util.*;
import com.qy.ntf.util.llPay.YTHttpHandler;
import com.qy.ntf.util.wxPay.WXNotifyParam;
import com.qy.ntf.util.wxPay.WXPayConfigImpl;
import com.qy.ntf.util.wxPay.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 实物商品订单 service服务实现
 */
@Slf4j
@Service("orderProductService")
public class OrderProductServiceImpl implements OrderProductService {

  @Autowired private OrderProductDao orderProductDao;
  @Autowired private RedisTemplate<String, String> redisTemplate;

  @Override
  public BaseMapper<OrderProduct> getDao() {
    return orderProductDao;
  }

  @Override
  public IPage<OrderProductDto> getListByPage(
      Class<OrderProductDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderProduct> queryWrapper) {
    IPage<OrderProductDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public void noifityWXin(WXNotifyParam wxp, boolean b) {
    log.info("noifityWXin-in:{}", wxp);
    try {
      log.debug("noifityWXin-flag:{}", b);
      if (wxp.getReturn_code().equals(ResourcesUtil.SUCCESS) && b) {
        String outtradeno = wxp.getOut_trade_no();
        String transactionId = wxp.getTransaction_id();

        log.info("微信异步通知参数：" + wxp);
        updateOrderByNotify(outtradeno, transactionId, 0);
      }
    } catch (Exception e) {
      log.error("noifityWXin-error:{}", e);
    }
  }

  @Override
  public void noifityAlipay(String outtradeno, String tradeNo) {
    updateOrderByNotify(outtradeno, tradeNo, 1);
  }

  @Override
  public void orderOutDate(Long orderID) {
    OrderProduct orderProduct = orderProductDao.selectById(orderID);
    if (orderProduct != null && orderProduct.getOrderFlag() == 0) {
      orderProduct.setOrderFlag(-1);
      orderProduct.setUpdateTime(new Date());
      orderProductDao.updateById(orderProduct);
      OrderProductRecord orderProductRecord =
          orderProductRecordDao.selectById(orderProduct.getProductId());
      if (orderProductRecord != null) {
        StoreProduct storeProduct =
            storeProductDao.selectById(orderProductRecord.getStoreProductId());
        storeProduct.setSurplusCount(storeProduct.getSurplusCount() + orderProduct.getProCount());
        int i = storeProduct.getFrostCount() - orderProduct.getProCount();
        storeProduct.setFrostCount(i >= 0 ? i : i);
        storeProductDao.updateById(storeProduct);
      }
    }
  }

  @Override
  public IPage<OrderProductDto> listByPage(
      Integer pageNum, long pageSize, Long userId, PageSelectParam<OrderProductSelectDto> param) {
    IPage<OrderProductDto> result =
        orderProductDao.listByPage(new Page<>(pageNum, pageSize), userId, param);
    List<Long> orderIds =
        result.getRecords().stream().map(OrderProductDto::getId).collect(Collectors.toList());
    if (orderIds.size() > 0) {
      LambdaQueryWrapper<OrderProductRecord> que = new LambdaQueryWrapper<>();
      que.in(OrderProductRecord::getOrderProductId, orderIds);
      List<OrderProductRecord> orderProductRecords = orderProductRecordDao.selectList(que);

      Map<Long, SysAddress> address =
          sysAddressDao
              .selectBatchIds(
                  result.getRecords().stream()
                      .map(OrderProductDto::getSysAddressId)
                      .collect(Collectors.toList()))
              .stream()
              .collect(Collectors.toMap(SysAddress::getId, o -> o));

      LambdaQueryWrapper<OrderProAfter> afterQue = Wrappers.lambdaQuery();
      afterQue.in(OrderProAfter::getOrderProductId, orderIds).orderByAsc(BaseEntity::getCreateTime);
      List<OrderProAfter> orderProAfters = orderProAfterDao.selectList(afterQue);
      ModelMapper md = new ModelMapper();
      for (OrderProductDto record : result.getRecords()) {
        List<OrderProductRecord> collect =
            orderProductRecords.stream()
                .filter(o -> o.getOrderProductId().equals(record.getId()))
                .collect(Collectors.toList());
        if (collect.size() > 0) {
          record.setOrderProductRecord(collect.get(0));
        }
        if (record.getSysAddressId() != null && address.get(record.getSysAddressId()) != null) {
          record.setSysAddress(address.get(record.getSysAddressId()));
        }
        List<OrderProAfter> collect1 =
            orderProAfters.stream()
                .filter(o -> o.getOrderProductId().equals(record.getId()))
                .collect(Collectors.toList());
        List<OrderProAfterDto> map =
            md.map(collect1, new TypeToken<List<OrderProAfterDto>>() {}.getType());
        record.setOrderProAfterDto(map);
      }
    }
    return result;
  }

  @Override
  public Object updateOrderProduct(UpdateOrderParam param) {
    OrderProduct orderProduct = orderProductDao.selectById(param.getOrderId());
    if (!orderProduct.getCreateId().equals(param.getUserData().getId())) {
      throw new TokenInvalidException("订单与令牌信息不一致");
    }
    if (orderProduct.getOrderFlag() != 0 && param.getType() == 0) {
      throw new RuntimeException("订单状态异常，请刷新页面后再试");
    }
    OrderProductRecord orderProductRecord =
        orderProductRecordDao.selectById(orderProduct.getProductId());
    ModelMapper md = new ModelMapper();
    StoreProduct storeProduct = new StoreProduct();
    md.map(orderProductRecord, storeProduct);
    SysUser sysUser = sysUserDao.selectById(param.getUserData().getId());

    // 0去支付1取消订单2确认收货
    switch (param.getType()) {
      case 0:
        if (param.getPayType() != null) {
          switch (param.getPayType()) {
              // 0微信1支付宝2applePay3余额支付
            case 0:
              if (param.getFlagChnl() != null && "3".equals(param.getFlagChnl())) {
                return getWeixinPayH5Response(orderProduct, storeProduct);
              } else {
                return getWeixinPayResponse(orderProduct, storeProduct);
              }
            case 1:
              if (param.getFlagChnl() != null && "3".equals(param.getFlagChnl())) {
                return getAlipayTradeAppPayH5Response(orderProduct, storeProduct);
              } else {
                return getAlipayTradeAppPayResponse(orderProduct, storeProduct);
              }
            case 2:
              // TODO 苹果支付订单
              return null;
            case 3:
              //              if (sysUser.getBalance().compareTo(orderProduct.getTotalPrice()) >= 0)
              // {
              //
              // sysUser.setBalance(sysUser.getBalance().subtract(orderProduct.getTotalPrice()));
              //                sysUserDao.updateById(sysUser);
              //                // 添加流水
              //                addConBalanceRecord(orderProduct);
              //                orderProduct.setOrderFlag(1);
              //                orderProduct.setPayTime(new Date());
              //                orderProductDao.updateById(orderProduct);
              //                RBlockingQueue<Object> blockingFairQueue =
              //                    redissonClient.getBlockingQueue("delay_queue_call");
              //                RDelayedQueue<Object> delayedQueue =
              //                    redissonClient.getDelayedQueue(blockingFairQueue);
              //                delayedQueue.offer("orderAutoConfirm_" + orderProduct.getId(), 15,
              // TimeUnit.DAYS);
              //                return "success";
              //              } else {
              //                throw new RuntimeException("余额不足请充值后再试");
              //              }
              throw new RuntimeException("支付方式错误");
            case 4:
              OrderProductAddDto tmpDto = new OrderProductAddDto();
              tmpDto.setFlagChnl(param.getFlagChnl());
              tmpDto.setIdNo(param.getIdNo());
              tmpDto.setAcctName(param.getAcctName());
              tmpDto.setCardNo(param.getCardNo());
              tmpDto.setNoAgree(param.getNoAgree());

              return lianlianPayOrder(tmpDto, orderProduct, storeProduct, sysUser);
            default:
              throw new RuntimeException("支付方式异常");
          }
        }
        throw new RuntimeException("请确认支付方式");
      case 1:
        orderProduct.setOrderFlag(-1);
        orderProduct.setUpdateId(sysUser.getId());
        orderProduct.setUpdateTime(new Date());
        orderProductDao.updateById(orderProduct);
        // 冻结库存归还
        storeProduct = storeProductDao.selectById(orderProductRecord.getStoreProductId());
        int i = storeProduct.getFrostCount() - orderProduct.getProCount();
        storeProduct.setFrostCount(i >= 0 ? i : 0);
        storeProduct.setSurplusCount(storeProduct.getSurplusCount() + orderProduct.getProCount());
        storeProductDao.updateById(storeProduct);
        break;
      case 2:
        if (orderProduct.getOrderFlag() > 0) {
          orderProduct.setOrderFlag(3);
          orderProduct.setUpdateTime(new Date());
          orderProduct.setSaveTime(new Date());
          orderProductDao.updateById(orderProduct);
          return "success";
        } else {
          throw new RuntimeException("订单状态异常，无法确认收货");
        }
      default:
        throw new RuntimeException("操作类型错误");
    }
    return null;
  }

  @Override
  public String backOrder(Long id, UserDto userData) {
    OrderProduct orderProduct = orderProductDao.selectById(id);
    if (orderProduct == null) throw new RuntimeException("订单id异常");
    if (!orderProduct.getUId().equals(userData.getId()))
      throw new TokenInvalidException("订单与令牌信息不符");
    if (orderProduct.getProductType() == 2) throw new RuntimeException("积分兑换商品，无法发起退款");
    if (orderProduct.getOrderFlag() == 1
        || orderProduct.getOrderFlag() == 2
        || orderProduct.getOrderFlag() == 3) {
      orderProduct.setOrderFlag(-2);
      orderProductDao.updateById(orderProduct);
      return "success";
    }

    throw new MyException("此订单状态暂无法直接退款");
  }

  @Autowired private SysMessageDao sysMessageDao;

  @Override
  public OrderProduct sendProduct(Long id, SysUserAdminDto userData, String orderNum) {
    OrderProduct orderProduct = orderProductDao.selectById(id);
    if (orderProduct == null) throw new RuntimeException("订单id异常");
    if (orderProduct.getSaleAfterActive() != null && orderProduct.getSaleAfterActive() == 1) {
      throw new RuntimeException("订单已开启售后，请到售后订单菜单中处理");
    }
    if (orderProduct.getOrderFlag() == 1) {
      orderProduct.setOrderFlag(2);
      orderProduct.setLogisticsOrder(orderNum);
      orderProduct.setSendTime(new Date());
      orderProductDao.updateById(orderProduct);
      SysMessage sysMessage = new SysMessage();
      sysMessage.setMsgType(1);
      sysMessage.setMessage("您购买的订单已发货，物流单号：" + orderNum);
      sysMessage.setMsgTitle("订单发货通知");
      sysMessage.setCreateId(orderProduct.getCreateId());
      sysMessage.setCreateTime(new Date());
      sysMessageDao.insert(sysMessage);

      // 应添加极光推送
      //      AppJpushUtil.sendPush(
      //          Collections.singletonList(userData.getId()),
      //          sysMessage.getMsgTitle(),
      //          sysMessage.getMessage());
      RBlockingQueue<Object> blockingFairQueue =
          redissonClient.getBlockingQueue("delay_queue_call");
      RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
      delayedQueue.offer("orderAutoConfirm_" + orderProduct.getId(), 14, TimeUnit.DAYS);
      //      redisTemplate
      //          .opsForValue()
      //          .set(
      //              "orderAutoConfirm_" + orderProduct.getId(),
      //              orderProduct.getOrderFingerprint(),
      //              14,
      //              TimeUnit.DAYS);
      return orderProduct;
    }
    throw new RuntimeException("订单状态异常,暂无法发货");
  }

  @Autowired private RedissonClient redissonClient;

  @Override
  public void autoConfirm(Long orderID) {

    OrderProduct orderProduct = orderProductDao.selectById(orderID);
    if (orderProduct.getOrderFlag() == 2 && orderProduct.getSaleAfterActive() != 1) {
      orderProduct.setOrderFlag(3);
      orderProduct.setSaveTime(new Date());
      orderProduct.setUpdateTime(new Date());
      orderProductDao.updateById(orderProduct);
      SysMessage sysMessage = new SysMessage();
      sysMessage.setMsgType(1);
      sysMessage.setMessage("您购买的订单15天未收货，订单已自动收货,物流单号:" + orderProduct.getLogisticsOrder());
      sysMessage.setMsgTitle("系统自动收货通知");
      sysMessage.setCreateId(orderProduct.getCreateId());
      sysMessage.setCreateTime(new Date());
      sysMessageDao.insert(sysMessage);
    }
  }
  /** 三种售后方式： 用户-type :0仅退款1退货退款2换货 用户再次确认收货 平台-type:同意、驳回退款；同意驳回退货退款；再次发货（即代表已收到货） */
  @Autowired private OrderProAfterDao orderProAfterDao;

  @AccessLimit(second = 1, times = 1)
  @Override
  public Object saleAfterActive(UserDto userData, AfterSalesParam param) {
    OrderProduct orderProduct = orderProductDao.selectById(param.getOrderId());
    if (orderProduct.getProductType() != null && orderProduct.getProductType() == 2) {
      throw new RuntimeException("兑换商品无法发起售后");
    }
    if (orderProduct.getSaleAfterActive() == 1) {
      throw new RuntimeException("存在尚未完结的售后");
    }
    if ((orderProduct.getOrderFlag() > 0)
        && (orderProduct.getSaveTime() == null
            || System.currentTimeMillis() - orderProduct.getSaveTime().getTime()
                < 7 * 24 * 3600000)) {
      if (orderProduct.getSaleAfterActive() == 0 || orderProduct.getSaleAfterActive() == 2) {
        if (param.getType() == 0 || param.getType() == 1 || param.getType() == 2) {
          // 0仅退款1退货退款2换货
          OrderProAfter orderProAfter = new OrderProAfter();
          orderProAfter.setOrderProductId(orderProduct.getId());
          orderProAfter.setOrderType(param.getType());
          orderProAfter.setOrderAfterState(0);
          orderProAfter.setOrderReason(param.getOrderReason());
          orderProAfter.setOrderProductState(param.getOrderProductState());
          orderProAfter.setVoucherImgs(param.getVoucherImgs());
          orderProAfter.setPlateTransNum("");
          orderProAfter.setUserTransNum("");
          orderProAfter.setRemark(param.getRemark());
          orderProAfter.setUserIsGet(param.getUserIsGet());
          orderProAfter.setCreateId(userData.getCreateId());
          orderProAfter.setCreateTime(new Date());
          orderProAfter.setState(1);
          orderProAfterDao.insert(orderProAfter);
        } else {
          throw new RuntimeException("对不起售后方式错误");
        }
        orderProduct.setSaleAfterActive(1);
        orderProductDao.updateById(orderProduct);

      } else {
        throw new RuntimeException("对不起此订单已发起售后且未完结无法再次发起");
      }
      return null;
    } else {
      throw new RuntimeException("订单状态异常，或订单完成已超过7天无法发起售后");
    }
  }

  @Override
  public CellTotal productCollect() {
    CellTotal result = new CellTotal();
    List<CellResult> re = orderProductDao.getCollectLine();
    Collections.reverse(re);
    result.setTotalCount(new BigDecimal(re.stream().mapToInt(o -> o.getCount().intValue()).sum()));
    List<CellResult> cellResults = re.subList(0, Math.min(re.size(), 30));
    Collections.reverse(cellResults);
    result.setCellResultList(cellResults);
    result.setMonthCount(
        new BigDecimal(
            result.getCellResultList().stream().mapToInt(o -> o.getCount().intValue()).sum()));
    if (re.size() > 0)
      result.setTodayCount(
          re.get(re.size() - 1) != null ? re.get(re.size() - 1).getCount() : new BigDecimal(0));
    return result;
  }
  // 平台处理售后订单  操作类型：-1退款审核驳回1退款审核通过5退货退款(换货）审核同意-5退货退款(换货）审核驳回 1平台退款（确认退货退款且已收货拨款）3平台发货（换货）
  @Transactional
  @Override
  public String saleAfterDone(SysUserAdminDto userData, AdminAfterSales param) {
    LambdaQueryWrapper<OrderProAfter> que = Wrappers.lambdaQuery(OrderProAfter.class);
    que.eq(OrderProAfter::getOrderProductId, param.getOrderProductId());
    que.orderByDesc(BaseEntity::getCreateTime);
    List<OrderProAfter> orderProAfters = orderProAfterDao.selectList(que);
    if (orderProAfters.size() == 0) {
      throw new RuntimeException("订单id错误");
    }
    OrderProAfter orderProAfter = orderProAfters.get(0);
    OrderProduct orderProduct = orderProductDao.selectById(param.getOrderProductId());
    if (orderProduct.getSaleAfterActive() != 1) {
      throw new RuntimeException("订单id异常");
    }
    // 操作类型：-1退款审核驳回1退款审核通过5退货退款(换货）审核同意-5退货退款(换货）审核驳回 1平台退款（确认退货退款且已收货拨款）3平台发货（换货）
    if ((param.getDoType() == 1 || param.getDoType() == -1)
        && orderProAfter.getOrderType() == 0
        && orderProAfter.getOrderAfterState() == 0) {
      // -1退款审核驳回1退款审核通过
      if (orderProAfter.getOrderType() == 0) {
        OrderProAfter record = new OrderProAfter();
        record.setOrderProductId(param.getOrderProductId());
        record.setOrderType(0);
        record.setOrderAfterState(param.getDoType() == 1 ? 1 : -1);
        record.setOrderReason(param.getOrderReason());
        record.setCreateId(userData.getId());
        record.setCreateTime(new Date());
        orderProAfterDao.insert(record);
        // 订单退款逻辑调用支付宝微信啥的
        if (param.getDoType() == 1) {
          backOrderAndCheckPayType(orderProduct);
        }
        if (orderProduct.getPayType() != 4) {
          OrderProAfter record2 = new OrderProAfter();
          record2.setOrderProductId(param.getOrderProductId());
          record2.setOrderType(0);
          record2.setOrderAfterState(4); // 完成节点
          record2.setCreateId(userData.getId());
          record2.setCreateTime(new Date());
          orderProAfterDao.insert(record2);
          if (param.getDoType() == 1) {
            orderProduct.setOrderFlag(-2);
          }
          orderProduct.setSaleAfterActive(2);
          orderProduct.setUpdateTime(new Date());
          orderProductDao.updateById(orderProduct);
          SysMessage sysMessage = new SysMessage();
          sysMessage.setMsgType(1);
          if (param.getDoType() == 1) {
            sysMessage.setMessage("您的售后订单退款申请审核已通过,订单编号" + orderProduct.getOrderFingerprint());
          } else {
            sysMessage.setMessage("您的售后订单退款申请审核已驳回,订单编号" + orderProduct.getOrderFingerprint());
          }
          sysMessage.setMsgTitle("订单审核通知");
          sysMessage.setCreateId(orderProduct.getCreateId());
          sysMessage.setCreateTime(new Date());
          sysMessageDao.insert(sysMessage);
        }
      } else {
        throw new RuntimeException("售后订单状态异常无法操作");
      }
    } else if (param.getDoType() == 5 || param.getDoType() == -5) {
      // 5退货退款(换货）审核同意-5退货退款(换货）审核驳回
      if ((orderProAfter.getOrderType() == 1 || orderProAfter.getOrderType() == 2)
          && (orderProAfter.getOrderAfterState() == 0 || orderProAfter.getOrderAfterState() == 2)) {
        OrderProAfter record = new OrderProAfter();
        record.setOrderProductId(param.getOrderProductId());
        record.setOrderType(orderProAfter.getOrderType());
        record.setOrderAfterState(param.getDoType());
        record.setOrderReason(param.getOrderReason());
        record.setCreateId(userData.getId());
        record.setCreateTime(new Date());
        orderProAfterDao.insert(record);
        if (param.getDoType() == -5) {
          OrderProAfter record2 = new OrderProAfter();
          record2.setOrderProductId(param.getOrderProductId());
          record2.setOrderType(orderProAfter.getOrderType());
          record2.setOrderAfterState(4);
          record2.setCreateId(userData.getId());
          record2.setCreateTime(new Date(System.currentTimeMillis() + 10000L));
          orderProAfterDao.insert(record2);
          orderProduct.setSaleAfterActive(2);
          orderProduct.setUpdateTime(new Date());
          orderProductDao.updateById(orderProduct);
        }
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMsgType(1);
        if (param.getDoType() == 5) {
          sysMessage.setMessage("您的售后订单退货退款(换货）审核已通过,订单编号" + orderProduct.getOrderFingerprint());
        } else {
          sysMessage.setMessage("您的售后订单退货退款(换货）审核已驳回,订单编号" + orderProduct.getOrderFingerprint());
        }
        sysMessage.setMsgTitle("订单审核通知");
        sysMessage.setCreateId(orderProduct.getCreateId());
        sysMessage.setCreateTime(new Date());
        sysMessageDao.insert(sysMessage);
      } else {
        if ((param.getDoType() == 5 || param.getDoType() == -5)
            && orderProAfter.getOrderAfterState() == 2)
          throw new RuntimeException("该订单已审核通过，请选择退款选项");
        throw new RuntimeException("售后订单状态异常无法操作");
      }
    } else if (param.getDoType() == 1 && orderProAfter.getOrderType() == 1) {
      // 退货退款，平台确认收货，开始退款
      if (orderProAfter.getOrderAfterState() == 2) {
        OrderProAfter record = new OrderProAfter();
        record.setOrderProductId(param.getOrderProductId());
        record.setOrderType(1);
        record.setOrderAfterState(1);
        record.setOrderReason(param.getOrderReason());
        record.setCreateId(userData.getId());
        record.setCreateTime(new Date());
        orderProAfterDao.insert(record);
        // 正式退款调用微信支付宝相关
        backOrderAndCheckPayType(orderProduct);
        OrderProAfter record2 = new OrderProAfter();
        record2.setOrderProductId(param.getOrderProductId());
        record2.setOrderType(1);
        record2.setOrderAfterState(4);
        record2.setCreateId(userData.getId());
        record2.setCreateTime(new Date());
        orderProAfterDao.insert(record2);
        orderProduct.setSaleAfterActive(0);
        orderProduct.setUpdateTime(new Date());
        orderProduct.setOrderFlag(-2);
        orderProductDao.updateById(orderProduct);
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMsgType(1);
        sysMessage.setMessage("您的售后订单退货退款审核已通过,订单编号" + orderProduct.getOrderFingerprint());
        sysMessage.setMsgTitle("订单发货通知");
        sysMessage.setCreateId(orderProduct.getCreateId());
        sysMessage.setCreateTime(new Date());
        sysMessageDao.insert(sysMessage);
      } else {
        throw new RuntimeException("售后订单状态异常无法操作,原因：用户尚未发货无法直接退款");
      }
    } else if (param.getDoType() == 3) {
      // 3平台N+1发货（用户换货）
      if (orderProAfter.getOrderType() == 2 && orderProAfter.getOrderAfterState() == 2) {
        OrderProAfter record = new OrderProAfter();
        record.setOrderProductId(param.getOrderProductId());
        record.setOrderType(2);
        record.setOrderAfterState(3);
        record.setPlateTransNum(param.getLogisticsOrder());
        record.setLogisticsOrgName(param.getLogisticsOrgName());
        record.setCreateId(userData.getId());
        record.setCreateTime(new Date());
        orderProAfterDao.insert(record);
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMsgType(1);
        sysMessage.setMessage("您的售后换货订单已发货,物流单号:" + record.getLogisticsOrgName());
        sysMessage.setMsgTitle("订单发货通知");
        sysMessage.setCreateId(orderProduct.getCreateId());
        sysMessage.setCreateTime(new Date());
        sysMessageDao.insert(sysMessage);
        // 添加延期队列到期自动收货
        RBlockingQueue<Object> blockingFairQueue =
            redissonClient.getBlockingQueue("delay_queue_call");
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer("afterOrderSend_" + param.getOrderProductId(), 14, TimeUnit.DAYS);
        //        redisTemplate
        //            .opsForValue()
        //            .set(
        //                "afterOrderSend_" + param.getOrderProductId(),
        //                orderProduct.getOrderFingerprint(),
        //                14,
        //                TimeUnit.DAYS);
      } else {
        throw new RuntimeException("售后订单状态异常无法操作,原因：用户尚未发货");
      }
    }
    return "success";
  }

  public Map<String, String> getWeixinRefundResponse(OrderProduct orderProduct) {
    Map<String, String> result = new HashMap<>(16);
    try {
      // 封装微信退款接口参数
      HashMap<String, String> refundParam = new HashMap<>(16);
      refundParam.put("out_trade_no", orderProduct.getOrderFingerprint());
      refundParam.put("out_refund_no", UUID.randomUUID().toString().replaceAll("-", ""));

      refundParam.put("total_fee", PayUtils.yuanToFen(orderProduct.getTotalPrice()));
      refundParam.put("refund_fee", PayUtils.yuanToFen(orderProduct.getTotalPrice()));
      refundParam.put("refund_desc", "退款-权益商城");
      WXPayConfig config = WXPayConfigImpl.getInstance();
      WXPay wxPay = new WXPay(config);
      result = wxPay.refund(refundParam);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  private void backOrderAndCheckPayType(OrderProduct orderProduct) {
    // 添加支付宝微信退款流程  0微信1支付宝2applePay3余额支付
    switch (orderProduct.getPayType()) {
      case 0:
        Map<String, String> weixinRefundResponse = getWeixinRefundResponse(orderProduct);
        log.info("微信订单退款：" + JSONObject.toJSONString(weixinRefundResponse));
        orderProduct.setOrderFlag(-2);
        orderProduct.setUpdateTime(new Date());
        orderProductDao.updateById(orderProduct);
        break;
      case 1:
        aliOrderBack(orderProduct);
        orderProduct.setOrderFlag(-2);
        orderProduct.setUpdateTime(new Date());
        orderProductDao.updateById(orderProduct);
        break;
      case 2:
        break;
      case 3:
        //        ConBalanceRecord record = new ConBalanceRecord();
        //        record.setUId(orderProduct.getCreateId());
        //        record.setOrderId(orderProduct.getId() + "");
        //        record.setOrderType(7);
        //        record.setTotalPrice(orderProduct.getTotalPrice());
        //        record.setTradingChannel(3);
        //        record.setCreateId(orderProduct.getCreateId());
        //        record.setCreateTime(new Date());
        //        conBalanceRecordDao.insert(record);
        //        SysUser sysUser = sysUserDao.selectById(orderProduct.getCreateId());
        //        sysUser.setBalance(sysUser.getBalance().add(orderProduct.getTotalPrice()));
        //        sysUserDao.updateById(sysUser);
        //        orderProduct.setOrderFlag(-2);
        //        orderProduct.setUpdateTime(new Date());
        //        orderProductDao.updateById(orderProduct);
        break;

      case 4:
        LLOrderBackParam param = new LLOrderBackParam();
        param.setOid_partner(ResourcesUtil.getProperty("lianlian_oid"));
        param.setSign_type("RSA");
        param.setNo_refund(orderProduct.getOrderFingerprint());
        SimpleDateFormat sp = new SimpleDateFormat("yyyyMMddHHmmss");
        param.setDt_refund(sp.format(new Date()));
        param.setMoney_refund(orderProduct.getTotalPrice().doubleValue() + "");
        param.setOid_paybill(orderProduct.getTradeNo());
        param.setNotify_url(ResourcesUtil.getProperty("lianlian_pay_order_product_back_notify"));
        param.setSign(genSign(JSON.parseObject(JSON.toJSONString(param))));
        String url = "https://traderapi.lianlianpay.com/refund.htm";
        String reqJson = JSON.toJSONString(param);
        log.info("连连pay请求报文为:" + reqJson);
        String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, url);
        log.info("连连pay结果报文为:" + resJson);

        break;
      default:
        break;
    }
  }

  @Override
  public void orderProductBack(LLOrderBackNotifyParam param) {
    if (param.getSta_refund() != null) {
      if (param.getSta_refund().equals("2")) {
        LambdaQueryWrapper<OrderProduct> que = Wrappers.lambdaQuery(OrderProduct.class);
        que.eq(OrderProduct::getOrderFingerprint, param.getNo_refund());
        List<OrderProduct> orderProducts = orderProductDao.selectList(que);
        if (orderProducts.size() > 0) {
          OrderProduct orderProduct = orderProducts.get(0);
          SysUser sysUser = sysUserDao.selectById(orderProduct.getCreateId());
          OrderProAfter record2 = new OrderProAfter();
          record2.setOrderProductId(orderProduct.getId());
          record2.setOrderType(0);
          record2.setOrderAfterState(4);
          record2.setCreateId(sysUser.getId());
          record2.setCreateTime(new Date());
          orderProAfterDao.insert(record2);
          orderProduct.setSaleAfterActive(2);
          orderProduct.setUpdateTime(new Date());
          orderProductDao.updateById(orderProduct);
          SysMessage sysMessage = new SysMessage();
          sysMessage.setMsgType(1);
          sysMessage.setMessage("您的售后订单退款申请审核已通过,订单编号" + orderProduct.getOrderFingerprint());
          sysMessage.setMsgTitle("订单发货通知");
          sysMessage.setCreateId(orderProduct.getCreateId());
          sysMessage.setCreateTime(new Date());
          sysMessageDao.insert(sysMessage);
        }
      }
    }
  }

  @Override
  public void saleAfterAutoConfirm(Long orderID) {
    OrderProduct orderProduct = orderProductDao.selectById(orderID);
    orderProduct.setSaleAfterActive(2);
    orderProduct.setUpdateTime(new Date());
    orderProductDao.updateById(orderProduct);
    OrderProAfter record = new OrderProAfter();
    record.setOrderProductId(orderID);
    record.setOrderType(2);
    record.setOrderAfterState(4);
    record.setUserIsGet(1);
    record.setCreateId(-1L);
    record.setCreateTime(new Date());
    orderProAfterDao.insert(record);
  }

  @Override
  public Object userSendBack(UserDto userData, AfterSalesParam param) {
    OrderProduct orderProduct = orderProductDao.selectById(param.getOrderId());
    if (orderProduct == null) throw new RuntimeException("对不起订单id异常");
    LambdaQueryWrapper<OrderProAfter> que = Wrappers.lambdaQuery(OrderProAfter.class);
    que.eq(OrderProAfter::getOrderProductId, param.getOrderId())
        .orderByDesc(BaseEntity::getCreateTime);
    List<OrderProAfter> orderProAfters = orderProAfterDao.selectList(que);
    OrderProAfter last = orderProAfters.get(0);
    if ((last.getOrderType() == 1 || last.getOrderType() == 2) && last.getOrderAfterState() == 5) {
      OrderProAfter orderProAfter = new OrderProAfter();
      orderProAfter.setOrderType(last.getOrderType());
      orderProAfter.setOrderProductId(param.getOrderId());
      orderProAfter.setOrderAfterState(2);
      orderProAfter.setUserTransNum(param.getLogisticsOrder());
      orderProAfter.setLogisticsOrgName(param.getLogisticsOrgName());
      orderProAfter.setCreateId(userData.getCreateId());
      orderProAfter.setCreateTime(new Date());
      orderProAfterDao.insert(orderProAfter);
      return "success";
    }

    throw new RuntimeException("售后订单状态异常，无法退货,请等待平台审核通过");
  }

  @Override
  public Object confirmSaleAfterReceive(UserDto userData, AfterSalesParam param) {
    OrderProduct orderProduct = orderProductDao.selectById(param.getOrderId());
    if (orderProduct == null) throw new RuntimeException("对不起订单id异常");
    orderProduct.setSaleAfterActive(2);
    orderProduct.setUpdateTime(new Date());
    orderProduct.setOrderFlag(3);
    orderProductDao.updateById(orderProduct);

    OrderProAfter orderProAfter = new OrderProAfter();
    orderProAfter.setOrderProductId(param.getOrderId());
    orderProAfter.setOrderType(2);
    orderProAfter.setOrderAfterState(4);
    orderProAfter.setUserIsGet(1);
    orderProAfter.setCreateId(userData.getCreateId());
    orderProAfter.setCreateTime(new Date());
    orderProAfterDao.insert(orderProAfter);
    return "success";
  }

  @Override
  public Object cancelOrderAfter(UserDto userData, AfterSalesParam param) {
    OrderProduct orderProduct = orderProductDao.selectById(param.getOrderId());
    if (orderProduct == null) throw new RuntimeException("对不起订单id异常");
    // 平台发货后无法撤销售后订单
    List<OrderProAfter> orderProAfters = orderProAfterDao.selectByOrderId(orderProduct.getId());
    if (orderProAfters.size() > 0 && orderProAfters.get(0).getOrderAfterState() == 3)
      throw new RuntimeException("平台已发货无法撤销");
    orderProduct.setSaleAfterActive(0);
    orderProduct.setUpdateTime(new Date());
    orderProductDao.updateById(orderProduct);

    OrderProAfter orderProAfter = new OrderProAfter();
    orderProAfter.setOrderProductId(param.getOrderId());
    orderProAfter.setOrderType(orderProAfters.get(0).getOrderType());
    orderProAfter.setOrderAfterState(-4);
    orderProAfter.setUserIsGet(null);
    orderProAfter.setCreateId(userData.getCreateId());
    orderProAfter.setCreateTime(new Date());
    orderProAfterDao.insert(orderProAfter);
    return "success";
  }

  @Override
  public AfterSaleResult afterOrderDetail(Long orderProductId) {
    AfterSaleResult res = new AfterSaleResult();
    OrderProduct orderProduct = orderProductDao.selectById(orderProductId);
    if (orderProduct == null) throw new RuntimeException("对不起订单id异常");
    res.setOrderProduct(orderProduct);
    res.setSysAddress(sysAddressDao.selectById(orderProduct.getSysAddressId()));
    res.setOrderProductRecord(orderProductRecordDao.selectByOrderId(orderProduct.getId()));
    List<OrderProAfter> orderProAfters = orderProAfterDao.selectByOrderId(orderProductId);
    List<OrderProAfter> real = new ArrayList<>();
    for (int i = 0; i < orderProAfters.size(); i++) {
      if (Strings.isEmpty(res.getLogisticsOrgName())
          && Strings.isNotEmpty(orderProAfters.get(i).getLogisticsOrgName())) {
        res.setLogisticsOrgName(orderProAfters.get(i).getLogisticsOrgName());
        res.setLogNum(
            Strings.isEmpty(orderProAfters.get(i).getUserTransNum())
                ? orderProAfters.get(i).getPlateTransNum()
                : orderProAfters.get(i).getUserTransNum());
      }
      if (orderProAfters.get(i).getOrderAfterState() == 0) {
        res.setCreateTime(orderProAfters.get(i).getCreateTime());
      }
      //      if (orderProAfters.get(i).getOrderAfterState() == 2) {
      //        res.getOrderProduct().setSendTime(orderProAfters.get(i).getCreateTime());
      //      }
      if (Strings.isNotEmpty(orderProAfters.get(i).getUserTransNum())) {
        res.setTurnTime(orderProAfters.get(i).getCreateTime());
      }
      if (orderProAfters.get(i).getOrderAfterState() == 0) {
        real.add(orderProAfters.get(i));
        i = orderProAfters.size();
      } else {
        real.add(orderProAfters.get(i));
      }
    }
    res.setOrderProAfters(real);
    if (res.getOrderProAfters().size() == 0) throw new RuntimeException("订单id异常");
    OrderProAfter record = res.getOrderProAfters().get(res.getOrderProAfters().size() - 1);
    res.getOrderProAfters()
        .forEach(
            o -> {
              o.setVoucherImgs(record.getVoucherImgs());
              o.setRemark(record.getRemark());
              o.setOrderReason(record.getOrderReason());
            });

    // 用户未完成售后不能再次申请
    // 平台发货后无法撤销售后订单

    // orderType:0仅退款1退货退款2换货
    //
    // orderAfterState: 当前售后订单状态-5审核驳回-1退款驳回0待平台审核1已退款2用户已发货3平台已发货4已完成本轮售后5审核通过
    // 根据orderAfterState orderType 梳理流程
    // 当orderType=0时 最多将会有三条记录，仅退款，orderAfterState 分别为【0,1,4】【0，-1,4】
    // 当orderType=1时 最多将会有五条记录，退货退款，orderAfterState 分别为【0,-5,4】【0，5，2，1,4】
    // 当orderType=1时 最多将会有五条记录，换货退款，orderAfterState 分别为【0,-5,4】【0，5，2，3,4】
    int userFlag = -1; // 用户应继续的操作-1无操作-2撤销售后0退货退款-发货1换货-发货2再次发起售后（平台已发货将无法撤销）3用户申请换货再次确认收货
    int adminFlag = -1; // 管理员应继续的操作0审核是否允许退款（退货退款，仅退款）1发货 -1无操作
    // orderType0仅退款1退货退款2换货
    OrderProAfter orderProAfter = res.getOrderProAfters().get(0);
    switch (orderProAfter.getOrderAfterState()) {
        // -1退款驳回0待平台审核1已退款2用户已发货3平台已发货4已完成本轮售后-4已撤回
      case -1:
        userFlag = 2;
        break;
      case 0:
        userFlag = -2;
        adminFlag = 0;
        //        if (orderProAfter.getOrderType() == 0) {
        //          adminFlag = 0;
        //        } else if (orderProAfter.getOrderType() == 1) {
        //          userFlag = 0;
        //        } else if (orderProAfter.getOrderType() == 2) {
        //          userFlag = 1;
        //        }
        break;
      case 1:
        break;
      case 2:
        if (orderProAfter.getOrderType() == 1) {
          adminFlag = 0;
        } else if (orderProAfter.getOrderType() == 2) {
          adminFlag = 1;
        }
        break;
      case 3:
        userFlag = 3;
        break;
      case 4:
        // 已完成本轮售后
        break;
      case -4:
        // 已撤销本轮售后
        break;
      case 5:
        if (orderProAfter.getOrderType() == 1) {
          userFlag = 0;
        } else if (orderProAfter.getOrderType() == 2) {
          userFlag = 1;
        }
        break;
      case -5:
        break;
      default:
        break;
    }
    res.setAdminFlag(adminFlag);
    res.setUserFlag(userFlag);
    return res;
  }

  @Override
  public List<SysAddress> getAddressByIds(List<Long> collect) {

    return collect.size() > 0 ? sysAddressDao.selectDetailByIds(collect) : new ArrayList<>();
  }

  @Override
  public Object getOrderPrice(OrderProductAddDto tmpDto) {
    StoreProduct storeProduct = storeProductDao.selectById(tmpDto.getUserId());
    ProductOrderPrice productOrderPrice = new ProductOrderPrice();
    productOrderPrice.setItemPrice(storeProduct.getCurPrice());
    productOrderPrice.setFreight(storeProduct.getFeight());
    productOrderPrice.setCount(tmpDto.getProCount());
    BigDecimal recordTotal =
        storeProduct
            .getCurPrice()
            .multiply(new BigDecimal(tmpDto.getProCount()))
            .add(storeProduct.getFeight());
    if (storeProduct.getFeight() != null) {
      recordTotal = recordTotal.add(storeProduct.getFeight());
    }
    productOrderPrice.setTotalPrice(BigDecimal.valueOf(recordTotal.doubleValue()));
    if (storeProduct.getVipPercent() != null) {
      recordTotal = recordTotal.multiply(storeProduct.getVipPercent());
    }
    productOrderPrice.setRealTotalPrice(recordTotal);
    return productOrderPrice;
  }

  public static IPage<SaleAfterOrder> listToInitPage(
      List<SaleAfterOrder> list, int pageNum, int pageSize) {
    List<SaleAfterOrder> pageList = new ArrayList<>();
    int curIdx = pageNum > 1 ? (pageNum - 1) * pageSize : 0;
    for (int i = 0; i < pageSize && curIdx + i < list.size(); i++) {
      pageList.add(list.get(curIdx + i));
    }
    IPage<SaleAfterOrder> page = new Page<>(pageNum, pageSize);
    page.setRecords(pageList);
    page.setTotal(list.size());
    return page;
  }

  @Override
  public IPage<SaleAfterOrder> saleAfterOrders(
      UserDto userData, SysUserAdminDto adminUserData, PageSelectParam<Integer> param) {
    Boolean isAdmin = false;
    if (adminUserData.getAdminAccount() != null) {
      isAdmin = true;
    }
    IPage<SaleAfterOrder> result =
        orderProductDao.saleAfterOrders(
            new Page(1, 9999999999L), param.getSelectParam(), isAdmin ? -1 : userData.getId());
    if (param.getSelectParam() != null) {
      result.setRecords(
          result.getRecords().stream()
              .filter(o -> o.getAfterState() == 5)
              .collect(Collectors.toList()));
    }
    List<Long> collect =
        result.getRecords().stream().map(SaleAfterOrder::getProId).collect(Collectors.toList());
    if (collect.size() > 0) {
      Map<Long, OrderProductRecord> productMap =
          orderProductRecordDao.selectBatchIds(collect).stream()
              .collect(Collectors.toMap(OrderProductRecord::getOrderProductId, o -> o));
      LambdaQueryWrapper<OrderProAfter> que = Wrappers.lambdaQuery(OrderProAfter.class);
      que.in(
          OrderProAfter::getOrderProductId,
          result.getRecords().stream()
              .map(SaleAfterOrder::getOrderId)
              .collect(Collectors.toList()));
      List<OrderProAfter> orderProAfters = orderProAfterDao.selectList(que);
      for (SaleAfterOrder record : result.getRecords()) {
        record.setStoreProduct(productMap.get(record.getOrderId()));
        List<OrderProAfter> orderAfters =
            orderProAfters.stream()
                .filter(
                    o ->
                        o.getOrderProductId().equals(record.getOrderId())
                            && Strings.isNotEmpty(o.getUserTransNum()))
                .collect(Collectors.toList());
        if (orderAfters.size() > 0) {
          record.setUserLog(orderAfters.get(0).getUserTransNum());
        }
      }
    }
    result = listToInitPage(result.getRecords(), param.getPageNum(), param.getPageSize());
    return result;
  }

  @Override
  public SysAddress getUserDefaultAddress(UserDto userData) {
    LambdaQueryWrapper<SysAddress> que = Wrappers.lambdaQuery();
    que.eq(SysAddress::getUId, userData.getId())
        .eq(SysAddress::getIsDefault, 1)
        .eq(BaseEntity::getState, 1);
    List<SysAddress> sysAddresses = sysAddressDao.selectList(que);
    if (sysAddresses.size() > 0) return sysAddresses.get(0);
    throw new RuntimeException("暂无默认收货地址");
  }

  @Override
  public JsonRootBean getTransInfo(String transNum) {
    try {
      String s = redisTemplate.opsForValue().get(transNum);
      if (s != null) {
        return JSONObject.parseObject(s, JsonRootBean.class);
      }
      AutoNumReq autoNumReq = new AutoNumReq();
      autoNumReq.setKey(ResourcesUtil.getProperty("100.key"));
      autoNumReq.setNum(transNum.trim());

      IBaseClient baseClient = new AutoNum();
      String result = baseClient.execute(autoNumReq).getBody();
      JSONArray jsonObject = JSONArray.parseObject(result, JSONArray.class);
      if (jsonObject.size() > 0) {
        QueryTrackReq queryTrackReq = new QueryTrackReq();
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(jsonObject.getJSONObject(0).getString("comCode"));
        queryTrackParam.setNum(transNum);
        String param = new Gson().toJson(queryTrackParam);

        queryTrackReq.setParam(param);
        queryTrackReq.setCustomer(ResourcesUtil.getProperty("100.customer"));
        queryTrackReq.setSign(
            SignUtils.querySign(
                param,
                ResourcesUtil.getProperty("100.key"),
                ResourcesUtil.getProperty("100.customer")));

        IBaseClient base = new QueryTrack();
        JsonRootBean res =
            JSONObject.parseObject(base.execute(queryTrackReq).getBody(), JsonRootBean.class);
        if (res != null) {
          RBlockingQueue<Object> blockingFairQueue =
              redissonClient.getBlockingQueue("delay_queue_call");
          RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
          delayedQueue.offer(transNum, 35, TimeUnit.MINUTES);
          //          redisTemplate
          //              .opsForValue()
          //              .set(transNum, JSONObject.toJSONString(res), 35, TimeUnit.MINUTES);
        }
        return res;
      }
      return null;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void noifitylianlian(LLNotifyParam param) {
    String outtradeno = param.getNo_order();
    String transactionId = param.getOid_paybill();

    log.info("连连实物订单异步通知参数：" + JSONObject.toJSONString(param));
    updateOrderByNotify(outtradeno, transactionId, 4);
  }

  private void updateOrderByNotify(String outtradeno, String transactionId, int payType) {
    LambdaQueryWrapper<OrderProduct> que = new LambdaQueryWrapper<>();
    que.eq(OrderProduct::getOrderFingerprint, outtradeno);
    List<OrderProduct> orderProducts = orderProductDao.selectList(que);

    if (orderProducts.size() > 0
        && (orderProducts.get(0).getOrderFlag().equals(0)
            || orderProducts.get(0).getOrderFlag().equals(-1))) {
      OrderProduct orderProduct = orderProducts.get(0);
      orderProduct.setOrderFlag(1);
      orderProduct.setTradeNo(transactionId);
      orderProduct.setUpdateTime(new Date());
      orderProduct.setPayTime(new Date());
      orderProductDao.updateById(orderProduct);
      addConBalanceRecord(orderProduct);
      StoreProduct storeProduct = storeProductDao.selectById(orderProduct.getProductId());
      // 冻结数量
      storeProduct.setFrostCount(storeProduct.getFrostCount() - orderProduct.getProCount());
      storeProduct.setUpdateTime(new Date());
      storeProductDao.updateById(storeProduct);
      ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
      conBalanceRecord.setUId(orderProduct.getCreateId());
      conBalanceRecord.setReType(2);
      conBalanceRecord.setOrderId(orderProduct.getId() + "");
      conBalanceRecord.setOrderType(3);
      conBalanceRecord.setTotalPrice(orderProduct.getTotalPrice());
      conBalanceRecord.setTradingChannel(payType);
      conBalanceRecord.setCreateId(orderProduct.getCreateId());
      conBalanceRecord.setCreateTime(new Date());
      conBalanceRecordDao.insert(conBalanceRecord);
      RBlockingQueue<Object> blockingFairQueue =
          redissonClient.getBlockingQueue("delay_queue_call");
      RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
      delayedQueue.offer("orderAutoConfirm_" + orderProduct.getId(), 15, TimeUnit.DAYS);
    } else {
      log.info("实物订单异步通知接收：outtradeno:" + outtradeno);
    }
  }

  @Override
  public OrderProductDto getOrderProductById(Long id) {
    Optional<OrderProductDto> optional = selectDataById(OrderProductDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Autowired private StoreProductDao storeProductDao;
  @Autowired private SysUserDao sysUserDao;
  @Autowired private SysAddressDao sysAddressDao;
  @Autowired private StoreProductTemplateDao storeProductTemplateDao;
  @Autowired private StoreProPoolService storeProPoolService;
  @Autowired private StoreTreasureDao storeTreasureDao;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Object save(OrderProductAddDto tmpDto, UserDto userData) {
    /** 准备参数 验证请求参数 验证订单，计算金额是否一致、 校验支付方式 生成口令 统一返回结果 */
    SysUser sysUser = sysUserDao.selectById(tmpDto.getUserId());
    StoreProduct storeProduct = storeProductDao.selectById(tmpDto.getProductId());
    SysAddress sysAddress = sysAddressDao.selectById(tmpDto.getSysAddressId());
    if (sysAddress == null || storeProduct == null || sysUser == null)
      throw new RuntimeException("请求参数已过期，请刷新后再试");
    if (storeProduct.getSurplusCount() >= tmpDto.getProCount()) {
      if (!sysAddress.getCreateId().equals(tmpDto.getUserId())) {
        throw new RuntimeException("收货地址异常,请刷新页面再试");
      }
      if (storeProduct.getProType() != 2) {
        if (storeProduct.getProType() == 1) {
          // 特权商品定是判否拥有资格购买
          LambdaQueryWrapper<StoreProductTemplate> q =
              Wrappers.lambdaQuery(StoreProductTemplate.class);
          q.eq(StoreProductTemplate::getProductId, storeProduct.getId())
              .eq(BaseEntity::getState, 1);
          List<StoreProductTemplate> needTreasures = storeProductTemplateDao.selectList(q);
          StoreProductTemplate storeProductTemplate = new StoreProductTemplate();
          storeProductTemplate.setNeedId(-11111L);
          needTreasures.add(storeProductTemplate);
          List<Long> storeTreasures =
              storeTreasureDao
                  .selectBatchIds(
                      needTreasures.stream()
                          .map(StoreProductTemplate::getNeedId)
                          .collect(Collectors.toSet()))
                  .stream()
                  .filter(o -> o.getState() == 1)
                  .map(StoreTreasure::getId)
                  .collect(Collectors.toList());
          needTreasures =
              needTreasures.stream()
                  .filter(o -> storeTreasures.contains(o.getNeedId()))
                  .collect(Collectors.toList());
          List<MyTreasure> myTreasure =
              storeProPoolService.getMytreasure(new MyTreasure(), userData);
          boolean flag = true;
          for (StoreProductTemplate needTreasure : needTreasures) {
            if (flag) {
              long count =
                  myTreasure.stream()
                      .filter(
                          o ->
                              o.getSTreasureId().equals(needTreasure.getNeedId())
                                  && o.getIsSaling() == 0)
                      .count();
              if (count < needTreasure.getNeedCount()) {
                flag = false;
              }
            }
          }
          if (!flag) {
            throw new RuntimeException("尚未持有所需藏品无法购买");
          }
        }
        // 普通商品
        // 校验请求参数
        BigDecimal recordTotal =
            storeProduct.getCurPrice().multiply(new BigDecimal(tmpDto.getProCount()));
        if (storeProduct.getVipPercent() != null
            && sysUser.getVipEndTime() != null
            && sysUser.getVipEndTime().getTime() > System.currentTimeMillis()) {
          recordTotal = recordTotal.multiply(storeProduct.getVipPercent());
        }
        if (storeProduct.getFeight() != null) {
          recordTotal = recordTotal.add(storeProduct.getFeight());
        }
        if (recordTotal.compareTo(tmpDto.getTotalPrice()) == 0) {
          // 创建订单
          OrderProduct orderProduct = addProductOrder(tmpDto, storeProduct.getProType());
          // 创建商品快照
          OrderProductRecord orderProductRecord = addOrderProductCopy(storeProduct, orderProduct);
          orderProduct.setProductId(orderProductRecord.getId());
          orderProductDao.updateById(orderProduct);
          // 冻结数量
          storeProduct.setFrostCount(storeProduct.getFrostCount() + orderProduct.getProCount());
          storeProduct.setSurplusCount(storeProduct.getSurplusCount() - orderProduct.getProCount());
          storeProductDao.updateById(storeProduct);

          switch (tmpDto.getPayType()) {
              // 0微信1支付宝2applePay3余额支付
            case 0:
              if (tmpDto.getFlagChnl() != null && "3".equals(tmpDto.getFlagChnl())) {
                return getWeixinPayH5Response(orderProduct, storeProduct);
              } else {
                return getWeixinPayResponse(orderProduct, storeProduct);
              }
            case 1:
              if (tmpDto.getFlagChnl() != null && "3".equals(tmpDto.getFlagChnl())) {
                return getAlipayTradeAppPayH5Response(orderProduct, storeProduct);
              } else {
                return getAlipayTradeAppPayResponse(orderProduct, storeProduct);
              }
            case 2:
              // TODO 苹果支付订单
              return null;
            case 3:
              //              if (sysUser.getBalance().compareTo(recordTotal) >= 0) {
              //                sysUser.setBalance(sysUser.getBalance().subtract(recordTotal));
              //                sysUserDao.updateById(sysUser);
              //                orderProduct.setOrderFlag(1);
              //                orderProduct.setPayTime(new Date());
              //                orderProductDao.updateById(orderProduct);
              //                // 添加流水
              //                addConBalanceRecord(orderProduct);
              //                RBlockingQueue<Object> blockingFairQueue =
              //                    redissonClient.getBlockingQueue("delay_queue_call");
              //                RDelayedQueue<Object> delayedQueue =
              //                    redissonClient.getDelayedQueue(blockingFairQueue);
              //                delayedQueue.offer("orderAutoConfirm_" + orderProduct.getId(), 15,
              // TimeUnit.DAYS);
              //                return "success";
              //              } else {
              //                throw new RuntimeException("余额不足请充值后再试");
              //              }
              throw new RuntimeException("支付方式错误");
            case 4:
              // 连连支付
              return lianlianPayOrder(tmpDto, orderProduct, storeProduct, sysUser);
            default:
              throw new RuntimeException("支付方式异常");
          }
        } else {
          throw new RuntimeException("页面已过期，请刷新后再试");
        }
      } else {
        // 兑换专区-积分商品
        if (tmpDto
                .getProPrice()
                .multiply(new BigDecimal(tmpDto.getProCount()))
                .add(tmpDto.getFreight() == null ? new BigDecimal(0) : tmpDto.getFreight())
                .compareTo(new BigDecimal(sysUser.getMetaCount()))
            <= 0) {
          tmpDto.setPayType(4);
          // 创建订单
          OrderProduct orderProduct = addProductOrder(tmpDto, 2);
          orderProduct.setPayTime(new Date());
          orderProductDao.updateById(orderProduct);

          // 创建商品快照
          OrderProductRecord orderProductRecord = addOrderProductCopy(storeProduct, orderProduct);
          orderProduct.setProductId(orderProductRecord.getId());
          orderProductDao.updateById(orderProduct);
          // 冻结数量
          storeProduct.setFrostCount(storeProduct.getFrostCount() + orderProduct.getProCount());
          storeProduct.setSurplusCount(storeProduct.getSurplusCount() - orderProduct.getProCount());
          storeProductDao.updateById(storeProduct);
          // 支付积分 修改订单状态，创建积分流水
          BigDecimal total = tmpDto.getProPrice().multiply(new BigDecimal(tmpDto.getProCount()));
          sysUser.setMetaCount(new BigDecimal(sysUser.getMetaCount()).subtract(total).longValue());
          sysUserDao.updateById(sysUser);
          orderProduct.setOrderFlag(1);
          orderProductDao.updateById(orderProduct);
          addIntegralRecord(tmpDto.getUserId(), orderProduct.getId(), total);
          return "success";
        } else {
          return "积分不足";
        }
      }

    } else {
      throw new RuntimeException("库存不足，订单创建失败");
    }
  }

  private Object lianlianPayOrder(
      OrderProductAddDto tmpDto,
      OrderProduct orderProduct,
      StoreProduct storeProduct,
      SysUser user) {
    SimpleDateFormat sp = new SimpleDateFormat("yyyyMMddHHmmss");
    InsertLianLianOrder.RiskItem riskItem = new InsertLianLianOrder.RiskItem();
    riskItem.setFrms_ware_category("1001"); // 虚拟卡销售 1001  5999
    riskItem.setUser_info_mercht_userno(orderProduct.getUId() + "");
    riskItem.setUser_info_bind_phone(user.getPhone());
    riskItem.setUser_info_dt_register(
        sp.format(user.getCreateTime() == null ? new Date() : user.getCreateTime()));
    riskItem.setGoods_name(storeProduct.getProTitle());
    riskItem.setFrms_client_chnl(
        tmpDto.getFlagChnl().equals("0") || tmpDto.getFlagChnl().equals("1") ? "10" : "16");
    riskItem.setFrms_ip_addr(PayUtils.getIp());
    riskItem.setUser_auth_flag("1");
    riskItem.setRepay_auth_flag("1");

    String url = "https://payserverapi.lianlianpay.com/v1/paycreatebill";

    InsertLianLianOrder order = new InsertLianLianOrder();
    order.setTime_stamp(sp.format(new Date()));
    order.setOid_partner(ResourcesUtil.getProperty("lianlian_oid"));
    //        order.setOid_partner("202207080003331008");
    order.setUser_id(orderProduct.getUId() + "");
    order.setBusi_partner("109001");
    order.setNo_order(orderProduct.getOrderFingerprint());

    order.setDt_order(sp.format(new Date()));
    order.setMoney_order(orderProduct.getTotalPrice().doubleValue() + "");
    order.setNotify_url(ResourcesUtil.getProperty("lianlianpay_order_product_notify"));
    order.setRisk_item(JSONObject.toJSONString(riskItem));
    //    order.setFlag_pay_product(Strings.isEmpty(tmpDto.getNoAgree()) ? "5" : "0");
    order.setFlag_pay_product("0");
    order.setFlag_chnl(tmpDto.getFlagChnl());
    order.setId_no(tmpDto.getIdNo());
    order.setAcct_name(tmpDto.getAcctName());
    order.setCard_no(tmpDto.getCardNo());
    order.setNo_agree(tmpDto.getNoAgree());
    order.setSign(genSign(JSON.parseObject(JSON.toJSONString(order))));
    String reqJson = JSON.toJSONString(order);
    log.info("连连pay请求报文为:" + reqJson);
    String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, url);
    log.info("连连pay结果报文为:" + resJson);
    return JSONObject.parseObject(resJson);
  }

  @Override
  public String genSign(JSONObject reqObj) {
    // // 生成待签名串
    String sign_src = genSignData(reqObj);
    return getSignRSA(sign_src);
  }
  /**
   * RSA签名验证
   *
   * @return
   */
  public static String getSignRSA(String sign_src) {
    return TraderRSAUtil.sign(ResourcesUtil.getProperty("lianlian_private_key"), sign_src);
  }
  /**
   * 生成待签名串
   *
   * @return
   */
  public static String genSignData(JSONObject jsonObject) {
    StringBuilder content = new StringBuilder();

    // 按照key做首字母升序排列
    List<String> keys = new ArrayList<String>(jsonObject.keySet());
    keys.sort(String.CASE_INSENSITIVE_ORDER);
    for (int i = 0; i < keys.size(); i++) {
      String key = (String) keys.get(i);
      // sign 和ip_client 不参与签名
      if ("sign".equals(key)) {
        continue;
      }
      String value = (String) jsonObject.getString(key);
      // 空串不参与签名
      if (null == value) {
        continue;
      }
      content.append(i == 0 ? "" : "&").append(key).append("=").append(value);
    }
    String signSrc = content.toString();
    if (signSrc.startsWith("&")) {
      signSrc = signSrc.replaceFirst("&", "");
    }
    return signSrc;
  }

  private Object aliOrderBack(OrderProduct orderProduct) {
    try {
      String appId = ResourcesUtil.ALI_APP_ID;
      // 公钥
      // MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlN0HXsbwxiRsnZfbefFiNpEeLcE5b4nSiDJ7A2Rt0pS2tvJsHZ4UnlUim/6UzxLKAzxn6VpLyKNc3INUctr1ZPtMbLHnDt1vBmXRqeBjear+Vnw6G2Xr4ixoWqRzZAmvlOuOkUJ+G60qU//Kz9YoIm3MnvA3becF8nnLylfkg+pM05+yZkK+3sjXzsS9dGfs3Zs2+jE9ePyRNtYviYXWu9RuOSSGDY06HKVNxHGQUi7TqUZ+W6xRhP0VAfnK5pMBA2CAwywsCv/GRjg6naOUo7wzohqhxBX3WsasqdUBfRkWg2ozkQa77ucPZAWPDRngHXbL2PdbiPSx3SUpL+nZxQIDAQAB
      String aliPrivateKey =
          "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCU3QdexvDGJGydl9t58WI2kR4twTlvidKIMnsDZG3SlLa28mwdnhSeVSKb/pTPEsoDPGfpWkvIo1zcg1Ry2vVk+0xssecO3W8GZdGp4GN5qv5WfDobZeviLGhapHNkCa+U646RQn4brSpT/8rP1igibcye8Ddt5wXyecvKV+SD6kzTn7JmQr7eyNfOxL10Z+zdmzb6MT14/JE21i+Jhda71G45JIYNjTocpU3EcZBSLtOpRn5brFGE/RUB+crmkwEDYIDDLCwK/8ZGODqdo5SjvDOiGqHEFfdaxqyp1QF9GRaDajORBrvu5w9kBY8NGeAddsvY91uI9LHdJSkv6dnFAgMBAAECggEACyOe8ZChY7JGDmTWn4FYgAzL3VCgI6CEiHx+h/pz3VYTdg0d2fmCQXbNaC7co8IcK7HRdLy0/wZ6ZGXPY+jOhAfp6BhH2ezn6eqkjbmkt+37qi0RjAtMY1g/VskHeWzgHpyhxmzbUubaS/7QBk1YI3tj3GDNRQQMheBnR3TcPKKlufR7ri8umRQeow5v41YD1R/AYDU3zX0UxEtsXUNWz6PmqaULlLBB31np5TNTfgshBREgvy/0DVsHtIHcWxNkrwkMapAHaEuR9q9rDsIDpVfRtJxv/aGxIqhYVkDz94MjDsPNUrw7tbcz74R058giXTTMsx8ZPUSAL1UKb5VT4QKBgQD0J8zIacWdL1exDUNKv94xiUAqxB2j0RdN9eUxdO8cSfb9AJxHypNscB2pPsDc0u29R53Za4GkoSihhZ+JSiU8QrZW17nB3JiGoyO4C6NDyQ2g2Jb0am5zEMH/5gp9svyInPVmqd6EJTujXUiAsG2RcY8A+QA0Oh3ozPF68sIhjwKBgQCcFciY1D999yFmscn1vquaLH6s/xOZg7m2ccr0cHrz66r/wbwsNqodCrbEuZ61GJeVU8PnUrAk4wrlGiFqR6pV9PxBHioQYFywsZ/nT8C2XnfqQXe+XVM45ArIZphI4YuBsKSc9AWjA5EGnKSadeJ8VKq4gPfC2gYb+B/10+19awKBgQCYKpnxqiJUVaY0nYx78Nq9SsooHTRP3cfFeeRPD47atapugkvkXnfFFJcX3Rl8RyWOWy0gzWTuQta83DfS69gLF5TmyOpnzWFuQAzJ7s7hN1P8FCD40cBmjGIsZ6XQM5Y6WoCDbIlXGJFzvnaqZcrT4895jra21iW/6sLxmoytNwKBgFsNXhrBXlSGUObOeikwVHy4ziDvICjirfifQyz7XM5kQTm3c6U7MluEv3/dZJbyRKMo3VRZaUXraJSjfLC6I8THCEyYYyNwg0HULJrMbHg2fa+bB1Z2rLC4Xw3uw5FoeXBrcmvY8lzZHMYFpQQyFrA+1+SP33i8pOm2AjburVEHAoGBAKOKnAxaCLiqeDFYeuK4SzjdMbtXDIcHaSQ/hH2P0tnquJquBMIDqVCOEn0o7Oe0pFZ6wD8W2aHPx6fywbaJD7Vz2HBk0cdEHVmSrD5vlSAE2qeDzHNRd397e4GRyXaCwPtZZaGd9dpNB0t9K8+PYi0EYz2Dt/as2ynycXvvcKHh";
      // 构造client
      CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
      // 设置网关地址
      certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
      // 设置应用Id
      certAlipayRequest.setAppId(appId);
      // 设置应用私钥
      certAlipayRequest.setPrivateKey(aliPrivateKey);
      // 设置请求格式，固定值json
      certAlipayRequest.setFormat("json");
      // 设置字符集
      certAlipayRequest.setCharset("utf-8");
      // 设置签名类型
      certAlipayRequest.setSignType("RSA2");
      // 设置应用公钥证书路径
      certAlipayRequest.setCertPath("/usr/local/wzd/appCertPublicKey_2021003131656770.crt");
      // 设置支付宝公钥证书路径
      certAlipayRequest.setAlipayPublicCertPath("/usr/local/wzd/alipayCertPublicKey_RSA2.crt");
      // 设置支付宝根证书路径
      certAlipayRequest.setRootCertPath("/usr/local/wzd/alipayRootCert.crt");
      // 构造client
      AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
      AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
      JSONObject bizContent = new JSONObject();
      bizContent.put("out_trade_no", orderProduct.getOrderFingerprint());
      bizContent.put("refund_amount", orderProduct.getTotalPrice());

      request.setBizContent(bizContent.toString());
      AlipayTradeRefundResponse response = alipayClient.certificateExecute(request);
      log.info("ali订单退款 返回参数：" + JSONObject.toJSONString(response));
      return response.getBody();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private Object getAlipayTradeAppPayResponse(
      OrderProduct orderProduct, StoreProduct storeProduct) {
    try {
      String appId = ResourcesUtil.ALI_APP_ID;
      // 公钥
      // MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlN0HXsbwxiRsnZfbefFiNpEeLcE5b4nSiDJ7A2Rt0pS2tvJsHZ4UnlUim/6UzxLKAzxn6VpLyKNc3INUctr1ZPtMbLHnDt1vBmXRqeBjear+Vnw6G2Xr4ixoWqRzZAmvlOuOkUJ+G60qU//Kz9YoIm3MnvA3becF8nnLylfkg+pM05+yZkK+3sjXzsS9dGfs3Zs2+jE9ePyRNtYviYXWu9RuOSSGDY06HKVNxHGQUi7TqUZ+W6xRhP0VAfnK5pMBA2CAwywsCv/GRjg6naOUo7wzohqhxBX3WsasqdUBfRkWg2ozkQa77ucPZAWPDRngHXbL2PdbiPSx3SUpL+nZxQIDAQAB
      String aliPrivateKey =
          "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCU3QdexvDGJGydl9t58WI2kR4twTlvidKIMnsDZG3SlLa28mwdnhSeVSKb/pTPEsoDPGfpWkvIo1zcg1Ry2vVk+0xssecO3W8GZdGp4GN5qv5WfDobZeviLGhapHNkCa+U646RQn4brSpT/8rP1igibcye8Ddt5wXyecvKV+SD6kzTn7JmQr7eyNfOxL10Z+zdmzb6MT14/JE21i+Jhda71G45JIYNjTocpU3EcZBSLtOpRn5brFGE/RUB+crmkwEDYIDDLCwK/8ZGODqdo5SjvDOiGqHEFfdaxqyp1QF9GRaDajORBrvu5w9kBY8NGeAddsvY91uI9LHdJSkv6dnFAgMBAAECggEACyOe8ZChY7JGDmTWn4FYgAzL3VCgI6CEiHx+h/pz3VYTdg0d2fmCQXbNaC7co8IcK7HRdLy0/wZ6ZGXPY+jOhAfp6BhH2ezn6eqkjbmkt+37qi0RjAtMY1g/VskHeWzgHpyhxmzbUubaS/7QBk1YI3tj3GDNRQQMheBnR3TcPKKlufR7ri8umRQeow5v41YD1R/AYDU3zX0UxEtsXUNWz6PmqaULlLBB31np5TNTfgshBREgvy/0DVsHtIHcWxNkrwkMapAHaEuR9q9rDsIDpVfRtJxv/aGxIqhYVkDz94MjDsPNUrw7tbcz74R058giXTTMsx8ZPUSAL1UKb5VT4QKBgQD0J8zIacWdL1exDUNKv94xiUAqxB2j0RdN9eUxdO8cSfb9AJxHypNscB2pPsDc0u29R53Za4GkoSihhZ+JSiU8QrZW17nB3JiGoyO4C6NDyQ2g2Jb0am5zEMH/5gp9svyInPVmqd6EJTujXUiAsG2RcY8A+QA0Oh3ozPF68sIhjwKBgQCcFciY1D999yFmscn1vquaLH6s/xOZg7m2ccr0cHrz66r/wbwsNqodCrbEuZ61GJeVU8PnUrAk4wrlGiFqR6pV9PxBHioQYFywsZ/nT8C2XnfqQXe+XVM45ArIZphI4YuBsKSc9AWjA5EGnKSadeJ8VKq4gPfC2gYb+B/10+19awKBgQCYKpnxqiJUVaY0nYx78Nq9SsooHTRP3cfFeeRPD47atapugkvkXnfFFJcX3Rl8RyWOWy0gzWTuQta83DfS69gLF5TmyOpnzWFuQAzJ7s7hN1P8FCD40cBmjGIsZ6XQM5Y6WoCDbIlXGJFzvnaqZcrT4895jra21iW/6sLxmoytNwKBgFsNXhrBXlSGUObOeikwVHy4ziDvICjirfifQyz7XM5kQTm3c6U7MluEv3/dZJbyRKMo3VRZaUXraJSjfLC6I8THCEyYYyNwg0HULJrMbHg2fa+bB1Z2rLC4Xw3uw5FoeXBrcmvY8lzZHMYFpQQyFrA+1+SP33i8pOm2AjburVEHAoGBAKOKnAxaCLiqeDFYeuK4SzjdMbtXDIcHaSQ/hH2P0tnquJquBMIDqVCOEn0o7Oe0pFZ6wD8W2aHPx6fywbaJD7Vz2HBk0cdEHVmSrD5vlSAE2qeDzHNRd397e4GRyXaCwPtZZaGd9dpNB0t9K8+PYi0EYz2Dt/as2ynycXvvcKHh";
      // 构造client
      CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
      // 设置网关地址
      certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
      // 设置应用Id
      certAlipayRequest.setAppId(appId);
      // 设置应用私钥
      certAlipayRequest.setPrivateKey(aliPrivateKey);
      // 设置请求格式，固定值json
      certAlipayRequest.setFormat("json");
      // 设置字符集
      certAlipayRequest.setCharset("utf-8");
      // 设置签名类型
      certAlipayRequest.setSignType("RSA2");
      // 设置应用公钥证书路径
      certAlipayRequest.setCertPath("/usr/local/wzd/appCertPublicKey_2021003131656770.crt");
      // 设置支付宝公钥证书路径
      certAlipayRequest.setAlipayPublicCertPath("/usr/local/wzd/alipayCertPublicKey_RSA2.crt");
      // 设置支付宝根证书路径
      certAlipayRequest.setRootCertPath("/usr/local/wzd/alipayRootCert.crt");
      // 构造client
      AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
      // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
      AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
      // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
      AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
      model.setBody(orderProduct.getId() + "");
      model.setSubject(storeProduct.getProTitle());
      model.setOutTradeNo(orderProduct.getOrderFingerprint());
      model.setTimeoutExpress("30m");
      model.setTotalAmount(orderProduct.getTotalPrice() + "");
      model.setProductCode("QUICK_MSECURITY_PAY");
      request.setBizModel(model);
      request.setNotifyUrl(ResourcesUtil.ALIPAY_ORDER_PRODUCT_NOTIFY_URL);
      try {
        // 这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        System.out.println(response.getBody()); // 就是orderString 可以直接给客户端请求，无需再做处理。
        return response.getBody();
      } catch (AlipayApiException e) {
        e.printStackTrace();
      }
      return null;
    } catch (AlipayApiException e) {
      throw new RuntimeException(e);
    }
  }

  private Object getAlipayTradeAppPayH5Response(
      OrderProduct orderProduct, StoreProduct storeProduct) {
    try {

      String appId = ResourcesUtil.ALI_APP_ID;
      AlipayConfig alipayConfig = new AlipayConfig();
      alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
      alipayConfig.setAppId(appId);
      alipayConfig.setPrivateKey(ResourcesUtil.getProperty("ali_75private_key"));
      alipayConfig.setAppCertPath("/nginx/cert/appCertPublicKey_2021003146630241.crt");
      alipayConfig.setAlipayPublicCertPath("/nginx/cert/alipayCertPublicKey_RSA2.crt");
      alipayConfig.setRootCertPath("/nginx/cert/alipayRootCert.crt");
      alipayConfig.setFormat("json");
      alipayConfig.setCharset("UTF-8");
      alipayConfig.setSignType("RSA2");
      DefaultAlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
      AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
      request.setNotifyUrl(ResourcesUtil.ALIPAY_ORDER_PRODUCT_NOTIFY_URL);
      //      request.setReturnUrl("");
      JSONObject bizContent = new JSONObject();
      bizContent.put("out_trade_no", orderProduct.getOrderFingerprint());
      bizContent.put("total_amount", orderProduct.getTotalPrice());
      bizContent.put("subject", storeProduct.getProTitle());
      bizContent.put("product_code", "QUICK_WAP_WAY");
      bizContent.put("return_url", "http://web.xskymeta.com/#/pages/pay/jieguo/jieguo");
      request.setBizContent(bizContent.toString());
      AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
      if (response.isSuccess()) {
        System.out.println("调用成功");
        return response.getBody();
      } else {
        System.out.println("调用失败");
      }
      //      String appId = ResourcesUtil.ALI_APP_ID;
      //      // 公钥
      //      //
      // MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlN0HXsbwxiRsnZfbefFiNpEeLcE5b4nSiDJ7A2Rt0pS2tvJsHZ4UnlUim/6UzxLKAzxn6VpLyKNc3INUctr1ZPtMbLHnDt1vBmXRqeBjear+Vnw6G2Xr4ixoWqRzZAmvlOuOkUJ+G60qU//Kz9YoIm3MnvA3becF8nnLylfkg+pM05+yZkK+3sjXzsS9dGfs3Zs2+jE9ePyRNtYviYXWu9RuOSSGDY06HKVNxHGQUi7TqUZ+W6xRhP0VAfnK5pMBA2CAwywsCv/GRjg6naOUo7wzohqhxBX3WsasqdUBfRkWg2ozkQa77ucPZAWPDRngHXbL2PdbiPSx3SUpL+nZxQIDAQAB
      //      String aliPrivateKey =
      //
      // "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCU3QdexvDGJGydl9t58WI2kR4twTlvidKIMnsDZG3SlLa28mwdnhSeVSKb/pTPEsoDPGfpWkvIo1zcg1Ry2vVk+0xssecO3W8GZdGp4GN5qv5WfDobZeviLGhapHNkCa+U646RQn4brSpT/8rP1igibcye8Ddt5wXyecvKV+SD6kzTn7JmQr7eyNfOxL10Z+zdmzb6MT14/JE21i+Jhda71G45JIYNjTocpU3EcZBSLtOpRn5brFGE/RUB+crmkwEDYIDDLCwK/8ZGODqdo5SjvDOiGqHEFfdaxqyp1QF9GRaDajORBrvu5w9kBY8NGeAddsvY91uI9LHdJSkv6dnFAgMBAAECggEACyOe8ZChY7JGDmTWn4FYgAzL3VCgI6CEiHx+h/pz3VYTdg0d2fmCQXbNaC7co8IcK7HRdLy0/wZ6ZGXPY+jOhAfp6BhH2ezn6eqkjbmkt+37qi0RjAtMY1g/VskHeWzgHpyhxmzbUubaS/7QBk1YI3tj3GDNRQQMheBnR3TcPKKlufR7ri8umRQeow5v41YD1R/AYDU3zX0UxEtsXUNWz6PmqaULlLBB31np5TNTfgshBREgvy/0DVsHtIHcWxNkrwkMapAHaEuR9q9rDsIDpVfRtJxv/aGxIqhYVkDz94MjDsPNUrw7tbcz74R058giXTTMsx8ZPUSAL1UKb5VT4QKBgQD0J8zIacWdL1exDUNKv94xiUAqxB2j0RdN9eUxdO8cSfb9AJxHypNscB2pPsDc0u29R53Za4GkoSihhZ+JSiU8QrZW17nB3JiGoyO4C6NDyQ2g2Jb0am5zEMH/5gp9svyInPVmqd6EJTujXUiAsG2RcY8A+QA0Oh3ozPF68sIhjwKBgQCcFciY1D999yFmscn1vquaLH6s/xOZg7m2ccr0cHrz66r/wbwsNqodCrbEuZ61GJeVU8PnUrAk4wrlGiFqR6pV9PxBHioQYFywsZ/nT8C2XnfqQXe+XVM45ArIZphI4YuBsKSc9AWjA5EGnKSadeJ8VKq4gPfC2gYb+B/10+19awKBgQCYKpnxqiJUVaY0nYx78Nq9SsooHTRP3cfFeeRPD47atapugkvkXnfFFJcX3Rl8RyWOWy0gzWTuQta83DfS69gLF5TmyOpnzWFuQAzJ7s7hN1P8FCD40cBmjGIsZ6XQM5Y6WoCDbIlXGJFzvnaqZcrT4895jra21iW/6sLxmoytNwKBgFsNXhrBXlSGUObOeikwVHy4ziDvICjirfifQyz7XM5kQTm3c6U7MluEv3/dZJbyRKMo3VRZaUXraJSjfLC6I8THCEyYYyNwg0HULJrMbHg2fa+bB1Z2rLC4Xw3uw5FoeXBrcmvY8lzZHMYFpQQyFrA+1+SP33i8pOm2AjburVEHAoGBAKOKnAxaCLiqeDFYeuK4SzjdMbtXDIcHaSQ/hH2P0tnquJquBMIDqVCOEn0o7Oe0pFZ6wD8W2aHPx6fywbaJD7Vz2HBk0cdEHVmSrD5vlSAE2qeDzHNRd397e4GRyXaCwPtZZaGd9dpNB0t9K8+PYi0EYz2Dt/as2ynycXvvcKHh";
      //      CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
      //      certAlipayRequest.setServerUrl("https://alipay.trade.wap.pay");
      //      certAlipayRequest.setAppId(appId);
      //      certAlipayRequest.setPrivateKey(aliPrivateKey);
      //      certAlipayRequest.setFormat("json");
      //      certAlipayRequest.setCharset("utf-8");
      //      certAlipayRequest.setSignType("RSA2");
      //      certAlipayRequest.setCertPath(ResourcesUtil.getProperty("ali_CertPath"));
      //      certAlipayRequest.setAlipayPublicCertPath(
      //          ResourcesUtil.getProperty("ali_AlipayPublicCertPath"));
      //      certAlipayRequest.setRootCertPath(ResourcesUtil.getProperty("ali_RootCertPath"));
      //      // 构造client
      //      AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
      //      // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
      //      AlipayTradePayRequest request = new AlipayTradePayRequest();
      //      //
      // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
      //      AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
      //      model.setBody(orderProduct.getId() + "");
      //      model.setSubject(storeProduct.getProTitle());
      //      model.setOutTradeNo(orderProduct.getOrderFingerprint());
      //      model.setTimeoutExpress("30m");
      //      model.setTotalAmount(orderProduct.getTotalPrice() + "");
      //      model.setProductCode("QUICK_WAP_WAY");
      //      request.setBizModel(model);
      //      request.setNotifyUrl(ResourcesUtil.ALIPAY_ORDER_PRODUCT_NOTIFY_URL);
      //      try {
      //        // 这里和普通的接口调用不同，使用的是sdkExecute
      //        AlipayTradePayResponse response = alipayClient.pageExecute(request);
      //        //        AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
      //        System.out.println(
      //            JSONObject.toJSONString(response.getBody())); // 就是orderString
      // 可以直接给客户端请求，无需再做处理。
      //        return response.getBody();
      //      } catch (AlipayApiException e) {
      //        e.printStackTrace();
      //      }
      return null;
    } catch (AlipayApiException e) {
      throw new RuntimeException(e);
    }
  }

  private Object getWeixinPayResponse(OrderProduct orderProduct, StoreProduct storeProduct) {
    // 微信支付
    HashMap<String, String> payParam = new HashMap<>(16);
    payParam.put("body", storeProduct.getProTitle());
    payParam.put("out_trade_no", orderProduct.getOrderFingerprint());
    payParam.put(
        "total_fee", orderProduct.getTotalPrice().multiply(new BigDecimal(100)).longValue() + "");
    payParam.put("spbill_create_ip", PayUtils.getIp());
    payParam.put("notify_url", ResourcesUtil.WEIXIN_ORDER_PRODUCT_NOTIFY_URL);
    payParam.put("trade_type", "APP");
    // 附加数据包，异步通知中使用
    //    payParam.put("attach", JSONObject.toJSONString(orderProduct));

    Map<String, String> r;
    try {
      WXPayConfig config = WXPayConfigImpl.getInstance();
      WXPay wxPay = new WXPay(config);
      r = wxPay.unifiedOrder(payParam);
      Map<String, String> appResult = new HashMap<>(16);
      appResult.put("appid", ResourcesUtil.WX_APP_ID);
      appResult.put("partnerid", ResourcesUtil.WX_PARTNER_ID);
      appResult.put("prepayid", r.get("prepay_id"));
      appResult.put("package", "Sign=WXPay");
      appResult.put("noncestr", WXPayUtil.generateNonceStr());
      appResult.put("timestamp", System.currentTimeMillis() / 1000 + "");
      String sign = WXPayUtil.generateSignature(appResult, ResourcesUtil.WX_API_KEY);
      appResult.put("sign", sign);
      return appResult;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private Object getWeixinPayH5Response(OrderProduct orderProduct, StoreProduct storeProduct) {
    Map<String, String> r;
    try {
      // 微信支付
      HashMap<String, String> payParam = new HashMap<>(16);
      payParam.put("appid", ResourcesUtil.WX_APP_ID);
      payParam.put("mch_id", ResourcesUtil.WX_PARTNER_ID);
      payParam.put("nonce_str", WXPayUtil.generateNonceStr());
      payParam.put("out_trade_no", orderProduct.getOrderFingerprint());
      payParam.put(
          "total_fee", orderProduct.getTotalPrice().multiply(new BigDecimal(100)).longValue() + "");
      payParam.put("spbill_create_ip", PayUtils.getIp());
      payParam.put("notify_url", ResourcesUtil.WEIXIN_ORDER_PRODUCT_NOTIFY_URL);
      payParam.put("trade_type", "MWEB");
      payParam.put(
          "scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://xskymeta.com\"}}");
      payParam.put("body", storeProduct.getProTitle());
      String sign = WXPayUtil.generateSignature(payParam, ResourcesUtil.WX_API_KEY);
      payParam.put("sign", sign);
      // 附加数据包，异步通知中使用
      //    payParam.put("attach", JSONObject.toJSONString(orderProduct));

      WXPayConfig config = WXPayConfigImpl.getInstance();
      WXPay wxPay = new WXPay(config);
      r = wxPay.unifiedOrder(payParam);
      System.out.println("微信支付返回值：" + JSONObject.toJSONString(r));
      return r;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void addConBalanceRecord(OrderProduct orderProduct) {
    ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
    conBalanceRecord.setUId(orderProduct.getUId());
    conBalanceRecord.setReType(2);
    conBalanceRecord.setOrderId(orderProduct.getId() + "");
    conBalanceRecord.setOrderType(3);
    conBalanceRecord.setTotalPrice(orderProduct.getTotalPrice());
    conBalanceRecord.setTradingChannel(orderProduct.getPayType());
    conBalanceRecord.setCreateId(orderProduct.getUId());
    conBalanceRecord.setCreateTime(new Date());
    conBalanceRecordDao.insert(conBalanceRecord);
  }

  private void addIntegralRecord(Long userId, Long id, BigDecimal total) {
    ConIntegralRecord conIntegralRecord = new ConIntegralRecord();
    conIntegralRecord.setUId(userId);
    conIntegralRecord.setRecordType(2);
    conIntegralRecord.setMetaCount(total.intValue());
    conIntegralRecord.setOrderId(id);
    conIntegralRecord.setCreateId(userId);
    conIntegralRecord.setCreateTime(new Date());
    conIntegralRecord.setUpdateTime(new Date());
    conIntegralRecordDao.insert(conIntegralRecord);
  }

  @Autowired private ConBalanceRecordDao conBalanceRecordDao;
  @Autowired private OrderProductRecordDao orderProductRecordDao;
  @Autowired private ConIntegralRecordDao conIntegralRecordDao;

  private OrderProductRecord addOrderProductCopy(
      StoreProduct storeProduct, OrderProduct orderProduct) {
    OrderProductRecord orderProductRecord = new OrderProductRecord();
    orderProductRecord.setOrderProductId(orderProduct.getId());
    orderProductRecord.setStoreProductId(storeProduct.getId());
    orderProductRecord.setProType(storeProduct.getProType());
    orderProductRecord.setProTitle(storeProduct.getProTitle());
    orderProductRecord.setOldPrice(storeProduct.getOldPrice());
    orderProductRecord.setCurPrice(storeProduct.getCurPrice());
    orderProductRecord.setProDetail(storeProduct.getProDetail());
    orderProductRecord.setHeaderIndex(storeProduct.getHeaderIndex());
    orderProductRecord.setBanners(storeProduct.getBanners());
    orderProductRecord.setTotalCount(storeProduct.getTotalCount());
    orderProductRecord.setSurplusCount(storeProduct.getSurplusCount());
    orderProductRecord.setFrostCount(storeProduct.getFrostCount());
    orderProductRecord.setCreateId(storeProduct.getCreateId());
    orderProductRecord.setCreateTime(storeProduct.getCreateTime());
    orderProductRecord.setUpdateId(storeProduct.getUpdateId());
    orderProductRecord.setUpdateTime(storeProduct.getUpdateTime());
    orderProductRecord.setState(storeProduct.getState());
    orderProductRecordDao.insert(orderProductRecord);
    return orderProductRecord;
  }

  private OrderProduct addProductOrder(OrderProductAddDto tmpDto, Integer productType) {
    OrderProduct orderProduct = new OrderProduct();
    orderProduct.setOrderFingerprint(UUID.randomUUID().toString().replaceAll("-", ""));
    orderProduct.setUId(tmpDto.getUserId());
    orderProduct.setProductType(productType);
    orderProduct.setProductId(tmpDto.getProductId());
    orderProduct.setTotalPrice(tmpDto.getTotalPrice());
    orderProduct.setProCount(tmpDto.getProCount());
    orderProduct.setProPrice(tmpDto.getProPrice());
    orderProduct.setSysAddressId(tmpDto.getSysAddressId());
    orderProduct.setFreight(tmpDto.getFreight());
    orderProduct.setPayType(tmpDto.getPayType());
    orderProduct.setPayEndTime(new Date(System.currentTimeMillis() + 15L * 60000));
    orderProduct.setOrderRemark(tmpDto.getOrderRemark());
    orderProduct.setCreateId(tmpDto.getUserId());
    orderProduct.setCreateTime(new Date());
    orderProductDao.insert(orderProduct);
    RBlockingQueue<Object> blockingFairQueue = redissonClient.getBlockingQueue("delay_queue_call");
    RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
    delayedQueue.offer("orderProduct_" + orderProduct.getId(), 15, TimeUnit.MINUTES);
    //    redisTemplate
    //        .opsForValue()
    //        .set(
    //            "orderProduct_" + orderProduct.getId(),
    //            orderProduct.getOrderFingerprint(),
    //            15,
    //            TimeUnit.MINUTES);
    return orderProduct;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(OrderProductUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderProductDto dto = new OrderProductDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(OrderProductDto dto) {
    OrderProductDto orderProduct = getOrderProductById(dto.getId());
    if (orderProduct.getState() == 1) {
      orderProduct.setState(2);
    } else {
      orderProduct.setState(1);
    }
    orderProduct.setUpdateTime(new Date());
    orderProduct.setUpdateId(dto.getUpdateId());
    updateDataById(orderProduct.getId(), orderProduct);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<OrderProductDto> list(OrderProductDto dto) {
    LambdaQueryWrapper<OrderProduct> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(OrderProductDto.class, queryWrapper);
  }
}
