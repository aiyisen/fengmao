package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.OrderVipAddDto;
import com.qy.ntf.bean.dto.OrderVipDto;
import com.qy.ntf.bean.dto.OrderVipUpdateDto;
import com.qy.ntf.bean.entity.OrderVip;
import com.qy.ntf.bean.param.LLNotifyParam;
import com.qy.ntf.util.wxPay.WXNotifyParam;

import java.util.List;
/**
 * @author 王振读 2022-06-17 22:00:33 DESC : vip购买订单 service服务
 */
public interface OrderVipService extends BaseService<OrderVipDto, OrderVip> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  OrderVipDto getOrderVipById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(OrderVipAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(OrderVipUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(OrderVipDto dto);

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
  List<OrderVipDto> list(OrderVipDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<OrderVipDto> getListByPage(
      Class<OrderVipDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderVip> queryWrapper);

  void setFail(Long orderID);

  void noifityWXin(WXNotifyParam wxp, boolean b);

  void noifityAlipay(String outtradeno, String tradeNo);

  void noifitylianlian(LLNotifyParam param);

  void lianlianRecharge(LLNotifyParam param);
}
