package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.OrderProPoolRecordAddDto;
import com.qy.ntf.bean.dto.OrderProPoolRecordDto;
import com.qy.ntf.bean.dto.OrderProPoolRecordUpdateDto;
import com.qy.ntf.bean.entity.OrderProPoolRecord;
import com.qy.ntf.dao.OrderProPoolRecordDao;
import com.qy.ntf.service.OrderProPoolRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池商品快照 service服务实现
 */
@Service("orderProPoolRecordService")
public class OrderProPoolRecordServiceImpl implements OrderProPoolRecordService {

  @Autowired private OrderProPoolRecordDao orderProPoolRecordDao;

  @Override
  public BaseMapper<OrderProPoolRecord> getDao() {
    return orderProPoolRecordDao;
  }

  @Override
  public IPage<OrderProPoolRecordDto> getListByPage(
      Class<OrderProPoolRecordDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderProPoolRecord> queryWrapper) {
    IPage<OrderProPoolRecordDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public OrderProPoolRecordDto getOrderProPoolRecordById(Long id) {
    Optional<OrderProPoolRecordDto> optional = selectDataById(OrderProPoolRecordDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(OrderProPoolRecordAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderProPoolRecordDto dto = new OrderProPoolRecordDto();
    md.map(tmpDto, dto);
    insertData(dto, OrderProPoolRecord.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(OrderProPoolRecordUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderProPoolRecordDto dto = new OrderProPoolRecordDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(OrderProPoolRecordDto dto) {
    OrderProPoolRecordDto orderProPoolRecord = getOrderProPoolRecordById(dto.getId());
    if (orderProPoolRecord.getState() == 1) {
      orderProPoolRecord.setState(2);
    } else {
      orderProPoolRecord.setState(1);
    }
    orderProPoolRecord.setUpdateTime(new Date());
    orderProPoolRecord.setUpdateId(dto.getUpdateId());
    updateDataById(orderProPoolRecord.getId(), orderProPoolRecord);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<OrderProPoolRecordDto> list(OrderProPoolRecordDto dto) {
    LambdaQueryWrapper<OrderProPoolRecord> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(OrderProPoolRecordDto.class, queryWrapper);
  }
}
