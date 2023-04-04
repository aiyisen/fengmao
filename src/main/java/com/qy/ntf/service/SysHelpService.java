package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysHelpAddDto;
import com.qy.ntf.bean.dto.SysHelpDto;
import com.qy.ntf.bean.dto.SysHelpUpdateDto;
import com.qy.ntf.bean.entity.SysHelp;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 帮助中心 service服务
 */
public interface SysHelpService extends BaseService<SysHelpDto, SysHelp> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysHelpDto getSysHelpById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysHelpAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysHelpUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysHelpDto dto);

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
  List<SysHelpDto> list();

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysHelpDto> getListByPage(
      Class<SysHelpDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysHelp> queryWrapper);
}
