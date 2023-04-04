package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.ConBalanceRecordAddDto;
import com.qy.ntf.bean.dto.ConBalanceRecordDto;
import com.qy.ntf.bean.dto.ConBalanceRecordUpdateDto;
import com.qy.ntf.bean.entity.ConBalanceRecord;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 余额记录 service服务
 */
public interface ConBalanceRecordService
    extends BaseService<ConBalanceRecordDto, ConBalanceRecord> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  ConBalanceRecordDto getConBalanceRecordById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(ConBalanceRecordAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(ConBalanceRecordUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(ConBalanceRecordDto dto);

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
  List<ConBalanceRecordDto> list(ConBalanceRecordDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<ConBalanceRecordDto> getListByPage(
      Class<ConBalanceRecordDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<ConBalanceRecord> queryWrapper);
}
