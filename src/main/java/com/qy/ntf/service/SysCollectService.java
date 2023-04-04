package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.customResult.CellTotal;
import com.qy.ntf.bean.customResult.IndexData;
import com.qy.ntf.bean.dto.SysCollectAddDto;
import com.qy.ntf.bean.dto.SysCollectDto;
import com.qy.ntf.bean.dto.SysCollectUpdateDto;
import com.qy.ntf.bean.entity.SysCollect;

import java.util.List;
/**
 * @author 王振读 2022-06-01 21:24:13 DESC : service服务
 */
public interface SysCollectService extends BaseService<SysCollectDto, SysCollect> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysCollectDto getSysCollectById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysCollectAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysCollectUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysCollectDto dto);

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
  List<SysCollectDto> list(SysCollectDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysCollectDto> getListByPage(
      Class<SysCollectDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysCollect> queryWrapper);

  CellTotal treasureCollect();

  IndexData indexData();
}
