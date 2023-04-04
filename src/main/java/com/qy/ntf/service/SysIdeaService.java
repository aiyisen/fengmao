package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysIdeaAddDto;
import com.qy.ntf.bean.dto.SysIdeaDto;
import com.qy.ntf.bean.dto.SysIdeaUpdateDto;
import com.qy.ntf.bean.entity.SysIdea;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 反馈建议 service服务
 */
public interface SysIdeaService extends BaseService<SysIdeaDto, SysIdea> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysIdeaDto getSysIdeaById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysIdeaAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysIdeaUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysIdeaDto dto);

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
  List<SysIdeaDto> list(SysIdeaDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysIdeaDto> getListByPage(
      Class<SysIdeaDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysIdea> queryWrapper);
}
