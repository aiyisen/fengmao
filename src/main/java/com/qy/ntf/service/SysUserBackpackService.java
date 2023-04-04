package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysUserBackpackAddDto;
import com.qy.ntf.bean.dto.SysUserBackpackDto;
import com.qy.ntf.bean.dto.SysUserBackpackUpdateDto;
import com.qy.ntf.bean.entity.SysUserBackpack;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 用户背包（记录虚拟商品购买订单标识） service服务
 */
public interface SysUserBackpackService extends BaseService<SysUserBackpackDto, SysUserBackpack> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysUserBackpackDto getSysUserBackpackById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysUserBackpackAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysUserBackpackUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysUserBackpackDto dto);

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
  List<SysUserBackpackDto> list(SysUserBackpackDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysUserBackpackDto> getListByPage(
      Class<SysUserBackpackDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysUserBackpack> queryWrapper);
}
