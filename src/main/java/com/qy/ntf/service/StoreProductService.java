package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.customResult.NeedBuyTreasure;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.StoreProduct;
import com.qy.ntf.util.PageSelectParam;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 权益商城-普通专区/特权专区/兑换专区 主体 service服务
 */
public interface StoreProductService extends BaseService<StoreProductDto, StoreProduct> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  StoreProductDto getStoreProductById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(StoreProductAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(StoreProductUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(StoreProductDto dto);

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
  List<StoreProductDto> list(StoreProductDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<StoreProductDto> getListByPage(
      Class<StoreProductDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreProduct> queryWrapper);

  IPage<StoreProductDto> appPagelist(
      PageSelectParam<StoreProductSelectDto> param, UserDto userData);

  List<NeedBuyTreasure> getNeedTreasure(Long id, UserDto userData);

  void saveTemp(StoreProductAddLinDto dto);

  void updateTemp(StoreProductAddLinDto dto);

  void deleteById(Long id, SysUserAdminDto adminUserData);

  IPage<StoreProductDto> privilegeList(PageSelectParam<Integer> param, UserDto userData);
}
