package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.OrderProductRecordAddDto;
import com.qy.ntf.bean.dto.OrderProductRecordDto;
import com.qy.ntf.bean.dto.OrderProductRecordUpdateDto;
import com.qy.ntf.bean.entity.OrderProductRecord;
import com.qy.ntf.dao.OrderProductRecordDao;
import com.qy.ntf.service.OrderProductRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 实物商品快照 service服务实现
 */
@Service("orderProductRecordService")
public class OrderProductRecordServiceImpl implements OrderProductRecordService {

  @Autowired private OrderProductRecordDao orderProductRecordDao;

  @Override
  public BaseMapper<OrderProductRecord> getDao() {
    return orderProductRecordDao;
  }

  @Override
  public IPage<OrderProductRecordDto> getListByPage(
      Class<OrderProductRecordDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderProductRecord> queryWrapper) {
    IPage<OrderProductRecordDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public OrderProductRecordDto getOrderProductRecordById(Long id) {
    Optional<OrderProductRecordDto> optional = selectDataById(OrderProductRecordDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(OrderProductRecordAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderProductRecordDto dto = new OrderProductRecordDto();
    md.map(tmpDto, dto);
    insertData(dto, OrderProductRecord.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(OrderProductRecordUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderProductRecordDto dto = new OrderProductRecordDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(OrderProductRecordDto dto) {
    OrderProductRecordDto orderProductRecord = getOrderProductRecordById(dto.getId());
    if (orderProductRecord.getState() == 1) {
      orderProductRecord.setState(2);
    } else {
      orderProductRecord.setState(1);
    }
    orderProductRecord.setUpdateTime(new Date());
    orderProductRecord.setUpdateId(dto.getUpdateId());
    updateDataById(orderProductRecord.getId(), orderProductRecord);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<OrderProductRecordDto> list(OrderProductRecordDto dto) {
    LambdaQueryWrapper<OrderProductRecord> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(OrderProductRecordDto.class, queryWrapper);
  }
}
