package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.ConWithdraw;
import com.qy.ntf.util.llPay.WithDrawalResult;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 提现订单表 service服务
 */
public interface ConWithdrawService extends BaseService<ConWithdrawDto, ConWithdraw> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  ConWithdrawDto getConWithdrawById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   * @param userData
   * @param ipAddress
   */
  WithDrawalResult save(ConWithdrawAddDto dto, UserDto userData, String ipAddress);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(ConWithdrawUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(ConWithdrawDto dto);

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
  List<ConWithdrawDto> list(ConWithdrawDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<ConWithdrawDto> getListByPage(
      Class<ConWithdrawDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<ConWithdraw> queryWrapper);

  String doAllocate(ConWithdrawDto tmpDto, SysUserAdminDto adminDto);

  void updateStateByNotify(String string);

  ConWithdraw selectById(String txnSeqno);

  void updateById(ConWithdraw conWithdraw);
}
