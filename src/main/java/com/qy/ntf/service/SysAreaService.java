package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysAreaAddDto;
import com.qy.ntf.bean.dto.SysAreaDto;
import com.qy.ntf.bean.dto.SysAreaUpdateDto;
import com.qy.ntf.bean.entity.SysArea;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 全国区域信息表 service服务
 */
public interface SysAreaService extends BaseService<SysAreaDto, SysArea> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysAreaDto getSysAreaById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysAreaAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysAreaUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysAreaDto dto);

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
  List<SysAreaDto> list(SysAreaDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysAreaDto> getListByPage(
      Class<SysAreaDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysArea> queryWrapper);

  List<SysArea> getAll();
}
