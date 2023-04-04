package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.OrderProAfter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 王振读 email 2022-06-13 22:37:54 DESC : 权益商城售后订单 仅退款014 退货退款0234 退货024 Dao
 */
@Mapper
public interface OrderProAfterDao extends BaseMapper<OrderProAfter> {

  @Select(
      "select order_pro_after.* from order_pro_after where orderProductId = #{orderProductId} order by id desc")
  List<OrderProAfter> selectByOrderId(@Param("orderProductId") Long orderProductId);
}
