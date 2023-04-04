package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.customResult.MyTreasure;
import com.qy.ntf.bean.customResult.NeededRes;
import com.qy.ntf.bean.customResult.TreaTrading;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.*;
import com.qy.ntf.dao.*;
import com.qy.ntf.service.OrderTreasurePoolService;
import com.qy.ntf.service.StoreProPoolService;
import com.qy.ntf.service.StoreTreasureNeededService;
import com.qy.ntf.util.PageSelectParam;
import com.qy.ntf.util.ResourcesUtil;
import com.qy.ntf.util.WenchangDDC;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-05-28 15:09:30 DESC : service服务实现
 */
@Service("storeTreasureNeededService")
@Slf4j
public class StoreTreasureNeededServiceImpl implements StoreTreasureNeededService {

  @Autowired private StoreTreasureNeededDao storeTreasureNeededDao;

  @Override
  public BaseMapper<StoreTreasureNeeded> getDao() {
    return storeTreasureNeededDao;
  }

  @Override
  public IPage<StoreTreasureNeededDto> getListByPage(
      Class<StoreTreasureNeededDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreTreasureNeeded> queryWrapper) {
    IPage<StoreTreasureNeededDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public IPage<TreaTrading> tradingPage(PageSelectParam<Integer> param, UserDto userData) {
    IPage<TreaTrading> treaTradingIPage =
        storeTreasureNeededDao.tradingPage(
            new Page<>(param.getPageNum(), param.getPageSize()),
            param.getSelectParam(),
            userData.getId());
    if (treaTradingIPage.getRecords().size() > 0) {
      Map<Long, StoreTreasure> collect =
          storeTreasureDao
              .selectBatchIds(
                  treaTradingIPage.getRecords().stream()
                      .map(TreaTrading::getSTreasureId)
                      .collect(Collectors.toSet()))
              .stream()
              .collect(Collectors.toMap(StoreTreasure::getId, o -> o));
      for (TreaTrading o : treaTradingIPage.getRecords()) {
        StoreTreasure storeTreasure = collect.get(o.getSTreasureId());
        if (storeTreasure != null) {
          if (storeTreasure.getCouldCompound() == 1) {
            o.setImgPath(storeTreasure.getHeadImgPath());
          } else {
            o.setImgPath(storeTreasure.getIndexImgPath());
          }
        }
      }
    }
    return treaTradingIPage;
  }

  @Autowired SysOrgDao sysOrgDao;

  @Override
  public List<StoreTreasureDto> getColudNeededList() {

    List<StoreTreasureDto> neededList = storeTreasureNeededDao.getNeededList();
    if (neededList.size() > 0) {
      List<SysOrg> sysOrgs =
          sysOrgDao.selectBatchIds(
              neededList.stream().map(StoreTreasureDto::getSysOrgId).collect(Collectors.toList()));
      Map<Long, SysOrg> orgMap = sysOrgs.stream().collect(Collectors.toMap(SysOrg::getId, o -> o));
      for (StoreTreasureDto storeTreasure : neededList) {
        storeTreasure.setSysOrg(orgMap.get(storeTreasure.getSysOrgId()));
      }
    }
    return neededList;
  }

  @Autowired private OrderTreasurePoolDao orderTreasurePoolDao;
  @Autowired private StoreTreasureDao storeTreasureDao;

  @Override
  public List<NeededRes> getNeededList(Long id, UserDto userData) {
    LambdaQueryWrapper<StoreTreasureNeeded> que = new LambdaQueryWrapper<>();
    que.eq(StoreTreasureNeeded::getStoreTreasureId, id);
    List<StoreTreasureNeeded> storeTreasureNeededs = storeTreasureNeededDao.selectList(que);
    List<MyTreasure> userTreasure =
        storeProPoolService.getMytreasure(new MyTreasure(), userData); // 用户持有的藏品
    List<Long> teIds =
        storeTreasureNeededs.stream()
            .map(StoreTreasureNeeded::getNeedStoreTreasureId)
            .collect(Collectors.toList());
    List<StoreTreasure> storeTreasures =
        teIds.size() > 0 ? storeTreasureDao.selectBatchIds(teIds) : new ArrayList<>();
    //    storeTreasures =
    //        storeTreasures.stream().filter(o -> o.getState() == 1).collect(Collectors.toList());
    ArrayList<NeededRes> result = new ArrayList<>();
    for (StoreTreasureNeeded storeTreasureNeeded : storeTreasureNeededs) {
      List<StoreTreasure> collect =
          storeTreasures.stream()
              .filter(o -> o.getId().equals(storeTreasureNeeded.getNeedStoreTreasureId()))
              .collect(Collectors.toList());
      if (collect.size() > 0) {
        NeededRes neededRes = new NeededRes();
        neededRes.setTType(collect.get(0).getTType());
        neededRes.setNeedId(collect.get(0).getId());
        neededRes.setTitle(collect.get(0).getTreasureTitle());
        neededRes.setHeadImg(collect.get(0).getIndexImgPath());
        neededRes.setNeedCount(storeTreasureNeeded.getNeedCount());

        neededRes.setHasCount(
            (int)
                userTreasure.stream()
                    .filter(
                        o ->
                            Objects.equals(
                                o.getStoreTreasure().getDdcId(), collect.get(0).getDdcId()))
                    .count());
        result.add(neededRes);
      }
    }
    return result;
  }

  //  @Autowired private StoreProPoolDao storeProPoolDao;
  @Autowired private SysUserDao sysUserDao;
  @Autowired private SysUserBackpackDao sysUserBackpackDao;
  @Autowired private StoreProPoolService storeProPoolService;

  @Override
  @Transactional
  public String addCompound(Long id, UserDto userData) {
    //    LambdaQueryWrapper<StoreProPool> que = new LambdaQueryWrapper<>();
    //    que.eq(BaseEntity::getCreateId, userData.getId()).eq(StoreProPool::getIsCompound, 1);
    //    List<StoreProPool> storeProPools = storeProPoolDao.selectList(que);
    //    if (storeProPools.size() > 0) throw new RuntimeException("用户至多合成一个藏品");
    StoreTreasure storeTreasure1 = storeTreasureDao.selectById(id);
    if (storeTreasure1.getDownTime() != null
        && storeTreasure1.getDownTime().getTime() < System.currentTimeMillis()) {
      throw new RuntimeException("合成时间已截止");
    }
    if (storeTreasure1.getUpTime() != null
        && storeTreasure1.getUpTime().getTime() > System.currentTimeMillis()) {
      throw new RuntimeException("合成时间未开始");
    }
    if (storeTreasure1.getState() != 1) {
      throw new RuntimeException("藏品已下架");
    }
    if (storeTreasure1.getSurplusCount() < 1) {
      throw new RuntimeException("库存不足");
    }
    List<StoreTreasureNeeded> storeTreasureNeededs =
        storeTreasureNeededDao.selectByStoreTreasureId(id);
    if (storeTreasureNeededs != null && storeTreasureNeededs.size() == 0) {
      throw new RuntimeException("合成藏品所需数量为0，待平台补充后方可合成该藏品");
    }
    List<Long> needIds =
        storeTreasureNeededs.stream()
            .map(StoreTreasureNeeded::getNeedStoreTreasureId)
            .collect(Collectors.toList());
    Map<Long, StoreTreasure> needMap =
        storeTreasureDao.selectBatchIds(needIds).stream()
            .collect(Collectors.toMap(StoreTreasure::getId, o -> o));
    List<MyTreasure> userTreasure =
        storeProPoolService.getMytreasure(new MyTreasure(), userData); // 用户持有的藏品

    for (StoreTreasureNeeded storeTreasureNeeded : storeTreasureNeededs) {
      List<MyTreasure> myTreasureStream =
          userTreasure.stream()
              .filter(
                  o ->
                      o.getStoreTreasure()
                          .getDdcId()
                          .equals(
                              needMap.get(storeTreasureNeeded.getNeedStoreTreasureId()).getDdcId()))
              .collect(Collectors.toList());
      if (myTreasureStream.size() > 0
          && myTreasureStream.stream().mapToInt(MyTreasure::getTotalCount).sum()
              >= storeTreasureNeeded.getNeedCount()) {
        log.info(
            "合成初步校验通过："
                + storeTreasureNeeded.getNeedStoreTreasureId()
                + " 数量："
                + storeTreasureNeeded.getNeedCount());

        if (myTreasureStream.stream().filter(o -> 1 != o.getIsSaling()).count()
            >= storeTreasureNeeded.getNeedCount()) {
          log.info("下架藏品校验通过");
        } else {
          throw new RuntimeException("存在正在寄售的藏品，或数量不足，请调整后再试");
        }
      } else {
        StoreTreasure storeTreasure =
            storeTreasureDao.selectById(storeTreasureNeeded.getNeedStoreTreasureId());
        throw new RuntimeException(
            "合成条件不满足,缺少："
                + storeTreasure.getTreasureTitle()
                + " 藏品，所需数量："
                + storeTreasureNeeded.getNeedCount());
      }
    }
    StoreTreasure storeTreasure = storeTreasureDao.selectById(id);
    SysUser sysUser = sysUserDao.selectById(userData.getId());
    // sysUserBackpack添加合成消耗记录
    LambdaQueryWrapper<SysUserBackpack> ques = new LambdaQueryWrapper<>();
    ques.eq(BaseEntity::getState, 1)
        .eq(SysUserBackpack::getUId, userData.getId())
        .orderByAsc(BaseEntity::getCreateTime);
    List<SysUserBackpack> sysUserBackpacks = sysUserBackpackDao.selectList(ques);
    Set<Long> treids =
        sysUserBackpacks.stream()
            .filter(o -> o.getDdcId() == null)
            .map(SysUserBackpack::getSTreasureId)
            .collect(Collectors.toSet());
    treids.add(-1111L);
    Map<Long, StoreTreasure> treMap =
        storeTreasureDao.selectBatchIds(treids).stream()
            .collect(Collectors.toMap(StoreTreasure::getId, o -> o));

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
        if (all.get(i).getOrderFingerprint().equals(sysUserBackpack.getOrderFingerprint())) {
          index = i;
        }
      }
      if (index != -1) all.remove(index);
    }

    LambdaQueryWrapper<StoreTreasure> qu = new LambdaQueryWrapper<>();
    qu.eq(BaseEntity::getState, 1)
        .in(
            StoreTreasure::getOrderFingerprint,
            all.stream().map(SysUserBackpack::getOrderFingerprint).collect(Collectors.toSet()))
        .eq(BaseEntity::getCreateId, userData.getId());
    List<String> storetreasureOrderF =
        storeTreasureDao.selectList(qu).stream()
            .map(StoreTreasure::getOrderFingerprint)
            .collect(Collectors.toList());

    for (StoreTreasureNeeded storeTreasureNeeded : storeTreasureNeededs) {
      for (int i = 0; i < storeTreasureNeeded.getNeedCount(); i++) {
        Optional<SysUserBackpack> first =
            all.stream()
                .filter(
                    o -> {
                      if (o.getDdcId() != null) {
                        return o.getDdcId()
                                .equals(
                                    needMap
                                        .get(storeTreasureNeeded.getNeedStoreTreasureId())
                                        .getDdcId())
                            && !storetreasureOrderF.contains(o.getOrderFingerprint());
                      } else {
                        StoreTreasure tmpTre = treMap.get(o.getSTreasureId());
                        return tmpTre
                                .getDdcId()
                                .equals(
                                    needMap
                                        .get(storeTreasureNeeded.getNeedStoreTreasureId())
                                        .getDdcId())
                            && !storetreasureOrderF.contains(o.getOrderFingerprint());
                      }
                    })
                .findFirst();
        if (first.isPresent()) {
          SysUserBackpack sysUserBackpack = first.get();
          SysUserBackpack re = new SysUserBackpack();
          re.setUId(sysUser.getId());
          re.setSTreasureId(storeTreasureNeeded.getNeedStoreTreasureId());
          re.setOrderTreasurePoolId(-1L);
          re.setBeforeUserId(-1L);
          re.setAfterUserId(-1L);
          re.setTreasureFrom(0);
          re.setOrderFingerprint(sysUserBackpack.getOrderFingerprint());
          re.setFinType(7);
          re.setCreateId(sysUser.getId());
          re.setCreateTime(new Date());
          sysUserBackpackDao.insert(re);
          Iterator<SysUserBackpack> iterator = all.iterator();
          boolean fla = false;
          while (iterator.hasNext()) {
            SysUserBackpack next = iterator.next();
            if (!fla && next.getOrderFingerprint().equals(sysUserBackpack.getOrderFingerprint())) {
              iterator.remove();
              fla = true;
            }
          }
        }
      }
    }

    SysUserBackpack sysUserBackpack = new SysUserBackpack();
    sysUserBackpack.setUId(sysUser.getId());
    sysUserBackpack.setSTreasureId(storeTreasure.getId());
    sysUserBackpack.setOrderTreasurePoolId(-1L);
    sysUserBackpack.setBeforeUserId(-1L);
    sysUserBackpack.setAfterUserId(sysUser.getId());
    sysUserBackpack.setTreasureFrom(0);
    sysUserBackpack.setOrderFingerprint(UUID.randomUUID().toString().replaceAll("-", ""));
    sysUserBackpack.setFinType(6);
    sysUserBackpack.setCreateId(sysUser.getId());
    sysUserBackpack.setCreateTime(new Date());
    orderTreasurePoolService.transDDc(
        ResourcesUtil.getProperty("bsn_plate_addr"),
        sysUser,
        storeTreasure,
        sysUserBackpack,
        new WenchangDDC());
    StoreTreasureRecord emptyTreaRecord =
        orderTreasurePoolService.getEmptyTreaRecord(storeTreasure.getId());
    if (emptyTreaRecord != null) {
      emptyTreaRecord.setOrderFingerprint(sysUserBackpack.getOrderFingerprint());
      emptyTreaRecord.setUserId(sysUserBackpack.getCreateId());
      emptyTreaRecord.setUpdateTime(new Date());
      storeTreasureRecordDao.updateById(emptyTreaRecord);
    }
    sysUserBackpackDao.insert(sysUserBackpack);
    storeTreasure.setSurplusCount(storeTreasure.getSurplusCount() - 1);
    storeTreasureDao.updateById(storeTreasure);

    OrderTreasurePool orderTreasurePool = new OrderTreasurePool();
    orderTreasurePool.setOrderFingerprint(sysUserBackpack.getOrderFingerprint());
    orderTreasurePool.setTeaPoId(storeTreasure.getId());
    orderTreasurePool.setItemType(0);
    orderTreasurePool.setCurPrice(new BigDecimal(0));
    orderTreasurePool.setTotalPrice(new BigDecimal(0));
    orderTreasurePool.setTotalCount(1);
    orderTreasurePool.setPayEndTime(new Date());
    orderTreasurePool.setCreateId(sysUser.getId());
    orderTreasurePool.setCreateTime(new Date());
    orderTreasurePool.setState(-1);
    orderTreasurePool.setOrderFlag(2);
    orderTreasurePool.setPayType(-1);
    orderTreasurePoolDao.insert(orderTreasurePool);

    StoreTreasure record = new StoreTreasure();
    record.setSysCateId(storeTreasure.getSysCateId());
    record.setTurnMaxPrice(storeTreasure.getTurnMaxPrice());
    record.setTurnMinPrice(storeTreasure.getTurnMinPrice());
    record.setStripId(storeTreasure.getStripId());
    record.setTType(0);
    record.setRType(1);
    record.setTNum(storeTreasure.getTNum());
    record.setTreasureTitle(storeTreasure.getTreasureTitle());
    record.setIndexImgPath(storeTreasure.getIndexImgPath());
    record.setDetail(storeTreasure.getDetail());
    record.setHeadImgPath(storeTreasure.getHeadImgPath());
    record.setTotalCount(1);
    record.setSurplusCount(1);
    record.setFrostCount(0);
    record.setPrice(storeTreasure.getPrice());
    record.setIntroduce(storeTreasure.getIntroduce());
    record.setAuthInfo(storeTreasure.getAuthInfo());
    record.setSense(storeTreasure.getSense());
    record.setNeedKnow(storeTreasure.getNeedKnow());
    record.setTransationId(storeTreasure.getTransationId());
    record.setDdcUrl(storeTreasure.getDdcUrl());
    record.setDdcId(storeTreasure.getDdcId());
    record.setOrderFingerprint(UUID.randomUUID().toString().replaceAll("-", ""));
    record.setBeforeRule(storeTreasure.getBeforeRule());
    record.setSysOrgId(storeTreasure.getSysOrgId());
    record.setRuleCount(0);
    record.setFromUser(1);
    record.setIsSale(0);
    record.setCreateId(sysUser.getId());
    record.setCreateTime(new Date());
    record.setState(-1);
    storeTreasureDao.insert(record);
    return "SUCCESS";
  }

  @Autowired private StoreTreasureRecordDao storeTreasureRecordDao;
  @Autowired private OrderTreasurePoolService orderTreasurePoolService;

  @Override
  public StoreTreasureNeededDto getStoreTreasureNeededById(Long id) {
    Optional<StoreTreasureNeededDto> optional = selectDataById(StoreTreasureNeededDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(StoreTreasureNeededAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreTreasureNeededDto dto = new StoreTreasureNeededDto();
    md.map(tmpDto, dto);
    insertData(dto, StoreTreasureNeeded.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(StoreTreasureNeededUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreTreasureNeededDto dto = new StoreTreasureNeededDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(StoreTreasureNeededDto dto) {
    StoreTreasureNeededDto storeTreasureNeeded = getStoreTreasureNeededById(dto.getId());
    if (storeTreasureNeeded.getState() == 1) {
      storeTreasureNeeded.setState(2);
    } else {
      storeTreasureNeeded.setState(1);
    }
    storeTreasureNeeded.setUpdateTime(new Date());
    storeTreasureNeeded.setUpdateId(dto.getUpdateId());
    updateDataById(storeTreasureNeeded.getId(), storeTreasureNeeded);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<StoreTreasureNeededDto> list(StoreTreasureNeededDto dto) {
    LambdaQueryWrapper<StoreTreasureNeeded> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(StoreTreasureNeededDto.class, queryWrapper);
  }
}
