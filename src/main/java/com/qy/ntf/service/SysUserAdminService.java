package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysUserAdminAddDto;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.dto.SysUserAdminUpdateDto;
import com.qy.ntf.bean.entity.SysUserAdmin;
import com.qy.ntf.bean.param.UpdateAdminRoleParam;

import java.util.List;
/**
 * @author 王振读 2022-05-31 22:14:55 DESC : 管理员表 service服务
 */
public interface SysUserAdminService extends BaseService<SysUserAdminDto, SysUserAdmin> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysUserAdminDto getSysUserAdminById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysUserAdminAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysUserAdminUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysUserAdminDto dto);

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
  List<SysUserAdminDto> list(SysUserAdminDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysUserAdminDto> getListByPage(
      Class<SysUserAdminDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysUserAdmin> queryWrapper);

  void resetPassword(Long userId, String md5);

  List<SysUserAdmin> selectByAccount(String adminAccount);

  void updateRoles(UpdateAdminRoleParam param);

  void updateAdminRoleIds(SysUserAdminDto user);
}
