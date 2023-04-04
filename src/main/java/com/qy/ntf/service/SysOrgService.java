package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysOrgAddDto;
import com.qy.ntf.bean.dto.SysOrgDto;
import com.qy.ntf.bean.dto.SysOrgUpdateDto;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.entity.SysOrg;

import java.util.List;
/**
 * @author 王振读 2022-07-05 20:10:31 DESC : 发行方 service服务
 */
public interface SysOrgService extends BaseService<SysOrgDto, SysOrg> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysOrgDto getSysOrgById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   * @param adminUserData
   */
  void save(SysOrgAddDto dto, SysUserAdminDto adminUserData);

  /**
   * 更新方法
   *
   * @param dto
   * @param adminUserData
   */
  void update(SysOrgUpdateDto dto, SysUserAdminDto adminUserData);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysOrgDto dto);

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
  List<SysOrgDto> list(SysOrgDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysOrgDto> getListByPage(
      Class<SysOrgDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysOrg> queryWrapper);

  List<SysOrg> getListByIds(List<Long> orgIds);
}
