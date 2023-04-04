package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.OrderTreasureRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 藏品快照 Dao
 */
@Mapper
public interface OrderTreasureRecordDao extends BaseMapper<OrderTreasureRecord> {
  @Select("select * from order_treasure_pool where orderFingerprint = #{orderFingerprint}  limit 1")
  OrderTreasureRecord selectByOrderF(@Param("orderFingerprint") String orderFingerprint);
}
