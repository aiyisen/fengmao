package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysMenuAddDto;
import com.qy.ntf.bean.dto.SysMenuDto;
import com.qy.ntf.bean.dto.SysMenuUpdateDto;
import com.qy.ntf.bean.entity.SysMenu;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统权限 service服务
 */
public interface SysMenuService extends BaseService<SysMenuDto, SysMenu> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysMenuDto getSysMenuById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysMenuAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysMenuUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysMenuDto dto);

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
  List<SysMenuDto> list(SysMenuDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysMenuDto> getListByPage(
      Class<SysMenuDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysMenu> queryWrapper);
}
