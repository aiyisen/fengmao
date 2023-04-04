package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.customResult.OrderVipUserInfo;
import com.qy.ntf.bean.entity.OrderVip;
import com.qy.ntf.bean.param.OrderVipSelectParam;
import com.qy.ntf.dao.provider.OrderVipDaoProvider;
import org.apache.ibatis.annotations.*;

/**
 * @author 王振读 email 2022-06-17 22:00:33 DESC : vip购买订单 Dao
 */
@Mapper
public interface OrderVipDao extends BaseMapper<OrderVip> {

  @Select("select * from order_vip where order_vip.tradeNo = #{tradeNo} limit 1")
  OrderVip selectByTradeNo(@Param("tradeNo") String tradeNo);

  @SelectProvider(type = OrderVipDaoProvider.class, method = "orderCollectData")
  IPage<OrderVipUserInfo> orderCollectData(Page<OrderVip> page, OrderVipSelectParam selectParam);

  @Update("update order_vip set state=-1 where uId=#{uId}")
  void deleteByUId(@Param("uId") Long id);
}
