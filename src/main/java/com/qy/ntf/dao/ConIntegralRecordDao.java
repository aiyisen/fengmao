package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.customResult.InviteRecordRes;
import com.qy.ntf.bean.entity.ConIntegralRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 积分流水记录（记录总和>=0) Dao
 */
@Mapper
public interface ConIntegralRecordDao extends BaseMapper<ConIntegralRecord> {
  @Select(
      "select \n"
          + "sys_user_invite_link.integralCount as sendCount,\n"
          + "sys_user.username as userName,\n"
          + "sys_user.phone as phone,\n"
          + "sys_user.headImg as headImg,\n"
          + "sys_user.userIndex as userIndex\n"
          + "from sys_user_invite_link RIGHT join sys_user on sys_user.id = sys_user_invite_link.seUId\n"
          + "where sys_user_invite_link.uId = #{userId}")
  List<InviteRecordRes> getIntiteRecord(@Param("userId") Long id);

  @Select(
      "select * from con_integral_record where uId = #{userId} and recordType=0 and to_days(createTime) = to_days(now())")
  List<InviteRecordRes> todayGet(@Param("userId") Long id);

  @Update("update con_integral_record set state=-1 where uId=#{uId}")
  void delectByUId(@Param("uId") Long id);
}
