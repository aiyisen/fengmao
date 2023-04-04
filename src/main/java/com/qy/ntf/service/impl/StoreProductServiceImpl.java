package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.customResult.MyTreasure;
import com.qy.ntf.bean.customResult.NeedBuyTreasure;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.*;
import com.qy.ntf.dao.*;
import com.qy.ntf.service.StoreProPoolService;
import com.qy.ntf.service.StoreProductService;
import com.qy.ntf.util.PageSelectParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 权益商城-普通专区/特权专区/兑换专区 主体 service服务实现
 */
@Service("storeProductService")
public class StoreProductServiceImpl implements StoreProductService {

  @Autowired private StoreProductDao storeProductDao;
  @Autowired private StoreProductTemplateDao storeProductTemplateDao;

  @Override
  public BaseMapper<StoreProduct> getDao() {
    return storeProductDao;
  }

  @Override
  public IPage<StoreProductDto> getListByPage(
      Class<StoreProductDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreProduct> queryWrapper) {
    IPage<StoreProductDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    if (selectPageList.getRecords().size() > 0
        && selectPageList.getRecords().get(0).getProType() == 1) {
      List<Long> productIds =
          selectPageList.getRecords().stream()
              .map(StoreProductDto::getId)
              .collect(Collectors.toList());
      LambdaQueryWrapper<StoreProductTemplate> que =
          Wrappers.lambdaQuery(StoreProductTemplate.class);
      que.in(StoreProductTemplate::getProductId, productIds);
      List<StoreProductTemplate> storeProductTemplates = storeProductTemplateDao.selectList(que);
      for (StoreProductDto record : selectPageList.getRecords()) {
        List<StoreProductTemplate> need =
            storeProductTemplates.stream()
                .filter(o -> o.getProductId().equals(record.getId()))
                .collect(Collectors.toList());
        List<StoreProductTemplateAddDto> tmpRe = new ArrayList<>();
        for (StoreProductTemplate storeProductTemplate : need) {
          if (storeProductTemplate.getNeedId() != null
              && storeProductTemplate.getNeedCount() != null) {
            StoreProductTemplateAddDto tmpRecord = new StoreProductTemplateAddDto();
            tmpRecord.setNeedCount(storeProductTemplate.getNeedCount());
            tmpRecord.setNeedId(storeProductTemplate.getNeedId());
            tmpRe.add(tmpRecord);
          }
        }
        record.setTreaCount(
            tmpRe.stream().mapToInt(StoreProductTemplateAddDto::getNeedCount).sum());
        record.setStoreTreaIds(tmpRe);
      }
    }
    return selectPageList;
  }

  @Autowired private SysUserDao sysUserDao;

  @Override
  public IPage<StoreProductDto> appPagelist(
      PageSelectParam<StoreProductSelectDto> param, UserDto userData) {
    LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(BaseEntity::getState, 1);
    if (param.getSelectParam().getProType() != null) {
      queryWrapper.eq(StoreProduct::getProType, param.getSelectParam().getProType());
    }
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    SysUser sysUser = null;
    if (userData != null) {
      sysUser = sysUserDao.selectById(userData.getId());
    }
    IPage<StoreProductDto> storeProductDtoIPage =
        selectPageList(
            StoreProductDto.class,
            param.getPageNum(),
            Long.valueOf(param.getPageSize()),
            queryWrapper);
    List<Long> collect =
        storeProductDtoIPage.getRecords().stream()
            .map(StoreProductDto::getId)
            .collect(Collectors.toList());
    collect.add(-11111L);
    List<StoreProductDto> counts = storeProductTemplateDao.selectNeedCount(collect);
    for (StoreProductDto record : storeProductDtoIPage.getRecords()) {
      List<StoreProductDto> id =
          counts.stream()
              .filter(
                  o -> {
                    if (o.getId() != null) {
                      return o.getId().equals(record.getId());
                    } else {
                      return false;
                    }
                  })
              .collect(Collectors.toList());
      if (id.size() > 0 && id.get(0).getTreaCount() != null) {
        record.setTreaCount(id.get(0).getTreaCount());
      }
      if (sysUser != null) {
        if (sysUser.getVipEndTime() != null
            && sysUser.getVipEndTime().getTime() > System.currentTimeMillis()) {
          // 是会员无需重置折扣
        } else {
          record.setVipPercent(new BigDecimal(1));
        }
      } else {
        record.setVipPercent(new BigDecimal(1));
      }
    }
    return storeProductDtoIPage;
  }

  @Autowired private StoreTreasureDao storeTreasureDao;
  //  @Autowired private StoreProPoolDao storeProPoolDao;
  @Autowired private OrderTreasurePoolDao orderTreasurePoolDao;

  @Override
  public List<NeedBuyTreasure> getNeedTreasure(Long id, UserDto userData) {
    List<StoreProductTemplate> needTreasure = storeProductTemplateDao.getNeedTreasure(id);
    List<Long> treasureIds =
        needTreasure.stream()
            .filter(o -> o.getNeedType() == 0)
            .map(StoreProductTemplate::getNeedId)
            .collect(Collectors.toList());
    List<Long> poolIds =
        needTreasure.stream()
            .filter(o -> o.getNeedType() == 1)
            .map(StoreProductTemplate::getNeedId)
            .collect(Collectors.toList());
    List<NeedBuyTreasure> result = new ArrayList<>();
    if (treasureIds.size() > 0) {
      List<StoreTreasure> storeTreasures = storeTreasureDao.selectBatchIds(treasureIds);
      storeTreasures =
          storeTreasures.stream().filter(o -> o.getState() == 1).collect(Collectors.toList());
      for (StoreTreasure storeTreasure : storeTreasures) {
        List<StoreProductTemplate> collect =
            needTreasure.stream()
                .filter(o -> o.getNeedType() == 0 && o.getNeedId().equals(storeTreasure.getId()))
                .collect(Collectors.toList());
        NeedBuyTreasure re = new NeedBuyTreasure();
        re.setIsBuy(false);
        re.setCount(collect.get(0).getNeedCount());
        re.setTreasureTitle(storeTreasure.getTreasureTitle());
        re.setHeadImgPath(
            storeTreasure.getTType() == 2
                ? storeTreasure.getHeadImgPath()
                : storeTreasure.getIndexImgPath());
        re.setTeaPoId(storeTreasure.getId());
        re.setTType(storeTreasure.getTType());
        result.add(re);
      }
    }
    //    if (poolIds.size() > 0) {
    //      List<StoreProPool> storeProPools = storeProPoolDao.selectBatchIds(poolIds);
    //      for (StoreProPool storeTreasure : storeProPools) {
    //        List<StoreProductTemplate> collect =
    //            needTreasure.stream()
    //                .filter(o -> o.getNeedType() == 1 &&
    // o.getNeedId().equals(storeTreasure.getId()))
    //                .collect(Collectors.toList());
    //        NeedBuyTreasure re = new NeedBuyTreasure();
    //        re.setIsBuy(false);
    //        re.setCount(collect.get(0).getNeedCount());
    //        re.setTreasureTitle(storeTreasure.getProPoolTitle());
    //        re.setHeadImgPath(storeTreasure.getIndexPath());
    //        re.setTeaPoId(storeTreasure.getId());
    //        result.add(re);
    //      }
    //    }

    if (userData != null) {
      LambdaQueryWrapper<OrderTreasurePool> que = new LambdaQueryWrapper<>();
      que.eq(BaseEntity::getCreateId, userData.getId()).eq(OrderTreasurePool::getOrderFlag, 2);
      List<MyTreasure> mytreasure = storeProPoolService.getMytreasure(new MyTreasure(), userData);
      for (NeedBuyTreasure item : result) {
        List<MyTreasure> collect =
            mytreasure.stream()
                .filter(o -> o.getSTreasureId().equals(item.getTeaPoId()))
                .collect(Collectors.toList());
        item.setIsBuy(collect.size() >= item.getCount());
      }
    }
    return result;
  }

  @Override
  public void saveTemp(StoreProductAddLinDto param) {
    ModelMapper md = new ModelMapper();
    StoreProduct dto = new StoreProduct();
    md.map(param, dto);
    dto.setSurplusCount(param.getTotalCount());
    storeProductDao.insert(dto);
    if (param.getStoreTreaIds() != null) {
      for (StoreProductTemplateAddDto storeTreaId : param.getStoreTreaIds()) {
        if (storeTreaId.getNeedCount() != null && storeTreaId.getNeedId() != null) {
          StoreProductTemplate re = new StoreProductTemplate();
          re.setProductId(dto.getId());
          re.setNeedId(storeTreaId.getNeedId());
          re.setNeedType(0);
          re.setNeedCount(storeTreaId.getNeedCount());
          re.setCreateId(param.getCreateId());
          re.setCreateTime(new Date());
          re.setState(1);
          storeProductTemplateDao.insert(re);
        }
      }
    }
  }

  @Override
  public void updateTemp(StoreProductAddLinDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreProductDto dto = new StoreProductDto();
    md.map(tmpDto, dto);
    dto.setSurplusCount(tmpDto.getTotalCount());
    updateDataById(dto.getId(), dto);
    storeProductTemplateDao.deleteLink(dto.getId());
    if (tmpDto.getStoreTreaIds() != null) {
      for (StoreProductTemplateAddDto storeTreaId : tmpDto.getStoreTreaIds()) {
        StoreProductTemplate re = new StoreProductTemplate();
        re.setProductId(dto.getId());
        re.setNeedId(storeTreaId.getNeedId());
        re.setNeedType(0);
        re.setNeedCount(storeTreaId.getNeedCount());
        re.setCreateId(tmpDto.getCreateId());
        re.setCreateTime(new Date());
        re.setState(1);
        storeProductTemplateDao.insert(re);
      }
    }
  }

  @Override
  public void deleteById(Long id, SysUserAdminDto adminUserData) {
    StoreProductDto storeProduct = getStoreProductById(id);
    storeProduct.setState(-1);
    storeProduct.setUpdateTime(new Date());
    storeProduct.setUpdateId(adminUserData.getId());
    updateDataById(storeProduct.getId(), storeProduct);
  }

  @Autowired private StoreProPoolService storeProPoolService;

  @Override
  public IPage<StoreProductDto> privilegeList(PageSelectParam<Integer> param, UserDto userData) {
    PageSelectParam<StoreProductSelectDto> p = new PageSelectParam<>();
    p.setSelectParam(new StoreProductSelectDto());
    p.getSelectParam().setProType(1);
    p.setPageNum(1);
    p.setPageSize(99999999);
    List<StoreProductDto> allResult = appPagelist(p, userData).getRecords();
    List<Long> productIds =
        allResult.stream().map(StoreProductDto::getId).collect(Collectors.toList());
    productIds.add(-211111L);
    LambdaQueryWrapper<StoreProductTemplate> q = Wrappers.lambdaQuery(StoreProductTemplate.class);
    q.in(StoreProductTemplate::getProductId, productIds).eq(BaseEntity::getState, 1);
    List<StoreProductTemplate> storeProductTemplates = storeProductTemplateDao.selectList(q);
    if (param.getSelectParam() != 1) {
      // 匹配用户可以购买的藏品
      List<MyTreasure> myTreasure = storeProPoolService.getMytreasure(new MyTreasure(), userData);
      List<StoreProductDto> result = new ArrayList<>();
      for (StoreProductDto storeProductDto : allResult) {
        List<StoreProductTemplate> needTreasures =
            storeProductTemplates.stream()
                .filter(o -> o.getProductId().equals(storeProductDto.getId()))
                .collect(Collectors.toList());
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
        if (flag) {
          result.add(storeProductDto);
        }
      }
      return listToPage(result, param.getPageNum(), param.getPageSize());
    } else {
      return listToPage(allResult, param.getPageNum(), param.getPageSize());
    }
  }

  public static IPage<StoreProductDto> listToPage(
      List<StoreProductDto> list, int pageNum, int pageSize) {
    List<StoreProductDto> pageList = new ArrayList<>();
    int curIdx = pageNum > 1 ? (pageNum - 1) * pageSize : 0;
    for (int i = 0; i < pageSize && curIdx + i < list.size(); i++) {
      pageList.add(list.get(curIdx + i));
    }
    IPage<StoreProductDto> page = new Page<>(pageNum, pageSize);
    page.setRecords(pageList);
    page.setTotal(list.size());
    return page;
  }

  @Override
  public StoreProductDto getStoreProductById(Long id) {
    Optional<StoreProductDto> optional = selectDataById(StoreProductDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(StoreProductAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreProductDto dto = new StoreProductDto();
    md.map(tmpDto, dto);
    dto.setSurplusCount(tmpDto.getTotalCount());

    insertData(dto, StoreProduct.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(StoreProductUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreProductDto dto = new StoreProductDto();
    md.map(tmpDto, dto);
    dto.setSurplusCount(tmpDto.getSurplusCount());
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(StoreProductDto dto) {
    StoreProductDto storeProduct = getStoreProductById(dto.getId());
    if (storeProduct.getState() == 1) {
      storeProduct.setState(0);
    } else {
      storeProduct.setState(1);
    }
    storeProduct.setUpdateTime(new Date());
    storeProduct.setUpdateId(dto.getUpdateId());
    updateDataById(storeProduct.getId(), storeProduct);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<StoreProductDto> list(StoreProductDto dto) {
    LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(StoreProductDto.class, queryWrapper);
  }
}
