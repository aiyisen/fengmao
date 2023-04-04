package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.ConWithdraw;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 提现订单表 Dao
 */
@Mapper
public interface ConWithdrawDao extends BaseMapper<ConWithdraw> {
  @Update("update con_withdraw set state =-1 where uId=#{uId}")
  void deleteByUId(@Param("uId") Long id);
}
