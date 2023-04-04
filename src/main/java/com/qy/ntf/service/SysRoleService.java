package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysRoleAddDto;
import com.qy.ntf.bean.dto.SysRoleDto;
import com.qy.ntf.bean.dto.SysRoleUpdateDto;
import com.qy.ntf.bean.entity.SysRole;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统角色 service服务
 */
public interface SysRoleService extends BaseService<SysRoleDto, SysRole> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysRoleDto getSysRoleById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysRoleAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysRoleUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysRoleDto dto);

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
  List<SysRoleDto> list(SysRoleDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysRoleDto> getListByPage(
      Class<SysRoleDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysRole> queryWrapper);
}
