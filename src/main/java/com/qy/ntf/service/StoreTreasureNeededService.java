package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.customResult.NeededRes;
import com.qy.ntf.bean.customResult.TreaTrading;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.StoreTreasureNeeded;
import com.qy.ntf.util.PageSelectParam;

import java.util.List;
/**
 * @author 王振读 2022-05-28 15:09:30 DESC : service服务
 */
public interface StoreTreasureNeededService
    extends BaseService<StoreTreasureNeededDto, StoreTreasureNeeded> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  StoreTreasureNeededDto getStoreTreasureNeededById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(StoreTreasureNeededAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(StoreTreasureNeededUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(StoreTreasureNeededDto dto);

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
  List<StoreTreasureNeededDto> list(StoreTreasureNeededDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<StoreTreasureNeededDto> getListByPage(
      Class<StoreTreasureNeededDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreTreasureNeeded> queryWrapper);

  IPage<TreaTrading> tradingPage(PageSelectParam<Integer> param, UserDto userData);

  List<StoreTreasureDto> getColudNeededList();

  List<NeededRes> getNeededList(Long id, UserDto userData);

  String addCompound(Long id, UserDto userData);
}
