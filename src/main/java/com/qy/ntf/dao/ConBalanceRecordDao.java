package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.ConBalanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 余额记录 Dao
 */
@Mapper
public interface ConBalanceRecordDao extends BaseMapper<ConBalanceRecord> {
  @Update("update con_balance_record set state=-1 where uId=#{uId}")
  void delectByUId(@Param("uId") Long id);
}
