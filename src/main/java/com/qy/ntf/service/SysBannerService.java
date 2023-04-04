package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysBannerAddDto;
import com.qy.ntf.bean.dto.SysBannerDto;
import com.qy.ntf.bean.dto.SysBannerUpdateDto;
import com.qy.ntf.bean.entity.SysBanner;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统轮播 service服务
 */
public interface SysBannerService extends BaseService<SysBannerDto, SysBanner> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysBannerDto getSysBannerById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysBannerAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysBannerUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysBannerDto dto);

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
   * @param type
   * @return
   */
  List<SysBannerDto> list(Integer type);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysBannerDto> getListByPage(
      Class<SysBannerDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysBanner> queryWrapper);
}
