package com.qy.ntf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.customResult.MyTreasure;
import com.qy.ntf.bean.customResult.StorePoolPriceRecord;
import com.qy.ntf.bean.dto.StoreProPoolDto;
import com.qy.ntf.bean.dto.StoreProPoolUpdateDto;
import com.qy.ntf.bean.dto.StoreTreasureDto;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.entity.*;
import com.qy.ntf.bean.param.TurnParam;
import com.qy.ntf.dao.*;
import com.qy.ntf.service.StoreProPoolService;
import com.qy.ntf.util.CustomResponse;
import com.qy.ntf.util.PageSelectParam;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池主体 service服务实现
 */
@Service("storeProPoolService")
public class StoreProPoolServiceImpl implements StoreProPoolService {

  //  @Autowired private StoreProPoolDao storeProPoolDao;
  @Autowired private StoreTreasureDao storeTreasureDao;

  @Override
  public BaseMapper<StoreTreasure> getDao() {
    return storeTreasureDao;
  }

  //  @Override
  //  public IPage<StoreProPoolDto> getListByPage(
  //      Class<StoreProPoolDto> clazz,
  //      long currentPage,
  //      Long pageSize,
  //      LambdaQueryWrapper<StoreProPool> queryWrapper) {
  //    IPage<StoreProPoolDto> selectPageList =
  //        selectPageList(clazz, currentPage, pageSize, queryWrapper);
  //    return selectPageList;
  //  }

  @Autowired private StoreProPoolReadRecordDao storeProPoolReadRecordDao;
  @Autowired private SysDictonaryDao sysDictonaryDao;

  @Override
  public CustomResponse<IPage<StoreProPoolDto>> appPagelist(
      PageSelectParam<StoreProPoolDto> param, UserDto userData, String percent, String maxMeta) {
    //    String count = sysDictonaryDao.selectByAlias("no_vip_pol_max_count");
    //    int maxCount = Integer.parseInt(count);
    //    if (userData != null && userData.getIsVip() != null && 1 == userData.getIsVip()) {
    //      maxCount = -1;
    //    }
    //    if ((param.getPageNum() - 1) * param.getPageSize() < maxCount) {
    //      LambdaQueryWrapper<StoreProPool> queryWrapper = new LambdaQueryWrapper<>();
    //      queryWrapper.eq(BaseEntity::getState, 1);
    //      queryWrapper.orderByDesc(BaseEntity::getCreateTime).eq(StoreProPool::getIsSale, 0);
    //      IPage<StoreProPoolDto> result =
    //          selectPageList(
    //              StoreProPoolDto.class,
    //              param.getPageNum(),
    //              Long.valueOf(param.getPageSize()),
    //              queryWrapper);
    //      List<StoreProPoolDto> tmplist = storeProPoolDao.getVipRecord();
    //      if (tmplist.size() > 0) {
    //        int i = Integer.parseInt(result.getTotal() + "") / tmplist.size();
    //        List<StoreProPoolDto> randomThreeInfoList =
    //            getRandomThreeInfoList(tmplist, Math.min(i, tmplist.size()));
    //        result.getRecords().addAll(randomThreeInfoList);
    //      }
    //      List<Long> creators =
    //
    // result.getRecords().stream().map(BaseEntity::getCreateId).collect(Collectors.toList());
    //      Map<Long, SysUser> sysUserMap =
    //          creators.size() > 0
    //              ? sysUserDao.selectBatchIds(creators).stream()
    //                  .collect(Collectors.toMap(SysUser::getId, o -> o))
    //              : new HashMap<>();
    //      for (StoreProPoolDto record : result.getRecords()) {
    //        record.setCreater(sysUserMap.get(record.getCreateId()));
    //      }
    //
    //      CustomResponse<IPage<StoreProPoolDto>> success = CustomResponse.success(result);
    //      success.setSendMeta(getSendMeta(userData, percent, maxMeta));
    //      if (success.getSendMeta() > 0) {
    //        if (userData != null) reFresh(userData, success.getSendMeta());
    //      }
    //      if (userData != null && success.getData().getRecords().size() > 0) {
    //        LambdaQueryWrapper<StoreProPoolCol> q = Wrappers.lambdaQuery();
    //        q.in(
    //                StoreProPoolCol::getProPoolId,
    //                success.getData().getRecords().stream()
    //                    .map(StoreProPoolDto::getId)
    //                    .collect(Collectors.toList()))
    //            .eq(StoreProPoolCol::getUId, userData.getId());
    //        q.eq(BaseEntity::getState, 1);
    //        q.orderByDesc(BaseEntity::getCreateTime);
    //        List<StoreProPoolCol> storeProPoolCols = storeProPoolColDao.selectList(q);
    //        Map<Long, StoreProPoolCol> collect =
    //            storeProPoolCols.stream()
    //                .collect(Collectors.toMap(StoreProPoolCol::getProPoolId, o -> o));
    //        for (StoreProPoolDto record : success.getData().getRecords()) {
    //          if (collect.get(record.getId()) != null) {
    //            record.setIsCollect(1);
    //          } else {
    //            record.setIsCollect(0);
    //          }
    //        }
    //      }
    //      return success;
    //    } else {
    //      throw new RuntimeException("今日浏览量已达上限");
    //    }
    return null;
  }

  @Autowired StoreProPoolColDao storeProPoolColDao;

  private Integer getSendMeta(UserDto userData, String percent, String maxMeta) {
    if (userData != null) {
      if (Math.random() < Double.parseDouble(percent)) {
        int integer = Integer.parseInt(maxMeta);
        int res = new BigDecimal(integer * Math.random() + "").intValue();
        if (userData.getIsVip() == 1) {
          String count = sysDictonaryDao.selectByAlias("vip_pol_meta_multiple");
          return res * Integer.parseInt(count);
        }
        return res;
      }
    }
    return 0;
  }

  /**
   * 在list集合中随机取出指定数量的元素
   *
   * @param list 取元素的集合
   * @param count 个数
   * @return
   */
  public static List<StoreProPoolDto> getRandomThreeInfoList(
      List<StoreProPoolDto> list, int count) {
    List<StoreProPoolDto> olist = new ArrayList<>();
    if (list.size() <= count) {
      return list;
    } else {
      Random random = new Random();
      for (int i = 0; i < count; i++) {
        int intRandom = random.nextInt(list.size() - 1);
        olist.add(list.get(intRandom));
        list.remove(list.get(intRandom));
      }
      return olist;
    }
  }

  @Autowired private OrderTreasurePoolDao orderTreasurePoolDao;

  @Override
  public List<StorePoolPriceRecord> getPoolPriceRecord(Long id, Integer type) {
    List<StorePoolPriceRecord> result = ganeralPriceRecord(type);
    long time = 1L;
    switch (type) {
      case 0:
        time = System.currentTimeMillis() - 7 * 3600 * 24000;
        break;
      case 1:
        time = System.currentTimeMillis() - 30L * 3600 * 24000;
        break;
      case 2:
        time = System.currentTimeMillis() - 90L * 3600 * 24000;
        break;
      case 3:
        time = System.currentTimeMillis() - 365L * 3600 * 24000;
        break;
      default:
        break;
    }
    List<StorePoolPriceRecord> poolPriceRecord = orderTreasurePoolDao.getPoolPriceRecord(id, time);
    for (int i = 0; i < result.size(); i++) {
      int finalI = i;
      BigDecimal price = new BigDecimal(0);
      if (i != 0) {
        List<StorePoolPriceRecord> collect =
            poolPriceRecord.stream()
                .filter(
                    o ->
                        o.getCreateTime().getTime() < result.get(finalI).getCreateTime().getTime()
                            && o.getCreateTime().getTime()
                                > result.get(finalI - 1).getCreateTime().getTime())
                .collect(Collectors.toList());
        long sum = collect.stream().mapToLong(o -> o.getPrice().longValue()).sum();
        if (sum != 0) {
          price =
              new BigDecimal(
                  String.valueOf(new BigDecimal(sum).divide(new BigDecimal(collect.size() + ""))));
        }
      } else {

        List<StorePoolPriceRecord> collect =
            poolPriceRecord.stream()
                .filter(
                    o -> o.getCreateTime().getTime() < result.get(finalI).getCreateTime().getTime())
                .collect(Collectors.toList());
        long sum = collect.stream().mapToLong(o -> o.getPrice().longValue()).sum();
        if (sum != 0) {
          price =
              new BigDecimal(
                  String.valueOf(new BigDecimal(sum).divide(new BigDecimal(collect.size() + ""))));
        }
      }
      result.get(i).setPrice(price);
    }
    Collections.reverse(result);
    return result;
  }

  private List<StorePoolPriceRecord> ganeralPriceRecord(Integer type) {
    //    0一周1一月2三月3一年  一周的话给7个点一个月10个点三个月12个点1年12个点
    List<StorePoolPriceRecord> result = new ArrayList<>();
    try {
      SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
      switch (type) {
        case 0:
          for (int i = 0; i < 7; i++) {
            Date tmp = new Date(new Date().getTime() - i * 24 * 3600000);
            StorePoolPriceRecord storePoolPriceRecord = new StorePoolPriceRecord();
            storePoolPriceRecord.setCreateTime((Date) sp.parseObject(sp.format(tmp)));
            result.add(storePoolPriceRecord);
          }
          break;
        case 1:
          for (int i = 0; i < 10; i++) {
            Date tmp = new Date(new Date().getTime() - (long) i * 3 * 24 * 3600000);
            StorePoolPriceRecord storePoolPriceRecord = new StorePoolPriceRecord();
            storePoolPriceRecord.setCreateTime((Date) sp.parseObject(sp.format(tmp)));
            result.add(storePoolPriceRecord);
          }
          break;
        case 2:
          for (int i = 0; i < 10; i++) {
            Date tmp = new Date(new Date().getTime() - (long) i * 9 * 24 * 3600000);
            StorePoolPriceRecord storePoolPriceRecord = new StorePoolPriceRecord();
            storePoolPriceRecord.setCreateTime((Date) sp.parseObject(sp.format(tmp)));
            result.add(storePoolPriceRecord);
          }
          break;
        case 3:
          for (int i = 0; i < 12; i++) {
            Date tmp = new Date(new Date().getTime() - (long) i * 30 * 24 * 3600000);
            StorePoolPriceRecord storePoolPriceRecord = new StorePoolPriceRecord();
            storePoolPriceRecord.setCreateTime((Date) sp.parseObject(sp.format(tmp)));
            result.add(storePoolPriceRecord);
          }
          break;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  @Autowired private SysUserDao sysUserDao;
  @Autowired private ConIntegralRecordDao conIntegralRecordDao;

  @Override
  public String reFresh(UserDto userData, Integer count) {
    SysUser sysUser = sysUserDao.selectById(userData.getId());
    sysUser.setMetaCount(sysUser.getMetaCount() + count);
    sysUserDao.updateById(sysUser);
    ConIntegralRecord conIntegralRecord = new ConIntegralRecord();
    conIntegralRecord.setUId(sysUser.getId());
    conIntegralRecord.setRecordType(3);
    conIntegralRecord.setMetaCount(count);
    conIntegralRecord.setCreateId(sysUser.getCreateId());
    conIntegralRecord.setCreateTime(new Date());
    conIntegralRecordDao.insert(conIntegralRecord);
    return "SUCCESS";
  }

  @Autowired private SysUserBackpackDao sysUserBackpackDao;
  @Autowired private StoreTreasureRecordDao storeTreasureRecordDao;

  @Override
  public List<MyTreasure> getMytreasure(MyTreasure param, UserDto userData) {
    LambdaQueryWrapper<SysUserBackpack> que = new LambdaQueryWrapper<>();
    que.eq(BaseEntity::getState, 1)
        .eq(SysUserBackpack::getUId, userData.getId())
        .orderByAsc(BaseEntity::getCreateTime);
    List<SysUserBackpack> sysUserBackpacks = sysUserBackpackDao.selectList(que);
    // 全部的
    List<SysUserBackpack> all =
        sysUserBackpacks.stream()
            .filter(
                o ->
                    o.getFinType() == 2
                        || o.getFinType() == 4
                        || o.getFinType() == 6
                        || o.getFinType() == 8)
            .collect(Collectors.toList());
    // 售出赠送的 消耗
    List<SysUserBackpack> out =
        sysUserBackpacks.stream()
            .filter(o -> o.getFinType() == 1 || o.getFinType() == 3 || o.getFinType() == 7)
            .collect(Collectors.toList());
    for (SysUserBackpack sysUserBackpack : out) {
      int index = -1;
      for (int i = 0; i < all.size(); i++) {
        if (all.get(i).getOrderFingerprint() != null
            && all.get(i).getOrderFingerprint().equals(sysUserBackpack.getOrderFingerprint())) {
          index = i;
        }
      }
      if (index != -1) all.remove(index);
    }
    if (param.getIsCheck() == null || 0 == param.getIsCheck()) {
      all =
          all.stream()
              .filter(o -> o.getIsCheck() == null || o.getIsCheck() == 1)
              .collect(Collectors.toList());
    } else {
      all =
          all.stream()
              .filter(o -> o.getIsCheck() != null && o.getIsCheck() == 0)
              .collect(Collectors.toList());
    }
    ArrayList<MyTreasure> result = new ArrayList<>();
    List<String> allOrderFigerprint =
        all.stream().map(SysUserBackpack::getOrderFingerprint).collect(Collectors.toList());
    List<String> isSalingOrderFigerprint =
        StoreProPoolServiceImpl.addCheckIsSalingParam(
            allOrderFigerprint, storeTreasureDao, userData.getId());
    List<String> saledOrderFigerprint =
        StoreProPoolServiceImpl.addCheckIsSaliedParam(
            allOrderFigerprint, storeTreasureDao, userData.getId());
    for (SysUserBackpack sysUserBackpack : all) {
      //      if (sysUserBackpack.getTreasureFrom() != null && sysUserBackpack.getTreasureFrom() ==
      // 0) {
      StoreTreasure storeTreasure = storeTreasureDao.selectById(sysUserBackpack.getSTreasureId());
      if (sysUserBackpack.getIsCheck() != null
          && sysUserBackpack.getIsCheck() == 0
          && storeTreasure.getPId() != null) {
        storeTreasure = storeTreasureDao.selectById(storeTreasure.getPId());
      }
      if (storeTreasure == null) {
        System.out.println(123);
      }
      MyTreasure myTreasure = new MyTreasure();
      myTreasure.setId(storeTreasure.getId());
      myTreasure.setTitle(storeTreasure.getTreasureTitle());
      myTreasure.setIsSaling(
          isSalingOrderFigerprint.contains(sysUserBackpack.getOrderFingerprint()) ? 1 : 0);
      if (myTreasure.getIsSaling() != 1) {
        myTreasure.setIsSaling(
            saledOrderFigerprint.contains(sysUserBackpack.getOrderFingerprint()) ? 1 : 0);
      }
      myTreasure.setTransationId(sysUserBackpack.getTransationId());
      myTreasure.setSTreasureId(sysUserBackpack.getSTreasureId());
      myTreasure.setDdcId(sysUserBackpack.getDdcId());
      myTreasure.setDdcUrl(sysUserBackpack.getDdcUrl());
      myTreasure.setTransationId(sysUserBackpack.getTransationId());
      myTreasure.setTotalCount(1);
      myTreasure.setTransFrom(sysUserBackpack.getTreasureFrom());
      myTreasure.setIsCheck(sysUserBackpack.getIsCheck());
      myTreasure.setHeadImg(
          storeTreasure.getTType() == 2
              ? storeTreasure.getIndexImgPath()
              : storeTreasure.getHeadImgPath());
      myTreasure.setIsCompund(sysUserBackpack.getFinType() == 6);
      myTreasure.setOrderFingerprint(sysUserBackpack.getOrderFingerprint());
      myTreasure.setCreateTime(storeTreasure.getCreateTime());
      result.add(myTreasure);
      //      }
    }

    List<String> collect =
        result.stream().map(MyTreasure::getOrderFingerprint).collect(Collectors.toList());
    List<String> collect1 = addCheckIsSalingParam(collect, storeTreasureDao, userData.getId());
    result.forEach(
        o -> {
          o.setIsSaling(collect1.contains(o.getOrderFingerprint()) ? 1 : 0);
          if (o.getIsSaling() != 1) {
            o.setIsSaling(saledOrderFigerprint.contains(o.getOrderFingerprint()) ? 1 : 0);
          }
        });

    if (result.size() > 0) {
      List<Long> poolId = result.stream().map(MyTreasure::getId).collect(Collectors.toList());
      poolId.add(-222L);
      List<StoreTreasure> storeProPools = storeTreasureDao.selectBatchIds(poolId);
      Map<Long, StoreTreasure> poolMap =
          storeProPools.stream().collect(Collectors.toMap(StoreTreasure::getId, o -> o));
      for (MyTreasure myTreasure : result) {
        if (poolMap.get(myTreasure.getId()) != null) {

          myTreasure.setStoreTreasure(
              JSONObject.parseObject(
                  JSONObject.toJSONString(poolMap.get(myTreasure.getId())), StoreTreasure.class));
          Long orgId = myTreasure.getStoreTreasure().getSysOrgId();
          myTreasure.setSysOrg(sysOrgDao.selectById(orgId));
          if (Strings.isNotEmpty(myTreasure.getTransationId())) {
            myTreasure.getStoreTreasure().setTransationId(myTreasure.getTransationId());
            myTreasure.getStoreTreasure().setLinkInfo(myTreasure.getTransationId());
          }
          LambdaQueryWrapper<StoreTreasureRecord> q =
              Wrappers.lambdaQuery(StoreTreasureRecord.class);
          q.eq(StoreTreasureRecord::getOrderFingerprint, myTreasure.getOrderFingerprint());
          List<StoreTreasureRecord> storeTreasureRecords = storeTreasureRecordDao.selectList(q);
          if (storeTreasureRecords.size() > 0) {
            myTreasure.getStoreTreasure().setTNum(storeTreasureRecords.get(0).getStrNum());
          }
          if (myTreasure.getIsCompund() && myTreasure.getStoreTreasure() != null) {
            myTreasure.getStoreTreasure().setPrice(new BigDecimal(0));
          }
          if (myTreasure.getStoreTreasure().getTType() == 2) {
            String headImgPath = new String(myTreasure.getStoreTreasure().getHeadImgPath());
            String indexImgPath = new String(myTreasure.getStoreTreasure().getIndexImgPath());
            myTreasure.getStoreTreasure().setHeadImgPath(indexImgPath);
            myTreasure.getStoreTreasure().setIndexImgPath(headImgPath);
            myTreasure.setTotalCount(myTreasure.getStoreTreasure().getTotalCount());
          } else if (myTreasure.getStoreTreasure().getTType() == 0) {
            myTreasure.setTotalCount(myTreasure.getStoreTreasure().getTotalCount());
          } else if (myTreasure.getStoreTreasure().getTType() == 3) {
            List<StoreTreasure> bodItems =
                storeTreasureDao.selectByPid(myTreasure.getStoreTreasure().getId());
            if (bodItems.size() > 0) {
              myTreasure
                  .getStoreTreasure()
                  .setHeadImgPath(
                      Strings.join(
                          bodItems.stream()
                              .map(StoreTreasure::getIndexImgPath)
                              .collect(Collectors.toList()),
                          ';'));
              myTreasure
                  .getStoreTreasure()
                  .setTotalCount(bodItems.stream().mapToInt(StoreTreasure::getTotalCount).sum());
              if (myTreasure.getStoreTreasure().getTotalCount() == 0) {
                myTreasure.getStoreTreasure().setTotalCount(-1);
              }
              myTreasure.setChildren(bodItems);
            }
            myTreasure.setTotalCount(myTreasure.getStoreTreasure().getTotalCount());
          } else if (myTreasure.getStoreTreasure().getTType() == 4) {
            List<StoreTreasure> bodItems =
                storeTreasureDao.selectByPid(myTreasure.getStoreTreasure().getPId());
            if (bodItems.size() > 0) {
              myTreasure
                  .getStoreTreasure()
                  .setTotalCount(bodItems.stream().mapToInt(StoreTreasure::getTotalCount).sum());
              if (myTreasure.getStoreTreasure().getTotalCount() == 0) {
                myTreasure.getStoreTreasure().setTotalCount(-1);
              }
              myTreasure.setChildren(bodItems);
            }
            myTreasure.setTotalCount(myTreasure.getStoreTreasure().getTotalCount());
          }
        }
      }
    }
    Collections.reverse(result);
    return result;
  }

  @Override
  public IPage<StorePoolPriceRecord> getPoolPriceRecordByPage(
      PageSelectParam<StoreProPoolDto> param, int i) {
    return orderTreasurePoolDao.getPoolPriceRecordByPage(
        new Page<>(Long.valueOf(param.getPageNum()), Long.valueOf(param.getPageSize())),
        param.getSelectParam().getId());
  }

  @Override
  public Object cancelOut(MyTreasure myTreasure, UserDto userData) {

    LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery();
    que.eq(StoreTreasure::getOrderFingerprint, myTreasure.getOrderFingerprint())
        .eq(BaseEntity::getCreateId, userData.getId())
        .orderByDesc(BaseEntity::getCreateTime);
    List<StoreTreasure> storeTreasures = storeTreasureDao.selectList(que);
    if (storeTreasures.size() > 0) {
      LambdaQueryWrapper<OrderTreasurePool> orderQue =
          Wrappers.lambdaQuery(OrderTreasurePool.class);
      orderQue
          .eq(OrderTreasurePool::getOrderFlag, 0)
          .eq(OrderTreasurePool::getTeaPoId, storeTreasures.get(0).getId());
      List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(orderQue);
      if (orderTreasurePools.size() > 0) {
        throw new RuntimeException("该藏品已被下单，暂时无法下架");
      }
      storeTreasures.get(0).setState(-1);
      storeTreasures.get(0).setUpdateTime(new Date());
      storeTreasureDao.updateById(storeTreasures.get(0));
      return "SUCCESS";
    } else {
      throw new RuntimeException("参数错误，请联系客服");
    }
  }

  @Override
  public Object updatePoolPriceAndTag(MyTreasure myTreasure, UserDto userData) {
    //    LambdaQueryWrapper<SysUserBackpack> que = Wrappers.lambdaQuery();
    //    que.eq(SysUserBackpack::getUId, userData.getId());
    //    que.in(SysUserBackpack::getFinType, Arrays.asList(2, 4, 6));
    //    que.eq(SysUserBackpack::getOrderFingerprint, myTreasure.getOrderFingerprint());
    //    que.orderByDesc(BaseEntity::getCreateTime);
    //    List<SysUserBackpack> sysUserBackpacks = sysUserBackpackDao.selectList(que);
    //    if (sysUserBackpacks.size() == 0) throw new RuntimeException("参数异常");
    //    SysUserBackpack sysUserBackpack = sysUserBackpacks.get(0);
    //    LambdaQueryWrapper<StoreProPool> q = Wrappers.lambdaQuery();
    //    q.eq(StoreProPool::getOrderFingerprint, sysUserBackpack.getOrderFingerprint());
    //    q.eq(BaseEntity::getCreateId, userData.getId());
    //    q.eq(BaseEntity::getState, 1);
    //    q.orderByDesc(BaseEntity::getCreateTime);
    //    StoreProPool storeProPool = storeProPoolDao.selectOne(q);
    //    if (storeProPool == null) throw new RuntimeException("参数异常");
    //    storeProPool.setUpdateTime(new Date());
    //    storeProPool.setUpdateId(userData.getId());
    //    storeProPool.setProPrice(myTreasure.getProPrice());
    //    storeProPool.setTagCon(myTreasure.getTagCon());
    //    storeProPoolDao.updateById(storeProPool);
    return "SUCCESS";
  }

  @Autowired private OrderTreasureRecordDao orderTreasureRecordDao;

  @Override
  public MyTreasure openBindBox(MyTreasure param, UserDto userData) {
    LambdaQueryWrapper<SysUserBackpack> que = Wrappers.lambdaQuery(SysUserBackpack.class);
    que.eq(SysUserBackpack::getOrderFingerprint, param.getOrderFingerprint());
    List<Integer> fin = new ArrayList<>();
    fin.add(2);
    fin.add(4);
    fin.add(6);
    fin.add(8);
    que.in(SysUserBackpack::getFinType, fin)
        .eq(BaseEntity::getState, 1)
        .eq(SysUserBackpack::getIsCheck, 0)
        .eq(SysUserBackpack::getUId, userData.getId())
        .orderByDesc(BaseEntity::getCreateTime);
    List<SysUserBackpack> list = sysUserBackpackDao.selectList(que);
    if (list.size() > 0) {
      SysUserBackpack sysUserBackpack = list.get(0);
      sysUserBackpack.setIsCheck(1);
      sysUserBackpackDao.updateById(sysUserBackpack);

      StoreTreasure storeTreasure = storeTreasureDao.selectById(sysUserBackpack.getSTreasureId());
      StoreTreasureRecord storeTreasureRecord =
          storeTreasureRecordDao.selectByOrderFing(param.getOrderFingerprint());
      storeTreasure.setTNum(storeTreasureRecord.getStrNum());
      OrderTreasureRecord orderTreasureRecord =
          orderTreasureRecordDao.selectByOrderF(param.getOrderFingerprint());
      if (orderTreasureRecord != null) {
        orderTreasureRecord.setTNum(storeTreasureRecord.getStrNum());
        orderTreasureRecordDao.updateById(orderTreasureRecord);
      }
      MyTreasure myTreasure = new MyTreasure();
      myTreasure.setStoreTreasure(storeTreasure);
      myTreasure.setId(storeTreasure.getId());
      myTreasure.setTitle(storeTreasure.getTreasureTitle());
      myTreasure.setIsSaling(0);
      myTreasure.setSTreasureId(sysUserBackpack.getSTreasureId());
      myTreasure.setDdcId(sysUserBackpack.getDdcId());
      myTreasure.setDdcUrl(sysUserBackpack.getDdcUrl());
      myTreasure.setTransationId(sysUserBackpack.getTransationId());
      myTreasure.setTotalCount(1);
      myTreasure.setTransFrom(sysUserBackpack.getTreasureFrom());
      myTreasure.setHeadImg(
          storeTreasure.getTType() == 2
              ? storeTreasure.getIndexImgPath()
              : storeTreasure.getHeadImgPath());
      myTreasure.setOrderFingerprint(sysUserBackpack.getOrderFingerprint());
      myTreasure.setCreateTime(storeTreasure.getCreateTime());
      return myTreasure;
    }
    throw new RuntimeException("对不起查无结果");
  }

  @Override
  public IPage<StoreTreasureDto> turnTreasure(PageSelectParam<String> param, UserDto userData) {
    IPage<StoreTreasureDto> res =
        storeTreasureDao.turnAndTurnPage(
            new Page(param.getPageNum(), param.getPageSize()), param.getSelectParam());

    return res;
  }

  @Override
  public IPage<StoreTreasureDto> isSalingTreasure(PageSelectParam<String> param, UserDto userData) {
    TurnParam p = new TurnParam();
    p.setKeyWord(param.getSelectParam());
    Page<StoreTreasureDto> res =
        storeTreasureDao.turnPage(new Page(param.getPageNum(), param.getPageSize()), p);
    Set<Long> userIds =
        res.getRecords().stream().map(BaseEntity::getCreateId).collect(Collectors.toSet());
    userIds.add(-1111L);
    Map<Long, SysUser> userMap =
        sysUserDao.selectBatchIds(userIds).stream()
            .collect(Collectors.toMap(SysUser::getId, o -> o));
    for (StoreTreasureDto o : res.getRecords()) {
      o.setCreator(userMap.get(o.getCreateId()));
    }
    return res;
  }

  static List<String> addCheckIsSalingParam(
      List<String> collect, StoreTreasureDao storeTreasureDao, Long id) {
    if (collect.size() > 0) {
      LambdaQueryWrapper<StoreTreasure> qu = new LambdaQueryWrapper<>();
      qu.eq(BaseEntity::getState, 1)
          .in(StoreTreasure::getOrderFingerprint, collect)
          .eq(BaseEntity::getCreateId, id);
      return storeTreasureDao.selectList(qu).stream()
          .map(StoreTreasure::getOrderFingerprint)
          .collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }

  static List<String> addCheckIsSaliedParam(
      List<String> collect, StoreTreasureDao storeTreasureDao, Long id) {
    if (collect.size() > 0) {
      LambdaQueryWrapper<StoreTreasure> qu = new LambdaQueryWrapper<>();
      qu.eq(BaseEntity::getState, -1)
          .eq(StoreTreasure::getIsSale, 1)
          .in(StoreTreasure::getOrderFingerprint, collect)
          .eq(BaseEntity::getCreateId, id);
      return storeTreasureDao.selectList(qu).stream()
          .map(StoreTreasure::getOrderFingerprint)
          .collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public StoreProPoolDto getStoreProPoolById(Long id) {
    //    Optional<StoreProPoolDto> optional = selectDataById(StoreProPoolDto.class, id);
    //    if (optional.isPresent()) {
    //      return optional.get();
    //    } else {
    return null;
    //    }
  }

  @Autowired private SysOrgDao sysOrgDao;

  //  @Override
  //  @Transactional(rollbackFor = Exception.class)
  //  public void save(StoreProPoolAddDto tmpDto) {
  //    ModelMapper md = new ModelMapper();
  //    StoreProPoolDto dto = new StoreProPoolDto();
  //    md.map(tmpDto, dto);
  //    dto.setProNum(UUID.randomUUID().toString().replaceAll("-", ""));
  //    dto.setOrderFingerprint(dto.getProNum());
  //    dto.setFromType(0);
  //    SysOrg sysOrg = sysOrgDao.selectById(tmpDto.getSysOrgId());
  //    if (sysOrg != null) {
  //      dto.setOrgName(sysOrg.getOrgname());
  //      dto.setOrgIndexPath(sysOrg.getOrgimg());
  //    }
  //    insertData(dto, StoreProPool.class);
  //  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(StoreProPoolUpdateDto tmpDto) {
    //    ModelMapper md = new ModelMapper();
    //    StoreProPoolDto dto = new StoreProPoolDto();
    //    md.map(tmpDto, dto);
    //    dto.setFromType(0);
    //    if (Strings.isEmpty(dto.getProNum()) || Strings.isEmpty(dto.getOrderFingerprint())) {
    //      dto.setOrderFingerprint(UUID.randomUUID().toString().replaceAll("-", ""));
    //      dto.setProNum(dto.getOrderFingerprint());
    //    }
    //    SysOrg sysOrg = sysOrgDao.selectById(tmpDto.getSysOrgId());
    //    if (sysOrg != null) {
    //      dto.setOrgName(sysOrg.getOrgname());
    //      dto.setOrgIndexPath(sysOrg.getOrgimg());
    //    }
    //    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(StoreProPoolDto dto) {
    //    StoreProPoolDto storeProPool = getStoreProPoolById(dto.getId());
    //    if (storeProPool.getState() == 1) {
    //      storeProPool.setState(0);
    //    } else {
    //      storeProPool.setState(1);
    //    }
    //    storeProPool.setUpdateTime(new Date());
    //    storeProPool.setUpdateId(dto.getUpdateId());
    //    updateDataById(storeProPool.getId(), storeProPool);
  }

  //  @Override
  //  @Transactional(rollbackFor = Exception.class)
  //  public void delete(Long id, SysUserAdminDto adminUserData) {
  //    StoreProPool storeProPool = storeProPoolDao.selectById(id);
  //    if (storeProPool == null) throw new RuntimeException("id异常");
  //    storeProPool.setState(-1);
  //    storeProPool.setUpdateId(adminUserData.getId());
  //    storeProPool.setUpdateTime(new Date());
  //    storeProPoolDao.updateById(storeProPool);
  //  }

  //  @Override
  //  public List<StoreProPoolDto> list(StoreProPoolDto dto) {
  //    LambdaQueryWrapper<StoreProPool> queryWrapper = new LambdaQueryWrapper<>();
  //    return selectList(StoreProPoolDto.class, queryWrapper);
  //  }
}
