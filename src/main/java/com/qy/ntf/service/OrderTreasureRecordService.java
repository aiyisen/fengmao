package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.OrderTreasureRecordAddDto;
import com.qy.ntf.bean.dto.OrderTreasureRecordDto;
import com.qy.ntf.bean.dto.OrderTreasureRecordUpdateDto;
import com.qy.ntf.bean.entity.OrderTreasureRecord;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品快照 service服务
 */
public interface OrderTreasureRecordService
    extends BaseService<OrderTreasureRecordDto, OrderTreasureRecord> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  OrderTreasureRecordDto getOrderTreasureRecordById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(OrderTreasureRecordAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(OrderTreasureRecordUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(OrderTreasureRecordDto dto);

  /**
   * 删除方法
   *
   * @param id
   */
  void delete(Long id);

  /**
   * 列表查询
   *
   * @param dto
   * @return
   */
  List<OrderTreasureRecordDto> list(OrderTreasureRecordDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<OrderTreasureRecordDto> getListByPage(
      Class<OrderTreasureRecordDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderTreasureRecord> queryWrapper);
}
