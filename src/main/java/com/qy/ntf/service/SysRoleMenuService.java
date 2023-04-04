package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysRoleMenuAddDto;
import com.qy.ntf.bean.dto.SysRoleMenuDto;
import com.qy.ntf.bean.dto.SysRoleMenuUpdateDto;
import com.qy.ntf.bean.entity.SysRoleMenu;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 角色-菜单 service服务
 */
public interface SysRoleMenuService extends BaseService<SysRoleMenuDto, SysRoleMenu> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysRoleMenuDto getSysRoleMenuById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysRoleMenuAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysRoleMenuUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysRoleMenuDto dto);

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
  List<SysRoleMenuDto> list(SysRoleMenuDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysRoleMenuDto> getListByPage(
      Class<SysRoleMenuDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysRoleMenu> queryWrapper);
}
