package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysAddressAddDto;
import com.qy.ntf.bean.dto.SysAddressDto;
import com.qy.ntf.bean.dto.SysAddressUpdateDto;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.entity.SysAddress;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 收货地址 service服务
 */
public interface SysAddressService extends BaseService<SysAddressDto, SysAddress> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  SysAddressDto getSysAddressById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(SysAddressAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysAddressUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(SysAddressDto dto);

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
  List<SysAddressDto> list(SysAddressDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<SysAddressDto> getListByPage(
      Class<SysAddressDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysAddress> queryWrapper);

  SysAddress updateDefault(Long id, UserDto userData);
}
