package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.StoreTreasureRecord;
import com.qy.ntf.dao.provider.StoreTreasureRecordProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 实物商品订单 Dao
 */
@Mapper
public interface StoreTreasureRecordDao extends BaseMapper<StoreTreasureRecord> {
  @InsertProvider(type = StoreTreasureRecordProvider.class, method = "insertBatch")
  void insertBatch(List<StoreTreasureRecord> allRecord);

  @Select(
      "select * from store_treasure_record where orderFingerprint = #{orderFingerprint} limit 1")
  StoreTreasureRecord selectByOrderFing(@Param("orderFingerprint") String orderFingerprint);

  @Select("select count(*) from store_treasure_record where strId = #{strId}")
  Integer getCountByStrId(@Param("strId") Long valueOf);
}
