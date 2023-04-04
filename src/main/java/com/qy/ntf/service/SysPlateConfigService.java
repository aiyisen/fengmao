package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysPlateConfigAddDto;
import com.qy.ntf.bean.dto.SysPlateConfigDto;
import com.qy.ntf.bean.dto.SysPlateConfigUpdateDto;
import com.qy.ntf.bean.entity.SysPlateConfig;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 平台信息 service服务
 */
public interface SysPlateConfigService extends BaseService<SysPlateConfigDto, SysPlateConfig> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysPlateConfigDto getSysPlateConfigById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysPlateConfigAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysPlateConfigUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysPlateConfigDto dto);

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
  SysPlateConfigDto plateInfo();

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysPlateConfigDto> getListByPage(
      Class<SysPlateConfigDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysPlateConfig> queryWrapper);

  SysPlateConfigDto appGetPlate();
}
