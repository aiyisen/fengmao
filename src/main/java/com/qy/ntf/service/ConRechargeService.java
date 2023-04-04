package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.ConRechargeAddDto;
import com.qy.ntf.bean.dto.ConRechargeDto;
import com.qy.ntf.bean.dto.ConRechargeUpdateDto;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.entity.ConRecharge;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 充值订单表 service服务
 */
public interface ConRechargeService extends BaseService<ConRechargeDto, ConRecharge> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  ConRechargeDto getConRechargeById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   * @param userData
   */
  Object save(ConRechargeAddDto dto, UserDto userData, String ip);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(ConRechargeUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(ConRechargeDto dto);

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
   * @param userData
   * @return
   */
  List<ConRechargeDto> list(ConRechargeDto dto, UserDto userData);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<ConRechargeDto> getListByPage(
      Class<ConRechargeDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<ConRecharge> queryWrapper);
}
