package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.OrderProAfterAddDto;
import com.qy.ntf.bean.dto.OrderProAfterDto;
import com.qy.ntf.bean.dto.OrderProAfterUpdateDto;
import com.qy.ntf.bean.entity.OrderProAfter;
import com.qy.ntf.dao.OrderProAfterDao;
import com.qy.ntf.service.OrderProAfterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-06-13 22:37:54 DESC : 权益商城售后订单 仅退款014 退货退款0234 退货024 service服务实现
 */
@Service("orderProAfterService")
public class OrderProAfterServiceImpl implements OrderProAfterService {

  @Autowired private OrderProAfterDao orderProAfterDao;

  @Override
  public BaseMapper<OrderProAfter> getDao() {
    return orderProAfterDao;
  }

  @Override
  public IPage<OrderProAfterDto> getListByPage(
      Class<OrderProAfterDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderProAfter> queryWrapper) {
    IPage<OrderProAfterDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public OrderProAfterDto getOrderProAfterById(Long id) {
    Optional<OrderProAfterDto> optional = selectDataById(OrderProAfterDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(OrderProAfterAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderProAfterDto dto = new OrderProAfterDto();
    md.map(tmpDto, dto);
    insertData(dto, OrderProAfter.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(OrderProAfterUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderProAfterDto dto = new OrderProAfterDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(OrderProAfterDto dto) {
    OrderProAfterDto orderProAfter = getOrderProAfterById(dto.getId());
    if (orderProAfter.getState() == 1) {
      orderProAfter.setState(2);
    } else {
      orderProAfter.setState(1);
    }
    orderProAfter.setUpdateTime(new Date());
    orderProAfter.setUpdateId(dto.getUpdateId());
    updateDataById(orderProAfter.getId(), orderProAfter);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<OrderProAfterDto> list(OrderProAfterDto dto) {
    LambdaQueryWrapper<OrderProAfter> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(OrderProAfterDto.class, queryWrapper);
  }
}
