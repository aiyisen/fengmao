package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysDropAddDto;
import com.qy.ntf.bean.dto.SysDropDto;
import com.qy.ntf.bean.dto.SysDropUpdateDto;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.entity.SysDrop;
import com.qy.ntf.bean.param.AddDropParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 王振读 2022-08-15 15:18:56 DESC : service服务
 */
public interface SysDropService extends BaseService<SysDropDto, SysDrop> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysDropDto getSysDropById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysDropAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysDropUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysDropDto dto);

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
  List<SysDropDto> list(SysDropDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @param selectParam
   * @return 分页的设备参数
   */
  IPage<SysDropDto> getListByPage(
      Class<SysDropDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysDrop> queryWrapper,
      Integer selectParam);

  void addDrop(AddDropParam param, SysUserAdminDto adminUserData);

  String addImportDrop(
      Set<Map<String, Object>> mobiles, Long streId, SysUserAdminDto adminUserData);
}
