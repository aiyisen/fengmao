package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.customResult.MyTreasure;
import com.qy.ntf.bean.customResult.StorePoolPriceRecord;
import com.qy.ntf.bean.dto.StoreProPoolDto;
import com.qy.ntf.bean.dto.StoreProPoolUpdateDto;
import com.qy.ntf.bean.dto.StoreTreasureDto;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.entity.StoreTreasure;
import com.qy.ntf.util.CustomResponse;
import com.qy.ntf.util.PageSelectParam;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池主体 service服务
 */
public interface StoreProPoolService extends BaseService<StoreTreasureDto, StoreTreasure> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  StoreProPoolDto getStoreProPoolById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  //  void save(StoreProPoolAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(StoreProPoolUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(StoreProPoolDto dto);

  /**
   * 删除方法
   *
   * @param id
   * @param adminUserData
   */
  //  void delete(Long id, SysUserAdminDto adminUserData);

  /**
   * 列表查询
   *
   * @param dto
   * @return
   */
  //  List<StoreProPoolDto> list(StoreProPoolDto dto);

  /**
   * 常规分页查询
   *
   * <p>// * @param currentPage 当前页数 （第一页或者第三页） // * @param pageSize 页数大小（每页记录数） // * @param
   * queryWrapper 查询条件
   *
   * @return 分页的设备参数
   */
  //  IPage<StoreProPoolDto> getListByPage(
  //      Class<StoreProPoolDto> clazz,
  //      long currentPage,
  //      Long pageSize,
  //      LambdaQueryWrapper<StoreProPool> queryWrapper);

  CustomResponse<IPage<StoreProPoolDto>> appPagelist(
      PageSelectParam<StoreProPoolDto> param, UserDto userData, String percent, String maxMeta);

  List<StorePoolPriceRecord> getPoolPriceRecord(Long id, Integer type);

  String reFresh(UserDto userData, Integer count);

  List<MyTreasure> getMytreasure(MyTreasure param, UserDto userData);

  IPage<StorePoolPriceRecord> getPoolPriceRecordByPage(
      PageSelectParam<StoreProPoolDto> param, int i);

  Object cancelOut(MyTreasure myTreasure, UserDto userData);

  Object updatePoolPriceAndTag(MyTreasure myTreasure, UserDto userData);

  MyTreasure openBindBox(MyTreasure param, UserDto userData);

  IPage<StoreTreasureDto> turnTreasure(PageSelectParam<String> param, UserDto userData);

  IPage<StoreTreasureDto> isSalingTreasure(PageSelectParam<String> param, UserDto userData);
}
