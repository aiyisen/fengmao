package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysSeriesAddDto;
import com.qy.ntf.bean.dto.SysSeriesDto;
import com.qy.ntf.bean.dto.SysSeriesUpdateDto;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.entity.SysSeries;

import java.util.List;
/**
 * @author 王振读 2022-07-15 19:26:43 DESC : 系列主体 service服务
 */
public interface SysSeriesService extends BaseService<SysSeriesDto, SysSeries> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysSeriesDto getSysSeriesById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   * @param adminUserData
   */
  void save(SysSeriesAddDto dto, SysUserAdminDto adminUserData);

  /**
   * 更新方法
   *
   * @param dto
   * @param adminUserData
   */
  void update(SysSeriesUpdateDto dto, SysUserAdminDto adminUserData);

  /**
   * 更新状态方法
   *
   * @param dto
   * @param adminUserData
   */
  void updateState(SysSeriesDto dto, SysUserAdminDto adminUserData);

  /**
   * 删除方法
   *
   * @param id
   * @param adminUserData
   */
  void delete(Long id, SysUserAdminDto adminUserData);

  /**
   * 列表查询
   *
   * @param dto
   * @return
   */
  List<SysSeriesDto> list(SysSeriesDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysSeriesDto> getListByPage(
      Class<SysSeriesDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysSeries> queryWrapper);
}
