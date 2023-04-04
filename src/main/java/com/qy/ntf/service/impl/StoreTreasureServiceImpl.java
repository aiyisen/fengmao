package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.*;
import com.qy.ntf.bean.param.AddCouldCompParam;
import com.qy.ntf.bean.param.BindBoxAddParam;
import com.qy.ntf.bean.param.TreaNeedParam;
import com.qy.ntf.bean.param.TurnParam;
import com.qy.ntf.dao.*;
import com.qy.ntf.service.StoreTreasureNeededService;
import com.qy.ntf.service.StoreTreasureService;
import com.qy.ntf.service.SysPlateConfigService;
import com.qy.ntf.util.AvataUtil;
import com.qy.ntf.util.PageSelectParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品首发/超前申购 主体 service服务实现
 */
@Service("storeTreasureService")
@Slf4j
public class StoreTreasureServiceImpl implements StoreTreasureService {

  @Autowired private StoreTreasureDao storeTreasureDao;
  @Autowired private StoreTreasureCheckUserDao storeTreasureCheckUserDao;
  @Autowired private SysOrgDao sysOrgDao;

  @Override
  public BaseMapper<StoreTreasure> getDao() {
    return storeTreasureDao;
  }

  @Override
  public IPage<StoreTreasureDto> getListByPage(
      Class<StoreTreasureDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreTreasure> queryWrapper) {
    IPage<StoreTreasureDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    Set<Long> collect =
        selectPageList.getRecords().stream()
            .map(StoreTreasureDto::getSysOrgId)
            .collect(Collectors.toSet());
    List<Long> stripIds =
        selectPageList.getRecords().stream()
            .map(StoreTreasureDto::getStripId)
            .collect(Collectors.toList());
    stripIds.add(-1111L);
    Map<Long, StoreTreIosPrice> stripMap =
        storeTreIosPriceDao.selectBatchIds(stripIds).stream()
            .collect(Collectors.toMap(StoreTreIosPrice::getId, o -> o));
    if (collect.size() > 0) {
      LambdaQueryWrapper<SysOrg> que = Wrappers.lambdaQuery(SysOrg.class);
      que.in(SysOrg::getId, collect);
      Map<Long, SysOrg> map =
          sysOrgDao.selectList(que).stream().collect(Collectors.toMap(SysOrg::getId, o -> o));
      selectPageList.getRecords().forEach(o -> o.setSysOrg(map.get(o.getSysOrgId())));
      List<Long> ids =
          selectPageList.getRecords().stream()
              .map(StoreTreasureDto::getId)
              .collect(Collectors.toList());
      LambdaQueryWrapper<StoreTreasureNeeded> q = Wrappers.lambdaQuery(StoreTreasureNeeded.class);
      q.in(StoreTreasureNeeded::getStoreTreasureId, ids);
      List<StoreTreasureNeeded> storeTreasureNeededs = storeTreasureNeededDao.selectList(q);
      for (StoreTreasureDto record : selectPageList.getRecords()) {
        if (record.getStripId() != null) {
          record.setStoreTreIosPrice(stripMap.get(record.getStripId()));
        }
        List<StoreTreasureNeeded> collect1 =
            storeTreasureNeededs.stream()
                .filter(o -> o.getStoreTreasureId().equals(record.getId()))
                .collect(Collectors.toList());
        List<TreaNeedParam> neededRes = new ArrayList<>();
        for (StoreTreasureNeeded storeTreasureNeeded : collect1) {
          TreaNeedParam tmp = new TreaNeedParam();
          tmp.setId(storeTreasureNeeded.getNeedStoreTreasureId());
          tmp.setCount(storeTreasureNeeded.getNeedCount());
          neededRes.add(tmp);
        }
        record.setNeededTreId(neededRes);
      }
    }
    return selectPageList;
  }

  @Autowired private OrderTreasurePoolDao orderTreasurePoolDao;
  @Autowired private StoreTreIosPriceDao storeTreIosPriceDao;

  @Override
  public IPage<StoreTreasureDto> appPagelist(
      PageSelectParam<StoreTreasureSelectDto> param, UserDto userData) {
    LambdaQueryWrapper<StoreTreasure> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(BaseEntity::getState, 1);
    //    if (param.getSelectParam().getTType() != null) {
    queryWrapper.eq(StoreTreasure::getTType, 0);
    //    }
    queryWrapper.eq(StoreTreasure::getFromUser, 0);
    if (param.getSelectParam().getSysSeriesId() != null) {
      queryWrapper.eq(StoreTreasure::getSysSeriesId, param.getSelectParam().getSysSeriesId());
    }
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    IPage<StoreTreasureDto> storeTreasureDtoIPage =
        selectPageList(
            StoreTreasureDto.class,
            param.getPageNum(),
            Long.valueOf(param.getPageSize()),
            queryWrapper);
    //    LambdaQueryWrapper<OrderTreasurePool> ques = new LambdaQueryWrapper<>();
    //    List<Long> collect2 =
    //        storeTreasureDtoIPage.getRecords().stream()
    //            .map(StoreTreasureDto::getId)
    //            .collect(Collectors.toList());
    //    collect2.add(-1111L);
    //    ques.eq(OrderTreasurePool::getItemType, 1)
    //        .in(OrderTreasurePool::getTeaPoId, collect2)
    //        .eq(OrderTreasurePool::getPayType, -1);
    //    List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(ques);
    LambdaQueryWrapper<StoreTreIosPrice> qI = Wrappers.lambdaQuery(StoreTreIosPrice.class);
    List<Long> priceIds =
        storeTreasureDtoIPage.getRecords().stream()
            .map(StoreTreasureDto::getStripId)
            .collect(Collectors.toList());
    priceIds.add(-111L);
    qI.in(StoreTreIosPrice::getId, priceIds);
    List<StoreTreIosPrice> storeTreIosPrices = storeTreIosPriceDao.selectList(qI);
    Map<Long, StoreTreIosPrice> priceMap =
        storeTreIosPrices.stream().collect(Collectors.toMap(StoreTreIosPrice::getId, o -> o));

    for (StoreTreasureDto record : storeTreasureDtoIPage.getRecords()) {
      //      Long count =
      //          orderTreasurePools.stream().filter(o ->
      // o.getTeaPoId().equals(record.getId())).count();
      //      record.setFrostCount(Integer.valueOf(count + ""));
      if (record.getStripId() != null) {
        record.setStoreTreIosPrice(priceMap.get(record.getStripId()));
      }
      if (record.getSurplusCount() <= 0) {
        record.setIsDone(-1);
      } else if (record.getUpTime() != null) {
        record.setIsDone(record.getUpTime().getTime() < System.currentTimeMillis() ? 1 : 0);
      }
    }
    if (userData != null) {
      LambdaQueryWrapper<SysBeforeUser> befQue = Wrappers.lambdaQuery(SysBeforeUser.class);
      befQue.eq(SysBeforeUser::getPhone, userData.getPhone());
      List<SysBeforeUser> sysBeforeUsers = sysBeforeUserDao.selectList(befQue);
      Set<Long> ids =
          sysBeforeUsers.stream().map(SysBeforeUser::getStreaId).collect(Collectors.toSet());
      storeTreasureDtoIPage
          .getRecords()
          .forEach(
              o -> {
                if (ids.contains(o.getId())) {
                  o.setHasBefore(1);
                } else {
                  o.setHasBefore(0);
                }
              });
    }
    //      orderTreasurePools =
    //          orderTreasurePools.stream()
    //              .filter(o -> o.getCreateId().equals(userData.getId()))
    //              .collect(Collectors.toList());
    //      LambdaQueryWrapper<StoreTreasureCheckUser> q = Wrappers.lambdaQuery();
    //      List<Long> collect3 =
    //          storeTreasureDtoIPage.getRecords().stream()
    //              .map(StoreTreasureDto::getId)
    //              .collect(Collectors.toList());
    //      collect3.add(-1111L);
    //      q.eq(StoreTreasureCheckUser::getUId, userData.getId())
    //          .in(StoreTreasureCheckUser::getStoreTreasureId, collect3);
    //      List<StoreTreasureCheckUser> storeTreasureCheckUsers =
    //          storeTreasureCheckUserDao.selectList(q);
    //
    //      for (StoreTreasureDto record : storeTreasureDtoIPage.getRecords()) {
    //        List<OrderTreasurePool> collect =
    //            orderTreasurePools.stream()
    //                .filter(
    //                    o ->
    //                        o.getTeaPoId().equals(record.getId())
    //                            && o.getPayType() != -1
    //                            && o.getCreateId().equals(userData.getId()))
    //                .collect(Collectors.toList());
    //        record.setIsBuy(collect.size() > 0);
    //        List<StoreTreasureCheckUser> collect1 =
    //            storeTreasureCheckUsers.stream()
    //                .filter(o -> o.getStoreTreasureId().equals(record.getId()))
    //                .collect(Collectors.toList());
    //        // 是否已中签-1未参与0未1已中签
    //        if (collect1.size() > 0) {
    //          record.setCheckState(1);
    //        } else {
    //          List<OrderTreasurePool> collect4 =
    //              orderTreasurePools.stream()
    //                  .filter(
    //                      o ->
    //                          o.getTeaPoId().equals(record.getId())
    //                              && o.getPayType() == -1
    //                              && o.getCreateId().equals(userData.getId()))
    //                  .collect(Collectors.toList());
    //          if (collect4.size() > 0) {
    //            record.setCheckState(0);
    //          } else {
    //            record.setCheckState(-1);
    //          }
    //        }
    //      }
    //    }
    //    List<Long> collect =
    //        storeTreasureDtoIPage.getRecords().stream()
    //            .map(BaseEntity::getCreateId)
    //            .collect(Collectors.toList());
    //    collect.add(-1111L);
    //    List<SysUser> sysUsers = sysUserDao.selectBatchIds(collect);
    //    for (StoreTreasureDto record : storeTreasureDtoIPage.getRecords()) {
    //      List<SysUser> collect1 =
    //          sysUsers.stream()
    //              .filter(o -> o.getId().equals(record.getCreateId()))
    //              .collect(Collectors.toList());
    //      if (collect1.size() > 0) {
    //        SysUser sysUser = collect1.get(0);
    //        sysUser.setPass(null);
    //        sysUser.setMetaCount(null);
    //        sysUser.setBalance(null);
    //        record.setCreator(sysUser);
    //      }
    //      // -1未开始  0报名中  1已结束 2已完结
    //
    //      if (record.getTType() == 1) {
    //        if (record.getDownTime().getTime() < System.currentTimeMillis()) {
    //          record.setIsDone(2);
    //        } else if ((record.getCheckTime() != null
    //                && record.getCheckTime().getTime() < System.currentTimeMillis())
    //            && record.getDownTime() != null
    //            && record.getDownTime().getTime() > System.currentTimeMillis()) {
    //          record.setIsDone(1);
    //        } else if (record.getCheckTime().getTime() > System.currentTimeMillis()
    //            && record.getDownTime().getTime() > System.currentTimeMillis()
    //            && record.getUpTime().getTime() < System.currentTimeMillis()) {
    //          record.setIsDone(0);
    //        } else if (record.getUpTime() != null
    //            && record.getUpTime().getTime() > System.currentTimeMillis()) {
    //          record.setIsDone(-1);
    //        }
    //      }
    //    }
    Set<Long> orgIds =
        storeTreasureDtoIPage.getRecords().stream()
            .map(StoreTreasureDto::getSysOrgId)
            .collect(Collectors.toSet());
    if (orgIds.size() > 0) {

      LambdaQueryWrapper<SysOrg> que = Wrappers.lambdaQuery(SysOrg.class);
      que.in(SysOrg::getId, orgIds);
      Map<Long, SysOrg> map =
          sysOrgDao.selectList(que).stream().collect(Collectors.toMap(SysOrg::getId, o -> o));
      storeTreasureDtoIPage.getRecords().forEach(o -> o.setSysOrg(map.get(o.getSysOrgId())));
    }
    for (StoreTreasureDto record : storeTreasureDtoIPage.getRecords()) {
      record.setLinkInfo(record.getTransationId());
    }
    return storeTreasureDtoIPage;
  }

  @Autowired private SysBeforeUserDao sysBeforeUserDao;
  @Autowired private SysSeriesDao sysSeriesDao;
  @Autowired private SysUserDao sysUserDao;

  @Override
  public IPage<String> checkedUserPhone(PageSelectParam<Long> id) {
    return storeTreasureCheckUserDao.getCheckedUserPhone(
        new Page(id.getPageNum(), id.getPageSize()), id.getSelectParam());
  }

  @Override
  public void batchAdd(StoreTreasureAddDto dto) {
    save(dto);
  }

  @Override
  public List<StoreTreasureDto> getCouldCompById(Long id) {
    return storeTreasureDao.getCouldCompById(id);
  }

  @Override
  public String addCouldComp(AddCouldCompParam param, SysUserAdminDto adminUserData) {
    StoreTreasure storeTreasure = storeTreasureDao.selectById(param.getStId());
    if (storeTreasure == null) throw new RuntimeException("藏品id不正确");
    LambdaQueryWrapper<StoreTreasureNeeded> que = new LambdaQueryWrapper<>();
    que.eq(StoreTreasureNeeded::getStoreTreasureId, storeTreasure.getId());
    List<StoreTreasureNeeded> storeTreasureNeededs = storeTreasureNeededDao.selectList(que);
    if (storeTreasureNeededs.size() > 0) throw new RuntimeException("不可重复添加");
    storeTreasure.setCouldCompound(1);
    storeTreasureDao.updateById(storeTreasure);
    for (AddCouldCompParam.Item item : param.getItems()) {
      StoreTreasureNeeded storeTreasureNeeded = new StoreTreasureNeeded();
      storeTreasureNeeded.setStoreTreasureId(param.getStId());
      storeTreasureNeeded.setNeedStoreTreasureId(item.getNeedId());
      storeTreasureNeeded.setNeedCount(item.getNeedCount());
      storeTreasureNeeded.setCreateId(adminUserData.getId());
      storeTreasureNeeded.setCreateTime(new Date());
      storeTreasureNeededDao.insert(storeTreasureNeeded);
    }
    return "success";
  }

  @Override
  public void deleteById(Long id, SysUserAdminDto adminUserData) {
    StoreTreasureDto storeTreasure = getStoreTreasureById(id, null);
    if (storeTreasure == null) throw new RuntimeException("id异常");
    storeTreasure.setState(-1);
    storeTreasure.setUpdateTime(new Date());
    storeTreasure.setUpdateId(adminUserData.getId());
    updateDataById(storeTreasure.getId(), storeTreasure);
    if (storeTreasure.getTType() == 3) {
      List<StoreTreasure> storeTreasures = storeTreasureDao.selectByPid(storeTreasure.getId());
      for (StoreTreasure treasure : storeTreasures) {
        treasure.setState(-1);
        treasure.setUpdateTime(new Date());
        treasure.setUpdateId(adminUserData.getId());
        storeTreasureDao.updateById(treasure);
      }
    }
  }

  @Autowired private StoreTreasureRecordDao storeTreasureRecordDao;

  @Override
  public void joinBlodCheck(String s) {
    //    StoreTreasure storeTreasure = storeTreasureDao.selectById(Long.valueOf(s));
    //    try {
    //      if (storeTreasure.getTransationId() == null) {
    //        throw new Exception("交易hash异常,藏品id：" + s);
    //      }
    //      WenchangDDC bsnUtil = new WenchangDDC();
    //      BigInteger ddcId = bsnUtil.getDdcIdFromTxHash(storeTreasure.getTransationId());
    //      if (ddcId != null) {
    //        storeTreasure.setDdcId(ddcId + "");
    //        String ddcUrl = bsnUtil.getDdcUrl(ddcId);
    //        if (ddcUrl != null) {
    //          storeTreasure.setDdcUrl(ddcUrl);
    //        }
    //        storeTreasure.setState(1);
    //        int length = storeTreasure.getTotalCount().toString().length();
    //        List<StoreTreasureRecord> allRecord = new ArrayList<>();
    //        for (Integer i = 1; i < storeTreasure.getTotalCount() + 1; i++) {
    //          // 添加藏品记录
    //          StoreTreasureRecord record = new StoreTreasureRecord();
    //          record.setStrId(storeTreasure.getId());
    //          record.setStrNum(String.format("%0" + length + "d", i));
    //          record.setCreateId(storeTreasure.getCreateId());
    //          record.setCreateTime(new Date());
    //          record.setState(1);
    //          allRecord.add(record);
    //        }
    //        storeTreasureRecordDao.insertBatch(allRecord);
    //        storeTreasure.setLinkInfo(storeTreasure.getTransationId());
    //        storeTreasureDao.updateById(storeTreasure);
    //      } else {
    //        storeTreasure.setState(3);
    //        storeTreasureDao.updateById(storeTreasure);
    //      }
    //
    //    } catch (Exception e) {
    //      log.info("上链交易查询结果异常：" + s + " 异常信息：" + e.getMessage());
    //      log.info("====== 重新添加回队列=======");
    //      RBlockingQueue<Object> blockingFairQueue =
    //          redissonClient.getBlockingQueue("delay_queue_call");
    //      RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
    //      delayedQueue.offer("blodJoin_" + storeTreasure.getId(), 30, TimeUnit.SECONDS);
    //    }
  }

  @Override
  public void bindBoxAdd(BindBoxAddParam dto, SysUserAdminDto adminUserData) {
    if (sysOrgDao.selectById(dto.getSysOrgId()) == null) {
      throw new RuntimeException("发行方异常");
    }
    if (storeTreIosPriceDao.selectById(dto.getStripId()) == null) {
      throw new RuntimeException("ios价格异常");
    }
    StoreTreasure storeTreasure = new StoreTreasure();
    storeTreasure.setStripId(dto.getStripId());
    storeTreasure.setTType(3);
    storeTreasure.setRType(1);
    storeTreasure.setTreasureTitle(dto.getTreasureTitle());
    storeTreasure.setIndexImgPath(dto.getIndexImgPath());
    storeTreasure.setTotalCount(0);
    storeTreasure.setSurplusCount(0);
    storeTreasure.setFrostCount(0);
    storeTreasure.setPrice(dto.getPrice());
    storeTreasure.setUpTime(dto.getUpTime());
    storeTreasure.setSysOrgId(dto.getSysOrgId());
    storeTreasure.setRuleCount(dto.getRuleCount());
    storeTreasure.setCreateId(adminUserData.getId());
    storeTreasure.setCreateTime(new Date());
    storeTreasure.setState(1);
    storeTreasure.setAuthInfo(dto.getAuthInfo());
    storeTreasure.setNeedKnow(sysDictonaryDao.selectByAlias("about_treasure"));
    storeTreasure.setLinkInfo(dto.getLinkInfo());
    storeTreasure.setIntroduce(dto.getIntroduce());
    storeTreasureDao.insert(storeTreasure);
  }

  @Override
  public void bindBoxUpdate(BindBoxAddParam dto, SysUserAdminDto adminUserData) {
    if (sysOrgDao.selectById(dto.getSysOrgId()) == null) {
      throw new RuntimeException("发行方异常");
    }
    if (storeTreIosPriceDao.selectById(dto.getStripId()) == null) {
      throw new RuntimeException("ios价格异常");
    }
    StoreTreasure storeTreasure = storeTreasureDao.selectById(dto.getId());
    if (storeTreasure != null) {
      if (storeTreasure.getTType() != 3) {
        throw new RuntimeException("藏品id异常");
      }
      // 修改主藏品
      storeTreasure.setStripId(dto.getStripId());
      storeTreasure.setTType(3);
      storeTreasure.setRType(1);
      storeTreasure.setTreasureTitle(dto.getTreasureTitle());
      storeTreasure.setIndexImgPath(dto.getIndexImgPath());
      storeTreasure.setTotalCount(0);
      storeTreasure.setSurplusCount(0);
      storeTreasure.setFrostCount(0);
      storeTreasure.setPrice(dto.getPrice());
      storeTreasure.setSysOrgId(dto.getSysOrgId());
      storeTreasure.setRuleCount(dto.getRuleCount());
      storeTreasure.setUpdateId(adminUserData.getId());
      storeTreasure.setUpdateTime(new Date());
      storeTreasure.setUpTime(dto.getUpTime());
      storeTreasure.setAuthInfo(dto.getAuthInfo());
      storeTreasure.setLinkInfo(dto.getLinkInfo());
      storeTreasure.setNeedKnow(sysDictonaryDao.selectByAlias("about_treasure"));
      storeTreasure.setIntroduce(dto.getIntroduce());
      storeTreasureDao.updateById(storeTreasure);
    } else {
      throw new RuntimeException("盲盒藏品主id异常");
    }
  }

  @Override
  public IPage<StoreTreasureDto> getBindListByPage(
      Class<StoreTreasureDto> storeTreasureDtoClass,
      Integer pageNum,
      long pageSize,
      LambdaQueryWrapper<StoreTreasure> queryWrapper) {
    IPage<StoreTreasureDto> selectPageList =
        selectPageList(storeTreasureDtoClass, pageNum, pageSize, queryWrapper);
    Set<Long> collect =
        selectPageList.getRecords().stream()
            .map(StoreTreasureDto::getSysOrgId)
            .collect(Collectors.toSet());
    collect.add(-1111L);
    Map<Long, SysOrg> map =
        sysOrgDao.selectBatchIds(collect).stream().collect(Collectors.toMap(SysOrg::getId, o -> o));

    List<Long> stripIds =
        selectPageList.getRecords().stream()
            .map(StoreTreasureDto::getStripId)
            .collect(Collectors.toList());
    stripIds.add(-1111L);
    Map<Long, StoreTreIosPrice> stripMap =
        storeTreIosPriceDao.selectBatchIds(stripIds).stream()
            .collect(Collectors.toMap(StoreTreIosPrice::getId, o -> o));

    List<Long> allIds =
        selectPageList.getRecords().stream()
            .map(StoreTreasureDto::getId)
            .collect(Collectors.toList());
    allIds.add(-1111L);
    LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery(StoreTreasure.class);
    que.in(StoreTreasure::getPId, allIds);
    que.ne(BaseEntity::getState, -1);
    List<StoreTreasure> allChildrens = storeTreasureDao.selectList(que);
    if (collect.size() > 0) {
      for (StoreTreasureDto record : selectPageList.getRecords()) {
        if (record.getStripId() != null) {
          record.setStoreTreIosPrice(stripMap.get(record.getStripId()));
        }
        record.setSysOrg(map.get(record.getSysOrgId()));
        record.setChildren(
            allChildrens.stream()
                .filter(o -> o.getPId() != null && o.getPId().equals(record.getId()))
                .collect(Collectors.toList()));
        List<String> allImg =
            record.getChildren().stream()
                .map(StoreTreasure::getIndexImgPath)
                .collect(Collectors.toList());
        record.setHeadImgPath(Strings.join(allImg, ';'));
        record.setSurplusCount(
            record.getChildren().stream().mapToInt(StoreTreasure::getSurplusCount).sum());
        record.setTotalCount(
            record.getChildren().stream().mapToInt(StoreTreasure::getTotalCount).sum());
      }
    }
    return selectPageList;
  }

  @Override
  public IPage<StoreTreasureDto> getAppBindListByPage(
      Class<StoreTreasureDto> storeTreasureDtoClass,
      Integer pageNum,
      long pageSize,
      LambdaQueryWrapper<StoreTreasure> queryWrapper,
      UserDto userData) {
    queryWrapper.eq(BaseEntity::getState, 1);
    IPage<StoreTreasureDto> selectPageList =
        selectPageList(storeTreasureDtoClass, pageNum, pageSize, queryWrapper);
    Set<Long> collect =
        selectPageList.getRecords().stream()
            .map(StoreTreasureDto::getSysOrgId)
            .collect(Collectors.toSet());
    collect.add(-1111L);
    Map<Long, SysOrg> map =
        sysOrgDao.selectBatchIds(collect).stream().collect(Collectors.toMap(SysOrg::getId, o -> o));

    List<Long> stripIds =
        selectPageList.getRecords().stream()
            .map(StoreTreasureDto::getStripId)
            .collect(Collectors.toList());
    stripIds.add(-1111L);
    Map<Long, StoreTreIosPrice> stripMap =
        storeTreIosPriceDao.selectBatchIds(stripIds).stream()
            .collect(Collectors.toMap(StoreTreIosPrice::getId, o -> o));

    List<Long> allIds =
        selectPageList.getRecords().stream()
            .map(StoreTreasureDto::getId)
            .collect(Collectors.toList());
    allIds.add(-1111L);
    LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery(StoreTreasure.class);
    que.in(StoreTreasure::getPId, allIds);
    que.ne(StoreTreasure::getFromUser, 1);
    que.eq(BaseEntity::getState, 1);
    List<StoreTreasure> allChildrens = storeTreasureDao.selectList(que);
    if (collect.size() > 0) {
      for (StoreTreasureDto record : selectPageList.getRecords()) {
        if (record.getStripId() != null) {
          record.setStoreTreIosPrice(stripMap.get(record.getStripId()));
        }
        record.setSysOrg(map.get(record.getSysOrgId()));
        record.setChildren(
            allChildrens.stream()
                .filter(
                    o ->
                        o.getPId() != null
                            && o.getPId().equals(record.getId())
                            && o.getFromUser() != 1)
                .collect(Collectors.toList()));
        List<String> allImg =
            record.getChildren().stream()
                .map(StoreTreasure::getIndexImgPath)
                .collect(Collectors.toList());
        record.setHeadImgPath(Strings.join(allImg, ';'));
        record.setSurplusCount(
            record.getChildren().stream().mapToInt(StoreTreasure::getSurplusCount).sum());
        record.setTotalCount(
            record.getChildren().stream().mapToInt(StoreTreasure::getTotalCount).sum());
        if (record.getSurplusCount() > 0) {
          if (record.getUpTime() != null
              && record.getUpTime().getTime() < System.currentTimeMillis()) {
            record.setIsDone(1);
          } else {
            record.setIsDone(0);
          }
        } else {
          record.setIsDone(-1);
        }
      }
    }
    if (userData != null) {
      LambdaQueryWrapper<SysBeforeUser> befQue = Wrappers.lambdaQuery(SysBeforeUser.class);
      befQue.eq(SysBeforeUser::getPhone, userData.getPhone());
      List<SysBeforeUser> sysBeforeUsers = sysBeforeUserDao.selectList(befQue);
      Set<Long> ids =
          sysBeforeUsers.stream().map(SysBeforeUser::getStreaId).collect(Collectors.toSet());
      selectPageList
          .getRecords()
          .forEach(
              o -> {
                if (ids.contains(o.getId())) {
                  o.setHasBefore(1);
                } else {
                  o.setHasBefore(0);
                }
              });
    }
    return selectPageList;
  }

  @Override
  public IPage<StoreTreasureDto> bindBoxItemByPage(PageSelectParam<Long> param) {
    LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery(StoreTreasure.class);
    if (param.getSelectParam() != null) {
      que.eq(StoreTreasure::getPId, param.getSelectParam());
    }
    que.eq(StoreTreasure::getTType, 4);
    que.ne(BaseEntity::getState, -1);
    Page<StoreTreasureDto> page =
        storeTreasureDao.selectByQue(new Page(param.getPageNum(), param.getPageSize()), que);
    Set<Long> collect =
        page.getRecords().stream().map(StoreTreasureDto::getSysOrgId).collect(Collectors.toSet());
    collect.add(-1111L);
    Map<Long, SysOrg> orgMap =
        sysOrgDao.selectBatchIds(collect).stream().collect(Collectors.toMap(SysOrg::getId, o -> o));

    List<Long> stripIds =
        page.getRecords().stream().map(StoreTreasureDto::getStripId).collect(Collectors.toList());
    stripIds.add(-1111L);
    Map<Long, StoreTreIosPrice> stripMap =
        storeTreIosPriceDao.selectBatchIds(stripIds).stream()
            .collect(Collectors.toMap(StoreTreIosPrice::getId, o -> o));
    if (page.getRecords().size() > 0) {
      for (StoreTreasureDto o : page.getRecords()) {
        o.setSysOrg(orgMap.get(o.getSysOrgId()));
        o.setStoreTreIosPrice(stripMap.get(o.getStripId()));
      }
    }
    return page;
  }

  @Override
  public void bindBoxItemAdd(BindBoxItemAddDto dto, SysUserAdminDto adminUserData) {
    StoreTreasure main = storeTreasureDao.selectById(dto.getPId());
    if (main == null) throw new RuntimeException("主id异常");

    StoreTreasure record = new StoreTreasure();
    fullParam(dto, adminUserData, main, record);
    storeTreasureDao.insert(record);
  }

  @Override
  public void bindBoxItemUpdate(BindBoxItemAddDto dto, SysUserAdminDto adminUserData) {
    StoreTreasure main = storeTreasureDao.selectById(dto.getPId());
    if (main == null) throw new RuntimeException("主id异常");
    StoreTreasure old = storeTreasureDao.selectById(dto.getId());
    if (old == null) throw new RuntimeException("子藏品id异常");

    StoreTreasure record = new StoreTreasure();
    record.setId(dto.getId());
    fullParam(dto, adminUserData, main, record);
    if (old.getState() == 3) {
      record.setTotalCount(dto.getTotalCount());
      record.setSurplusCount(old.getTotalCount());
    } else {
      if (dto.getTotalCount().compareTo(old.getTotalCount()) != 0) {
        throw new RuntimeException("该藏品已上链无法修改总量");
      }
      record.setSurplusCount(old.getSurplusCount());
    }
    record.setState(old.getState());
    record.setCreateId(old.getCreateId());
    record.setCreateTime(old.getCreateTime());
    record.setTransationId(old.getTransationId());
    record.setDdcId(old.getDdcId());
    record.setDdcUrl(old.getDdcUrl());
    storeTreasureDao.updateById(record);
  }

  @Override
  public IPage<CalendarResult> launchCalendar(PageSelectParam<Object> param) {
    LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery(StoreTreasure.class);
    que.eq(BaseEntity::getState, 1);
    que.eq(StoreTreasure::getTType, 0);
    que.eq(StoreTreasure::getFromUser, 0);
    List<StoreTreasureDto> storeTreasures = selectList(StoreTreasureDto.class, que);
    List<Long> sysOrgIds =
        storeTreasures.stream().map(StoreTreasureDto::getSysOrgId).collect(Collectors.toList());
    sysOrgIds.add(-1111L);
    Map<Long, SysOrg> orgMap =
        sysOrgDao.selectBatchIds(sysOrgIds).stream()
            .collect(Collectors.toMap(SysOrg::getId, o -> o));
    HashMap<String, List<StoreTreasureDto>> tmpMap = new HashMap<>();
    SimpleDateFormat sp = new SimpleDateFormat("yyyyMMdd");
    for (StoreTreasureDto storeTreasure : storeTreasures) {
      String date = storeTreasure.getUpTime() != null ? sp.format(storeTreasure.getUpTime()) : "";
      List<StoreTreasureDto> storeTreasureDtos = tmpMap.get(date);
      storeTreasure.setSysOrg(orgMap.get(storeTreasure.getSysOrgId()));
      if (storeTreasure.getSurplusCount() > 0) {
        if (storeTreasure.getUpTime() != null
            && storeTreasure.getUpTime().getTime() < System.currentTimeMillis()) {
          storeTreasure.setIsDone(1);
        } else {
          storeTreasure.setIsDone(-1);
        }
      } else {
        storeTreasure.setIsDone(-1);
      }
      if (storeTreasureDtos == null) {
        storeTreasureDtos = new ArrayList<>();
        storeTreasureDtos.add(storeTreasure);
        tmpMap.put(date, storeTreasureDtos);
      } else {
        storeTreasureDtos.add(storeTreasure);
      }
    }
    List<CalendarResult> t = new ArrayList<>();
    tmpMap.keySet().stream()
        .sorted((o1, o2) -> Long.parseLong(o1) - (Long.parseLong(o2)) > 0 ? -1 : 1)
        .forEach(
            o -> {
              List<StoreTreasureDto> storeTreasureDtos = tmpMap.get(o);
              CalendarResult re = new CalendarResult();
              re.setDate(o);
              re.setStoreTreasureDtoList(storeTreasureDtos);
              t.add(re);
            });

    return getPageInfo(param.getPageNum(), param.getPageSize(), t);
  }

  @Override
  public IPage<StoreTreasureDto> turnPage(PageSelectParam<TurnParam> param) {
    Page<StoreTreasureDto> page =
        storeTreasureDao.turnPage(
            new Page<>(param.getPageNum(), param.getPageSize()), param.getSelectParam());
    if (page.getRecords().size() > 0) {
      Set<Long> orgIds =
          page.getRecords().parallelStream()
              .map(StoreTreasureDto::getSysOrgId)
              .collect(Collectors.toSet());
      Map<Long, SysOrg> orgMap =
          sysOrgDao.selectBatchIds(orgIds).parallelStream()
              .collect(Collectors.toMap(SysOrg::getId, o -> o));
      for (StoreTreasureDto o : page.getRecords()) {
        o.setSysOrg(orgMap.get(o.getSysOrgId()));
      }
    }
    return page;
  }

  @Override
  public StoreTreasure selectById(Long valueOf) {
    return storeTreasureDao.selectById(valueOf);
  }

  @Override
  public void updateById(StoreTreasure storeTreasure) {
    storeTreasureDao.updateById(storeTreasure);
  }

  public static IPage<CalendarResult> getPageInfo(
      int currentPage, int pageSize, List<CalendarResult> list) {
    int total = list.size();
    if (total > pageSize) {
      int toIndex = pageSize * currentPage;
      if (toIndex > total) {
        toIndex = total;
      }
      int totalPage = total % pageSize == 0 ? (total / pageSize) : (total / pageSize) + 1;
      if (totalPage < currentPage) {
        list = new ArrayList<>();
      } else {
        list = list.subList(pageSize * (currentPage - 1), toIndex);
      }
    } else {
      list = currentPage == 1 ? list : new ArrayList<>();
    }
    Page<CalendarResult> page = new Page<>(currentPage, pageSize);
    page.setRecords(list);
    page.setPages((total + pageSize - 1) / pageSize);
    page.setTotal(total);
    page.setCurrent(currentPage);
    page.setSize(pageSize);

    return page;
  }

  private void fullParam(
      BindBoxItemAddDto dto,
      SysUserAdminDto adminUserData,
      StoreTreasure main,
      StoreTreasure record) {
    record.setPId(dto.getPId());
    record.setStripId(main.getStripId());
    record.setTType(4);
    record.setRType(1);
    record.setTNum(dto.getTNum());
    record.setTreasureTitle(dto.getTreasureTitle());
    record.setIndexImgPath(dto.getIndexImgPath());
    record.setDetail(dto.getDetail());
    record.setHeadImgPath(dto.getHeadImgPath());
    record.setTotalCount(dto.getTotalCount());
    record.setSurplusCount(dto.getTotalCount());
    record.setFrostCount(0);
    record.setPrice(main.getPrice());
    record.setSense(dto.getSense());
    record.setCouldSale(dto.getCouldSale());
    record.setUpTime(main.getUpTime());
    record.setDownTime(main.getDownTime());
    record.setCouldCompound(0);
    record.setSysOrgId(dto.getSysOrgId());
    record.setRuleCount(0);
    record.setCreateId(adminUserData.getId());
    record.setCreateTime(new Date());
    record.setState(3);
    record.setSysCateId(dto.getSysCateId());
    record.setSysOrgId(dto.getSysOrgId());
    record.setIntroduce(dto.getIntroduce());
    record.setAuthInfo(dto.getAuthInfo());
    record.setLinkInfo(dto.getLinkInfo());
  }

  @Autowired private AvataRecordDao avataRecordDao;

  @Override
  public void joinBlod(String id) {
    StoreTreasure storeTreasure = storeTreasureDao.selectById(id);

    if (storeTreasure == null) {
      throw new RuntimeException("藏品id异常");
    }
    if (storeTreasure.getState() == 2) {
      throw new RuntimeException("该藏品正在上链中");
    }
    //    if (storeTreasure.getTotalCount().intValue() > 10) {
    //      throw new RuntimeException("批量上链数量不可超过10");
    //    }
    // state :-1逻辑删除0禁用1正常(已上链）2上链中3未上链
    if (storeTreasure.getState() == 3) {
      if ((storeTreasure.getRType() != null && storeTreasure.getRType() == 1)
          || storeTreasure.getCouldCompound() == 1) {
        Integer totalCount = storeTreasure.getTotalCount();
        int times = totalCount / 10;
        int surplu = totalCount % 10;
        try {
          if (times > 0) {
            for (int i = 0; i < times; i++) {
              Thread.sleep(1000L);
              AvataRecord avataRecord = new AvataRecord();
              avataRecord.setStrId(storeTreasure.getId());
              avataRecord.setTmpId(UUID.randomUUID().toString().replaceAll("-", ""));
              avataRecordDao.insert(avataRecord);
              try {
                storeTreasure.setTotalCount(10);
                upBlock(storeTreasure, avataRecord.getTmpId());
              } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("上链异常，请联系平台管理员，异常内容：" + e.getMessage());
              }
            }
            Thread.sleep(1000L);
            if (surplu > 0) {
              try {
                AvataRecord avataRecord = new AvataRecord();
                avataRecord.setStrId(storeTreasure.getId());
                avataRecord.setTmpId(UUID.randomUUID().toString().replaceAll("-", ""));
                avataRecordDao.insert(avataRecord);
                storeTreasure.setTotalCount(surplu);
                upBlock(storeTreasure, avataRecord.getTmpId());
              } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("上链异常，请联系平台管理员，异常内容：" + e.getMessage());
              }
            }
          } else {
            if (surplu > 0) {
              try {
                AvataRecord avataRecord = new AvataRecord();
                avataRecord.setStrId(storeTreasure.getId());
                avataRecord.setTmpId(UUID.randomUUID().toString().replaceAll("-", ""));
                avataRecordDao.insert(avataRecord);
                storeTreasure.setTotalCount(surplu);
                upBlock(storeTreasure, avataRecord.getTmpId());
              } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("上链异常，请联系平台管理员，异常内容：" + e.getMessage());
              }
            }
          }
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    } else {
      if (storeTreasure.getState() == 0) {
        throw new RuntimeException("藏品已冻结，请先解冻");
      }
      if (storeTreasure.getState() == 1) {
        throw new RuntimeException("该藏品已上链");
      }
      throw new RuntimeException("藏品状态异常");
    }
  }

  private void upBlock(StoreTreasure treasure, String tmpId) throws Exception {
    AvataUtil.mint(
        treasure.getIndexImgPath(), tmpId, treasure.getTotalCount(), treasure.getTreasureTitle());
  }

  @Override
  public StoreTreasureDto getStoreTreasureById(Long id, UserDto userData) {
    Optional<StoreTreasureDto> optional = selectDataById(StoreTreasureDto.class, id);

    if (userData != null && optional.isPresent()) {
      LambdaQueryWrapper<OrderTreasurePool> que = new LambdaQueryWrapper<>();
      que.eq(BaseEntity::getCreateId, userData.getId())
          .eq(OrderTreasurePool::getItemType, 1)
          .eq(OrderTreasurePool::getTeaPoId, optional.get().getId());
      List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(que);
      LambdaQueryWrapper<StoreTreasureCheckUser> q = Wrappers.lambdaQuery();
      q.eq(StoreTreasureCheckUser::getUId, userData.getId())
          .eq(StoreTreasureCheckUser::getStoreTreasureId, optional.get().getId());
      List<StoreTreasureCheckUser> storeTreasureCheckUsers =
          storeTreasureCheckUserDao.selectList(q);
      List<OrderTreasurePool> collect =
          orderTreasurePools.stream()
              .filter(o -> o.getTeaPoId().equals(optional.get().getId()))
              .collect(Collectors.toList());
      optional.get().setIsBuy(collect.size() > 0);
      List<StoreTreasureCheckUser> collect1 =
          storeTreasureCheckUsers.stream()
              .filter(o -> o.getStoreTreasureId().equals(optional.get().getId()))
              .collect(Collectors.toList());
      StoreTreasureDto storeTreasureDto = optional.get();
      if (storeTreasureDto.getTType() == 1) {
        LambdaQueryWrapper<OrderTreasurePool> ques = new LambdaQueryWrapper<>();
        ques.eq(OrderTreasurePool::getItemType, 1)
            .eq(OrderTreasurePool::getTeaPoId, storeTreasureDto.getId())
            .eq(OrderTreasurePool::getPayType, -1);
        orderTreasurePools = orderTreasurePoolDao.selectList(ques);
        Long count =
            orderTreasurePools.stream()
                .filter(o -> o.getTeaPoId().equals(storeTreasureDto.getId()))
                .count();
        storeTreasureDto.setFrostCount(Integer.valueOf(count + ""));
        StoreTreasureCheckUser storeTreasureCheckUser =
            storeTreasureCheckUserDao.selectByIdAndUid(userData.getId(), storeTreasureDto.getId());
        // 中签状态-1未中1已中2未开始3未参与
        List<OrderTreasurePool> joinOrders =
            orderTreasurePools.stream()
                .filter(o -> o.getCreateId().equals(userData.getId()))
                .collect(Collectors.toList());
        if (storeTreasureDto.getUpTime() != null
            && storeTreasureDto.getUpTime().getTime() > System.currentTimeMillis()) {
          storeTreasureDto.setCheckState(2);
        } else if (joinOrders.size() == 0) {
          storeTreasureDto.setCheckState(3);
        } else if (storeTreasureCheckUser != null) {
          storeTreasureDto.setCheckState(1);
        } else {
          storeTreasureDto.setCheckState(-1);
        }
        // -1未开始  0报名中  1已结束（超前申购特有属性）2已完结
        if (storeTreasureDto.getDownTime().getTime() < System.currentTimeMillis()) {
          storeTreasureDto.setIsDone(2);
        } else if ((storeTreasureDto.getCheckTime() != null
                && storeTreasureDto.getCheckTime().getTime() < System.currentTimeMillis())
            && storeTreasureDto.getDownTime() != null
            && storeTreasureDto.getDownTime().getTime() > System.currentTimeMillis()) {
          storeTreasureDto.setIsDone(1);
        } else if (storeTreasureDto.getCheckTime().getTime() > System.currentTimeMillis()
            && storeTreasureDto.getDownTime().getTime() > System.currentTimeMillis()
            && storeTreasureDto.getUpTime().getTime() < System.currentTimeMillis()) {
          storeTreasureDto.setIsDone(0);
        } else if (storeTreasureDto.getUpTime() != null
            && storeTreasureDto.getUpTime().getTime() > System.currentTimeMillis()) {
          storeTreasureDto.setIsDone(-1);
        }
        return storeTreasureDto;
      }
      storeTreasureDto.setNeededRes(storeTreasureNeededService.getNeededList(id, userData));
      if (storeTreasureDto.getStripId() != null) {
        storeTreasureDto.setStoreTreIosPrice(
            storeTreIosPriceDao.selectById(storeTreasureDto.getStripId()));
      }
    }
    return optional.orElse(null);
  }

  @Autowired private StoreTreasureNeededService storeTreasureNeededService;
  @Autowired private SysPlateConfigService sysPlateConfigService;
  @Autowired private StoreTreasureNeededDao storeTreasureNeededDao;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private SysDictonaryDao sysDictonaryDao;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(StoreTreasureAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreTreasure dto = new StoreTreasure();
    md.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    md.map(tmpDto, dto);
    LambdaQueryWrapper<StoreTreasure> que = new LambdaQueryWrapper<>();
    que.eq(StoreTreasure::getTNum, tmpDto.getTNum());
    List<StoreTreasure> storeTreasures = storeTreasureDao.selectList(que);
    if (storeTreasures.size() > 0) {
      throw new RuntimeException("藏品编号已被占用");
    }
    que = new LambdaQueryWrapper<>();
    que.eq(StoreTreasure::getTreasureTitle, tmpDto.getTreasureTitle());
    storeTreasures = storeTreasureDao.selectList(que);
    if (storeTreasures.size() > 0) {
      throw new RuntimeException("藏品名称已被占用");
    }
    dto.setNeedKnow(sysDictonaryDao.selectByAlias("about_treasure"));
    dto.setState(3);
    dto.setSurplusCount(dto.getTotalCount());
    dto.setTurnMaxPrice(tmpDto.getTurnMaxPrice());
    dto.setTurnMinPrice(tmpDto.getTurnMinPrice());
    storeTreasureDao.insert(dto);
    if (tmpDto.getCouldCompound() != null
        && tmpDto.getCouldCompound() == 1
        && tmpDto.getNeededTreId() != null) {
      for (TreaNeedParam id : tmpDto.getNeededTreId()) {
        StoreTreasureNeeded storeTreasureNeeded = new StoreTreasureNeeded();
        storeTreasureNeeded.setStoreTreasureId(dto.getId());
        storeTreasureNeeded.setNeedStoreTreasureId(id.getId());
        storeTreasureNeeded.setNeedCount(id.getCount());
        storeTreasureNeeded.setCreateId(tmpDto.getCreateId());
        storeTreasureNeeded.setCreateTime(new Date());
        storeTreasureNeededDao.insert(storeTreasureNeeded);
      }
    }
  }

  @Autowired private RedissonClient redissonClient;

  private String getLinkAddress(StoreTreasure dto) {

    return UUID.randomUUID().toString();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(StoreTreasureUpdateDto tmpDto, SysUserAdminDto adminUserData) {
    ModelMapper md = new ModelMapper();
    StoreTreasureDto dto = new StoreTreasureDto();
    md.map(tmpDto, dto);
    dto.setUpdateId(adminUserData.getId());
    dto.setUpdateTime(new Date());
    StoreTreasure old = storeTreasureDao.selectById(tmpDto.getId());
    if (old == null) {
      throw new RuntimeException("id异常");
    }
    if (old.getState() == 1 && old.getTotalCount().compareTo(tmpDto.getTotalCount()) != 0) {
      throw new RuntimeException("藏品已完成上链，总量不允许修改");
    } else {
      dto.setSurplusCount(old.getSurplusCount());
      dto.setState(old.getState());
    }
    dto.setNeedKnow(sysDictonaryDao.selectByAlias("about_treasure"));
    updateDataById(dto.getId(), dto);
    //    if (tmpDto.getTType() == 1) {
    //      if (old.getCheckTime().getTime() < System.currentTimeMillis()) {
    //        if (old.getCheckTime().getTime() != tmpDto.getCheckTime().getTime()) {
    //          throw new RuntimeException("藏品已开奖，无法修改开奖时间");
    //        }
    //      }
    //      System.out.println((dto.getCheckTime().getTime() - System.currentTimeMillis()) / 1000);
    //      Boolean delete = redisTemplate.delete("storeTreasureCheck_" + dto.getId());
    //      // 超前申购定时抽签问题
    //      RBlockingQueue<Object> blockingFairQueue =
    //          redissonClient.getBlockingQueue("delay_queue_call");
    //      RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
    //      delayedQueue.offer(
    //          "storeTreasureCheck_" + dto.getId(),
    //          (dto.getCheckTime().getTime() - System.currentTimeMillis()) / 1000,
    //          TimeUnit.SECONDS);
    //    }
    if (tmpDto.getCouldCompound() != null && tmpDto.getCouldCompound() == 1) {
      LambdaQueryWrapper<StoreTreasureNeeded> que = Wrappers.lambdaQuery(StoreTreasureNeeded.class);
      que.eq(StoreTreasureNeeded::getStoreTreasureId, tmpDto.getId());
      storeTreasureNeededDao.delete(que);
      if (tmpDto.getNeededTreId() != null && tmpDto.getNeededTreId().size() > 0) {
        for (TreaNeedParam id : tmpDto.getNeededTreId()) {
          StoreTreasureNeeded storeTreasureNeeded = new StoreTreasureNeeded();
          storeTreasureNeeded.setStoreTreasureId(dto.getId());
          storeTreasureNeeded.setNeedStoreTreasureId(id.getId());
          storeTreasureNeeded.setNeedCount(id.getCount());
          storeTreasureNeeded.setCreateId(adminUserData.getId());
          storeTreasureNeeded.setCreateTime(new Date());
          storeTreasureNeededDao.insert(storeTreasureNeeded);
        }
      }
    }
  }

  @Override
  public void updateState(StoreTreasureDto dto) {
    StoreTreasureDto storeTreasure = getStoreTreasureById(dto.getId(), null);
    if (storeTreasure == null) throw new RuntimeException("id异常");
    if (storeTreasure.getTType() != 3) {
      if (storeTreasure.getDdcId() == null && storeTreasure.getDdcUrl() == null) {
        throw new RuntimeException("对不起上链未成功的商品无法修改状态");
      }
    }
    if (storeTreasure.getState() == 1) {
      storeTreasure.setState(0);
    } else {
      storeTreasure.setState(1);
    }
    storeTreasure.setUpdateTime(new Date());
    storeTreasure.setUpdateId(dto.getUpdateId());
    updateDataById(storeTreasure.getId(), storeTreasure);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<StoreTreasureDto> list(StoreTreasureDto dto) {
    LambdaQueryWrapper<StoreTreasure> queryWrapper = new LambdaQueryWrapper<>();

    return selectList(StoreTreasureDto.class, queryWrapper);
  }
}
