package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysMessageAddDto;
import com.qy.ntf.bean.dto.SysMessageDto;
import com.qy.ntf.bean.dto.SysMessageUpdateDto;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.entity.SysMessage;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统信息 service服务
 */
public interface SysMessageService extends BaseService<SysMessageDto, SysMessage> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysMessageDto getSysMessageById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   * @param adminUserData
   */
  void save(SysMessageAddDto dto, SysUserAdminDto adminUserData);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysMessageUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysMessageDto dto);

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
  List<SysMessageDto> list(SysMessageDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysMessageDto> getListByPage(
      Class<SysMessageDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysMessage> queryWrapper);
}
