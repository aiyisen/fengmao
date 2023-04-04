package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysDictonaryAddDto;
import com.qy.ntf.bean.dto.SysDictonaryDto;
import com.qy.ntf.bean.dto.SysDictonaryUpdateDto;
import com.qy.ntf.bean.entity.SysDictonary;

import java.util.List;
/**
 * @author 王振读 2022-05-27 21:29:38 DESC : service服务
 */
public interface SysDictonaryService extends BaseService<SysDictonaryDto, SysDictonary> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysDictonaryDto getSysDictonaryById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysDictonaryAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysDictonaryUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysDictonaryDto dto);

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
  List<SysDictonaryDto> list(SysDictonaryDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysDictonaryDto> getListByPage(
      Class<SysDictonaryDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysDictonary> queryWrapper);

  String getDicByAlias(String pool_send_percent);

  List<String> valueList(String alias);
}
