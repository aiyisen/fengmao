package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.customResult.InviteRecordRes;
import com.qy.ntf.bean.dto.ConIntegralRecordAddDto;
import com.qy.ntf.bean.dto.ConIntegralRecordDto;
import com.qy.ntf.bean.dto.ConIntegralRecordUpdateDto;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.entity.ConIntegralRecord;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 积分流水记录（记录总和>=0) service服务
 */
public interface ConIntegralRecordService
    extends BaseService<ConIntegralRecordDto, ConIntegralRecord> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  ConIntegralRecordDto getConIntegralRecordById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(ConIntegralRecordAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(ConIntegralRecordUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(ConIntegralRecordDto dto);

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
  List<ConIntegralRecordDto> list(ConIntegralRecordDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<ConIntegralRecordDto> getListByPage(
      Class<ConIntegralRecordDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<ConIntegralRecord> queryWrapper);

  List<InviteRecordRes> getInviteRecord(UserDto userData);

  Object todayGet(UserDto userData);

  Object getTodayIntegral(UserDto userData);
}
