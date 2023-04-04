package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.StoreTreasureCheckUserAddDto;
import com.qy.ntf.bean.dto.StoreTreasureCheckUserDto;
import com.qy.ntf.bean.dto.StoreTreasureCheckUserUpdateDto;
import com.qy.ntf.bean.entity.StoreTreasureCheckUser;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 超亲申购中奖名单 service服务
 */
public interface StoreTreasureCheckUserService
    extends BaseService<StoreTreasureCheckUserDto, StoreTreasureCheckUser> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  StoreTreasureCheckUserDto getStoreTreasureCheckUserById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(StoreTreasureCheckUserAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(StoreTreasureCheckUserUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(StoreTreasureCheckUserDto dto);

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
  List<StoreTreasureCheckUserDto> list(StoreTreasureCheckUserDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<StoreTreasureCheckUserDto> getListByPage(
      Class<StoreTreasureCheckUserDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreTreasureCheckUser> queryWrapper);
}
