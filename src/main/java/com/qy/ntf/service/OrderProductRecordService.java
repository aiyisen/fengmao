package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.OrderProductRecordAddDto;
import com.qy.ntf.bean.dto.OrderProductRecordDto;
import com.qy.ntf.bean.dto.OrderProductRecordUpdateDto;
import com.qy.ntf.bean.entity.OrderProductRecord;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 实物商品快照 service服务
 */
public interface OrderProductRecordService
    extends BaseService<OrderProductRecordDto, OrderProductRecord> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  OrderProductRecordDto getOrderProductRecordById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(OrderProductRecordAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(OrderProductRecordUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(OrderProductRecordDto dto);

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
  List<OrderProductRecordDto> list(OrderProductRecordDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<OrderProductRecordDto> getListByPage(
      Class<OrderProductRecordDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderProductRecord> queryWrapper);
}
