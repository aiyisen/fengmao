package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.customResult.CellResult;
import com.qy.ntf.bean.customResult.CellTotal;
import com.qy.ntf.bean.customResult.IndexData;
import com.qy.ntf.bean.dto.SysCollectAddDto;
import com.qy.ntf.bean.dto.SysCollectDto;
import com.qy.ntf.bean.dto.SysCollectUpdateDto;
import com.qy.ntf.bean.entity.OrderProduct;
import com.qy.ntf.bean.entity.OrderTreasurePool;
import com.qy.ntf.bean.entity.SysCollect;
import com.qy.ntf.dao.OrderProductDao;
import com.qy.ntf.dao.OrderTreasurePoolDao;
import com.qy.ntf.dao.SysCollectDao;
import com.qy.ntf.service.SysCollectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-06-01 21:24:13 DESC : service服务实现
 */
@Service("sysCollectService")
public class SysCollectServiceImpl implements SysCollectService {

  @Autowired private SysCollectDao sysCollectDao;

  @Override
  public BaseMapper<SysCollect> getDao() {
    return sysCollectDao;
  }

  @Override
  public IPage<SysCollectDto> getListByPage(
      Class<SysCollectDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysCollect> queryWrapper) {
    IPage<SysCollectDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public CellTotal treasureCollect() {
    LambdaQueryWrapper<SysCollect> que = new LambdaQueryWrapper<>();
    List<CellResult> re = sysCollectDao.selectListByCount();
    CellTotal result = new CellTotal();
    result.setTotalCount(new BigDecimal(re.stream().mapToInt(o -> o.getCount().intValue()).sum()));
    re.subList(0, Math.min(re.size(), 30));
    Collections.reverse(re);
    result.setMonthCount(new BigDecimal(re.stream().mapToInt(o -> o.getCount().intValue()).sum()));
    if (re.size() > 0) result.setTodayCount(re.get(re.size() - 1).getCount());
    result.setCellResultList(re);
    return result;
  }

  @Override
  public IndexData indexData() {

    IndexData result = new IndexData();

    try {
      List<CellResult> re = sysCollectDao.selectListByMonth();
      result.setCellList(re);
      Date date = new Date(new Date().getTime() - 30 * 24 * 3600000L);
      LambdaQueryWrapper<OrderTreasurePool> treaOrder = Wrappers.lambdaQuery();
      treaOrder.gt(true, BaseEntity::getCreateTime, date);
      treaOrder.gt(true, OrderTreasurePool::getOrderFlag, 0);
      treaOrder.ne(true, OrderTreasurePool::getIsJoin, 1);
      List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(treaOrder);
      LambdaQueryWrapper<OrderProduct> proOrder = Wrappers.lambdaQuery();
      proOrder.ne(OrderProduct::getProductType, 2);
      proOrder.gt(true, OrderProduct::getOrderFlag, 0);
      List<OrderProduct> orderProducts = orderProductDao.selectList(proOrder);
      Calendar instance = Calendar.getInstance();
      SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
      String format = sp.format(new Date());
      format = format + " 00:00:00";

      List<CellResult> orders = new ArrayList<>();
      Date parse = sp.parse(format);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(parse);
      for (int i = 0; i < 30; i++) {
        List<OrderProduct> collect =
            orderProducts.stream()
                .filter(
                    o ->
                        calendar.getTimeInMillis() + 24 * 3600000 > o.getCreateTime().getTime()
                            && o.getCreateTime().getTime() > calendar.getTimeInMillis())
                .collect(Collectors.toList());
        List<OrderTreasurePool> pools =
            orderTreasurePools.stream()
                .filter(
                    o ->
                        calendar.getTimeInMillis() + 24 * 3600000 > o.getCreateTime().getTime()
                            && o.getCreateTime().getTime() > calendar.getTimeInMillis())
                .collect(Collectors.toList());
        String format1 = sp.format(calendar.getTime());
        BigDecimal reduce =
            collect.stream()
                .map(o -> o.getTotalPrice() == null ? BigDecimal.ZERO : o.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal reduce1 =
            pools.stream()
                .map(o -> o.getTotalPrice() == null ? BigDecimal.ZERO : o.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        CellResult tmpRes = new CellResult();
        tmpRes.setDate(format1);
        tmpRes.setCount(reduce.add(reduce1));
        orders.add(tmpRes);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
      }
      result.setOrderList(orders);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    //  首页数据
    return result;
  }

  @Autowired private OrderTreasurePoolDao orderTreasurePoolDao;
  @Autowired private OrderProductDao orderProductDao;

  @Override
  public SysCollectDto getSysCollectById(Long id) {
    Optional<SysCollectDto> optional = selectDataById(SysCollectDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysCollectAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysCollectDto dto = new SysCollectDto();
    md.map(tmpDto, dto);
    insertData(dto, SysCollect.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysCollectUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysCollectDto dto = new SysCollectDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysCollectDto dto) {
    SysCollectDto sysCollect = getSysCollectById(dto.getId());
    if (sysCollect.getState() == 1) {
      sysCollect.setState(2);
    } else {
      sysCollect.setState(1);
    }
    sysCollect.setUpdateTime(new Date());
    sysCollect.setUpdateId(dto.getUpdateId());
    updateDataById(sysCollect.getId(), sysCollect);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysCollectDto> list(SysCollectDto dto) {
    LambdaQueryWrapper<SysCollect> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysCollectDto.class, queryWrapper);
  }
}
