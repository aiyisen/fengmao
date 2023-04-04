package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.OrderProductRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 实物商品快照 Dao
 */
@Mapper
public interface OrderProductRecordDao extends BaseMapper<OrderProductRecord> {
  @Select("select * from order_product_record where orderProductId =#{id} limit 1")
  OrderProductRecord selectByOrderId(@Param("id") Long id);
}
