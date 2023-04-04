package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.StoreProPoolCol;
import com.qy.ntf.util.PageSelectParam;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池藏品收藏 service服务
 */
public interface StoreProPoolColService extends BaseService<StoreProPoolColDto, StoreProPoolCol> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  StoreProPoolColDto getStoreProPoolColById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(StoreProPoolColAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(StoreProPoolColUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(StoreProPoolColDto dto);

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
  List<StoreProPoolColDto> list(StoreProPoolColDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<StoreProPoolColDto> getListByPage(
      Class<StoreProPoolColDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreProPoolCol> queryWrapper);

  IPage<StoreProPoolDto> getListByPage(PageSelectParam<Object> param, UserDto userData);

  void deleteCollect(List<String> param, UserDto userData);
}
