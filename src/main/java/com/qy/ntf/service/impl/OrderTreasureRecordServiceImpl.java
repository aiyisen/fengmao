package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.OrderTreasureRecordAddDto;
import com.qy.ntf.bean.dto.OrderTreasureRecordDto;
import com.qy.ntf.bean.dto.OrderTreasureRecordUpdateDto;
import com.qy.ntf.bean.entity.OrderTreasureRecord;
import com.qy.ntf.dao.OrderTreasureRecordDao;
import com.qy.ntf.service.OrderTreasureRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品快照 service服务实现
 */
@Service("orderTreasureRecordService")
public class OrderTreasureRecordServiceImpl implements OrderTreasureRecordService {

  @Autowired private OrderTreasureRecordDao orderTreasureRecordDao;

  @Override
  public BaseMapper<OrderTreasureRecord> getDao() {
    return orderTreasureRecordDao;
  }

  @Override
  public IPage<OrderTreasureRecordDto> getListByPage(
      Class<OrderTreasureRecordDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderTreasureRecord> queryWrapper) {
    IPage<OrderTreasureRecordDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public OrderTreasureRecordDto getOrderTreasureRecordById(Long id) {
    Optional<OrderTreasureRecordDto> optional = selectDataById(OrderTreasureRecordDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(OrderTreasureRecordAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderTreasureRecordDto dto = new OrderTreasureRecordDto();
    md.map(tmpDto, dto);
    insertData(dto, OrderTreasureRecord.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(OrderTreasureRecordUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderTreasureRecordDto dto = new OrderTreasureRecordDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(OrderTreasureRecordDto dto) {
    OrderTreasureRecordDto orderTreasureRecord = getOrderTreasureRecordById(dto.getId());
    if (orderTreasureRecord.getState() == 1) {
      orderTreasureRecord.setState(2);
    } else {
      orderTreasureRecord.setState(1);
    }
    orderTreasureRecord.setUpdateTime(new Date());
    orderTreasureRecord.setUpdateId(dto.getUpdateId());
    updateDataById(orderTreasureRecord.getId(), orderTreasureRecord);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<OrderTreasureRecordDto> list(OrderTreasureRecordDto dto) {
    LambdaQueryWrapper<OrderTreasureRecord> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(OrderTreasureRecordDto.class, queryWrapper);
  }
}
