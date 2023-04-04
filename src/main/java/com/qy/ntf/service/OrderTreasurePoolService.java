package com.qy.ntf.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.customResult.CellTotal;
import com.qy.ntf.bean.customResult.OrderVipResult;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.*;
import com.qy.ntf.bean.param.*;
import com.qy.ntf.util.OrderApiResponse;
import com.qy.ntf.util.PageSelectParam;
import com.qy.ntf.util.TopResponse;
import com.qy.ntf.util.WenchangDDC;
import com.qy.ntf.util.sd.SdNotifyBody;
import com.qy.ntf.util.wxPay.WXNotifyParam;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池藏品订单 service服务
 */
public interface OrderTreasurePoolService
    extends BaseService<OrderTreasurePoolDto, OrderTreasurePool> {

  String updateOrderByNotify(String outtradeno, String transactionId, Integer payType);

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  OrderTreasurePoolDto getOrderTreasurePoolById(Long id);

  OrderTreasurePool selectById(Long id);

  StoreTreasureRecord getEmptyTreaRecord(Long strId);

  /**
   * 保存方法
   *
   * @param dto
   * @return
   */
  OrderApiResponse save(OrderTreasurePoolAddDto dto, String ip);

  void executeAsync(String transationId, Long sysBackId, String ddc_url);

  String genSign(JSONObject reqObj);

  String genPubSign(JSONObject reqObj);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(OrderTreasurePoolUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(OrderTreasurePoolDto dto);

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
  List<OrderTreasurePoolDto> list(OrderTreasurePoolDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<OrderTreasurePoolDto> getListByPage(
      Class<OrderTreasurePoolDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderTreasurePool> queryWrapper);

  void noifityWXin(WXNotifyParam wxp, boolean b);

  void noifityAlipay(String outtradeno, String tradeNo);

  void orderOutDate(Long orderID);

  IPage<OrderTreasurePoolDto> listByPage(
      Integer pageNum, long parseLong, Long id, Integer orderFlag, Integer itemType);

  IPage<OrderTreasurePoolDto> adminOrderPage(Integer pageNum, long parseLong, Integer orderFlag);

  OrderApiResponse updateOrderProduct(UpdateOrderParam param, String ip);

  void transDDc(
      String from,
      SysUser sysUser,
      StoreTreasure storeTreasure,
      SysUserBackpack sysUserBackpack,
      WenchangDDC bsnUtil);

  void transDDc(
      String from,
      String to,
      SysUser sysUser,
      StoreTreasure storeTreasure,
      SysUserBackpack sysUserBackpack,
      StoreTreasureRecord storeTreasureRecord);

  IPage<OrderTreasurePoolDto> updateSaleFlag(IPage<OrderTreasurePoolDto> page, UserDto userData);

  void weixinRecharge(WXNotifyParam wxp, boolean b);

  void alipayRecharge(String outtradeno, String tradeNo);

  CellTotal beforeCollect();

  CellTotal treasureCollect();

  Object addJoin(OrderTreasurePoolAddDto dto);

  IPage<StoreTreasureDto> myJoin(Integer pageNum, long parseLong, Long id);

  void checkTreasure(Long treasureId);

  Object buyVip(BuyVipParam dto, UserDto userData);

  OrderVipResult orderCollectData(PageSelectParam<OrderVipSelectParam> param);

  String noifitySD(SdNotifyBody param);

  void sdRecharge(SdNotifyBody param);

  BindBoxOrderCheckParam bindBoxOrderCheck(Long id);

  IPage<TopItem> consumptionCollect(Page page, Integer selectParam);

  IPage<TopItem> inviteCollect(Page page, Integer selectParam);

  TopResponse<IPage<TopItem>> updateConsumptionTopAndTotal(
      TopResponse<IPage<TopItem>> success, UserDto userData, Integer type);

  TopResponse<IPage<TopItem>> updateInviteCollectTopAndTotal(
      TopResponse<IPage<TopItem>> success, UserDto userData, Integer type);

  IPage<SysUser> orderTreaCopy(Page page, OrderTreaCopyParam selectParam);

  void orderTreaCopyCollect();

  void orderTreaCopyCollectRedisDalyed();
}
