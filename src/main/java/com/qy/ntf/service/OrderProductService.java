package com.qy.ntf.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.customResult.AfterSaleResult;
import com.qy.ntf.bean.customResult.CellTotal;
import com.qy.ntf.bean.customResult.JsonRootBean;
import com.qy.ntf.bean.customResult.SaleAfterOrder;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.OrderProduct;
import com.qy.ntf.bean.entity.SysAddress;
import com.qy.ntf.bean.param.*;
import com.qy.ntf.util.PageSelectParam;
import com.qy.ntf.util.wxPay.WXNotifyParam;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 实物商品订单 service服务
 */
public interface OrderProductService extends BaseService<OrderProductDto, OrderProduct> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  OrderProductDto getOrderProductById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   * @param userData
   * @return
   */
  Object save(OrderProductAddDto dto, UserDto userData);

  String genSign(JSONObject reqObj);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(OrderProductUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(OrderProductDto dto);

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
  List<OrderProductDto> list(OrderProductDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<OrderProductDto> getListByPage(
      Class<OrderProductDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderProduct> queryWrapper);

  void noifityWXin(WXNotifyParam wxp, boolean b);

  void noifityAlipay(String outtradeno, String tradeNo);

  void orderOutDate(Long orderID);

  IPage<OrderProductDto> listByPage(
      Integer pageNum, long parseLong, Long id, PageSelectParam<OrderProductSelectDto> param);

  Object updateOrderProduct(UpdateOrderParam param);

  String backOrder(Long id, UserDto userData);

  OrderProduct sendProduct(Long id, SysUserAdminDto userData, String orderNum);

  void autoConfirm(Long orderID);

  Object saleAfterActive(UserDto userData, AfterSalesParam param);

  CellTotal productCollect();

  String saleAfterDone(SysUserAdminDto userData, AdminAfterSales param);

  void orderProductBack(LLOrderBackNotifyParam param);

  void saleAfterAutoConfirm(Long orderID);

  Object userSendBack(UserDto userData, AfterSalesParam param);

  Object confirmSaleAfterReceive(UserDto userData, AfterSalesParam param);

  Object cancelOrderAfter(UserDto userData, AfterSalesParam param);

  AfterSaleResult afterOrderDetail(Long orderProductId);

  List<SysAddress> getAddressByIds(List<Long> collect);

  Object getOrderPrice(OrderProductAddDto dto);

  IPage<SaleAfterOrder> saleAfterOrders(
      UserDto userData, SysUserAdminDto adminUserData, PageSelectParam<Integer> param);

  SysAddress getUserDefaultAddress(UserDto userData);

  JsonRootBean getTransInfo(String transNum);

  void noifitylianlian(LLNotifyParam param);
}
