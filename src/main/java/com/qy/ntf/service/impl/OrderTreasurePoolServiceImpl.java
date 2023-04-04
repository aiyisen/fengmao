package com.qy.ntf.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.lianpay.api.util.TraderRSAUtil;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.customResult.*;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.*;
import com.qy.ntf.bean.param.*;
import com.qy.ntf.dao.*;
import com.qy.ntf.service.OrderTreasurePoolService;
import com.qy.ntf.service.StoreProPoolService;
import com.qy.ntf.util.*;
import com.qy.ntf.util.llPay.CashierPayCreateDemo;
import com.qy.ntf.util.llPay.YTHttpHandler;
import com.qy.ntf.util.sd.SdNotifyBody;
import com.qy.ntf.util.wxPay.WXNotifyParam;
import com.qy.ntf.util.wxPay.WXPayConfigImpl;
import com.qy.ntf.util.wxPay.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池藏品订单 service服务实现
 */
@Slf4j
@Service("orderTreasurePoolService")
public class OrderTreasurePoolServiceImpl implements OrderTreasurePoolService {

  @Autowired private OrderTreasurePoolDao orderTreasurePoolDao;
  @Autowired private RedisTemplate<String, String> redisTemplate;

  @Override
  public BaseMapper<OrderTreasurePool> getDao() {
    return orderTreasurePoolDao;
  }

  @Override
  public IPage<OrderTreasurePoolDto> getListByPage(
      Class<OrderTreasurePoolDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderTreasurePool> queryWrapper) {
    IPage<OrderTreasurePoolDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public void noifityWXin(WXNotifyParam wxp, boolean b) {
    log.info("noifityWXin-in:{}", wxp);
    try {
      log.debug("noifityWXin-flag:{}", b);
      if (b) {
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
    OrderTreasurePool orderTreasurePool = orderTreasurePoolDao.selectById(orderID);
    if (orderTreasurePool != null && orderTreasurePool.getOrderFlag() == 0) {
      orderTreasurePool.setOrderFlag(-1);
      orderTreasurePool.setUpdateTime(new Date());
      orderTreasurePoolDao.updateById(orderTreasurePool);
      //      if (orderTreasurePool.getItemType() == 2 || orderTreasurePool.getItemType() == 1) {
      //        StoreProPool storeProPool =
      // storeProPoolDao.selectById(orderTreasurePool.getTeaPoId());
      //        if (storeProPool.getIsSale() != null && storeProPool.getIsSale() == 1) {
      //          storeProPool.setIsSale(0);
      //          storeProPoolDao.updateById(storeProPool);
      //        }
      //      } else {
      //      OrderTreasureRecord orderTreasureRecord =
      //          orderTreasureRecordDao.selectById(orderTreasurePool.getTeaPoId());
      //      if (orderTreasureRecord != null) {
      StoreTreasure storeTreasure = storeTreasureDao.selectById(orderTreasurePool.getTeaPoId());
      if (storeTreasure.getFromUser() != null && storeTreasure.getFromUser() == 1) {
        storeTreasure.setUpdateTime(new Date());
        storeTreasure.setIsSale(0);
        storeTreasure.setState(1);
        storeTreasureDao.updateById(storeTreasure);
      } else {
        storeTreasure.setSurplusCount(storeTreasure.getSurplusCount() + 1);
        storeTreasure.setFrostCount(
            storeTreasure.getFrostCount() > 1 ? storeTreasure.getFrostCount() - 1 : 0);
        storeTreasureDao.updateById(storeTreasure);
      }
      //      }
      //      }
    }
  }

  @Autowired private StoreProPoolService storeProPoolService;

  @Override
  public IPage<OrderTreasurePoolDto> listByPage(
      Integer pageNum, long pageSize, Long userId, Integer orderFlag, Integer itemType) {
    IPage<OrderTreasurePoolDto> result =
        orderTreasurePoolDao.listByPage(new Page<>(pageNum, pageSize), userId, orderFlag, itemType);
    List<Long> orderIds =
        result.getRecords().stream().map(OrderTreasurePoolDto::getId).collect(Collectors.toList());
    MyTreasure myTreasure = new MyTreasure();
    myTreasure.setIsCheck(1);
    UserDto userData = new UserDto();
    userData.setId(userId);
    List<MyTreasure> mytreasures = storeProPoolService.getMytreasure(myTreasure, userData);
    if (orderIds.size() > 0) {
      LambdaQueryWrapper<OrderTreasureRecord> que = new LambdaQueryWrapper<>();
      que.in(OrderTreasureRecord::getOrderTreasurePoolId, orderIds);
      List<OrderTreasureRecord> orderTreasureRecords = orderTreasureRecordDao.selectList(que);
      ModelMapper md = new ModelMapper();
      List<OrderTreasureRecordDto> map =
          md.map(orderTreasureRecords, new TypeToken<List<OrderTreasureRecordDto>>() {}.getType());
      for (OrderTreasurePoolDto record : result.getRecords()) {
        List<OrderTreasureRecordDto> collect =
            map.stream()
                .filter(o -> o.getOrderTreasurePoolId().equals(record.getId()))
                .collect(Collectors.toList());
        if (collect.size() > 0) {
          record.setOrderTreasureRecord(collect.get(0));
          if (record.getOrderTreasureRecord() != null
              && record.getOrderTreasureRecord().getStripId() != null) {
            record
                .getOrderTreasureRecord()
                .setStoreTreIosPrice(
                    storeTreIosPriceDao.selectById(record.getOrderTreasureRecord().getStripId()));
          }
        }
        StoreTreasure storeTreasure = storeTreasureDao.selectById(record.getTeaPoId());
        if (storeTreasure != null && storeTreasure.getTType() == 4) {
          if (mytreasures.stream()
                  .filter(
                      o ->
                          o.getOrderFingerprint().equals(record.getOrderFingerprint())
                              && o.getIsCheck() == 0)
                  .count()
              > 0) {
            StoreTreasure parent = storeTreasureDao.selectById(storeTreasure.getPId());
            LambdaQueryWrapper<StoreTreasure> q = Wrappers.lambdaQuery(StoreTreasure.class);
            q.eq(BaseEntity::getState, 1);
            q.ne(StoreTreasure::getFromUser, 1);
            q.eq(StoreTreasure::getPId, storeTreasure.getPId());
            int sum =
                storeTreasureDao.selectList(q).stream()
                    .mapToInt(StoreTreasure::getTotalCount)
                    .sum();
            OrderTreasureRecordDto re = new OrderTreasureRecordDto();
            re.setOrderTreasurePoolId(record.getId());
            re.setTType(3);
            re.setTreasureTitle(parent.getTreasureTitle());
            re.setIndexImgPath(parent.getIndexImgPath());
            re.setHeadImgPath(parent.getHeadImgPath());
            re.setTotalCount(sum);
            re.setSurplusCount(parent.getSurplusCount());
            re.setFrostCount(parent.getFrostCount());
            re.setPrice(parent.getPrice());
            re.setIntroduce(parent.getIntroduce());
            re.setSense(parent.getSense());
            re.setNeedKnow(parent.getNeedKnow());
            re.setUpTime(parent.getUpTime());
            re.setCheckTime(parent.getCheckTime());
            re.setTNum("未知");
            re.setStripId(parent.getStripId());
            re.setAuthInfo(parent.getAuthInfo());
            re.setCreateTime(parent.getCreateTime());
            re.setState(parent.getState());
            record.setOrderTreasureRecord(re);
          }
        } else if (storeTreasure != null && record.getOrderTreasureRecord() == null) {
          OrderTreasureRecordDto re = new OrderTreasureRecordDto();
          re.setOrderTreasurePoolId(record.getId());
          re.setTType(0);
          re.setTreasureTitle(storeTreasure.getTreasureTitle());
          re.setIndexImgPath(storeTreasure.getIndexImgPath());
          re.setHeadImgPath(storeTreasure.getHeadImgPath());
          re.setTotalCount(storeTreasure.getTotalCount());
          re.setSurplusCount(storeTreasure.getSurplusCount());
          re.setFrostCount(storeTreasure.getFrostCount());
          re.setPrice(storeTreasure.getPrice());
          re.setIntroduce(storeTreasure.getIntroduce());
          re.setSense(storeTreasure.getSense());
          re.setNeedKnow(storeTreasure.getNeedKnow());
          re.setUpTime(storeTreasure.getUpTime());
          re.setCheckTime(storeTreasure.getCheckTime());
          re.setTNum(storeTreasure.getTNum());
          re.setStripId(storeTreasure.getStripId());
          re.setCreateTime(storeTreasure.getCreateTime());
          re.setState(storeTreasure.getState());
          record.setOrderTreasureRecord(re);
        }
      }
    }
    return result;
  }

  @Override
  public IPage<OrderTreasurePoolDto> adminOrderPage(
      Integer pageNum, long pageSize, Integer orderFlag) {
    IPage<OrderTreasurePoolDto> result =
        orderTreasurePoolDao.listByPageWithNoCreateId(
            new Page<>(pageNum, pageSize), null, orderFlag, -1);
    List<Long> orderIds =
        result.getRecords().stream().map(OrderTreasurePoolDto::getId).collect(Collectors.toList());
    if (orderIds.size() > 0) {
      LambdaQueryWrapper<OrderTreasureRecord> que = new LambdaQueryWrapper<>();
      que.in(OrderTreasureRecord::getOrderTreasurePoolId, orderIds);
      List<OrderTreasureRecord> orderTreasureRecords = orderTreasureRecordDao.selectList(que);
      ModelMapper md = new ModelMapper();
      List<OrderTreasureRecordDto> map =
          md.map(orderTreasureRecords, new TypeToken<List<OrderTreasureRecordDto>>() {}.getType());
      for (OrderTreasurePoolDto record : result.getRecords()) {
        List<OrderTreasureRecordDto> collect =
            map.stream()
                .filter(o -> o.getOrderTreasurePoolId().equals(record.getId()))
                .collect(Collectors.toList());
        if (collect.size() > 0) {
          record.setOrderTreasureRecord(collect.get(0));
        }
      }
    }
    List<Long> userIds =
        result.getRecords().stream().map(BaseEntity::getCreateId).collect(Collectors.toList());
    userIds.add(-111111L);
    Map<Long, SysUser> userMaps =
        sysUserDao.selectBatchIds(userIds).stream()
            .collect(Collectors.toMap(SysUser::getId, o -> o));
    for (OrderTreasurePoolDto record : result.getRecords()) {
      SysUser sysUser = userMaps.get(record.getCreateId());
      if (sysUser != null) {
        record.setCreatorPhone(sysUser.getPhone());
      }
    }
    return result;
  }

  @Override
  public OrderApiResponse updateOrderProduct(UpdateOrderParam param, String ip) {
    OrderTreasurePool orderProduct = orderTreasurePoolDao.selectById(param.getOrderId());
    if (param.getType() == 1 && orderProduct.getOrderFlag() != 0) {
      throw new RuntimeException("订单状态异常，请刷新后再试");
    }
    if (param.getPrice() != null && param.getPrice().compareTo(orderProduct.getTotalPrice()) != 0)
      throw new RuntimeException("支付金额异常请刷新后再试");
    LambdaQueryWrapper<OrderTreasureRecord> que = new LambdaQueryWrapper<>();
    que.eq(OrderTreasureRecord::getOrderTreasurePoolId, orderProduct.getId());
    List<OrderTreasureRecord> orderTreasureRecords = orderTreasureRecordDao.selectList(que);
    OrderTreasureRecord orderTreasureRecord = orderTreasureRecords.get(0);
    SysUser sysUser = sysUserDao.selectById(param.getUserData().getId());
    switch (param.getType()) {
        // 0取消订单1支付订单2收货/修改价格
      case 0:
        orderProduct.setOrderFlag(-1);
        orderProduct.setUpdateTime(new Date());
        orderProduct.setUpdateId(sysUser.getId());
        orderTreasurePoolDao.updateById(orderProduct);
        StoreTreasure storeTreasure1 = storeTreasureDao.selectById(orderProduct.getTeaPoId());
        storeTreasure1.setSurplusCount(storeTreasure1.getSurplusCount() + 1);
        storeTreasure1.setFrostCount(Math.max(storeTreasure1.getFrostCount() - 1, 0));
        if (storeTreasure1.getFromUser() != null && storeTreasure1.getFromUser() == 1) {
          storeTreasure1.setState(1);
          storeTreasure1.setIsSale(0);
        }
        storeTreasureDao.updateById(storeTreasure1);
        return OrderApiResponse.success("success");
      case 1:
        if (orderProduct.getTotalPrice().doubleValue() == 0) {
          param.setPayType(3);
        }
        StoreTreasure storeTreasure = storeTreasureDao.selectById(orderProduct.getTeaPoId());
        if (storeTreasure.getRuleCount() != null && storeTreasure.getRuleCount() != 0) {
          LambdaQueryWrapper<OrderTreasurePool> orderQue =
              Wrappers.lambdaQuery(OrderTreasurePool.class);
          orderQue
              .eq(BaseEntity::getCreateId, sysUser.getId())
              .eq(OrderTreasurePool::getTeaPoId, storeTreasure.getId())
              .eq(OrderTreasurePool::getOrderFlag, 2);
          List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(orderQue);
          if (orderTreasurePools.size() > storeTreasure.getRuleCount()) {
            throw new RuntimeException("已到达限购数量");
          }
        }
        switch (param.getPayType()) {
            // 0微信1支付宝2applePay3余额支付
            //          case 0:
            //            if (param.getFlagChnl() != null && "3".equals(param.getFlagChnl())) {
            //              return getWeixinPayH5Response(orderProduct,
            // storeTreasure.getTreasureTitle());
            //            } else {
            //              return getWeixinPayResponse(orderProduct,
            // storeTreasure.getTreasureTitle());
            //            }
            //          case 1:
            //            if (param.getFlagChnl() != null && "3".equals(param.getFlagChnl())) {
            //              return getAlipayTradeH5PayResponse(orderProduct,
            // storeTreasure.getTreasureTitle());
            //            } else {
            //              return getAlipayTradeAppPayResponse(orderProduct,
            // storeTreasure.getTreasureTitle());
            //            }
            //          case 2:
            //            String verifyResult = IosVerifyUtil.buyAppVerify(param.getPayload(), 1);
            //            if (verifyResult == null) {
            //              return ApiResponse.fail("苹果内购校验失败");
            //            } else {
            //              log.info("线上，苹果平台返回JSON:" + verifyResult);
            //              JSONObject appleReturn = JSONObject.parseObject(verifyResult);
            //              String states = appleReturn.getString("status");
            //              // 无数据则沙箱环境验证
            //              if ("21007".equals(states)) {
            //                verifyResult = IosVerifyUtil.buyAppVerify(param.getPayload(), 0);
            //                log.info("沙盒环境，苹果平台返回JSON:" + verifyResult);
            //                appleReturn = JSONObject.parseObject(verifyResult);
            //                states = appleReturn.getString("status");
            //              }
            //              log.info("苹果平台返回值：appleReturn" + appleReturn);
            //              // 前端所提供的收据是有效的    验证成功
            //              if (states.equals("0")) {
            //                String receipt = appleReturn.getString("receipt");
            //                JSONObject returnJson = JSONObject.parseObject(receipt);
            //                String inApp = returnJson.getString("in_app");
            //                List<HashMap> inApps = JSONObject.parseArray(inApp, HashMap.class);
            //                if (!CollectionUtils.isEmpty(inApps)) {
            //                  List<String> transactionIds =
            //                      inApps.stream()
            //                          .map(o -> (String) o.get("transaction_id"))
            //                          .collect(Collectors.toList());
            //                  // 交易列表包含当前交易，则认为交易成功
            //                  if (transactionIds.contains(param.getTransactionId())) {
            //                    // 添加流水
            //                    addConBalanceRecord(orderProduct, 4);
            //                    SysUserBackpack sysUserBackpack = new SysUserBackpack();
            //                    sysUserBackpack.setUId(orderProduct.getCreateId());
            //                    sysUserBackpack.setSTreasureId(orderProduct.getTeaPoId());
            //                    sysUserBackpack.setOrderTreasurePoolId(orderProduct.getId());
            //
            // sysUserBackpack.setOrderFingerprint(orderProduct.getOrderFingerprint());
            //                    sysUserBackpack.setFinType(4);
            //                    sysUserBackpack.setBeforeUserId(0L);
            //                    sysUserBackpack.setTreasureFrom(
            //                        orderProduct.getItemType() == 0 || orderProduct.getItemType()
            // == 1 ? 0 : 1);
            //                    sysUserBackpack.setAfterUserId(orderProduct.getCreateId());
            //                    sysUserBackpack.setCreateId(orderProduct.getCreateId());
            //                    sysUserBackpack.setCreateTime(new Date());
            //                    WenchangDDC bsnUtil = new WenchangDDC();
            //                    // 轉移ddc
            //                    //                    transDDc(sysUser, storeTreasure,
            // sysUserBackpack,
            //                    // bsnUtil);
            //                    sysUserBackpackDao.insert(sysUserBackpack);
            //                    orderProduct.setOrderFlag(2);
            //                    orderProduct.setPayTime(new Date());
            //                    orderTreasurePoolDao.updateById(orderProduct);
            //                    return "success";
            //                  }
            //                }
            //              }
            //              return ApiResponse.fail("apple 内购校验失败");
            //            }
          case 3:
            //            BigDecimal recordTotal =
            //                orderTreasureRecord
            //                    .getPrice()
            //                    .multiply(new BigDecimal(orderProduct.getTotalCount()));
            //            if (sysUser.getBalance().compareTo(recordTotal) >= 0) {
            //              sysUser.setBalance(sysUser.getBalance().subtract(recordTotal));
            //              sysUserDao.updateById(sysUser);
            //              // 添加购买流水
            //              addConBalanceRecord(orderProduct, 4);
            //              SysUserBackpack sysUserBackpack = new SysUserBackpack();
            //              sysUserBackpack.setUId(orderProduct.getCreateId());
            //              sysUserBackpack.setSTreasureId(orderProduct.getTeaPoId());
            //              sysUserBackpack.setOrderTreasurePoolId(orderProduct.getId());
            //              sysUserBackpack.setOrderFingerprint(orderProduct.getOrderFingerprint());
            //              sysUserBackpack.setFinType(4);
            //              sysUserBackpack.setBeforeUserId(0L);
            //              sysUserBackpack.setTreasureFrom(
            //                  orderProduct.getItemType() == 0 || orderProduct.getItemType() == 1 ?
            // 0 : 1);
            //              sysUserBackpack.setAfterUserId(orderProduct.getCreateId());
            //              sysUserBackpack.setCreateId(orderProduct.getCreateId());
            //              sysUserBackpack.setCreateTime(new Date());
            //              if (storeTreasure.getOrderFingerprint() == null
            //                  && (storeTreasure.getTType() == 3 || storeTreasure.getTType() == 4))
            // {
            //                sysUserBackpack.setIsCheck(0);
            //              } else {
            //                sysUserBackpack.setIsCheck(null);
            //              }
            //              WenchangDDC bsnUtil = new WenchangDDC();
            //              // 轉移ddc
            //              String from = ResourcesUtil.getProperty("bsn_plate_addr");
            //              transDDc(from, sysUser, storeTreasure, sysUserBackpack, bsnUtil);
            //              sysUserBackpackDao.insert(sysUserBackpack);
            //              orderProduct.setOrderFlag(2);
            //              orderProduct.setPayTime(new Date());
            //              orderTreasurePoolDao.updateById(orderProduct);
            //              if (storeTreasure.getFromUser() == 1) {
            //                SysUser saler = sysUserDao.selectById(storeTreasure.getCreateId()); //
            // 卖家
            //                from = saler.getLinkAddress();
            //                String consignment_fee =
            // sysDictonaryDao.selectByAlias("consignment_fee");
            //                saler.setBalance(
            //                    saler.getBalance() == null
            //                        ? orderProduct
            //                            .getTotalPrice()
            //                            .multiply(new BigDecimal(1).subtract(new
            // BigDecimal(consignment_fee)))
            //                        : saler
            //                            .getBalance()
            //                            .add(
            //                                orderProduct
            //                                    .getTotalPrice()
            //                                    .multiply(
            //                                        new BigDecimal(1)
            //                                            .subtract(new
            // BigDecimal(consignment_fee)))));
            //                sysUserDao.updateById(saler);
            //                ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
            //                conBalanceRecord.setUId(saler.getCreateId());
            //                conBalanceRecord.setOrderId(orderProduct.getId() + "");
            //                conBalanceRecord.setOrderType(6);
            //
            //                conBalanceRecord.setTotalPrice(
            //                    orderProduct
            //                        .getTotalPrice()
            //                        .multiply(new BigDecimal(1).subtract(new
            // BigDecimal(consignment_fee))));
            //                conBalanceRecord.setTradingChannel(3);
            //                conBalanceRecord.setCreateId(saler.getCreateId());
            //                conBalanceRecord.setCreateTime(new Date());
            //                log.info("添加出售交易流水信息：" + JSONObject.toJSONString(conBalanceRecord));
            //                conBalanceRecordDao.insert(conBalanceRecord);
            //                SysUserBackpack salerBack = new SysUserBackpack();
            //                salerBack.setUId(saler.getId());
            //                salerBack.setSTreasureId(orderProduct.getTeaPoId());
            //                salerBack.setOrderTreasurePoolId(orderProduct.getId());
            //                salerBack.setOrderFingerprint(storeTreasure.getOrderFingerprint());
            //                salerBack.setFinType(3);
            //                salerBack.setTreasureFrom(1);
            //                salerBack.setCreateId(saler.getCreateId());
            //                salerBack.setCreateTime(new Date());
            //                salerBack.setBeforeUserId(saler.getId());
            //                salerBack.setAfterUserId(sysUser.getId());
            //
            //                sysUserBackpackDao.insert(salerBack); // 添加出售记录
            //                LambdaQueryWrapper<StoreTreasureRecord> reQ =
            //                    Wrappers.lambdaQuery(StoreTreasureRecord.class);
            //                reQ.eq(
            //                    StoreTreasureRecord::getOrderFingerprint,
            // storeTreasure.getOrderFingerprint());
            //                List<StoreTreasureRecord> emptyTreaRecord =
            // storeTreasureRecordDao.selectList(reQ);
            //                if (emptyTreaRecord != null && emptyTreaRecord.size() > 0) {
            //
            // emptyTreaRecord.get(0).setOrderFingerprint(orderProduct.getOrderFingerprint());
            //                  emptyTreaRecord.get(0).setUserId(orderProduct.getCreateId());
            //                  emptyTreaRecord.get(0).setUpdateTime(new Date());
            //                  storeTreasureRecordDao.updateById(emptyTreaRecord.get(0));
            //                  orderTreasureRecord.setTNum(emptyTreaRecord.get(0).getStrNum());
            //                  orderTreasureRecordDao.updateById(orderTreasureRecords.get(0));
            //                }
            //              } else {
            //                StoreTreasureRecord emptyTreaRecord =
            // getEmptyTreaRecord(orderProduct.getTeaPoId());
            //                if (emptyTreaRecord != null) {
            //
            // emptyTreaRecord.setOrderFingerprint(orderProduct.getOrderFingerprint());
            //                  emptyTreaRecord.setUserId(orderProduct.getCreateId());
            //                  emptyTreaRecord.setUpdateTime(new Date());
            //                  storeTreasureRecordDao.updateById(emptyTreaRecord);
            //                }
            //              }
            //
            //              return OrderApiResponse.success("success");
            //            } else {
            //              throw new RuntimeException("余额不足请充值后再试");
            //            }
            throw new RuntimeException("支付方式错误");
          case 4:
            OrderTreasurePoolAddDto tmpDto = new OrderTreasurePoolAddDto();
            tmpDto.setItemType(
                storeTreasure.getFromUser() != null && storeTreasure.getFromUser() == 1 ? 2 : 0);
            tmpDto.setFlagChnl(param.getFlagChnl());
            tmpDto.setIdNo(param.getIdNo());
            tmpDto.setAcctName(param.getAcctName());
            tmpDto.setCardNo(param.getCardNo());
            tmpDto.setNoAgree(param.getNoAgree());
            orderProduct.setOrderFingerprint(UUID.randomUUID().toString().replaceAll("-", ""));
            orderTreasurePoolDao.updateById(orderProduct);
            OrderApiResponse<Object> success =
                OrderApiResponse.success(
                    sdPayOrder(
                        tmpDto,
                        orderProduct,
                        "藏品二次支付_" + orderProduct.getOrderFingerprint(),
                        sysUser,
                        ip));
            success.setOrderId(orderProduct.getId());
            return success;
          default:
            throw new RuntimeException("支付方式异常");
        }
      case 2:
        //        String orderFingerprint = orderProduct.getOrderFingerprint(); // 订单指纹
        //        StoreTreasure storeProPool =
        // storeTreasureDao.selectByFingerprint(orderFingerprint);
        //        storeProPool.setProPrice(param.getPrice());
        //        storeProPool.setUpdateId(sysUser.getId());
        //        storeProPool.setUpdateTime(new Date());
        //        storeProPoolDao.updateById(storeProPool);

        return OrderApiResponse.success("操作成功");
      default:
        throw new RuntimeException("操作类型错误");
    }
  }

  @Override
  public void transDDc(
      String from,
      SysUser sysUser,
      StoreTreasure storeTreasure,
      SysUserBackpack sysUserBackpack,
      WenchangDDC bsnUtil) {
    log.info(
        "==========ddc转移开始： from: "
            + from
            + " to: "
            + sysUser.getLinkAddress()
            + " ddcId："
            + storeTreasure.getId()
            + " sysUserBackId:"
            + sysUserBackpack.getId());
    //    transDDc(from,sysUser.getLinkAddress(),sysUser,storeTreasure,sysUserBackpack,)
    if (Strings.isNotEmpty(storeTreasure.getDdcId())) {
      try {
        sysUserBackpack.setTransationId(
            bsnUtil.safeTransferFrom(
                from,
                sysUser.getLinkAddress(),
                new BigInteger(storeTreasure.getDdcId()),
                new BigInteger(1 + ""),
                "".getBytes()));
        log.info("==========ddc 转移结束： 交易hash: " + sysUserBackpack.getTransationId());
      } catch (Exception e) {
        log.info("==========ddc转移异常：" + e.getMessage());
        e.printStackTrace();
      }
      sysUserBackpack.setDdcId(storeTreasure.getDdcId());
      sysUserBackpack.setDdcUrl(storeTreasure.getDdcUrl());
    }
  }

  @Override
  public void transDDc(
      String from,
      String to,
      SysUser sysUser,
      StoreTreasure storeTreasure,
      SysUserBackpack sysUserBackpack,
      StoreTreasureRecord storeTreasureRecord) {
    log.info(
        "==========ddc转移开始： "
            + " to: "
            + sysUser.getLinkAddress()
            + " streaId："
            + storeTreasure.getId()
            + " sysUserBackId:"
            + sysUserBackpack.getId()
            + "storeTreasureRecord:"
            + storeTreasureRecord.getId());
    if (Strings.isNotEmpty(storeTreasureRecord.getNftId())) {
      try {
        UUID uuid = UUID.randomUUID();
        sysUserBackpack.setTransationId(uuid.toString());
        AvataUtil.trans(from, storeTreasureRecord.getNftId(), uuid.toString(), to);
        log.info("==========ddc 转移结束： 交易hash: " + sysUserBackpack.getTransationId());
      } catch (Exception e) {
        log.info("==========ddc转移异常：" + e.getMessage());
        e.printStackTrace();
      }
      sysUserBackpack.setDdcId(storeTreasure.getDdcId());
      sysUserBackpack.setDdcUrl(storeTreasure.getDdcUrl());
    }
  }

  @Override
  public IPage<OrderTreasurePoolDto> updateSaleFlag(
      IPage<OrderTreasurePoolDto> page, UserDto userData) {
    List<String> collect =
        page.getRecords().stream()
            .map(OrderTreasurePoolDto::getOrderFingerprint)
            .collect(Collectors.toList());
    List<String> collect1 =
        StoreProPoolServiceImpl.addCheckIsSalingParam(collect, storeTreasureDao, userData.getId());
    collect.add(-1111 + "");
    LambdaQueryWrapper<StoreTreasure> qu = Wrappers.lambdaQuery(StoreTreasure.class);
    qu.in(StoreTreasure::getOrderFingerprint, collect);
    List<StoreTreasure> storeTreasures = storeTreasureDao.selectList(qu);
    Map<String, StoreTreasure> orderFe =
        storeTreasures.stream()
            .collect(Collectors.toMap(StoreTreasure::getOrderFingerprint, t -> t));
    page.getRecords()
        .forEach(
            o -> {
              o.setIsSaleIng(collect1.contains(o.getOrderFingerprint()) ? 1 : 0);
              o.setPayEndTime(o.getPayTime());
              if (orderFe.get(o.getOrderFingerprint()) != null) {
                if (o.getOrderTreasureRecord() != null) {
                  o.getOrderTreasureRecord()
                      .setSalePrice(orderFe.get(o.getOrderFingerprint()).getPrice());
                }
              }
            });
    return page;
  }

  @Override
  public void weixinRecharge(WXNotifyParam wxp, boolean b) {
    try {
      log.debug("noifityWXin-flag:{}", b);
      if (wxp.getReturn_code().equals(ResourcesUtil.SUCCESS) && b) {
        String orderFingerprint = wxp.getOut_trade_no();
        String transactionId = wxp.getTransaction_id();

        log.info("微信异步通知参数：" + wxp);
        updateBalanceByNotify(orderFingerprint, transactionId, 0);
      }
    } catch (Exception e) {
      log.error("noifityWXin-error:{}", e);
    }
  }

  @Autowired private ConRechargeDao conRechargeDao;

  private void updateBalanceByNotify(
      String orderFingerprint, String transactionId, Integer payType) {
    log.info("支付回调，订单指纹：" + orderFingerprint + " 支付宝、微信流失tradeNo:" + transactionId);
    LambdaQueryWrapper<ConRecharge> que = new LambdaQueryWrapper<>();
    que.eq(ConRecharge::getOrderFingerprint, orderFingerprint);
    que.eq(ConRecharge::getOrderFlag, 0);
    List<ConRecharge> conRecharges = conRechargeDao.selectList(que);
    if (conRecharges.size() > 0) {
      ConRecharge conRecharge = conRecharges.get(0);
      conRecharge.setOrderFlag(1);
      conRechargeDao.updateById(conRecharge);
      SysUser sysUser = sysUserDao.selectById(conRecharge.getUId());
      //      sysUser.setBalance(sysUser.getBalance().add(conRecharge.getTotalPrice()));
      //      sysUserDao.updateById(sysUser);
      ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
      conBalanceRecord.setUId(sysUser.getId());
      conBalanceRecord.setReType(0);
      conBalanceRecord.setOrderId(conRecharge.getId() + "");
      conBalanceRecord.setOrderType(0);
      conBalanceRecord.setTotalPrice(conRecharges.get(0).getTotalPrice());
      conBalanceRecord.setTradingChannel(payType);
      conBalanceRecord.setCreateId(sysUser.getId());
      conBalanceRecord.setCreateTime(new Date());
      conBalanceRecordDao.insert(conBalanceRecord);
    }
  }

  @Override
  public void alipayRecharge(String orderFingerprint, String tradeNo) {
    updateBalanceByNotify(orderFingerprint, tradeNo, 1);
  }

  @Override
  public CellTotal beforeCollect() {
    CellTotal result = new CellTotal();
    List<CellResult> re = orderTreasurePoolDao.getCollectLine(1);
    if (re.size() > 0) {
      Collections.reverse(re);
      result.setTotalCount(
          new BigDecimal(re.stream().mapToInt(o -> o.getCount().intValue()).sum()));
      result.setTodayCount(re.get(0) != null ? re.get(0).getCount() : new BigDecimal(0));
      List<CellResult> cellResults = re.subList(0, Math.min(re.size(), 30));
      Collections.reverse(cellResults);
      result.setCellResultList(cellResults);
      result.setMonthCount(
          new BigDecimal(
              result.getCellResultList().stream().mapToInt(o -> o.getCount().intValue()).sum()));
    }
    return result;
  }

  @Override
  public CellTotal treasureCollect() {
    CellTotal result = new CellTotal();
    List<CellResult> re = orderTreasurePoolDao.getCollectLine(0);
    Collections.reverse(re);
    result.setTotalCount(new BigDecimal(re.stream().mapToInt(o -> o.getCount().intValue()).sum()));
    List<CellResult> cellResults = re.subList(0, Math.min(re.size(), 30));
    Collections.reverse(cellResults);
    result.setCellResultList(cellResults);
    result.setMonthCount(
        new BigDecimal(
            result.getCellResultList().stream().mapToInt(o -> o.getCount().intValue()).sum()));
    if (cellResults.size() > 0)
      result.setTodayCount(result.getCellResultList().get(cellResults.size() - 1).getCount());
    return result;
  }

  @Override
  @Transactional
  public String updateOrderByNotify(String outtradeno, String transactionId, Integer payType) {
    LambdaQueryWrapper<OrderTreasurePool> que = new LambdaQueryWrapper<>();
    que.eq(OrderTreasurePool::getOrderFingerprint, outtradeno);
    List<OrderTreasurePool> orderProducts = orderTreasurePoolDao.selectList(que);
    if (orderProducts.size() > 0
        && (orderProducts.get(0).getOrderFlag().equals(0)
            || orderProducts.get(0).getOrderFlag().equals(-1))) {
      log.info("修正订单已支付状态， 订单指纹：" + outtradeno);
      OrderTreasurePool orderTreasurePool = orderProducts.get(0);
      orderTreasurePool.setOrderFlag(2);
      orderTreasurePool.setPayTime(new Date());
      orderTreasurePool.setTradeNo(transactionId);
      orderTreasurePool.setUpdateTime(new Date());
      orderTreasurePoolDao.updateById(orderTreasurePool);
      //      addConBalanceRecord(orderTreasurePool, 4);
      log.info("清除冻结数量");
      SysUser buyer = sysUserDao.selectById(orderTreasurePool.getCreateId());
      StoreTreasure storeProPool = storeTreasureDao.selectById(orderTreasurePool.getTeaPoId());
      if (orderTreasurePool.getItemType() == 0 || orderTreasurePool.getItemType() == 3) {
        // 首页藏品
        StoreTreasure storeTreasure = storeTreasureDao.selectById(orderTreasurePool.getTeaPoId());
        storeTreasure.setFrostCount(
            storeTreasure.getFrostCount() - orderTreasurePool.getTotalCount());
        storeTreasure.setUpdateTime(new Date());
        storeTreasureDao.updateById(storeTreasure);
        SysUserBackpack sysUserBackpack = new SysUserBackpack();
        sysUserBackpack.setUId(orderTreasurePool.getCreateId());
        sysUserBackpack.setSTreasureId(orderTreasurePool.getTeaPoId());
        sysUserBackpack.setOrderTreasurePoolId(orderTreasurePool.getId());
        sysUserBackpack.setOrderFingerprint(orderTreasurePool.getOrderFingerprint());
        sysUserBackpack.setFinType(4);
        sysUserBackpack.setBeforeUserId(0L);
        sysUserBackpack.setTreasureFrom(
            orderTreasurePool.getItemType() == 0 || orderTreasurePool.getItemType() == 1 ? 0 : 1);
        sysUserBackpack.setIsCheck(orderTreasurePool.getItemType() == 3 ? 0 : null);
        sysUserBackpack.setAfterUserId(orderTreasurePool.getCreateId());
        sysUserBackpack.setCreateId(orderTreasurePool.getCreateId());
        sysUserBackpack.setCreateTime(new Date());
        StoreTreasureRecord emptyTreaRecord = getEmptyTreaRecord(orderTreasurePool.getTeaPoId());
        if (emptyTreaRecord != null) {
          emptyTreaRecord.setOrderFingerprint(orderTreasurePool.getOrderFingerprint());
          emptyTreaRecord.setUserId(orderTreasurePool.getCreateId());
          emptyTreaRecord.setUpdateTime(new Date());
          storeTreasureRecordDao.updateById(emptyTreaRecord);
          LambdaQueryWrapper<OrderTreasureRecord> orderTreasureReQue =
              Wrappers.lambdaQuery(OrderTreasureRecord.class);
          orderTreasureReQue.eq(
              OrderTreasureRecord::getOrderTreasurePoolId, orderTreasurePool.getId());
          List<OrderTreasureRecord> orderTreasureRecords =
              orderTreasureRecordDao.selectList(orderTreasureReQue);
          if (orderTreasureRecords.size() > 0) {
            orderTreasureRecords.get(0).setTNum(emptyTreaRecord.getStrNum());
            orderTreasureRecordDao.updateById(orderTreasureRecords.get(0));
          }
        }
        WenchangDDC bsnUtil = new WenchangDDC();
        if (Strings.isNotEmpty(storeTreasure.getDdcId())) {
          SysUser sysUser = buyer;
          try {
            //            sysUserBackpack.setTransationId(
            //                bsnUtil.safeTransferFrom(
            //                    ResourcesUtil.getProperty("bsn_plate_addr"),
            //                    sysUser.getLinkAddress(),
            //                    new BigInteger(storeTreasure.getDdcId()),
            //                    new BigInteger(1 + ""),
            //                    "".getBytes()));
            transDDc(
                "iaa1tvt9xkw5t2znq687u23mtqvmpk9spakpe5kezx",
                sysUser.getLinkAddress(),
                sysUser,
                storeTreasure,
                sysUserBackpack,
                emptyTreaRecord);
          } catch (Exception e) {
            log.info("==========ddc转移异常：" + e.getMessage());
            log.info("==========ddc转移异常：" + e.getMessage());
            log.info("==========ddc转移异常：" + e.getMessage());
            log.info("==========ddc转移异常：" + e.getMessage());
            log.info("==========ddc转移异常：" + e.getMessage());
            log.info("==========ddc转移异常：" + e.getMessage());
            e.printStackTrace();
          }
          sysUserBackpack.setDdcId(storeTreasure.getDdcId());
          sysUserBackpack.setDdcUrl(storeTreasure.getDdcUrl());
        }
        sysUserBackpackDao.insert(sysUserBackpack); // 添加买记录
        //        SysUser sysUser = buyer;
        //        createDDC(sysUser, storeTreasure, orderTreasurePool, sysUserBackpack, null);
      } else if (orderTreasurePool.getItemType() == 1) {
        // 流转中心商品

        if (storeProPool.getFromUser() == 1) {
          storeProPool.setUpdateTime(new Date());
          storeProPool.setIsSale(1);
          storeProPool.setState(-1);
          storeTreasureDao.updateById(storeProPool);
          // 来源用户 0查找卖家 1转移标识2给卖家充值，添加售出流水
          ArrayList<Integer> tempType = new ArrayList<>();
          tempType.add(2);
          tempType.add(4);
          tempType.add(6);
          tempType.add(8);
          LambdaQueryWrapper<SysUserBackpack> qu = new LambdaQueryWrapper<>();
          qu.eq(SysUserBackpack::getOrderFingerprint, storeProPool.getOrderFingerprint())
              .in(SysUserBackpack::getFinType, tempType)
              .orderByDesc(BaseEntity::getCreateTime);
          List<SysUserBackpack> sysUserBackpacks = sysUserBackpackDao.selectList(qu);
          if (sysUserBackpacks.size() > 0) {
            SysUser saler = sysUserDao.selectById(sysUserBackpacks.get(0).getUId()); // 卖家
            SysUserBackpack sysUserBackpack = new SysUserBackpack();
            sysUserBackpack.setUId(saler.getId());
            sysUserBackpack.setSTreasureId(orderTreasurePool.getTeaPoId());
            sysUserBackpack.setOrderTreasurePoolId(orderTreasurePool.getId());
            sysUserBackpack.setOrderFingerprint(storeProPool.getOrderFingerprint());
            sysUserBackpack.setFinType(3);
            sysUserBackpack.setTreasureFrom(1);
            sysUserBackpack.setCreateId(saler.getCreateId());
            sysUserBackpack.setCreateTime(new Date());
            sysUserBackpack.setBeforeUserId(saler.getId());
            sysUserBackpack.setAfterUserId(orderTreasurePool.getCreateId());
            sysUserBackpackDao.insert(sysUserBackpack); // 添加出售记录
            SysUserBackpack sysUserBackpack1 = new SysUserBackpack();
            sysUserBackpack1.setUId(orderTreasurePool.getCreateId());
            sysUserBackpack1.setSTreasureId(orderTreasurePool.getTeaPoId());
            sysUserBackpack1.setOrderFingerprint(orderTreasurePool.getOrderFingerprint());
            sysUserBackpack1.setFinType(4);
            sysUserBackpack1.setTreasureFrom(1);
            sysUserBackpack1.setCreateId(orderTreasurePool.getCreateId());
            sysUserBackpack1.setCreateTime(new Date());
            sysUserBackpack1.setBeforeUserId(saler.getId());
            sysUserBackpack1.setAfterUserId(orderTreasurePool.getCreateId());

            LambdaQueryWrapper<StoreTreasureRecord> reQ =
                Wrappers.lambdaQuery(StoreTreasureRecord.class);
            reQ.eq(StoreTreasureRecord::getUserId, saler.getId())
                .eq(StoreTreasureRecord::getOrderFingerprint, storeProPool.getOrderFingerprint());
            List<StoreTreasureRecord> storeTreasureRecords = storeTreasureRecordDao.selectList(reQ);
            if (storeTreasureRecords.size() > 0) {
              storeTreasureRecords
                  .get(0)
                  .setOrderFingerprint(orderTreasurePool.getOrderFingerprint());
              storeTreasureRecords.get(0).setUserId(orderTreasurePool.getCreateId());
              storeTreasureRecords.get(0).setUpdateTime(new Date());
              storeTreasureRecordDao.updateById(storeTreasureRecords.get(0));
              LambdaQueryWrapper<OrderTreasureRecord> orderTreasureReQue =
                  Wrappers.lambdaQuery(OrderTreasureRecord.class);
              orderTreasureReQue.eq(
                  OrderTreasureRecord::getOrderTreasurePoolId, orderTreasurePool.getId());
              List<OrderTreasureRecord> orderTreasureRecords =
                  orderTreasureRecordDao.selectList(orderTreasureReQue);
              if (orderTreasureRecords.size() > 0) {
                orderTreasureRecords.get(0).setTNum(storeTreasureRecords.get(0).getStrNum());
                orderTreasureRecordDao.updateById(orderTreasureRecords.get(0));
              }
            }
            WenchangDDC bsnUtil = new WenchangDDC();
            StoreTreasure storeTreasure =
                storeTreasureDao.selectById(orderTreasurePool.getTeaPoId());
            try {
              //              bsnUtil.safeTransferFrom(
              //                  saler.getLinkAddress(),
              //                  buyer.getLinkAddress(),
              //                  new BigInteger(orderTreasurePool.getTeaPoId() + ""),
              //                  new BigInteger(1 + ""),
              //                  "".getBytes());
              transDDc(
                  saler.getLinkAddress(),
                  buyer.getLinkAddress(),
                  buyer,
                  storeTreasure,
                  sysUserBackpack,
                  storeTreasureRecords.get(0));
            } catch (Exception e) {
              e.printStackTrace();
              log.info("ddc转移异常");
            }
            sysUserBackpackDao.insert(sysUserBackpack1); // 添加购买记录
            String consignment_fee = sysDictonaryDao.selectByAlias("consignment_fee");
            //            saler.setBalance(
            //                saler
            //                    .getBalance()
            //                    .add(
            //                        orderTreasurePool
            //                            .getTotalPrice()
            //                            .multiply(
            //                                new BigDecimal(1).subtract(new
            // BigDecimal(consignment_fee)))));
            //            sysUserDao.updateById(saler); // 转账给卖家

            ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
            conBalanceRecord.setUId(saler.getId());
            conBalanceRecord.setOrderId(orderTreasurePool.getId() + "");
            conBalanceRecord.setOrderType(6);
            conBalanceRecord.setTotalPrice(
                orderTreasurePool
                    .getTotalPrice()
                    .multiply(new BigDecimal(1).subtract(new BigDecimal(consignment_fee))));
            conBalanceRecord.setTradingChannel(3);
            conBalanceRecord.setCreateId(saler.getId());
            conBalanceRecord.setCreateTime(new Date());
            conBalanceRecordDao.insert(conBalanceRecord);

          } else {
            log.error(
                "订单通知逻辑异常##################################"
                    + JSONObject.toJSONString(orderTreasurePool));
          }
        }
      }
      ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
      conBalanceRecord.setUId(orderProducts.get(0).getCreateId());
      conBalanceRecord.setReType(2);
      conBalanceRecord.setOrderId(orderProducts.get(0).getId() + "");
      conBalanceRecord.setOrderType(4);
      conBalanceRecord.setTotalPrice(orderProducts.get(0).getTotalPrice());
      conBalanceRecord.setTradingChannel(payType);
      conBalanceRecord.setCreateId(orderProducts.get(0).getCreateId());
      conBalanceRecord.setCreateTime(new Date());
      conBalanceRecordDao.insert(conBalanceRecord);
      return "Success";

    } else {
      log.info("订单支付异步通知，重复！重复！重复！ 订单指纹: " + outtradeno);
    }
    return null;
  }

  @Autowired private SysUserBackpackDao sysUserBackpackDao;

  @Override
  public OrderTreasurePoolDto getOrderTreasurePoolById(Long id) {
    Optional<OrderTreasurePoolDto> optional = selectDataById(OrderTreasurePoolDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Autowired private SysUserDao sysUserDao;
  @Autowired private StoreTreasureDao storeTreasureDao;
  @Autowired private ConIntegralRecordDao conIntegralRecordDao;
  @Autowired private StoreTreasureCheckUserDao storeTreasureCheckUserDao;

  @Transactional(rollbackFor = Exception.class)
  @Override
  public Object addJoin(OrderTreasurePoolAddDto tmpDto) {
    // 超前申购订单
    SysUser sysUser = sysUserDao.selectById(tmpDto.getUserId());
    StoreTreasure storeTreasure = storeTreasureDao.selectById(tmpDto.getTeaPoId());
    if (storeTreasure.getPrice().compareTo(tmpDto.getCurPrice()) != 0)
      throw new RuntimeException("页面已过期请刷新后再试");
    if (storeTreasure.getUpTime() != null) {
      if (storeTreasure.getUpTime().getTime() > System.currentTimeMillis()) {
        throw new RuntimeException("超前申购尚未开始");
      }
    }
    if (storeTreasure.getCheckTime() != null) {
      if (storeTreasure.getCheckTime().getTime() < System.currentTimeMillis()) {
        throw new RuntimeException("超前申购已结束");
      }
    }

    LambdaQueryWrapper<OrderTreasurePool> q = Wrappers.lambdaQuery(OrderTreasurePool.class);
    q.eq(OrderTreasurePool::getTeaPoId, tmpDto.getTeaPoId())
        .eq(BaseEntity::getCreateId, tmpDto.getUserId());
    List<OrderTreasurePool> tmList = orderTreasurePoolDao.selectList(q);
    if (tmList.size() > 0) {
      throw new RuntimeException("不可重复参加");
    }
    if (sysUser.getMetaCount() >= storeTreasure.getPrice().longValue()) {

      sysUser.setMetaCount(
          new BigDecimal(sysUser.getMetaCount()).subtract(storeTreasure.getPrice()).longValue());
      sysUser.setUpdateTime(new Date());
      sysUserDao.updateById(sysUser);
      OrderTreasurePool orderTreasurePool = new OrderTreasurePool();
      orderTreasurePool.setOrderFingerprint(UUID.randomUUID().toString().replaceAll("-", ""));
      orderTreasurePool.setTeaPoId(tmpDto.getTeaPoId());
      orderTreasurePool.setItemType(tmpDto.getItemType());
      orderTreasurePool.setCurPrice(tmpDto.getCurPrice());
      orderTreasurePool.setTotalCount(1);
      orderTreasurePool.setCheckFlag(0);
      orderTreasurePool.setOrderFlag(2);
      orderTreasurePool.setTotalPrice(tmpDto.getTotalPrice());
      orderTreasurePool.setPayType(-1);
      orderTreasurePool.setIsJoin(1);
      orderTreasurePool.setPayEndTime(null);
      orderTreasurePool.setCreateId(tmpDto.getUserId());
      orderTreasurePool.setCreateTime(new Date());
      boolean flag = true;
      String orderNum = null;
      LambdaQueryWrapper<OrderTreasurePool> que = new LambdaQueryWrapper<>();
      while (flag) {
        orderNum = IdUtil.getRamdomIndex();
        que.clear();
        que.eq(OrderTreasurePool::getOrderNum, orderNum);
        que.eq(OrderTreasurePool::getTeaPoId, orderTreasurePool.getTeaPoId());
        List<OrderTreasurePool> tmps = orderTreasurePoolDao.selectList(que);
        if (tmps.size() == 0) {
          flag = false;
        }
      }
      orderTreasurePool.setOrderNum(orderNum);
      orderTreasurePoolDao.insert(orderTreasurePool);
      // 创建商品快照
      addOrderTreasurePoolCopy(storeTreasure, orderTreasurePool);
      ConIntegralRecord conIntegralRecord = new ConIntegralRecord();
      conIntegralRecord.setUId(sysUser.getId());
      conIntegralRecord.setRecordType(2);
      conIntegralRecord.setMetaCount(storeTreasure.getPrice().intValue());
      conIntegralRecord.setOrderId(orderTreasurePool.getId());
      conIntegralRecord.setCreateId(sysUser.getId());
      conIntegralRecord.setCreateTime(new Date());
      conIntegralRecordDao.insert(conIntegralRecord);
      return orderTreasurePool.getOrderNum();
    }
    return "积分不足";
  }

  @Autowired private SysOrgDao sysOrgDao;
  @Autowired private StoreTreIosPriceDao storeTreIosPriceDao;

  @Override
  public IPage<StoreTreasureDto> myJoin(Integer pageNum, long parseLong, Long userId) {
    IPage<StoreTreasureDto> res =
        storeTreasureDao.getMyJoin(new Page<>(pageNum, parseLong), userId);
    LambdaQueryWrapper<OrderTreasurePool> que = new LambdaQueryWrapper<>();
    List<Long> collect2 =
        res.getRecords().stream().map(StoreTreasureDto::getId).collect(Collectors.toList());
    collect2.add(-1111L);
    que.eq(BaseEntity::getCreateId, userId)
        .eq(OrderTreasurePool::getItemType, 1)
        .in(OrderTreasurePool::getTeaPoId, collect2);
    List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(que);
    LambdaQueryWrapper<OrderTreasurePool> ques = new LambdaQueryWrapper<>();
    ques.eq(OrderTreasurePool::getItemType, 1)
        .in(OrderTreasurePool::getTeaPoId, collect2)
        .eq(OrderTreasurePool::getPayType, -1);
    List<OrderTreasurePool> allOrder = orderTreasurePoolDao.selectList(ques);
    for (StoreTreasureDto record : res.getRecords()) {
      Long count = allOrder.stream().filter(o -> o.getTeaPoId().equals(record.getId())).count();
      record.setFrostCount(Integer.valueOf(count + ""));
    }
    LambdaQueryWrapper<SysOrg> q = Wrappers.lambdaQuery(SysOrg.class);
    Set<Long> collect1 =
        res.getRecords().stream().map(StoreTreasureDto::getSysOrgId).collect(Collectors.toSet());
    if (collect1.size() == 0) {
      collect1.add(-11111L);
    }
    q.in(SysOrg::getId, collect1);
    List<SysOrg> sysOrgs = sysOrgDao.selectList(q);
    Map<Long, SysOrg> sysOrgMap = sysOrgs.stream().collect(Collectors.toMap(SysOrg::getId, o -> o));
    LambdaQueryWrapper<StoreTreIosPrice> qI = Wrappers.lambdaQuery(StoreTreIosPrice.class);
    List<Long> collect3 =
        res.getRecords().stream().map(StoreTreasureDto::getStripId).collect(Collectors.toList());
    collect3.add(-111L);
    qI.in(StoreTreIosPrice::getId, collect3);
    List<StoreTreIosPrice> storeTreIosPrices = storeTreIosPriceDao.selectList(qI);
    Map<Long, StoreTreIosPrice> priceMap =
        storeTreIosPrices.stream().collect(Collectors.toMap(StoreTreIosPrice::getId, o -> o));
    for (StoreTreasureDto record : res.getRecords()) {
      List<OrderTreasurePool> collect =
          orderTreasurePools.stream()
              .filter(o -> o.getTeaPoId().equals(record.getId()))
              .collect(Collectors.toList());
      record.setIsBuy(collect.size() > 0);
      StoreTreasureCheckUser storeTreasureCheckUser =
          storeTreasureCheckUserDao.selectByIdAndUid(userId, record.getId());
      if (storeTreasureCheckUser != null) {
        record.setCheckState(1);
      } else if (record.getUpTime() != null
          && record.getUpTime().getTime() > System.currentTimeMillis()) {
        record.setCheckState(2);
      } else {
        record.setCheckState(-1);
      }
      record.setSysOrg(sysOrgMap.get(record.getSysOrgId()));
      if (record.getTType() == 1) {
        if (record.getDownTime().getTime() < System.currentTimeMillis()) {
          record.setIsDone(2);
        } else if ((record.getCheckTime() != null
                && record.getCheckTime().getTime() < System.currentTimeMillis())
            && record.getDownTime() != null
            && record.getDownTime().getTime() > System.currentTimeMillis()) {
          record.setIsDone(1);
        } else if (record.getCheckTime().getTime() > System.currentTimeMillis()
            && record.getDownTime().getTime() > System.currentTimeMillis()
            && record.getUpTime().getTime() < System.currentTimeMillis()) {
          record.setIsDone(0);
        } else if (record.getUpTime() != null
            && record.getUpTime().getTime() > System.currentTimeMillis()) {
          record.setIsDone(-1);
        }
      }
      if (record.getStripId() != null) {
        record.setStoreTreIosPrice(priceMap.get(record.getStripId()));
      }
    }
    return res;
  }

  // String 可以为任意类型 也可以自定义类型

  public Map<Long, Integer> getCheck(Map<Long, Integer> keyChanceMap, Integer totalCount) {
    Map<Long, Integer> count = new HashMap<>();
    for (int i = 0; i < totalCount; i++) {

      Long item = chanceSelect(keyChanceMap);

      if (count.containsKey(item)) {
        i = i - 1;
      } else {
        count.put(item, 1);
      }
    }

    return count;
  }

  public static Long chanceSelect(Map<Long, Integer> keyChanceMap) {
    if (keyChanceMap == null || keyChanceMap.size() == 0) return null;

    Integer sum = 0;
    for (Integer value : keyChanceMap.values()) {
      sum += value;
    }
    // 从1开始
    Integer rand = new Random().nextInt(sum) + 1;

    for (Map.Entry<Long, Integer> entry : keyChanceMap.entrySet()) {
      rand = rand - entry.getValue();
      // 选中
      if (rand <= 0) {
        return entry.getKey();
      }
    }

    return null;
  }

  @Override
  public void checkTreasure(Long treasureId) {
    log.info("超前申购开始抽签");
    StoreTreasure storeTreasure = storeTreasureDao.selectById(treasureId);
    List<StoreTreasureCheckUser> records = storeTreasureCheckUserDao.selectByTreaId(treasureId);
    if (storeTreasure.getTType() == 1 && records.size() == 0) { // 若有记录说明抽签完成
      LambdaQueryWrapper<OrderTreasurePool> que = Wrappers.lambdaQuery();
      que.eq(OrderTreasurePool::getTeaPoId, treasureId).eq(OrderTreasurePool::getIsJoin, 1);
      List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(que);
      if (orderTreasurePools.size() > 0) {
        List<Long> userIds =
            orderTreasurePools.stream().map(BaseEntity::getCreateId).collect(Collectors.toList());
        List<SysUser> sysUsers = sysUserDao.selectBatchIds(userIds);
        Map<Long, SysUser> vipUsers =
            sysUsers.stream()
                .filter(o -> o.getVipEndTime() != null && o.getVipEndTime().after(new Date()))
                .collect(Collectors.toMap(SysUser::getId, o -> o));
        Map<Long, Integer> keyChanceMap = new HashMap<>();
        for (OrderTreasurePool orderTreasurePool : orderTreasurePools) {
          if (vipUsers.get(orderTreasurePool.getCreateId()) != null) {
            keyChanceMap.put(orderTreasurePool.getId(), 10);
          } else {
            keyChanceMap.put(orderTreasurePool.getId(), 1);
          }
        }
        Map<Long, Integer> checkedOrder = null;
        if (keyChanceMap.keySet().size() > storeTreasure.getSurplusCount()) {
          // 选中的订单
          checkedOrder = getCheck(keyChanceMap, storeTreasure.getSurplusCount());
        } else {
          checkedOrder = keyChanceMap;
        }
        updateCheckOrder(checkedOrder.keySet());
      }
    }
  }

  @Autowired private OrderVipDao orderVipDao;
  @Autowired private SysDictonaryDao sysDictonaryDao;

  @Override
  public Object buyVip(BuyVipParam dto, UserDto userData) {
    SysUser sysUser = sysUserDao.selectById(userData.getId());
    String vip_total_count = sysDictonaryDao.selectByAlias("vip_total_count");
    int integer = Integer.parseInt(vip_total_count);
    if (integer <= 0) throw new RuntimeException("会员数量不足");
    if (sysUser.getVipEndTime() != null
        && sysUser.getVipEndTime().getTime() > System.currentTimeMillis()) {
      throw new RuntimeException("已是会员，无需重复购买");
    } else {
      String vip_price = sysDictonaryDao.selectByAlias("vip_price");
      OrderVip orderVip = new OrderVip();
      orderVip.setUId(userData.getId());
      orderVip.setTradeNo(UUID.randomUUID().toString().replaceAll("-", ""));
      orderVip.setPayTotal(new BigDecimal(vip_price));
      orderVip.setPayType(dto.getPayType());
      orderVip.setOrderFlag(0);
      orderVip.setCreateId(userData.getId());
      orderVip.setCreateTime(new Date());
      orderVipDao.insert(orderVip);
      RBlockingQueue<Object> blockingFairQueue =
          redissonClient.getBlockingQueue("delay_queue_call");
      RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
      delayedQueue.offer("vip_order_" + orderVip.getId(), 15, TimeUnit.MINUTES);
      //      redisTemplate
      //          .opsForValue()
      //          .set("vip_order_" + orderVip.getId(), orderVip.getTradeNo(), 15,
      // TimeUnit.MINUTES);
      switch (dto.getPayType()) {
          // 0微信1支付宝2applePay3余额支付
        case 0:
          if (dto.getFlagChnl() != null && "3".equals(dto.getFlagChnl())) {
            return getVipWeixinH5PayResponse(
                orderVip.getTradeNo(), orderVip.getPayTotal(), userData, "vip充值购买");
          } else {
            return getVipWeixinPayResponse(
                orderVip.getTradeNo(), orderVip.getPayTotal(), userData, "vip充值购买");
          }

        case 1:
          if (dto.getFlagChnl() != null && "3".equals(dto.getFlagChnl())) {
            return getVipAlipayTradeH5PayResponse(
                "vip充值购买", userData, orderVip.getTradeNo(), orderVip.getPayTotal());
          } else {
            return getVipAlipayTradeAppPayResponse(
                "vip充值购买", userData, orderVip.getTradeNo(), orderVip.getPayTotal());
          }
        case 2:
          // TODO 苹果支付订单
          return null;
        case 3:
          //          if (sysUser.getBalance().compareTo(new BigDecimal(vip_price)) >= 0) {
          //            sysUser.setBalance(sysUser.getBalance().subtract(new
          // BigDecimal(vip_price)));
          //            sysUserDao.updateById(sysUser);
          //            // 添加流水
          //            OrderTreasurePool tmp = new OrderTreasurePool();
          //            tmp.setCreateId(userData.getId());
          //            tmp.setId(orderVip.getId());
          //            tmp.setTotalPrice(new BigDecimal(vip_price));
          //            tmp.setPayType(dto.getPayType());
          //            addConBalanceRecord(tmp, 5);
          //            String vip_days = sysDictonaryDao.selectByAlias("vip_days");
          //            int vipDay = Integer.parseInt(vip_days);
          //            if (sysUser.getVipEndTime() == null) {
          //              Calendar instance = Calendar.getInstance();
          //              instance.add(Calendar.DAY_OF_MONTH, vipDay);
          //              sysUser.setVipEndTime(instance.getTime());
          //              sysUserDao.updateById(sysUser);
          //            } else {
          //              if (sysUser.getVipEndTime().getTime() > System.currentTimeMillis()) {
          //                Calendar instance = Calendar.getInstance();
          //                instance.setTime(sysUser.getVipEndTime());
          //                instance.add(Calendar.DAY_OF_MONTH, vipDay);
          //                sysUser.setVipEndTime(instance.getTime());
          //                sysUserDao.updateById(sysUser);
          //              } else {
          //                Calendar instance = Calendar.getInstance();
          //                instance.add(Calendar.DAY_OF_MONTH, vipDay);
          //                sysUser.setVipEndTime(instance.getTime());
          //                sysUserDao.updateById(sysUser);
          //              }
          //            }
          //            orderVip.setOrderFlag(1);
          //            orderVip.setUpdateTime(new Date());
          //            orderVipDao.updateById(orderVip);
          //            integer = integer - 1;
          //            sysDictonaryDao.updateByAlias("vip_total_count", integer);
          //
          //            return "success";
          //          } else {
          //            throw new RuntimeException("余额不足请充值后再试");
          //          }
          throw new RuntimeException("支付方式错误");
        case 4:
          return vipPayOrder(dto, orderVip, "会员购买", sysUser);
        default:
          throw new RuntimeException("支付方式异常");
      }
    }
  }

  @Autowired private RedissonClient redissonClient;

  private Object vipPayOrder(BuyVipParam tmpDto, OrderVip orderVip, String title, SysUser user) {
    SimpleDateFormat sp = new SimpleDateFormat("yyyyMMddHHmmss");
    InsertLianLianOrder.RiskItem riskItem = new InsertLianLianOrder.RiskItem();
    riskItem.setFrms_ware_category("1001"); // 虚拟卡销售 1001  5999
    riskItem.setUser_info_mercht_userno(orderVip.getCreateId() + "");
    riskItem.setUser_info_bind_phone(user.getPhone());
    riskItem.setUser_info_dt_register(
        sp.format(user.getCreateTime() == null ? new Date() : user.getCreateTime()));
    riskItem.setGoods_name(title);
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
    order.setUser_id(orderVip.getCreateId() + "");
    order.setBusi_partner("109001");
    order.setNo_order(orderVip.getTradeNo());

    order.setDt_order(sp.format(new Date()));
    order.setMoney_order(orderVip.getPayTotal().doubleValue() + "");
    order.setNotify_url(ResourcesUtil.getProperty("lianlianpay_vip_order_notify"));
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
    String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, url);
    return JSONObject.parseObject(resJson);
  }

  @Override
  public OrderVipResult orderCollectData(PageSelectParam<OrderVipSelectParam> param) {
    OrderVipResult result = new OrderVipResult();
    IPage<OrderVipUserInfo> list =
        orderVipDao.orderCollectData(
            new Page<>(param.getPageNum(), param.getPageSize()), param.getSelectParam());
    BigDecimal total = new BigDecimal(0);
    if (list.getRecords().size() > 0) {
      double sum =
          list.getRecords().stream()
              .mapToDouble(o -> o.getPayTotal() == null ? 0 : o.getPayTotal().doubleValue())
              .sum();
      total = new BigDecimal(sum);
    }
    result.setList(list.getRecords());
    result.setTotal(total);
    return result;
  }

  public String noifitySD(SdNotifyBody param) {
    log.info("noifitySD-in:{}", param);
    try {
      String outtradeno = param.getOrderCode();
      String transactionId = param.getTradeNo();

      log.info("SD异步通知参数：" + param);
      return updateOrderByNotify(outtradeno, transactionId, 4);
    } catch (Exception e) {
      log.error("noifityWXin-error:{}", e);
    }
    return null;
  }

  @Override
  public void sdRecharge(SdNotifyBody param) {
    try {
      String outtradeno = param.getOrderCode();
      String transactionId = param.getTradeNo();

      log.info("sd异步通知参数：" + param);
      updateBalanceByNotify(outtradeno, transactionId, 4);
    } catch (Exception e) {
      log.error("noifityWXin-error:{}", e);
    }
  }

  @Override
  public BindBoxOrderCheckParam bindBoxOrderCheck(Long id) {
    OrderTreasurePool orderTreasurePool = orderTreasurePoolDao.selectById(id);
    if (orderTreasurePool == null) {
      throw new RuntimeException("订单id异常");
    } else if (orderTreasurePool.getOrderFlag() == 0) {
      throw new RuntimeException("订单尚未支付成功");
    } else if (orderTreasurePool.getOrderFlag() == -1) {
      throw new RuntimeException("订单已取消");
    } else if (orderTreasurePool.getOrderFlag() == 2) {
      BindBoxOrderCheckParam res = new BindBoxOrderCheckParam();
      StoreTreasure storeTreasure = storeTreasureDao.selectById(orderTreasurePool.getTeaPoId());
      res.setOrderId(id);
      res.setIndexImgPath(storeTreasure.getIndexImgPath());
      res.setTreasureTitle(storeTreasure.getTreasureTitle());
      res.setTNum(storeTreasure.getTNum());
      return res;
    }
    throw new RuntimeException("订单状态异常");
  }

  @Override
  public IPage<TopItem> consumptionCollect(Page page, Integer type) {
    return orderTreasurePoolDao.consumptionCollect(page, type);
  }

  @Override
  public IPage<TopItem> inviteCollect(Page page, Integer selectParam) {
    return orderTreasurePoolDao.inviteCollect(page, selectParam);
  }

  @Override
  public TopResponse<IPage<TopItem>> updateConsumptionTopAndTotal(
      TopResponse<IPage<TopItem>> success, UserDto userData, Integer type) {
    IPage<TopItem> topItemIPage =
        orderTreasurePoolDao.consumptionCollect(new Page(1, 999999999999L), type);
    for (int i = 0; i < topItemIPage.getRecords().size(); i++) {
      if (topItemIPage.getRecords().get(i) != null) {
        TopItem topItem = topItemIPage.getRecords().get(i);
        if (topItem.getUserId().equals(userData.getId())) {
          success.setTopNum(i + 1);
          success.setTotal(topItem.getTotal());
          return success;
        }
      }
    }
    success.setTotal(new BigDecimal(0));
    success.setTopNum(-1);
    return success;
  }

  @Override
  public TopResponse<IPage<TopItem>> updateInviteCollectTopAndTotal(
      TopResponse<IPage<TopItem>> success, UserDto userData, Integer type) {
    IPage<TopItem> topItemIPage =
        orderTreasurePoolDao.inviteCollect(new Page(1, 999999999999L), type);
    for (int i = 0; i < topItemIPage.getRecords().size(); i++) {
      if (topItemIPage.getRecords().get(i) != null) {
        TopItem topItem = topItemIPage.getRecords().get(i);
        if (topItem.getUserId().equals(userData.getId())) {
          success.setTopNum(i + 1);
          success.setTotal(topItem.getTotal());
          return success;
        }
      }
    }
    success.setTotal(new BigDecimal(0));
    success.setTopNum(-1);
    return success;
  }

  @Override
  public IPage<SysUser> orderTreaCopy(Page page, OrderTreaCopyParam selectParam) {
    if (selectParam.getStreaIds() == null) {
      return new Page<SysUser>();
    }
    if (selectParam.getStreaIds().size() == 0) {
      return new Page<SysUser>();
    }
    List<StoreTreasure> storeTreasures = storeTreasureDao.selectBatchIds(selectParam.getStreaIds());
    //    List<String> titles =
    //
    // storeTreasures.stream().map(StoreTreasure::getTreasureTitle).collect(Collectors.toList());
    //    LambdaQueryWrapper<OrderTreasurePool> que = Wrappers.lambdaQuery(OrderTreasurePool.class);
    //    if (selectParam.getStreaIds() != null && selectParam.getStreaIds().size() > 0) {
    //      que.in(OrderTreasurePool::getTeaPoId, selectParam.getStreaIds());
    //    }
    //    que.eq(OrderTreasurePool::getOrderFlag, 2);
    //    Set<Long> allUserIds = orderTreasurePoolDao.selectByTitles(titles);
    //
    //    //    LambdaQueryWrapper<StoreTreasureRecord> ques =
    //    // Wrappers.lambdaQuery(StoreTreasureRecord.class);
    //    //    ques.in(StoreTreasureRecord::getStrId, selectParam.getStreaIds());
    //    //    ques.isNotNull(StoreTreasureRecord::getStrNum);
    //    //    List<StoreTreasureRecord> storeTreasureRecords =
    // storeTreasureRecordDao.selectList(ques);
    //    //    Set<Long> allUserIds =
    //    //
    //    //
    // orderTreasurePools.stream().map(OrderTreasurePool::getCreateId).collect(Collectors.toSet());
    //    //    Set<Long> allUserIds =
    //    //        storeTreasureRecords.stream()
    //    //            .map(StoreTreasureRecord::getUserId)
    //    //            .collect(Collectors.toSet());
    //    //    List<Long> real = new ArrayList<>();
    //    //    if (selectParam.getStreaIds() != null && selectParam.getStreaIds().size() > 0) {
    //    //      for (Long tmpUserId : allUserIds) {
    //    //        //        Set<OrderTreasurePool> tmpUserOrders =
    //    //        //            orderTreasurePools.stream()
    //    //        //                .filter(o -> o.getCreateId().compareTo(tmpUserId) == 0)
    //    //        //                .collect(Collectors.toSet());
    //    //        //        Set<Long> allBuyTeaPoIds =
    //    //        //
    //    //        //
    //    // tmpUserOrders.stream().map(OrderTreasurePool::getTeaPoId).collect(Collectors.toSet());
    //    //        //        boolean flag = true;
    //    //        //        for (Long streaId : selectParam.getStreaIds()) {
    //    //        //          if (flag && !allBuyTeaPoIds.contains(streaId)) {
    //    //        //            flag = false;
    //    //        //          }
    //    //        //        }
    //    //        //        if (flag) {
    //    //        real.add(tmpUserId);
    //    //        //        }
    //    //      }
    //    //    } else {
    //    //      real = new ArrayList<>(allUserIds);
    //    //    }
    //    LambdaQueryWrapper<SysUser> userQue = Wrappers.lambdaQuery(SysUser.class);
    //    allUserIds.add(-111111L);
    //    userQue.in(SysUser::getId, allUserIds);
    //    Page<SysUser> result = sysUserDao.selectPage(new Page(1, 999999999999L), userQue);
    //    result
    //        .getRecords()
    //        .forEach(
    //            o -> {
    //              MyTreasure noCheck = new MyTreasure();
    //              noCheck.setIsCheck(0);
    //              UserDto userData = new UserDto();
    //              userData.setId(o.getId());
    //              List<MyTreasure> mytreasure = storeProPoolService.getMytreasure(noCheck,
    // userData);
    //              List<MyTreasure> collect1 =
    //                  mytreasure.stream()
    //                      .filter(t -> titles.contains(t.getStoreTreasure().getTreasureTitle()))
    //                      .collect(Collectors.toList());
    //              Set<String> titleSet =
    //                  collect1.stream().map(MyTreasure::getTitle).collect(Collectors.toSet());
    //              //              MyTreasure noCheck1 = new MyTreasure();
    //              //              noCheck1.setIsCheck(1);
    //              //              List<MyTreasure> mytreasures =
    //              // storeProPoolService.getMytreasure(noCheck1, userData);
    //
    //              //              List<MyTreasure> collect =
    //              //                  mytreasures.stream()
    //              //                      .filter(t ->
    //              // titles.contains(t.getStoreTreasure().getTreasureTitle()))
    //              //                      .collect(Collectors.toList());
    //              //              titleSet.addAll(
    //              //
    //              // collect.stream().map(MyTreasure::getTitle).collect(Collectors.toSet()));
    //              Set<String> tmpSet = new HashSet<>();
    //              for (StoreTreasure storeTreasure : storeTreasures) {
    //                if (titleSet.contains(storeTreasure.getTreasureTitle())) {
    //                  tmpSet.add(storeTreasure.getTreasureTitle());
    //                }
    //              }
    //              if (tmpSet.size() == storeTreasures.size()) {
    //                o.setHasCount(new Integer(collect1.size() + ""));
    //              } else {
    //                o.setHasCount(0);
    //              }
    //            });
    List<CollectRecord> allCollectRecord = new ArrayList<>();
    for (StoreTreasure storeTreasure : storeTreasures) {
      List<String> range = redisTemplate.opsForList().range(storeTreasure.getId() + "", 0L, 0);
      if (range != null && range.size() > 0) {
        String stre = range.get(0);
        CollectRecord collectRecord = JSONObject.parseObject(stre, CollectRecord.class);
        allCollectRecord.add(collectRecord);
      }
    }
    List<Long> realUserIds = new ArrayList<>();
    for (int i = 0; i < allCollectRecord.size(); i++) {
      if (i == 0) {
        List<Long> tmpUIds =
            allCollectRecord.get(i).getTmpUserInfoList().stream()
                .map(TmpUserInfo::getUId)
                .collect(Collectors.toList());
        realUserIds.addAll(tmpUIds);
      } else {
        List<Long> collect =
            allCollectRecord.get(i).getTmpUserInfoList().stream()
                .filter(realUserIds::contains)
                .map(TmpUserInfo::getUId)
                .collect(Collectors.toList());
        realUserIds = collect;
      }
    }
    List<SysUser> sysUsers = sysUserDao.selectBatchIds(realUserIds);
    for (SysUser sysUser : sysUsers) {
      final Integer[] count = {0};
      allCollectRecord.stream()
          .forEach(
              o -> {
                TmpUserInfo tmpUserInfo =
                    o.getTmpUserInfoList().stream()
                        .filter(t -> t.getUId().equals(sysUser.getId()))
                        .findFirst()
                        .get();
                count[0] = count[0] + tmpUserInfo.getCount();
              });
      sysUser.setHasCount(count[0]);
    }

    Page<SysUser> realResult = new Page<>();
    if (sysUsers.size() > 0) {
      Integer count = sysUsers.size();
      Long fromIndex = (page.getCurrent() - 1) * page.getSize();
      Long toIndex = page.getCurrent() * page.getSize();
      if (toIndex > count) {
        toIndex = Long.valueOf(count);
      }
      List<SysUser> pageList = sysUsers.subList(fromIndex.intValue(), toIndex.intValue());
      realResult.setRecords(pageList);
      realResult.setTotal(sysUsers.size());
    }
    return realResult;
  }

  @Override
  public void orderTreaCopyCollect() {
    RBlockingQueue<Object> blockingFairQueue = redissonClient.getBlockingQueue("delay_queue_call");
    RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
    delayedQueue.offer("order_trea_copy_collect", 5, TimeUnit.SECONDS);
  }

  @Override
  public void orderTreaCopyCollectRedisDalyed() {
    // 用户持有藏品快照统计到redis中
    List<StoreTreasureRecord> allStoreTreaSureRecord =
        storeTreasureRecordDao.selectList(Wrappers.lambdaQuery(StoreTreasureRecord.class));
    Set<Long> allStrIds =
        allStoreTreaSureRecord.stream()
            .map(StoreTreasureRecord::getStrId)
            .collect(Collectors.toSet());
    for (Long allStrId : allStrIds) {
      CollectRecord collectRecord = new CollectRecord();
      StoreTreasure storeTreasure = storeTreasureDao.selectById(allStrId);
      if (storeTreasure != null) {
        collectRecord.setStrTitle(storeTreasure.getTreasureTitle());
        collectRecord.setStrId(allStrId);
        List<TmpUserInfo> tmpUserInfoList = new ArrayList<>();
        List<StoreTreasureRecord> tmpRecords =
            allStoreTreaSureRecord.stream()
                .filter(o -> o.getStrId().equals(allStrId))
                .collect(Collectors.toList());
        Set<Long> tmpUserId =
            tmpRecords.stream().map(StoreTreasureRecord::getUserId).collect(Collectors.toSet());
        for (Long userId : tmpUserId) {
          long count =
              tmpRecords.stream()
                  .filter(o -> o.getUserId() != null && o.getUserId().equals(userId))
                  .count();
          TmpUserInfo tmpUserInfo = new TmpUserInfo();
          tmpUserInfo.setUId(userId);
          tmpUserInfo.setCount(Math.toIntExact(count));
          tmpUserInfoList.add(tmpUserInfo);
        }
        collectRecord.setTmpUserInfoList(tmpUserInfoList);
        redisTemplate
            .opsForList()
            .leftPush(collectRecord.getStrId() + "", JSONObject.toJSONString(collectRecord));
      }
    }
    log.info("统计缓存完成");
  }

  @Override
  public OrderTreasurePool selectById(Long id) {
    return orderTreasurePoolDao.selectById(id);
  }

  private void updateCheckOrder(Set<Long> keySet) {
    List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectBatchIds(keySet);
    List<Long> tmpList = new ArrayList<>();
    orderTreasurePools =
        orderTreasurePools.stream()
            .filter(
                o -> {
                  if (!tmpList.contains(o.getCreateId())) {
                    tmpList.add(o.getCreateId());
                    return true;
                  } else {
                    return false;
                  }
                })
            .collect(Collectors.toList());
    for (OrderTreasurePool orderTreasurePool : orderTreasurePools) {
      orderTreasurePool.setCheckFlag(1);
      orderTreasurePool.setOrderFlag(2);
      orderTreasurePool.setUpdateTime(new Date());
      orderTreasurePoolDao.updateById(orderTreasurePool);
      StoreTreasureCheckUser storeTreasureCheckUser = new StoreTreasureCheckUser();
      storeTreasureCheckUser.setUId(orderTreasurePool.getCreateId());
      storeTreasureCheckUser.setStoreTreasureId(orderTreasurePool.getTeaPoId());
      storeTreasureCheckUser.setCreateId(orderTreasurePool.getCreateId());
      storeTreasureCheckUser.setCreateTime(new Date());
      storeTreasureCheckUserDao.insert(storeTreasureCheckUser);
    }
  }

  @Autowired private ThreadPoolTaskExecutor asyncServiceExecutor;
  @Autowired private SysBeforeUserDao sysBeforeUserDao;
  @Autowired private StoreTreasureRecordDao storeTreasureRecordDao;

  @Override
  public StoreTreasureRecord getEmptyTreaRecord(Long strId) {
    LambdaQueryWrapper<StoreTreasureRecord> qu = Wrappers.lambdaQuery(StoreTreasureRecord.class);
    qu.eq(StoreTreasureRecord::getStrId, strId);
    qu.isNull(StoreTreasureRecord::getUserId);
    qu.orderByAsc(StoreTreasureRecord::getId);
    List<StoreTreasureRecord> storeTreasureRecords = storeTreasureRecordDao.selectList(qu);
    //    StoreTreasure storeTreasure = storeTreasureDao.selectById(strId);
    if (storeTreasureRecords.size() == 0) {
      throw new RuntimeException("暂无可分配藏品编号请稍后再试");
      //      qu = Wrappers.lambdaQuery(StoreTreasureRecord.class);
      //      qu.eq(StoreTreasureRecord::getStrId, strId);
      //      storeTreasureRecords = storeTreasureRecordDao.selectList(qu);
      //      if (storeTreasureRecords.size() > 0) {
      //        StoreTreasureRecord record = new StoreTreasureRecord();
      //        record.setStrId(strId);
      //        record.setStrNum(storeTreasureRecords.size() + 1 + "");
      //        record.setCreateId(storeTreasure.getCreateId());
      //        record.setCreateTime(new Date());
      //        record.setState(1);
      //        storeTreasureRecordDao.insert(record);
      //        return record;
      //      } else {
      //
      //        int length = storeTreasure.getTotalCount().toString().length();
      //        StoreTreasureRecord tmp = null;
      //        for (Integer i = 0; i < storeTreasure.getTotalCount(); i++) {
      //          // 添加藏品记录
      //
      //          StoreTreasureRecord record = new StoreTreasureRecord();
      //          record.setStrId(storeTreasure.getId());
      //          record.setStrNum(String.format("%0" + length + "d", i));
      //          record.setCreateId(storeTreasure.getCreateId());
      //          record.setCreateTime(new Date());
      //          record.setState(1);
      //          if (tmp == null)
      //            tmp =
      //                JSONObject.parseObject(JSONObject.toJSONString(record),
      // StoreTreasureRecord.class);
      //          storeTreasureRecordDao.insert(record);
      //        }
      //        return tmp;
      //      }
    } else {
      Random r = new Random();
      int index = r.nextInt(storeTreasureRecords.size());
      return storeTreasureRecords.get(index);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public OrderApiResponse save(OrderTreasurePoolAddDto tmpDto, String ip) {
    log.info("==================用户准备下单");
    if (tmpDto.getTotalPrice().doubleValue() == 0) {
      tmpDto.setPayType(3);
    }

    SysUser sysUser = sysUserDao.selectById(tmpDto.getUserId());
    if (!"1".equals(sysUser.getIsTrue())) {
      throw new RuntimeException("请实名认证后在进行购买)");
    }
    if (tmpDto.getPayType() != 4) {
      if (Strings.isNotEmpty(tmpDto.getPass())) {
        if (sysUser.getOperationPass() == null) {
          throw new RuntimeException("用户暂未设置操作密码");
        }
        if (!DigestUtils.md5Hex(tmpDto.getPass()).equals(sysUser.getOperationPass())) {
          throw new RuntimeException("密码不正确");
        }
      } else {
        throw new RuntimeException("密码不可为空");
      }
    }
    StoreTreasure storeTreasure = storeTreasureDao.selectById(tmpDto.getTeaPoId());
    //    String bsn_flag = sysDictonaryDao.selectByAlias("bsn_flag");
    //    if (bsn_flag != null && bsn_flag.equals("1")) {
    if (storeTreasure.getState() != 1) {
      throw new RuntimeException("对不起该藏品暂未上链或已下架，故无法交易");
    }
    //    }
    if (storeTreasure.getFromUser() != null && storeTreasure.getFromUser() == 1) {
      tmpDto.setItemType(2);
    } else if (storeTreasure.getTType() == 3) {
      tmpDto.setItemType(3);
    } else {
      tmpDto.setItemType(0);
    }
    log.info("==================用户准备流转中心下单,纠正后请求参数：{}", tmpDto);
    //    try {
    //      if (tmpDto.getItemType() == 2) {
    //        Boolean flag = true;
    //        String whitePhone = sysDictonaryDao.selectByAlias("whitePhone");
    //        if (whitePhone != null && whitePhone.contains(sysUser.getPhone())) {
    //          flag = false;
    //        }
    //        if (flag) {
    //          SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
    //          String format = simp.format(new Date(new Date().getTime() - 24 * 3600 * 1000L));
    //          Date parse = simp.parse(format);
    //          LambdaQueryWrapper<OrderTreasurePool> orderQues =
    //              Wrappers.lambdaQuery(OrderTreasurePool.class);
    //          orderQues.eq(BaseEntity::getCreateId, sysUser.getId());
    //          orderQues.ge(BaseEntity::getCreateTime, parse);
    //          ArrayList<Integer> integers = new ArrayList<>();
    //          integers.add(0);
    //          integers.add(-1);
    //          orderQues.in(OrderTreasurePool::getOrderFlag, integers);
    //          List<OrderTreasurePool> allO =
    //              orderTreasurePoolDao.selectUnDoOrder(sysUser.getId(), parse);
    //          if (allO.size() >= 3) {
    //            throw new RuntimeException("超出代付款，取消订单，总和每24小时上限");
    //          }
    //        }
    //      }
    //    } catch (ParseException e) {
    //      throw new RuntimeException(e);
    //    }

    // region 藏品首发下单
    if (tmpDto.getItemType() == 0) {
      if (storeTreasure.getTType() != 0) {
        throw new RuntimeException("下单类型与藏品类型不符");
      }
      if (storeTreasure.getState() != 1) {
        throw new RuntimeException("藏品已被平台冻结暂无法下单");
      }
      try {
        StoreTreasureRecord emptyTreaRecord = getEmptyTreaRecord(tmpDto.getTeaPoId());
      } catch (Exception e) {
        storeTreasure.setSurplusCount(0);
        storeTreasure.setFrostCount(0);
        storeTreasureDao.updateById(storeTreasure);
        throw new RuntimeException("库存不足");
      }

      if (storeTreasure.getSurplusCount() >= 1) {

        // 藏品首发订单
        // 校验请求参数
        BigDecimal recordTotal = new BigDecimal("0");
        if ("1".equals(tmpDto.getFlagChnl()) && storeTreasure.getStripId() != null) {
          StoreTreIosPrice storeTreIosPrice =
              storeTreIosPriceDao.selectById(storeTreasure.getStripId());
          if (storeTreIosPrice != null) {
            recordTotal = storeTreIosPrice.getPrice();
            tmpDto.setPayType(2);
          } else {
            throw new RuntimeException("对不起，藏品信息异常，请刷新页面再试");
          }
        } else {
          recordTotal = storeTreasure.getPrice();
        }
        if (storeTreasure.getUpTime().getTime() > System.currentTimeMillis()) {
          LambdaQueryWrapper<SysBeforeUser> sysBeforeUser =
              Wrappers.lambdaQuery(SysBeforeUser.class);
          sysBeforeUser.eq(SysBeforeUser::getPhone, sysUser.getPhone());
          sysBeforeUser.eq(SysBeforeUser::getStreaId, tmpDto.getTeaPoId());
          List<SysBeforeUser> sysBeforeUsers = sysBeforeUserDao.selectList(sysBeforeUser);
          if (sysBeforeUsers.size() > 0) {
            String before_seconds = sysDictonaryDao.selectByAlias("before_seconds");
            if (storeTreasure.getUpTime().getTime() - Long.parseLong(before_seconds) * 1000
                > System.currentTimeMillis()) {
              throw new RuntimeException("暂未开售");
            } else {
              // 超前购用户 限购校验普通校验
              if (sysBeforeUsers.get(0).getRuleCount() != null
                  && sysBeforeUsers.get(0).getRuleCount() != 0) {
                ArrayList<Integer> orderFlags = new ArrayList<>();
                orderFlags.add(0);
                orderFlags.add(2);
                LambdaQueryWrapper<OrderTreasurePool> orderQue =
                    Wrappers.lambdaQuery(OrderTreasurePool.class);
                orderQue
                    .eq(BaseEntity::getCreateId, sysUser.getId())
                    .in(OrderTreasurePool::getOrderFlag, orderFlags)
                    .eq(OrderTreasurePool::getItemType, 0)
                    .eq(OrderTreasurePool::getTeaPoId, tmpDto.getTeaPoId());
                List<OrderTreasurePool> orderTreasurePools =
                    orderTreasurePoolDao.selectList(orderQue);
                if (orderTreasurePools.size() >= sysBeforeUsers.get(0).getRuleCount()) {
                  throw new RuntimeException("已到达限购数量");
                }
              }
            }
          } else {
            throw new RuntimeException("暂未开售");
          }
        } else {
          // 普通用户限购校验
          if (storeTreasure.getRuleCount() != null && storeTreasure.getRuleCount() != 0) {
            ArrayList<Integer> orderFlags = new ArrayList<>();
            orderFlags.add(0);
            orderFlags.add(2);
            LambdaQueryWrapper<OrderTreasurePool> orderQue =
                Wrappers.lambdaQuery(OrderTreasurePool.class);
            orderQue
                .eq(BaseEntity::getCreateId, sysUser.getId())
                .in(OrderTreasurePool::getOrderFlag, orderFlags)
                .eq(OrderTreasurePool::getItemType, 0)
                .eq(OrderTreasurePool::getTeaPoId, tmpDto.getTeaPoId());
            List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(orderQue);
            if (orderTreasurePools.size() >= storeTreasure.getRuleCount()) {
              throw new RuntimeException("已到达限购数量");
            }
          }
        }

        if (recordTotal.compareTo(tmpDto.getTotalPrice()) == 0) {
          // 创建订单
          OrderTreasurePool orderProduct = addTreasureOrder(tmpDto, true, null, 0);
          // 创建商品快照
          addOrderTreasurePoolCopy(storeTreasure, orderProduct);
          // 冻结数量
          storeTreasure.setFrostCount(storeTreasure.getFrostCount() + 1);
          storeTreasure.setSurplusCount(storeTreasure.getSurplusCount() - 1);
          storeTreasure.setUpdateTime(new Date());
          storeTreasureDao.updateById(storeTreasure);
          switch (tmpDto.getPayType()) {
              // 0微信1支付宝2applePay3余额支付
            case 0:
              //              if (tmpDto.getFlagChnl() != null && "3".equals(tmpDto.getFlagChnl()))
              // {
              //                return OrderApiResponse.success(
              //                    getWeixinPayH5Response(orderProduct,
              // storeTreasure.getTreasureTitle()));
              //              } else {
              //                return OrderApiResponse.success(
              //                    getWeixinPayResponse(orderProduct,
              // storeTreasure.getTreasureTitle()));
              //              }
              throw new RuntimeException("支付方式错误");
            case 1:
              //              if (tmpDto.getFlagChnl() != null && "3".equals(tmpDto.getFlagChnl()))
              // {
              //                return OrderApiResponse.success(
              //                    getAlipayTradeH5PayResponse(orderProduct,
              // storeTreasure.getTreasureTitle()));
              //              } else {
              //                return OrderApiResponse.success(
              //                    getAlipayTradeAppPayResponse(orderProduct,
              // storeTreasure.getTreasureTitle()));
              //              }
              throw new RuntimeException("支付方式错误");
            case 2:
              // 苹果创建内购订单，去藏品订单列表支付
              //              storeTreasure.setFrostCount(
              //                  storeTreasure.getFrostCount() - orderProduct.getTotalCount());
              //              storeTreasure.setUpdateTime(new Date());
              //              storeTreasureDao.updateById(storeTreasure);
              //              orderProduct.setOrderFlag(0);
              //              orderProduct.setUpdateTime(new Date());
              //              orderTreasurePoolDao.updateById(orderProduct);
              //              OrderApiResponse<OrderTreasurePool> success =
              // OrderApiResponse.success(orderProduct);
              //              success.setOrderId(orderProduct.getId());
              //              return success;
              break;
            case 3:
              // 余额支付
              //              if (sysUser.getBalance().compareTo(recordTotal) >= 0) {
              //                sysUser.setBalance(sysUser.getBalance().subtract(recordTotal));
              //                sysUserDao.updateById(sysUser);
              //                storeTreasure.setFrostCount(
              //                    storeTreasure.getFrostCount() - orderProduct.getTotalCount());
              //                storeTreasure.setUpdateTime(new Date());
              //                storeTreasureDao.updateById(storeTreasure);
              //                // 添加流水
              //                addConBalanceRecord(orderProduct, 4);
              //                SysUserBackpack sysUserBackpacks = new SysUserBackpack();
              //                StoreTreasureRecord emptyTreaRecord =
              // getEmptyTreaRecord(orderProduct.getTeaPoId());
              //
              // emptyTreaRecord.setOrderFingerprint(orderProduct.getOrderFingerprint());
              //                emptyTreaRecord.setUserId(orderProduct.getCreateId());
              //                emptyTreaRecord.setUpdateTime(new Date());
              //                storeTreasureRecordDao.updateById(emptyTreaRecord);
              //                LambdaQueryWrapper<OrderTreasureRecord> orderTreasureReQue =
              //                    Wrappers.lambdaQuery(OrderTreasureRecord.class);
              //                orderTreasureReQue.eq(
              //                    OrderTreasureRecord::getOrderTreasurePoolId,
              // orderProduct.getId());
              //                List<OrderTreasureRecord> orderTreasureRecords =
              //                    orderTreasureRecordDao.selectList(orderTreasureReQue);
              //                if (orderTreasureRecords.size() > 0) {
              //                  orderTreasureRecords.get(0).setTNum(emptyTreaRecord.getStrNum());
              //                  orderTreasureRecordDao.updateById(orderTreasureRecords.get(0));
              //                }
              //                transDDc(
              //                    "iaa1tvt9xkw5t2znq687u23mtqvmpk9spakpe5kezx",
              //                    sysUser.getLinkAddress(),
              //                    sysUser,
              //                    storeTreasure,
              //                    sysUserBackpacks,
              //                    emptyTreaRecord);
              //                sysUserBackpacks.setUId(orderProduct.getCreateId());
              //                sysUserBackpacks.setSTreasureId(orderProduct.getTeaPoId());
              //                sysUserBackpacks.setOrderTreasurePoolId(orderProduct.getId());
              //
              // sysUserBackpacks.setOrderFingerprint(orderProduct.getOrderFingerprint());
              //                sysUserBackpacks.setFinType(4);
              //                sysUserBackpacks.setBeforeUserId(0L);
              //                sysUserBackpacks.setTreasureFrom(0);
              //                sysUserBackpacks.setAfterUserId(orderProduct.getCreateId());
              //                sysUserBackpacks.setCreateId(orderProduct.getCreateId());
              //                sysUserBackpacks.setCreateTime(new Date());
              //
              //                sysUserBackpackDao.insert(sysUserBackpacks); // 添加出售记录
              //                orderProduct.setOrderFlag(2);
              //                orderProduct.setUpdateTime(new Date());
              //                orderProduct.setPayTime(new Date());
              //                orderTreasurePoolDao.updateById(orderProduct);
              //                return OrderApiResponse.success("success");
              //              } else {
              //                throw new RuntimeException("余额不足请充值后再试");
              //              }
              throw new RuntimeException("支付方式错误");
            case 4:
              //              OrderApiResponse<Object> res =
              //                  OrderApiResponse.success(
              //                      sdPayOrder(tmpDto, orderProduct,
              // storeTreasure.getTreasureTitle(), sysUser));
              //              res.setOrderId(orderProduct.getId());
              OrderApiResponse<Object> res =
                  OrderApiResponse.success(
                      sdPayOrder(
                          tmpDto, orderProduct, storeTreasure.getTreasureTitle(), sysUser, ip));
              res.setOrderId(orderProduct.getId());
              return res;
            default:
              throw new RuntimeException("支付方式异常");
          }
        } else {
          throw new RuntimeException("页面已过期，请刷新后再试");
        }
      } else {
        throw new RuntimeException("库存不足，订单创建失败");
      }
      // endregion
    } else if (tmpDto.getItemType() == 1) {
      throw new RuntimeException("下单类型错误");
    } else if (tmpDto.getItemType() == 3) {
      // 盲盒订单
      tmpDto.setItemType(4);
      if (storeTreasure.getState() != 1) {
        throw new RuntimeException("藏品已被平台冻结暂无法下单");
      }
      if (storeTreasure.getUpTime().getTime() > System.currentTimeMillis()) {
        LambdaQueryWrapper<SysBeforeUser> sysBeforeUser = Wrappers.lambdaQuery(SysBeforeUser.class);
        sysBeforeUser.eq(SysBeforeUser::getPhone, sysUser.getPhone());
        sysBeforeUser.eq(SysBeforeUser::getStreaId, tmpDto.getTeaPoId());
        List<SysBeforeUser> sysBeforeUsers = sysBeforeUserDao.selectList(sysBeforeUser);
        if (sysBeforeUsers.size() > 0) {
          String before_seconds = sysDictonaryDao.selectByAlias("before_seconds");
          if (storeTreasure.getUpTime().getTime() - Long.parseLong(before_seconds) * 1000
              > System.currentTimeMillis()) {
            throw new RuntimeException("暂未开售");
          } else {
            // 超前购用户 限购校验普通校验
            if (sysBeforeUsers.get(0).getRuleCount() != null
                && sysBeforeUsers.get(0).getRuleCount() != 0) {
              List<StoreTreasure> storeTreasures =
                  storeTreasureDao.selectByPid(tmpDto.getTeaPoId());
              Set<Long> ids =
                  storeTreasures.stream().map(StoreTreasure::getId).collect(Collectors.toSet());
              ids.add(-11111L);
              LambdaQueryWrapper<OrderTreasurePool> orderQue =
                  Wrappers.lambdaQuery(OrderTreasurePool.class);
              orderQue
                  .eq(BaseEntity::getCreateId, sysUser.getId())
                  .eq(OrderTreasurePool::getOrderFlag, 2)
                  //                  .eq(OrderTreasurePool::getItemType, 0)
                  .in(OrderTreasurePool::getTeaPoId, ids);
              List<OrderTreasurePool> orderTreasurePools =
                  orderTreasurePoolDao.selectList(orderQue);
              if (orderTreasurePools.size() >= sysBeforeUsers.get(0).getRuleCount()) {
                throw new RuntimeException("已到达限购数量");
              }
            }
          }
        } else {
          throw new RuntimeException("暂未开售");
        }
      } else {
        // 普通用户限购校验
        if (storeTreasure.getRuleCount() != null && storeTreasure.getRuleCount() != 0) {
          List<StoreTreasure> storeTreasures = storeTreasureDao.selectByPid(tmpDto.getTeaPoId());
          Set<Long> ids =
              storeTreasures.stream().map(StoreTreasure::getId).collect(Collectors.toSet());
          ids.add(-11111L);
          LambdaQueryWrapper<OrderTreasurePool> orderQue =
              Wrappers.lambdaQuery(OrderTreasurePool.class);
          orderQue
              .eq(BaseEntity::getCreateId, sysUser.getId())
              .eq(OrderTreasurePool::getOrderFlag, 2)
              //              .eq(OrderTreasurePool::getItemType, 0)
              .in(OrderTreasurePool::getTeaPoId, ids);
          List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(orderQue);
          if (orderTreasurePools.size() >= storeTreasure.getRuleCount()) {
            throw new RuntimeException("已到达限购数量");
          }
        }
      }
      LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery(StoreTreasure.class);
      que.in(StoreTreasure::getPId, storeTreasure.getId());
      que.ne(BaseEntity::getState, -1);

      List<StoreTreasure> allChildren = storeTreasureDao.selectList(que);
      allChildren =
          allChildren.stream().filter(o -> o.getSurplusCount() > 0).collect(Collectors.toList());
      if (allChildren.size() == 0) throw new RuntimeException("该盲盒下已无可用藏品");
      int prizeIndex = getPrizeIndex(allChildren);
      storeTreasure = allChildren.get(prizeIndex);

      // 创建订单
      tmpDto.setTeaPoId(storeTreasure.getId());
      OrderTreasurePool orderProduct = addTreasureOrder(tmpDto, true, null, 3);
      // 创建商品快照
      addOrderTreasurePoolCopy(storeTreasure, orderProduct);
      // 冻结数量
      storeTreasure.setFrostCount(storeTreasure.getFrostCount() + orderProduct.getTotalCount());
      storeTreasure.setSurplusCount(storeTreasure.getSurplusCount() - orderProduct.getTotalCount());
      storeTreasure.setUpdateTime(new Date());
      storeTreasureDao.updateById(storeTreasure);
      tmpDto.setPayType(tmpDto.getTotalPrice().doubleValue() == 0 ? 3 : tmpDto.getPayType());

      switch (tmpDto.getPayType()) {
          // 0微信1支付宝2applePay3余额支付
        case 0:
          if (tmpDto.getFlagChnl() != null && "3".equals(tmpDto.getFlagChnl())) {
            Object weixinPayH5Response =
                getWeixinPayH5Response(orderProduct, storeTreasure.getTreasureTitle());
            OrderApiResponse<Object> success = OrderApiResponse.success(weixinPayH5Response);
            success.setOrderId(orderProduct.getId());
            return success;
          } else {
            Object weixinPayH5Response =
                getWeixinPayResponse(orderProduct, storeTreasure.getTreasureTitle());
            OrderApiResponse<Object> success = OrderApiResponse.success(weixinPayH5Response);
            success.setOrderId(orderProduct.getId());
            return success;
          }
        case 1:
          if (tmpDto.getFlagChnl() != null && "3".equals(tmpDto.getFlagChnl())) {
            OrderApiResponse<Object> success =
                OrderApiResponse.success(
                    getAlipayTradeH5PayResponse(orderProduct, storeTreasure.getTreasureTitle()));
            success.setOrderId(orderProduct.getId());
            return success;
          } else {
            OrderApiResponse<Object> success =
                OrderApiResponse.success(
                    getAlipayTradeAppPayResponse(orderProduct, storeTreasure.getTreasureTitle()));
            success.setOrderId(orderProduct.getId());
            return success;
          }
        case 2:
          //  苹果支付只创建订单，去藏品订单列表支付
          //          storeTreasure.setFrostCount(storeTreasure.getFrostCount() -
          // orderProduct.getTotalCount());
          //          storeTreasure.setUpdateTime(new Date());
          //          storeTreasureDao.updateById(storeTreasure);
          //          // 添加流水
          //          orderProduct.setOrderFlag(0);
          //          orderTreasurePoolDao.updateById(orderProduct);
          //          OrderApiResponse<Object> success =
          // OrderApiResponse.success(orderProduct.getId());
          //          success.setOrderId(orderProduct.getId());
          //          return success;
          break;
        case 3:
          //          if (sysUser.getBalance().compareTo(storeTreasure.getPrice()) >= 0) {
          //            sysUser.setBalance(sysUser.getBalance().subtract(storeTreasure.getPrice()));
          //            sysUserDao.updateById(sysUser);
          //            storeTreasure.setFrostCount(
          //                storeTreasure.getFrostCount() - orderProduct.getTotalCount());
          //            storeTreasure.setUpdateTime(new Date());
          //            storeTreasureDao.updateById(storeTreasure);
          //            // 添加流水
          //            addConBalanceRecord(orderProduct, 4);
          //            orderProduct.setOrderFlag(2);
          //            orderProduct.setPayTime(new Date());
          //            orderTreasurePoolDao.updateById(orderProduct);
          //            SysUserBackpack sysUserBackpacks = new SysUserBackpack();
          //            sysUserBackpacks.setUId(orderProduct.getCreateId());
          //            sysUserBackpacks.setSTreasureId(orderProduct.getTeaPoId());
          //            sysUserBackpacks.setOrderTreasurePoolId(orderProduct.getId());
          //            sysUserBackpacks.setOrderFingerprint(orderProduct.getOrderFingerprint());
          //            sysUserBackpacks.setTreasureFrom(0);
          //            sysUserBackpacks.setFinType(4);
          //            sysUserBackpacks.setBeforeUserId(0L);
          //            sysUserBackpacks.setIsCheck(0);
          //            sysUserBackpacks.setAfterUserId(orderProduct.getCreateId());
          //            sysUserBackpacks.setCreateId(orderProduct.getCreateId());
          //            sysUserBackpacks.setCreateTime(new Date());
          //            StoreTreasureRecord emptyTreaRecord =
          // getEmptyTreaRecord(orderProduct.getTeaPoId());
          //            emptyTreaRecord.setOrderFingerprint(orderProduct.getOrderFingerprint());
          //            emptyTreaRecord.setUserId(orderProduct.getCreateId());
          //            emptyTreaRecord.setUpdateTime(new Date());
          //            storeTreasureRecordDao.updateById(emptyTreaRecord);
          //            LambdaQueryWrapper<OrderTreasureRecord> orderTreasureReQue =
          //                Wrappers.lambdaQuery(OrderTreasureRecord.class);
          //            orderTreasureReQue.eq(
          //                OrderTreasureRecord::getOrderTreasurePoolId, orderProduct.getId());
          //            List<OrderTreasureRecord> orderTreasureRecords =
          //                orderTreasureRecordDao.selectList(orderTreasureReQue);
          //            if (orderTreasureRecords.size() > 0) {
          //              orderTreasureRecords.get(0).setTNum(emptyTreaRecord.getStrNum());
          //              orderTreasureRecordDao.updateById(orderTreasureRecords.get(0));
          //            }
          //            //            WenchangDDC bsnUtils = new WenchangDDC();
          //            transDDc(
          //                "iaa1tvt9xkw5t2znq687u23mtqvmpk9spakpe5kezx",
          //                sysUser.getLinkAddress(),
          //                sysUser,
          //                storeTreasure,
          //                sysUserBackpacks,
          //                emptyTreaRecord);
          //            sysUserBackpackDao.insert(sysUserBackpacks); // 添加购买记录
          //
          //            OrderApiResponse<Object> success1 =
          // OrderApiResponse.success(orderProduct.getId());
          //            success1.setOrderId(orderProduct.getId());
          //            return success1;
          //          } else {
          //            throw new RuntimeException("余额不足请充值后再试");
          //          }
          throw new RuntimeException("支付方式错误");
        case 4:
          OrderApiResponse<Object> success2 =
              OrderApiResponse.success(
                  sdPayOrder(tmpDto, orderProduct, storeTreasure.getTreasureTitle(), sysUser, ip));
          success2.setOrderId(orderProduct.getId());
          return success2;
        default:
          throw new RuntimeException("支付方式异常");
      }
    } else if (tmpDto.getItemType() == 2) {
      // 流转中心下单
      log.info("==================用户准备流转中心下单");
      StoreTreasure storeProPool = storeTreasureDao.selectById(tmpDto.getTeaPoId());
      //      log.info("==================用户准备流转中心下单，藏品：{}", storeProPool);
      if (storeProPool.getState() == 1 && storeProPool.getIsSale() == 0) {
        LambdaQueryWrapper<OrderTreasurePool> orderQ =
            Wrappers.lambdaQuery(OrderTreasurePool.class);
        orderQ.eq(OrderTreasurePool::getOrderFingerprint, storeProPool.getOrderFingerprint());
        orderQ.eq(BaseEntity::getCreateId, sysUser.getId());
        List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(orderQ);
        if (orderTreasurePools.size() > 0) {
          throw new RuntimeException("流转中心中不可购买本人寄售藏品");
        } else if (storeProPool.getCreateId().equals(sysUser.getId())) {
          throw new RuntimeException("流转中心中不可购买本人寄售藏品");
        }
        // 流转中心订单
        // 校验请求参数
        BigDecimal recordTotal = storeProPool.getPrice();
        log.info("===========recordTotal: {}", recordTotal);
        log.info("===========tmpDtoPrice:{}", tmpDto.getTotalPrice());
        if (recordTotal.compareTo(tmpDto.getTotalPrice()) == 0) {
          // 创建订单
          OrderTreasurePool orderProduct = addTreasureOrder(tmpDto, true, null, 1);
          addOrderTreasurePoolCopy(storeProPool, orderProduct);
          storeProPool.setUpdateTime(new Date());
          storeProPool.setIsSale(1);
          storeProPool.setState(-1);
          storeTreasureDao.updateById(storeProPool);
          switch (tmpDto.getPayType()) {
              // 0微信1支付宝2applePay3余额支付
            case 0:
              if (tmpDto.getFlagChnl() != null && "3".equals(tmpDto.getFlagChnl())) {
                return OrderApiResponse.success(
                    getWeixinPayH5Response(orderProduct, storeProPool.getTreasureTitle()));
              } else {
                return OrderApiResponse.success(
                    getWeixinPayResponse(orderProduct, storeProPool.getTreasureTitle()));
              }
            case 1:
              if (tmpDto.getFlagChnl() != null && "3".equals(tmpDto.getFlagChnl())) {
                return OrderApiResponse.success(
                    getAlipayTradeH5PayResponse(orderProduct, storeProPool.getTreasureTitle()));
              } else {
                return OrderApiResponse.success(
                    getAlipayTradeAppPayResponse(orderProduct, storeProPool.getTreasureTitle()));
              }
            case 2:
              // TODO 苹果支付订单
              return null;
            case 3:
              //              if (sysUser.getBalance().compareTo(recordTotal) >= 0) {
              //                sysUser.setBalance(sysUser.getBalance().subtract(recordTotal));
              //                sysUserDao.updateById(sysUser);
              //                storeProPool.setSaleTime(new Date());
              //                storeTreasureDao.updateById(storeProPool);
              //                // 添加购买流水
              //                addConBalanceRecord(orderProduct, 4);
              //
              //                SysUser saler = null;
              //                //              if (storeProPool.getFromType() != 0) {
              //                // 商品来自用户
              //                String consignment_fee =
              // sysDictonaryDao.selectByAlias("consignment_fee");
              //                saler = sysUserDao.selectById(storeProPool.getCreateId());
              //                saler.setBalance(
              //                    saler.getBalance() == null
              //                        ? orderProduct
              //                            .getTotalPrice()
              //                            .multiply(new BigDecimal(1).subtract(new
              // BigDecimal(consignment_fee)))
              //                        : saler
              //                            .getBalance()
              //                            .add(
              //                                orderProduct
              //                                    .getTotalPrice()
              //                                    .multiply(
              //                                        new BigDecimal(1)
              //                                            .subtract(new
              // BigDecimal(consignment_fee)))));
              //                sysUserDao.updateById(saler);
              //                SysUserBackpack sysUserBackpack = new SysUserBackpack();
              //                sysUserBackpack.setUId(saler.getId());
              //                sysUserBackpack.setSTreasureId(orderProduct.getTeaPoId());
              //                sysUserBackpack.setOrderTreasurePoolId(orderProduct.getId());
              //                sysUserBackpack.setTreasureFrom(1);
              //
              // sysUserBackpack.setOrderFingerprint(storeTreasure.getOrderFingerprint());
              //                sysUserBackpack.setFinType(3);
              //                sysUserBackpack.setCreateId(saler.getCreateId());
              //                sysUserBackpack.setCreateTime(new Date());
              //                sysUserBackpack.setBeforeUserId(saler.getId());
              //                sysUserBackpack.setAfterUserId(orderProduct.getCreateId());
              //                sysUserBackpackDao.insert(sysUserBackpack); // 添加出售记录
              //                // 添加出售流水
              //                ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
              //                conBalanceRecord.setUId(saler.getCreateId());
              //                conBalanceRecord.setOrderId(orderProduct.getId() + "");
              //                conBalanceRecord.setOrderType(6);
              //                conBalanceRecord.setTotalPrice(
              //                    orderProduct
              //                        .getTotalPrice()
              //                        .multiply(new BigDecimal(1).subtract(new
              // BigDecimal(consignment_fee))));
              //                conBalanceRecord.setTradingChannel(3);
              //                conBalanceRecord.setCreateId(saler.getCreateId());
              //                conBalanceRecord.setCreateTime(new Date());
              //                log.info("添加出售交易流水信息：" + JSONObject.toJSONString(conBalanceRecord));
              //                conBalanceRecordDao.insert(conBalanceRecord);
              //
              //                LambdaQueryWrapper<StoreTreasureRecord> reQ =
              //                    Wrappers.lambdaQuery(StoreTreasureRecord.class);
              //                reQ.eq(StoreTreasureRecord::getUserId, saler.getId())
              //                    .eq(
              //                        StoreTreasureRecord::getOrderFingerprint,
              //                        storeTreasure.getOrderFingerprint());
              //                List<StoreTreasureRecord> storeTreasureRecords =
              //                    storeTreasureRecordDao.selectList(reQ);
              //                StoreTreasureRecord emptyTreaRecord = storeTreasureRecords.get(0);
              //
              // emptyTreaRecord.setOrderFingerprint(orderProduct.getOrderFingerprint());
              //                emptyTreaRecord.setUserId(orderProduct.getCreateId());
              //                emptyTreaRecord.setUpdateTime(new Date());
              //                storeTreasureRecordDao.updateById(emptyTreaRecord);
              //                LambdaQueryWrapper<OrderTreasureRecord> orderTreasureReQue =
              //                    Wrappers.lambdaQuery(OrderTreasureRecord.class);
              //                orderTreasureReQue.eq(
              //                    OrderTreasureRecord::getOrderTreasurePoolId,
              // orderProduct.getId());
              //                List<OrderTreasureRecord> orderTreasureRecords =
              //                    orderTreasureRecordDao.selectList(orderTreasureReQue);
              //                if (orderTreasureRecords.size() > 0) {
              //                  orderTreasureRecords.get(0).setTNum(emptyTreaRecord.getStrNum());
              //                  orderTreasureRecordDao.updateById(orderTreasureRecords.get(0));
              //                }
              //
              //                SysUserBackpack sysUserBackpack1 = new SysUserBackpack();
              //                sysUserBackpack1.setUId(orderProduct.getCreateId());
              //                sysUserBackpack1.setSTreasureId(orderProduct.getTeaPoId());
              //
              // sysUserBackpack1.setOrderFingerprint(orderProduct.getOrderFingerprint());
              //                sysUserBackpack1.setOrderTreasurePoolId(orderProduct.getId());
              //                sysUserBackpack1.setFinType(4);
              //                sysUserBackpack1.setTreasureFrom(1);
              //                sysUserBackpack1.setCreateId(orderProduct.getCreateId());
              //                sysUserBackpack1.setCreateTime(new Date());
              //                sysUserBackpack1.setBeforeUserId(saler.getCreateId());
              //                sysUserBackpack1.setAfterUserId(orderProduct.getCreateId());
              //                transDDc(
              //                    saler.getLinkAddress(),
              //                    sysUser.getLinkAddress(),
              //                    sysUser,
              //                    storeTreasure,
              //                    sysUserBackpack1,
              //                    emptyTreaRecord);
              //                sysUserBackpackDao.insert(sysUserBackpack1); // 添加购买记录
              //                orderProduct.setOrderFlag(2);
              //                orderProduct.setPayTime(new Date());
              //                orderProduct.setUpdateTime(new Date());
              //                orderTreasurePoolDao.updateById(orderProduct);
              //                return OrderApiResponse.success("success");
              //              } else {
              //                throw new RuntimeException("余额不足请充值后再试");
              //              }
              throw new RuntimeException("支付方式错误");
            case 4:
              log.info("=========准备调用连连");
              return OrderApiResponse.success(
                  sdPayOrder(tmpDto, orderProduct, storeProPool.getTreasureTitle(), sysUser, ip));
            default:
              throw new RuntimeException("支付方式异常");
          }
        } else {
          throw new RuntimeException("页面已过期，请刷新后再试");
        }
      } else {
        throw new RuntimeException("对不起藏品已被他人下单");
      }
    }
    throw new RuntimeException("服务器爆满，稍后再试");
  }
  /**
   * 根据Math.random()产生一个double型的随机数，判断每个奖品出现的概率
   *
   * @param prizes
   * @return random：奖品列表prizes中的序列（prizes中的第random个就是抽中的奖品）
   */
  public static int getPrizeIndex(List<StoreTreasure> prizes) {
    // DecimalFormat df = new DecimalFormat("######0.00");
    int random = -1;
    try {
      // 计算总权重
      double sumWeight = 0;
      for (StoreTreasure p : prizes) {
        sumWeight += p.getTotalCount();
      }

      // 产生随机数
      double randomNumber;
      randomNumber = Math.random();

      // 根据随机数在所有奖品分布的区域并确定所抽奖品
      double d1 = 0;
      double d2 = 0;
      for (int i = 0; i < prizes.size(); i++) {
        d2 += Double.parseDouble(String.valueOf(prizes.get(i).getTotalCount())) / sumWeight;
        if (i == 0) {
          d1 = 0;
        } else {
          d1 += Double.parseDouble(String.valueOf(prizes.get(i - 1).getTotalCount())) / sumWeight;
        }
        if (randomNumber >= d1 && randomNumber <= d2) {
          random = i;
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("生成抽奖随机数出错，出错原因：" + e.getMessage());
    }
    return random;
  }

  private void createDDC(
      SysUser sysUser,
      StoreTreasure storeTreasure,
      OrderTreasurePool orderProduct,
      SysUserBackpack sysUserBackpack,
      Integer i) {
    String bsn_flag = sysDictonaryDao.selectByAlias("bsn_flag");
    if (bsn_flag != null && bsn_flag.equals("1")) {
      WenchangDDC bsnUtil = new WenchangDDC();
      try {
        String transId =
            bsnUtil.safeTransferFrom(
                ResourcesUtil.getProperty("bsn_plate_addr"),
                sysUser.getLinkAddress(),
                new BigInteger(storeTreasure.getDdcId()),
                new BigInteger("1"),
                "".getBytes());
        sysUserBackpack.setTransationId(transId);
      } catch (Exception e) {
        log.info("ddc 转移异常:" + e.getMessage());
        //        if (e.getMessage().contains("account balance is not enough") && i != null && i ==
        // 1) {
        //          throw new RuntimeException("ddc 转移异常，请联系平台管理员");
        //        } else {
        //          bsnUtil.addBalance();
        //          createDDC(sysUser, storeTreasure, orderProduct, sysUserBackpack, 1);
        //        }
      }
    }
  }

  @Override
  public void executeAsync(String transationId, Long sysBackId, String ddc_url) {
    log.info(transationId + "已进入异步线程");
    log.info("获取ddcid异步线程开始, transationId:" + transationId + " sysBackPackId: " + sysBackId);
    try {
      Thread.sleep(15000L);
      BsnUtil bsnUtil = new BsnUtil();
      String ddcidByTxHash4 = bsnUtil.getDDCIDByTxHash4(transationId);
      SysUserBackpack sysUserBackpack = sysUserBackpackDao.selectById(sysBackId);
      if (sysUserBackpack != null) {
        sysUserBackpack.setDdcId(ddcidByTxHash4);
        sysUserBackpack.setDdcUrl(ddc_url);
        sysUserBackpackDao.updateById(sysUserBackpack);
      }
    } catch (Exception e) {
      log.error("线程休眠异常,延迟唤醒");
      e.printStackTrace();
      RBlockingQueue<Object> blockingFairQueue =
          redissonClient.getBlockingQueue("delay_queue_call");
      RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
      delayedQueue.offer(
          "BSN_DDC_ASYNC_" + transationId + "_" + sysBackId + "_" + ddc_url, 1, TimeUnit.MINUTES);
      //      redisTemplate
      //          .opsForValue()
      //          .set(
      //              "BSN_DDC_ASYNC_" + transationId + "_" + sysBackId + "_" + ddc_url,
      //              System.currentTimeMillis() + "",
      //              1,
      //              TimeUnit.MINUTES);
    }
    log.info("创建ddc异步线程结束");
  }

  private Object sdPayOrder(
      OrderTreasurePoolAddDto tmpDto,
      OrderTreasurePool orderProduct,
      String title,
      SysUser user,
      String ip) {
    StoreTreasure storeTreasure = storeTreasureDao.selectById(tmpDto.getTeaPoId());
    SysUser saler = null;
    if (tmpDto.getItemType() == 2) {
      saler = sysUserDao.selectById(storeTreasure.getCreateId());
      saler.setFlag(1);
      log.info("=====saler:{}", saler);
    }
    BigDecimal consignment_fee = new BigDecimal(sysDictonaryDao.selectByAlias("consignment_fee"));
    log.info("===============consignment_fee:{}", consignment_fee);
    return CashierPayCreateDemo.recharge(
        consignment_fee,
        saler,
        tmpDto,
        user.getPhone(),
        orderProduct.getOrderFingerprint(),
        orderProduct.getTotalPrice().doubleValue() + "",
        ResourcesUtil.getProperty("lianlianpay_treasure_order_notify"),
        title,
        user,
        ip);
  }

  /**
   * @param s
   * @return
   */
  public static byte[] getBytesBASE64(String s) {
    if (s == null) {
      return null;
    }
    try {
      byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(s.getBytes("UTF-8"));
      return b;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public String genSign(JSONObject reqObj) {
    // // 生成待签名串
    String sign_src = genSignData(reqObj);
    log.info("待加密字符串：" + sign_src);
    //    return getSignRSA(sign_src);
    try {
      PKCS8EncodedKeySpec priPKCS8 =
          new PKCS8EncodedKeySpec(
              getBytesBASE64(ResourcesUtil.getProperty("lianlian_private_key")));
      KeyFactory keyf = KeyFactory.getInstance("RSA");
      PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
      // 用私钥对信息生成数字签名
      java.security.Signature signet = java.security.Signature.getInstance("MD5withRSA");
      signet.initSign(myprikey);
      signet.update(sign_src.getBytes("UTF-8"));
      byte[] signed = signet.sign(); // 对信息的数字签名
      return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signed));
    } catch (java.lang.Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String genPubSign(JSONObject reqObj) {
    // // 生成待签名串
    String sign_src = genSignData(reqObj);
    log.info("待加密字符串：" + sign_src);
    return getPubSignRSA(sign_src);
  }
  /**
   * RSA签名验证
   *
   * @return
   */
  public static String getSignRSA(String sign_src) {
    return TraderRSAUtil.sign(ResourcesUtil.getProperty("lianlian_private_key"), sign_src);
  }

  public static String getPubSignRSA(String sign_src) {
    return TraderRSAUtil.sign(ResourcesUtil.getProperty("lianlian_public_key"), sign_src);
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
      if (Strings.isEmpty(value)) {
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

  @Autowired private OrderTreasureRecordDao orderTreasureRecordDao;
  @Autowired private ConBalanceRecordDao conBalanceRecordDao;
  //  @Autowired private StoreProPoolDao storeProPoolDao;

  private Object getAlipayTradeAppPayResponse(OrderTreasurePool orderProduct, String title) {
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
      // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
      AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
      // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
      AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
      model.setBody(orderProduct.getId() + "");
      model.setSubject(title);
      model.setOutTradeNo(orderProduct.getOrderFingerprint());
      model.setTimeoutExpress("30m");
      model.setTotalAmount(orderProduct.getCurPrice() + "");
      model.setProductCode("QUICK_MSECURITY_PAY");
      request.setBizModel(model);
      request.setNotifyUrl(ResourcesUtil.ALIPAY_ORDER_TREASURE_NOTIFY_URL);
      try {
        // 这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        System.out.println(response.getBody()); // 就是orderString 可以直接给客户端请求，无需再做处理。
        return response.getBody();
      } catch (AlipayApiException e) {
        e.printStackTrace();
      }
      return null;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Object getAlipayTradeH5PayResponse(OrderTreasurePool orderProduct, String title) {
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
      request.setNotifyUrl(ResourcesUtil.ALIPAY_ORDER_TREASURE_NOTIFY_URL);
      //      request.setReturnUrl("");
      JSONObject bizContent = new JSONObject();
      bizContent.put("out_trade_no", orderProduct.getOrderFingerprint());
      bizContent.put("total_amount", orderProduct.getTotalPrice());
      bizContent.put("subject", title);
      bizContent.put("product_code", "QUICK_WAP_WAY");
      bizContent.put("return_url", "https://web.11touch.net/#/pages/pay/jieguo/jieguo");
      request.setBizContent(bizContent.toString());
      AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
      if (response.isSuccess()) {
        System.out.println("调用成功");
        return response.getBody();
      } else {
        System.out.println("调用失败");
        return null;
      }
      //      String appId = ResourcesUtil.ALI_APP_ID;
      //      // 公钥
      //      //
      // MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlN0HXsbwxiRsnZfbefFiNpEeLcE5b4nSiDJ7A2Rt0pS2tvJsHZ4UnlUim/6UzxLKAzxn6VpLyKNc3INUctr1ZPtMbLHnDt1vBmXRqeBjear+Vnw6G2Xr4ixoWqRzZAmvlOuOkUJ+G60qU//Kz9YoIm3MnvA3becF8nnLylfkg+pM05+yZkK+3sjXzsS9dGfs3Zs2+jE9ePyRNtYviYXWu9RuOSSGDY06HKVNxHGQUi7TqUZ+W6xRhP0VAfnK5pMBA2CAwywsCv/GRjg6naOUo7wzohqhxBX3WsasqdUBfRkWg2ozkQa77ucPZAWPDRngHXbL2PdbiPSx3SUpL+nZxQIDAQAB
      //      String aliPrivateKey =
      //
      // "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCU3QdexvDGJGydl9t58WI2kR4twTlvidKIMnsDZG3SlLa28mwdnhSeVSKb/pTPEsoDPGfpWkvIo1zcg1Ry2vVk+0xssecO3W8GZdGp4GN5qv5WfDobZeviLGhapHNkCa+U646RQn4brSpT/8rP1igibcye8Ddt5wXyecvKV+SD6kzTn7JmQr7eyNfOxL10Z+zdmzb6MT14/JE21i+Jhda71G45JIYNjTocpU3EcZBSLtOpRn5brFGE/RUB+crmkwEDYIDDLCwK/8ZGODqdo5SjvDOiGqHEFfdaxqyp1QF9GRaDajORBrvu5w9kBY8NGeAddsvY91uI9LHdJSkv6dnFAgMBAAECggEACyOe8ZChY7JGDmTWn4FYgAzL3VCgI6CEiHx+h/pz3VYTdg0d2fmCQXbNaC7co8IcK7HRdLy0/wZ6ZGXPY+jOhAfp6BhH2ezn6eqkjbmkt+37qi0RjAtMY1g/VskHeWzgHpyhxmzbUubaS/7QBk1YI3tj3GDNRQQMheBnR3TcPKKlufR7ri8umRQeow5v41YD1R/AYDU3zX0UxEtsXUNWz6PmqaULlLBB31np5TNTfgshBREgvy/0DVsHtIHcWxNkrwkMapAHaEuR9q9rDsIDpVfRtJxv/aGxIqhYVkDz94MjDsPNUrw7tbcz74R058giXTTMsx8ZPUSAL1UKb5VT4QKBgQD0J8zIacWdL1exDUNKv94xiUAqxB2j0RdN9eUxdO8cSfb9AJxHypNscB2pPsDc0u29R53Za4GkoSihhZ+JSiU8QrZW17nB3JiGoyO4C6NDyQ2g2Jb0am5zEMH/5gp9svyInPVmqd6EJTujXUiAsG2RcY8A+QA0Oh3ozPF68sIhjwKBgQCcFciY1D999yFmscn1vquaLH6s/xOZg7m2ccr0cHrz66r/wbwsNqodCrbEuZ61GJeVU8PnUrAk4wrlGiFqR6pV9PxBHioQYFywsZ/nT8C2XnfqQXe+XVM45ArIZphI4YuBsKSc9AWjA5EGnKSadeJ8VKq4gPfC2gYb+B/10+19awKBgQCYKpnxqiJUVaY0nYx78Nq9SsooHTRP3cfFeeRPD47atapugkvkXnfFFJcX3Rl8RyWOWy0gzWTuQta83DfS69gLF5TmyOpnzWFuQAzJ7s7hN1P8FCD40cBmjGIsZ6XQM5Y6WoCDbIlXGJFzvnaqZcrT4895jra21iW/6sLxmoytNwKBgFsNXhrBXlSGUObOeikwVHy4ziDvICjirfifQyz7XM5kQTm3c6U7MluEv3/dZJbyRKMo3VRZaUXraJSjfLC6I8THCEyYYyNwg0HULJrMbHg2fa+bB1Z2rLC4Xw3uw5FoeXBrcmvY8lzZHMYFpQQyFrA+1+SP33i8pOm2AjburVEHAoGBAKOKnAxaCLiqeDFYeuK4SzjdMbtXDIcHaSQ/hH2P0tnquJquBMIDqVCOEn0o7Oe0pFZ6wD8W2aHPx6fywbaJD7Vz2HBk0cdEHVmSrD5vlSAE2qeDzHNRd397e4GRyXaCwPtZZaGd9dpNB0t9K8+PYi0EYz2Dt/as2ynycXvvcKHh";
      //      // 构造client
      //      CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
      //      // 设置网关地址
      //      certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
      //      // 设置应用Id
      //      certAlipayRequest.setAppId(appId);
      //      // 设置应用私钥
      //      certAlipayRequest.setPrivateKey(aliPrivateKey);
      //      // 设置请求格式，固定值json
      //      certAlipayRequest.setFormat("json");
      //      // 设置字符集
      //      certAlipayRequest.setCharset("utf-8");
      //      // 设置签名类型
      //      certAlipayRequest.setSignType("RSA2");
      //      // 设置应用公钥证书路径
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
      //      model.setSubject(title);
      //      model.setOutTradeNo(orderProduct.getOrderFingerprint());
      //      model.setTimeoutExpress("30m");
      //      model.setTotalAmount(orderProduct.getCurPrice() + "");
      //      model.setProductCode("QUICK_WAP_WAY");
      //      request.setBizModel(model);
      //      request.setNotifyUrl(ResourcesUtil.ALIPAY_ORDER_TREASURE_NOTIFY_URL);
      //      try {
      //        // 这里和普通的接口调用不同，使用的是sdkExecute
      //        AlipayTradePayResponse response = alipayClient.pageExecute(request);
      //        System.out.println(response.getBody()); // 就是orderString 可以直接给客户端请求，无需再做处理。
      //        return response.getBody();
      //      } catch (Exception e) {
      //        e.printStackTrace();
      //      }
    } catch (AlipayApiException e) {
      throw new RuntimeException(e);
    }
  }

  private Object getVipAlipayTradeAppPayResponse(
      String title, UserDto userDto, String tradeNo, BigDecimal total) {

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
      model.setBody(tradeNo);
      model.setSubject(title);
      model.setOutTradeNo(tradeNo);
      model.setTimeoutExpress("30m");
      model.setTotalAmount(String.valueOf(total.longValue()));
      model.setProductCode("QUICK_MSECURITY_PAY");
      request.setBizModel(model);
      request.setNotifyUrl(ResourcesUtil.ALIPAY_VIP_ORDER_TREASURE_NOTIFY_URL);
      try {
        // 这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        log.info("VIP购买，支付宝返回：" + JSONObject.toJSONString(response));
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

  private Object getVipAlipayTradeH5PayResponse(
      String title, UserDto userDto, String tradeNo, BigDecimal total) {

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
      request.setNotifyUrl(ResourcesUtil.ALIPAY_BALANCE_TREASURE_NOTIFY_URL);
      //      request.setReturnUrl("");
      JSONObject bizContent = new JSONObject();
      bizContent.put("out_trade_no", tradeNo);
      bizContent.put("total_amount", total);
      bizContent.put("subject", title);
      bizContent.put("product_code", "QUICK_WAP_WAY");
      bizContent.put("return_url", "https://web.11touch.net/#/pages/pay/jieguo/jieguo");
      request.setBizContent(bizContent.toString());
      AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
      if (response.isSuccess()) {
        System.out.println("调用成功");
        return response.getBody();
      } else {
        System.out.println("调用失败");
        return null;
      }
      //      String appId = ResourcesUtil.ALI_APP_ID;
      //      // 公钥
      //      //
      // MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlN0HXsbwxiRsnZfbefFiNpEeLcE5b4nSiDJ7A2Rt0pS2tvJsHZ4UnlUim/6UzxLKAzxn6VpLyKNc3INUctr1ZPtMbLHnDt1vBmXRqeBjear+Vnw6G2Xr4ixoWqRzZAmvlOuOkUJ+G60qU//Kz9YoIm3MnvA3becF8nnLylfkg+pM05+yZkK+3sjXzsS9dGfs3Zs2+jE9ePyRNtYviYXWu9RuOSSGDY06HKVNxHGQUi7TqUZ+W6xRhP0VAfnK5pMBA2CAwywsCv/GRjg6naOUo7wzohqhxBX3WsasqdUBfRkWg2ozkQa77ucPZAWPDRngHXbL2PdbiPSx3SUpL+nZxQIDAQAB
      //      String aliPrivateKey =
      //
      // "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCU3QdexvDGJGydl9t58WI2kR4twTlvidKIMnsDZG3SlLa28mwdnhSeVSKb/pTPEsoDPGfpWkvIo1zcg1Ry2vVk+0xssecO3W8GZdGp4GN5qv5WfDobZeviLGhapHNkCa+U646RQn4brSpT/8rP1igibcye8Ddt5wXyecvKV+SD6kzTn7JmQr7eyNfOxL10Z+zdmzb6MT14/JE21i+Jhda71G45JIYNjTocpU3EcZBSLtOpRn5brFGE/RUB+crmkwEDYIDDLCwK/8ZGODqdo5SjvDOiGqHEFfdaxqyp1QF9GRaDajORBrvu5w9kBY8NGeAddsvY91uI9LHdJSkv6dnFAgMBAAECggEACyOe8ZChY7JGDmTWn4FYgAzL3VCgI6CEiHx+h/pz3VYTdg0d2fmCQXbNaC7co8IcK7HRdLy0/wZ6ZGXPY+jOhAfp6BhH2ezn6eqkjbmkt+37qi0RjAtMY1g/VskHeWzgHpyhxmzbUubaS/7QBk1YI3tj3GDNRQQMheBnR3TcPKKlufR7ri8umRQeow5v41YD1R/AYDU3zX0UxEtsXUNWz6PmqaULlLBB31np5TNTfgshBREgvy/0DVsHtIHcWxNkrwkMapAHaEuR9q9rDsIDpVfRtJxv/aGxIqhYVkDz94MjDsPNUrw7tbcz74R058giXTTMsx8ZPUSAL1UKb5VT4QKBgQD0J8zIacWdL1exDUNKv94xiUAqxB2j0RdN9eUxdO8cSfb9AJxHypNscB2pPsDc0u29R53Za4GkoSihhZ+JSiU8QrZW17nB3JiGoyO4C6NDyQ2g2Jb0am5zEMH/5gp9svyInPVmqd6EJTujXUiAsG2RcY8A+QA0Oh3ozPF68sIhjwKBgQCcFciY1D999yFmscn1vquaLH6s/xOZg7m2ccr0cHrz66r/wbwsNqodCrbEuZ61GJeVU8PnUrAk4wrlGiFqR6pV9PxBHioQYFywsZ/nT8C2XnfqQXe+XVM45ArIZphI4YuBsKSc9AWjA5EGnKSadeJ8VKq4gPfC2gYb+B/10+19awKBgQCYKpnxqiJUVaY0nYx78Nq9SsooHTRP3cfFeeRPD47atapugkvkXnfFFJcX3Rl8RyWOWy0gzWTuQta83DfS69gLF5TmyOpnzWFuQAzJ7s7hN1P8FCD40cBmjGIsZ6XQM5Y6WoCDbIlXGJFzvnaqZcrT4895jra21iW/6sLxmoytNwKBgFsNXhrBXlSGUObOeikwVHy4ziDvICjirfifQyz7XM5kQTm3c6U7MluEv3/dZJbyRKMo3VRZaUXraJSjfLC6I8THCEyYYyNwg0HULJrMbHg2fa+bB1Z2rLC4Xw3uw5FoeXBrcmvY8lzZHMYFpQQyFrA+1+SP33i8pOm2AjburVEHAoGBAKOKnAxaCLiqeDFYeuK4SzjdMbtXDIcHaSQ/hH2P0tnquJquBMIDqVCOEn0o7Oe0pFZ6wD8W2aHPx6fywbaJD7Vz2HBk0cdEHVmSrD5vlSAE2qeDzHNRd397e4GRyXaCwPtZZaGd9dpNB0t9K8+PYi0EYz2Dt/as2ynycXvvcKHh";
      //      // 构造client
      //      CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
      //      // 设置网关地址
      //      certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
      //      // 设置应用Id
      //      certAlipayRequest.setAppId(appId);
      //      // 设置应用私钥
      //      certAlipayRequest.setPrivateKey(aliPrivateKey);
      //      // 设置请求格式，固定值json
      //      certAlipayRequest.setFormat("json");
      //      // 设置字符集
      //      certAlipayRequest.setCharset("utf-8");
      //      // 设置签名类型
      //      certAlipayRequest.setSignType("RSA2");
      //      // 设置应用公钥证书路径
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
      //      model.setBody(tradeNo);
      //      model.setSubject(title);
      //      model.setOutTradeNo(tradeNo);
      //      model.setTimeoutExpress("30m");
      //      model.setTotalAmount(String.valueOf(total.longValue()));
      //      model.setProductCode("QUICK_WAP_WAY");
      //      request.setBizModel(model);
      //      request.setNotifyUrl(ResourcesUtil.ALIPAY_VIP_ORDER_TREASURE_NOTIFY_URL);
      //      try {
      //        // 这里和普通的接口调用不同，使用的是sdkExecute
      //        AlipayTradePayResponse response = alipayClient.pageExecute(request);
      //        log.info("VIP购买，支付宝返回：" + JSONObject.toJSONString(response));
      //        System.out.println(response.getBody()); // 就是orderString 可以直接给客户端请求，无需再做处理。
      //        return response.getBody();
      //      } catch (AlipayApiException e) {
      //        e.printStackTrace();
      //      }
      //      return null;
    } catch (AlipayApiException e) {
      throw new RuntimeException(e);
    }
  }

  private Object getWeixinPayResponse(OrderTreasurePool orderProduct, String title) {
    // 微信支付
    HashMap<String, String> payParam = new HashMap<>(16);
    payParam.put("body", title);
    payParam.put("out_trade_no", orderProduct.getOrderFingerprint());
    payParam.put(
        "total_fee", orderProduct.getTotalPrice().multiply(new BigDecimal(100)).longValue() + "");
    payParam.put("spbill_create_ip", PayUtils.getIp());
    payParam.put("notify_url", ResourcesUtil.WEIXIN_ORDER_TREASURE_NOTIFY_URL);
    payParam.put("trade_type", "APP");
    // 附加数据包，异步通知中使用
    payParam.put("attach", orderProduct.getId() + "");

    Map<String, String> r;
    try {
      WXPayConfig config = WXPayConfigImpl.getInstance();
      WXPay wxPay = new WXPay(config);
      r = wxPay.unifiedOrder(payParam);
      log.info("微信统一下单返回参数" + JSONObject.toJSONString(r));
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

  private Object getWeixinPayH5Response(OrderTreasurePool orderProduct, String title) {
    // 微信支付
    Map<String, String> r;
    try {

      HashMap<String, String> payParam = new HashMap<>(16);
      payParam.put("appid", ResourcesUtil.WX_APP_ID);
      payParam.put("mch_id", ResourcesUtil.WX_PARTNER_ID);
      payParam.put("nonce_str", WXPayUtil.generateNonceStr());
      payParam.put("out_trade_no", orderProduct.getOrderFingerprint());
      payParam.put(
          "total_fee", orderProduct.getTotalPrice().multiply(new BigDecimal(100)).longValue() + "");
      payParam.put("spbill_create_ip", PayUtils.getIp());
      payParam.put("notify_url", ResourcesUtil.WEIXIN_ORDER_TREASURE_NOTIFY_URL);
      payParam.put("trade_type", "MWEB");
      payParam.put(
          "scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://xskymeta.com\"}}");
      payParam.put("body", title);
      String sign = WXPayUtil.generateSignature(payParam, ResourcesUtil.WX_API_KEY);
      payParam.put("sign", sign);
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

  private Object getVipWeixinPayResponse(
      String tradeNo, BigDecimal totalPrice, UserDto userData, String title) {
    // 微信支付
    HashMap<String, String> payParam = new HashMap<>(16);
    payParam.put("body", title);
    payParam.put("out_trade_no", tradeNo);
    payParam.put("total_fee", totalPrice.multiply(new BigDecimal(100)).longValue() + "");
    payParam.put("spbill_create_ip", PayUtils.getIp());
    payParam.put("notify_url", ResourcesUtil.WEIXIN_VIP_ORDER_TREASURE_NOTIFY_URL);
    payParam.put("trade_type", "APP");
    // 附加数据包，异步通知中使用
    //    payParam.put("attach", JSONObject.toJSONString(userData));

    Map<String, String> r;
    try {
      WXPayConfig config = WXPayConfigImpl.getInstance();
      WXPay wxPay = new WXPay(config);
      r = wxPay.unifiedOrder(payParam);
      log.info("微信VIP：" + JSONObject.toJSONString(r));
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

  private Object getVipWeixinH5PayResponse(
      String tradeNo, BigDecimal totalPrice, UserDto userData, String title) {
    Map<String, String> r;
    try {
      // 微信支付
      HashMap<String, String> payParam = new HashMap<>(16);
      payParam.put("appid", ResourcesUtil.WX_APP_ID);
      payParam.put("mch_id", ResourcesUtil.WX_PARTNER_ID);
      payParam.put("nonce_str", WXPayUtil.generateNonceStr());
      payParam.put("out_trade_no", tradeNo);
      payParam.put("total_fee", totalPrice.multiply(new BigDecimal(100)).longValue() + "");
      payParam.put("spbill_create_ip", PayUtils.getIp());
      payParam.put("notify_url", ResourcesUtil.WEIXIN_VIP_ORDER_TREASURE_NOTIFY_URL);
      payParam.put("trade_type", "MWEB");
      payParam.put(
          "scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://xskymeta.com\"}}");
      payParam.put("body", title);
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

  private void addConBalanceRecord(OrderTreasurePool orderProduct, Integer orderType) {
    ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
    conBalanceRecord.setUId(orderProduct.getCreateId());
    conBalanceRecord.setReType(2);
    conBalanceRecord.setOrderId(orderProduct.getId() + "");
    conBalanceRecord.setOrderType(orderType);
    conBalanceRecord.setTotalPrice(orderProduct.getTotalPrice());
    conBalanceRecord.setTradingChannel(orderProduct.getPayType());
    conBalanceRecord.setCreateId(orderProduct.getCreateId());
    conBalanceRecord.setCreateTime(new Date());
    log.info("添加交易流水信息：" + JSONObject.toJSONString(conBalanceRecord));
    conBalanceRecordDao.insert(conBalanceRecord);
  }

  private void addOrderTreasurePoolCopy(
      StoreTreasure storeTreasure, OrderTreasurePool orderProduct) {
    OrderTreasureRecord orderTreasureRecord = new OrderTreasureRecord();
    orderTreasureRecord.setTurnMaxPrice(storeTreasure.getTurnMaxPrice());
    orderTreasureRecord.setTurnMinPrice(storeTreasure.getTurnMinPrice());
    orderTreasureRecord.setTreasureId(storeTreasure.getId());
    orderTreasureRecord.setOrderTreasurePoolId(orderProduct.getId());
    orderTreasureRecord.setTType(storeTreasure.getTType());
    orderTreasureRecord.setTreasureTitle(storeTreasure.getTreasureTitle());
    orderTreasureRecord.setIndexImgPath(storeTreasure.getIndexImgPath());
    orderTreasureRecord.setHeadImgPath(storeTreasure.getHeadImgPath());
    orderTreasureRecord.setTotalCount(storeTreasure.getTotalCount());
    orderTreasureRecord.setSurplusCount(storeTreasure.getSurplusCount());
    orderTreasureRecord.setFrostCount(storeTreasure.getFrostCount());
    orderTreasureRecord.setPrice(storeTreasure.getPrice());
    orderTreasureRecord.setIntroduce(storeTreasure.getIntroduce());
    orderTreasureRecord.setSense(storeTreasure.getSense());
    orderTreasureRecord.setNeedKnow(storeTreasure.getNeedKnow());
    orderTreasureRecord.setSaleTime(storeTreasure.getSaleTime());
    orderTreasureRecord.setUpTime(storeTreasure.getUpTime());
    orderTreasureRecord.setCheckTime(storeTreasure.getCheckTime());
    orderTreasureRecord.setCreateId(storeTreasure.getCreateId());
    orderTreasureRecord.setCreateTime(storeTreasure.getCreateTime());
    orderTreasureRecord.setUpdateId(storeTreasure.getUpdateId());
    orderTreasureRecord.setUpdateTime(storeTreasure.getUpdateTime());
    orderTreasureRecord.setState(storeTreasure.getState());
    orderTreasureRecord.setStripId(storeTreasure.getStripId());
    orderTreasureRecord.setTNum(storeTreasure.getTNum());
    orderTreasureRecord.setAuthInfo(storeTreasure.getAuthInfo());
    orderTreasureRecordDao.insert(orderTreasureRecord);
  }

  private OrderTreasurePool addTreasureOrder(
      OrderTreasurePoolAddDto tmpDto, boolean needOut, String orderFingerprint, Integer itemType) {
    OrderTreasurePool orderTreasurePool = new OrderTreasurePool();
    orderTreasurePool.setOrderFingerprint(
        orderFingerprint == null
            ? UUID.randomUUID().toString().replaceAll("-", "")
            : orderFingerprint);
    orderTreasurePool.setTeaPoId(tmpDto.getTeaPoId());
    orderTreasurePool.setItemType(itemType);
    orderTreasurePool.setCurPrice(tmpDto.getCurPrice());
    orderTreasurePool.setTotalPrice(tmpDto.getTotalPrice());
    orderTreasurePool.setTotalCount(tmpDto.getCount());
    orderTreasurePool.setPayType(tmpDto.getPayType());
    orderTreasurePool.setPayEndTime(new Date(System.currentTimeMillis() + 3 * 60000));
    orderTreasurePool.setCreateId(tmpDto.getUserId());
    orderTreasurePool.setCreateTime(new Date());
    orderTreasurePoolDao.insert(orderTreasurePool);
    if (needOut) {
      RBlockingQueue<Object> blockingFairQueue =
          redissonClient.getBlockingQueue("delay_queue_call");
      RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
      delayedQueue.offer("orderTreasurePool_" + orderTreasurePool.getId(), 5, TimeUnit.MINUTES);
    }
    return orderTreasurePool;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(OrderTreasurePoolUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderTreasurePoolDto dto = new OrderTreasurePoolDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(OrderTreasurePoolDto dto) {
    OrderTreasurePoolDto orderTreasurePool = getOrderTreasurePoolById(dto.getId());
    if (orderTreasurePool.getState() == 1) {
      orderTreasurePool.setState(2);
    } else {
      orderTreasurePool.setState(1);
    }
    orderTreasurePool.setUpdateTime(new Date());
    orderTreasurePool.setUpdateId(dto.getUpdateId());
    updateDataById(orderTreasurePool.getId(), orderTreasurePool);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<OrderTreasurePoolDto> list(OrderTreasurePoolDto dto) {
    LambdaQueryWrapper<OrderTreasurePool> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(OrderTreasurePoolDto.class, queryWrapper);
  }
}
