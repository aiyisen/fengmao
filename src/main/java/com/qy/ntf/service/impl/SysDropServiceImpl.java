package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.SysDropAddDto;
import com.qy.ntf.bean.dto.SysDropDto;
import com.qy.ntf.bean.dto.SysDropUpdateDto;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.entity.*;
import com.qy.ntf.bean.param.AddDropParam;
import com.qy.ntf.dao.*;
import com.qy.ntf.service.OrderTreasurePoolService;
import com.qy.ntf.service.SysDropService;
import com.qy.ntf.util.WenchangDDC;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.utils.Strings;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-08-15 15:18:56 DESC : service服务实现
 */
@Service("sysDropService")
public class SysDropServiceImpl implements SysDropService {

  @Autowired private SysDropDao sysDropDao;

  @Override
  public BaseMapper<SysDrop> getDao() {
    return sysDropDao;
  }

  @Autowired private SysUserDao sysUserDao;
  @Autowired private StoreTreasureDao storeTreasureDao;
  // 查询空投记录
  @Override
  public IPage<SysDropDto> getListByPage(
      Class<SysDropDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysDrop> queryWrapper,
      Integer type) {
    if (type == null) throw new RuntimeException("type 不可为空");
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    LambdaQueryWrapper<StoreTreasure> q = Wrappers.lambdaQuery(StoreTreasure.class);
    List<Long> ids;
    if (type == 0) {
      // 普通藏品
      q.eq(StoreTreasure::getTType, 0);
      List<StoreTreasure> storeTreasures = storeTreasureDao.selectList(q);
      ids = storeTreasures.stream().map(StoreTreasure::getId).collect(Collectors.toList());
    } else {
      // 盲盒
      q.eq(StoreTreasure::getTType, 3);
      List<StoreTreasure> storeTreasures = storeTreasureDao.selectList(q);
      ids = storeTreasures.stream().map(StoreTreasure::getId).collect(Collectors.toList());
    }
    ids.add(-1111L);
    queryWrapper.in(SysDrop::getDropstreaid, ids);
    IPage<SysDropDto> selectPageList = selectPageList(clazz, currentPage, pageSize, queryWrapper);
    if (selectPageList.getRecords().size() > 0) {
      Set<Long> uids =
          selectPageList.getRecords().stream().map(SysDropDto::getUid).collect(Collectors.toSet());
      Map<Long, SysUser> userMap =
          sysUserDao.selectBatchIds(uids).stream()
              .collect(Collectors.toMap(SysUser::getId, o -> o));
      Set<Long> streIds =
          selectPageList.getRecords().stream()
              .map(SysDropDto::getDropstreaid)
              .collect(Collectors.toSet());
      streIds.add(-1111L);
      Map<Long, StoreTreasure> treasureMap =
          storeTreasureDao.selectBatchIds(streIds).stream()
              .collect(Collectors.toMap(StoreTreasure::getId, o -> o));
      for (SysDropDto record : selectPageList.getRecords()) {
        record.setSysUser(userMap.get(record.getUid()));
        record.setStoreTreasure(treasureMap.get(record.getDropstreaid()));
      }
    }
    return selectPageList;
  }

  @Autowired private SysUserBackpackDao sysUserBackpackDao;
  @Autowired private OrderTreasurePoolService orderTreasurePoolService;
  @Autowired private StoreTreasureRecordDao storeTreasureRecordDao;

  @Override
  public void addDrop(AddDropParam param, SysUserAdminDto adminUserData) {
    List<SysUser> allUserIds = sysUserDao.selectBatchIds(param.getUserIds());
    if (allUserIds.size() != param.getUserIds().size()) {
      throw new RuntimeException("含有非法用户Id");
    }
    if (param.getId() != null) {
      if (param.getCount() == null) {
        param.setCount(1);
      }
      // 空投nft
      StoreTreasure storeTreasures = storeTreasureDao.selectById(param.getId());
      if (storeTreasures == null) {
        throw new RuntimeException("藏品id异常");
      }
      if (storeTreasures.getState() != 0) {
        throw new RuntimeException("藏品暂未禁用，请将藏品禁用后再试，启用期间进行空投将会导致藏品超卖问题，空投完成后可重新启用");
      }
      WenchangDDC bsnUtils = new WenchangDDC();
      if (storeTreasures.getTType() == 3) {
        LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery(StoreTreasure.class);
        que.in(StoreTreasure::getPId, storeTreasures.getId());
        ArrayList<Integer> states = new ArrayList<>();
        states.add(1);
        states.add(0);
        que.in(BaseEntity::getState, states);
        List<StoreTreasure> allChildren = storeTreasureDao.selectList(que);
        if (allChildren.stream().mapToInt(StoreTreasure::getSurplusCount).sum()
            > param.getUserIds().size() * param.getCount()) {
          for (int i = 0; i < param.getCount(); i++) {
            for (SysUser allUserId : allUserIds) {
              if (allUserId.getIsTrue() != null && "1".equals(allUserId.getIsTrue())) {
                allChildren =
                    allChildren.stream()
                        .filter(o -> o.getSurplusCount() > 0)
                        .collect(Collectors.toList());
                if (allChildren.size() == 0) throw new RuntimeException("该盲盒下已无可用藏品");
                int prizeIndex = OrderTreasurePoolServiceImpl.getPrizeIndex(allChildren);
                StoreTreasure tempTrea = allChildren.get(prizeIndex); // 确定盲盒
                tempTrea.setSurplusCount(Math.max(tempTrea.getSurplusCount() - 1, 0));
                storeTreasureDao.updateById(tempTrea);

                SysUserBackpack sysUserBackpack = new SysUserBackpack();

                sysUserBackpack.setUId(allUserId.getId());
                sysUserBackpack.setSTreasureId(tempTrea.getId());
                sysUserBackpack.setOrderTreasurePoolId(-1L);
                sysUserBackpack.setBeforeUserId(-1L);
                sysUserBackpack.setAfterUserId(allUserId.getId());
                sysUserBackpack.setTreasureFrom(0);
                sysUserBackpack.setOrderFingerprint(
                    UUID.randomUUID().toString().replaceAll("-", ""));
                sysUserBackpack.setFinType(8);
                sysUserBackpack.setDdcId(tempTrea.getDdcId());
                sysUserBackpack.setDdcUrl(tempTrea.getDdcUrl());
                sysUserBackpack.setCreateId(allUserId.getCreateId());
                sysUserBackpack.setCreateTime(new Date());
                sysUserBackpack.setState(1);
                sysUserBackpack.setIsCheck(0);
                sysUserBackpackDao.insert(sysUserBackpack);
                StoreTreasureRecord emptyTreaRecord =
                    orderTreasurePoolService.getEmptyTreaRecord(tempTrea.getId());
                if (emptyTreaRecord != null) {
                  emptyTreaRecord.setOrderFingerprint(sysUserBackpack.getOrderFingerprint());
                  emptyTreaRecord.setUserId(sysUserBackpack.getCreateId());
                  emptyTreaRecord.setUpdateTime(new Date());
                  storeTreasureRecordDao.updateById(emptyTreaRecord);
                }
                //                orderTreasurePoolService.transDDc(
                //                    "iaa1tvt9xkw5t2znq687u23mtqvmpk9spakpe5kezx",
                //                    allUserId.getLinkAddress(),
                //                    tempTrea,
                //                    sysUserBackpack,
                //                    bsnUtils);
                OrderTreasureRecord orderTreasureRecord = new OrderTreasureRecord();
                orderTreasureRecord.setTreasureId(tempTrea.getId());
                orderTreasureRecord.setOrderTreasurePoolId(-1L);
                orderTreasureRecord.setTType(tempTrea.getTType());
                orderTreasureRecord.setTreasureTitle(tempTrea.getTreasureTitle());
                orderTreasureRecord.setIndexImgPath(tempTrea.getIndexImgPath());
                orderTreasureRecord.setHeadImgPath(tempTrea.getHeadImgPath());
                orderTreasureRecord.setTotalCount(tempTrea.getTotalCount());
                orderTreasureRecord.setSurplusCount(tempTrea.getSurplusCount());
                orderTreasureRecord.setFrostCount(tempTrea.getFrostCount());
                orderTreasureRecord.setPrice(tempTrea.getPrice());
                orderTreasureRecord.setIntroduce(tempTrea.getIntroduce());
                orderTreasureRecord.setSense(tempTrea.getSense());
                orderTreasureRecord.setNeedKnow(tempTrea.getNeedKnow());
                orderTreasureRecord.setSaleTime(tempTrea.getSaleTime());
                orderTreasureRecord.setUpTime(tempTrea.getUpTime());
                orderTreasureRecord.setCheckTime(tempTrea.getCheckTime());
                orderTreasureRecord.setCreateId(tempTrea.getCreateId());
                orderTreasureRecord.setCreateTime(tempTrea.getCreateTime());
                orderTreasureRecord.setUpdateId(tempTrea.getUpdateId());
                orderTreasureRecord.setUpdateTime(tempTrea.getUpdateTime());
                orderTreasureRecord.setState(tempTrea.getState());
                orderTreasureRecord.setStripId(tempTrea.getStripId());
                if (emptyTreaRecord != null) {
                  orderTreasureRecord.setTNum(emptyTreaRecord.getStrNum());
                }
                orderTreasureRecord.setAuthInfo(tempTrea.getAuthInfo());
                orderTreasureRecordDao.insert(orderTreasureRecord);

                SysDrop sysDrop = new SysDrop();
                sysDrop.setUid(allUserId.getId());
                sysDrop.setDropintegralcount(null);
                sysDrop.setDropstreaid(storeTreasures.getId());
                sysDrop.setCreateId(adminUserData.getId());
                sysDrop.setCreateTime(new Date());
                sysDrop.setState(1);
                sysDropDao.insert(sysDrop);
                OrderTreasurePool orderTreasurePool = new OrderTreasurePool();
                orderTreasurePool.setOrderFingerprint(
                    UUID.randomUUID().toString().replaceAll("-", ""));
                orderTreasurePool.setTeaPoId(storeTreasures.getId());
                orderTreasurePool.setItemType(3);
                orderTreasurePool.setCurPrice(new BigDecimal(0));
                orderTreasurePool.setTotalPrice(new BigDecimal(0));
                orderTreasurePool.setTotalCount(1);
                orderTreasurePool.setPayEndTime(new Date(System.currentTimeMillis() + 3 * 60000));
                orderTreasurePool.setCreateId(allUserId.getId());
                orderTreasurePool.setCreateTime(new Date());
                orderTreasurePool.setState(-1);
                orderTreasurePool.setOrderFlag(2);
                orderTreasurePool.setPayType(-1);
                orderTreasurePoolDao.insert(orderTreasurePool);
              }
            }
          }
        } else {
          throw new RuntimeException("盲盒剩余数量不足");
        }
      } else {
        if (storeTreasures.getState() == 1 || storeTreasures.getState() == 0) {
          if (storeTreasures.getSurplusCount() != null
              && storeTreasures.getSurplusCount() >= param.getUserIds().size() * param.getCount()) {
            storeTreasures.setSurplusCount(
                (int)
                    Math.max(
                        storeTreasures.getSurplusCount()
                            - allUserIds.stream()
                                    .filter(o -> o.getIsTrue() != null && "1".equals(o.getIsTrue()))
                                    .count()
                                * param.getCount(),
                        0));
            storeTreasures.setUpdateTime(new Date());
            storeTreasureDao.updateById(storeTreasures);
            for (int i = 0; i < param.getCount(); i++) {
              for (SysUser allUserId : allUserIds) {
                if (allUserId.getIsTrue() != null && "1".equals(allUserId.getIsTrue())) {
                  SysUserBackpack sysUserBackpack = new SysUserBackpack();
                  //                  orderTreasurePoolService.transDDc(
                  //                      ResourcesUtil.getProperty("bsn_plate_addr"),
                  //                      allUserId,
                  //                      storeTreasures,
                  //                      sysUserBackpack,
                  //                      bsnUtils);
                  sysUserBackpack.setUId(allUserId.getId());
                  sysUserBackpack.setSTreasureId(param.getId());
                  sysUserBackpack.setOrderTreasurePoolId(-1L);
                  sysUserBackpack.setBeforeUserId(-1L);
                  sysUserBackpack.setAfterUserId(allUserId.getId());
                  sysUserBackpack.setTreasureFrom(0);
                  sysUserBackpack.setOrderFingerprint(
                      UUID.randomUUID().toString().replaceAll("-", ""));
                  sysUserBackpack.setFinType(8);
                  sysUserBackpack.setDdcId(storeTreasures.getDdcId());
                  sysUserBackpack.setDdcUrl(storeTreasures.getDdcUrl());
                  sysUserBackpack.setCreateId(allUserId.getCreateId());
                  sysUserBackpack.setCreateTime(new Date());
                  sysUserBackpack.setState(1);
                  sysUserBackpack.setIsCheck(null);
                  sysUserBackpackDao.insert(sysUserBackpack);
                  StoreTreasureRecord emptyTreaRecord =
                      orderTreasurePoolService.getEmptyTreaRecord(storeTreasures.getId());
                  if (emptyTreaRecord != null) {
                    emptyTreaRecord.setOrderFingerprint(sysUserBackpack.getOrderFingerprint());
                    emptyTreaRecord.setUserId(sysUserBackpack.getCreateId());
                    emptyTreaRecord.setUpdateTime(new Date());
                    storeTreasureRecordDao.updateById(emptyTreaRecord);
                  }
                  OrderTreasureRecord orderTreasureRecord = new OrderTreasureRecord();
                  orderTreasureRecord.setTreasureId(storeTreasures.getId());
                  orderTreasureRecord.setOrderTreasurePoolId(-1L);
                  orderTreasureRecord.setTType(storeTreasures.getTType());
                  orderTreasureRecord.setTreasureTitle(storeTreasures.getTreasureTitle());
                  orderTreasureRecord.setIndexImgPath(storeTreasures.getIndexImgPath());
                  orderTreasureRecord.setHeadImgPath(storeTreasures.getHeadImgPath());
                  orderTreasureRecord.setTotalCount(storeTreasures.getTotalCount());
                  orderTreasureRecord.setSurplusCount(storeTreasures.getSurplusCount());
                  orderTreasureRecord.setFrostCount(storeTreasures.getFrostCount());
                  orderTreasureRecord.setPrice(storeTreasures.getPrice());
                  orderTreasureRecord.setIntroduce(storeTreasures.getIntroduce());
                  orderTreasureRecord.setSense(storeTreasures.getSense());
                  orderTreasureRecord.setNeedKnow(storeTreasures.getNeedKnow());
                  orderTreasureRecord.setSaleTime(storeTreasures.getSaleTime());
                  orderTreasureRecord.setUpTime(storeTreasures.getUpTime());
                  orderTreasureRecord.setCheckTime(storeTreasures.getCheckTime());
                  orderTreasureRecord.setCreateId(storeTreasures.getCreateId());
                  orderTreasureRecord.setCreateTime(storeTreasures.getCreateTime());
                  orderTreasureRecord.setUpdateId(storeTreasures.getUpdateId());
                  orderTreasureRecord.setUpdateTime(storeTreasures.getUpdateTime());
                  orderTreasureRecord.setState(storeTreasures.getState());
                  orderTreasureRecord.setStripId(storeTreasures.getStripId());
                  if (emptyTreaRecord != null) {
                    orderTreasureRecord.setTNum(emptyTreaRecord.getStrNum());
                  }
                  orderTreasureRecord.setAuthInfo(storeTreasures.getAuthInfo());
                  orderTreasureRecordDao.insert(orderTreasureRecord);
                  SysDrop sysDrop = new SysDrop();
                  sysDrop.setUid(allUserId.getId());
                  sysDrop.setDropintegralcount(null);
                  sysDrop.setDropstreaid(param.getId());
                  sysDrop.setCreateId(adminUserData.getId());
                  sysDrop.setCreateTime(new Date());
                  sysDrop.setState(1);
                  sysDropDao.insert(sysDrop);
                  OrderTreasurePool orderTreasurePool = new OrderTreasurePool();
                  orderTreasurePool.setOrderFingerprint(sysUserBackpack.getOrderFingerprint());
                  orderTreasurePool.setTeaPoId(storeTreasures.getId());
                  orderTreasurePool.setItemType(0);
                  orderTreasurePool.setCurPrice(new BigDecimal(0));
                  orderTreasurePool.setTotalPrice(new BigDecimal(0));
                  orderTreasurePool.setTotalCount(1);
                  orderTreasurePool.setPayEndTime(new Date(System.currentTimeMillis() + 3 * 60000));
                  orderTreasurePool.setCreateId(allUserId.getId());
                  orderTreasurePool.setCreateTime(new Date());
                  orderTreasurePool.setState(1);
                  orderTreasurePool.setOrderFlag(2);
                  orderTreasurePool.setPayType(0);
                  orderTreasurePoolDao.insert(orderTreasurePool);
                  orderTreasureRecord.setOrderTreasurePoolId(orderTreasurePool.getId());
                  orderTreasureRecordDao.updateById(orderTreasureRecord);
                }
              }
            }

          } else {
            throw new RuntimeException("藏品剩余库存不足");
          }
        } else {
          throw new RuntimeException("藏品暂未上链无法空投");
        }
      }

    } else {
      throw new RuntimeException("缺失藏品id");
      //      // 空投积分
      //      for (SysUser user : allUserIds) {
      //        if (user.getMetaCount() != null) {
      //          user.setMetaCount(user.getMetaCount() + param.getIntegral());
      //          ConIntegralRecord conIntegralRecord = new ConIntegralRecord();
      //          conIntegralRecord.setUId(user.getId());
      //          conIntegralRecord.setRecordType(5);
      //          conIntegralRecord.setMetaCount(param.getIntegral().intValue());
      //          conIntegralRecord.setOrderId(-1L);
      //          conIntegralRecord.setCreateId(user.getId());
      //          conIntegralRecord.setCreateTime(new Date());
      //          conIntegralRecord.setState(1);
      //          conIntegralRecordDao.insert(conIntegralRecord);
      //          sysUserDao.updateById(user);
      //          SysDrop sysDrop = new SysDrop();
      //          sysDrop.setUid(user.getId());
      //          sysDrop.setDropintegralcount(param.getIntegral());
      //          sysDrop.setCreateId(adminUserData.getId());
      //          sysDrop.setCreateTime(new Date());
      //          sysDrop.setState(1);
      //          sysDropDao.insert(sysDrop);
      //        }
      //        //
      //      }
    }
  }

  @Autowired private OrderTreasurePoolDao orderTreasurePoolDao;
  @Autowired private OrderTreasureRecordDao orderTreasureRecordDao;

  @Override
  public String addImportDrop(
      Set<Map<String, Object>> mobiles, Long streId, SysUserAdminDto adminUserData) {
    if (mobiles != null && mobiles.size() > 0) {
      List<String> allMobiles =
          mobiles.stream().map(o -> o.get("mobile").toString()).collect(Collectors.toList());
      LambdaQueryWrapper<SysUser> qu = Wrappers.lambdaQuery(SysUser.class);
      qu.in(SysUser::getPhone, allMobiles);
      Map<String, SysUser> allUser =
          sysUserDao.selectList(qu).stream().collect(Collectors.toMap(SysUser::getPhone, o -> o));
      Set<String> realUserMobile = allUser.keySet();
      Set<String> needRegPhone =
          mobiles.stream()
              .filter(o -> !realUserMobile.contains(o.get("mobile").toString()))
              .map(o -> o.get("mobile").toString())
              .collect(Collectors.toSet());

      StringBuilder sb = new StringBuilder();
      List<String> successMobile = new ArrayList<>();
      List<String> needBeTrue = new ArrayList<>();
      sb = sb.append("导入结果如下：");
      for (Map<String, Object> tmp : mobiles) {
        if (!needRegPhone.contains(tmp.get("mobile").toString())) {
          SysUser sysUser = allUser.get(tmp.get("mobile").toString());
          if (sysUser != null && sysUser.getIsTrue() != null && "1".equals(sysUser.getIsTrue())) {
            AddDropParam tmpParam = new AddDropParam();
            tmpParam.setUserIds(Collections.singletonList(sysUser.getId()));
            tmpParam.setCount(Integer.parseInt(tmp.get("count").toString()));
            tmpParam.setId(streId);
            try {
              addDrop(tmpParam, adminUserData);
              successMobile.add(sysUser.getPhone());
            } catch (Exception e) {
              sb =
                  sb.append(tmp.get("mobile").toString())
                      .append(" 导入失败：")
                      .append(e.getMessage())
                      .append(";");
            }
          } else {
            if (sysUser != null) {
              needBeTrue.add(sysUser.getPhone());
            }
          }
        }
      }
      if (needRegPhone.size() > 0) {
        sb = sb.append(" 如下用户需注册后再试：").append(Strings.join(new ArrayList<>(needRegPhone), ","));
      }
      if (needBeTrue.size() > 0) {
        sb = sb.append(" 如下用户需实名后方可接收空投：").append(Strings.join(new ArrayList<>(needBeTrue), ","));
      }
      sb = sb.append(" 如下用户导入成功： " + Strings.join(successMobile, ","));
      return sb.toString();
    } else {
      throw new RuntimeException("导入数据为空");
    }
  }

  @Autowired private ConIntegralRecordDao conIntegralRecordDao;

  @Override
  public SysDropDto getSysDropById(Long id) {
    Optional<SysDropDto> optional = selectDataById(SysDropDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysDropAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysDropDto dto = new SysDropDto();
    md.map(tmpDto, dto);
    insertData(dto, SysDrop.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysDropUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysDropDto dto = new SysDropDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysDropDto dto) {
    SysDropDto sysDrop = getSysDropById(dto.getId());
    if (sysDrop.getState() == 1) {
      sysDrop.setState(2);
    } else {
      sysDrop.setState(1);
    }
    sysDrop.setUpdateTime(new Date());
    sysDrop.setUpdateId(dto.getUpdateId());
    updateDataById(sysDrop.getId(), sysDrop);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysDropDto> list(SysDropDto dto) {
    LambdaQueryWrapper<SysDrop> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysDropDto.class, queryWrapper);
  }
}
