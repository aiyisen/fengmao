package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.StoreTreasure;
import com.qy.ntf.bean.param.AddCouldCompParam;
import com.qy.ntf.bean.param.BindBoxAddParam;
import com.qy.ntf.bean.param.TurnParam;
import com.qy.ntf.util.PageSelectParam;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品首发/超前申购 主体 service服务
 */
public interface StoreTreasureService extends BaseService<StoreTreasureDto, StoreTreasure> {

  /**
   * 主键详情
   *
   * @param id
   * @param userData
   * @return
   */
  StoreTreasureDto getStoreTreasureById(Long id, UserDto userData);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(StoreTreasureAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   * @param adminUserData
   */
  void update(StoreTreasureUpdateDto dto, SysUserAdminDto adminUserData);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(StoreTreasureDto dto);

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
  List<StoreTreasureDto> list(StoreTreasureDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<StoreTreasureDto> getListByPage(
      Class<StoreTreasureDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreTreasure> queryWrapper);

  IPage<StoreTreasureDto> appPagelist(
      PageSelectParam<StoreTreasureSelectDto> param, UserDto userData);

  IPage<String> checkedUserPhone(PageSelectParam<Long> id);

  void batchAdd(StoreTreasureAddDto dto);

  List<StoreTreasureDto> getCouldCompById(Long id);

  String addCouldComp(AddCouldCompParam param, SysUserAdminDto adminUserData);

  void deleteById(Long dto, SysUserAdminDto adminUserData);

  void joinBlod(String id);

  void joinBlodCheck(String s);

  void bindBoxAdd(BindBoxAddParam dto, SysUserAdminDto adminUserData);

  void bindBoxUpdate(BindBoxAddParam dto, SysUserAdminDto adminUserData);

  IPage<StoreTreasureDto> getBindListByPage(
      Class<StoreTreasureDto> storeTreasureDtoClass,
      Integer pageNum,
      long parseLong,
      LambdaQueryWrapper<StoreTreasure> queryWrapper);

  IPage<StoreTreasureDto> getAppBindListByPage(
      Class<StoreTreasureDto> storeTreasureDtoClass,
      Integer pageNum,
      long pageSize,
      LambdaQueryWrapper<StoreTreasure> queryWrapper,
      UserDto userData);

  IPage<StoreTreasureDto> bindBoxItemByPage(PageSelectParam<Long> param);

  void bindBoxItemAdd(BindBoxItemAddDto dto, SysUserAdminDto adminUserData);

  void bindBoxItemUpdate(BindBoxItemAddDto dto, SysUserAdminDto adminUserData);

  IPage<CalendarResult> launchCalendar(PageSelectParam<Object> param);

  IPage<StoreTreasureDto> turnPage(PageSelectParam<TurnParam> param);

  StoreTreasure selectById(Long valueOf);

  void updateById(StoreTreasure storeTreasure);
}
