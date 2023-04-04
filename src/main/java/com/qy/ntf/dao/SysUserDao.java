package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.customResult.CellResult;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.param.TopSelectParam;
import com.qy.ntf.dao.provider.SysUserProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 系统用户 Dao
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUser> {
  @Select(
      "select DATE_FORMAT(sys_user.createTime,'%Y-%m-%d') as date, count(id) as `count` from sys_user GROUP BY `date` limit 30")
  List<CellResult> getCollectLine();

  @Select("select * from sys_user where phone = #{mobile} limit 1")
  SysUser selectByMobile(@Param("mobile") String mobile);

  @Select(
      "\tSELECT\n"
          + "\t\t\ta.* \n"
          + "\t\tFROM\n"
          + "\t\t\t( SELECT sys_user.phone, sys_user.id, count( phone ) AS cou,isTrue,sys_user.username FROM sys_user GROUP BY sys_user.phone ) AS a \n"
          + "\t\tWHERE\n"
          + "\t\t\ta.cou > 1 ")
  List<SysUser> getUnInvalidUser();

  @Select("select * from sys_user where sys_user.phone=#{phone}")
  List<SysUser> selectByPhone(@Param("phone") String phone);

  @Select(
      "SELECT\n"
          + "\tsys_user.* \n"
          + "FROM\n"
          + "\tsys_user\n"
          + "\tLEFT JOIN sys_user_invite_link ON sys_user_invite_link.uId = sys_user.id \n"
          + "WHERE\n"
          + "\tsys_user_invite_link.seUId = #{uId}")
  SysUser getInviteUser(@Param("uId") Long id);

  @Select("select * from sys_user where userIndex = #{userIndex} limit 1")
  SysUser selectByInviteCode(@Param("userIndex") String userIndex);

  @SelectProvider(type = SysUserProvider.class, method = "findUserByTop")
  List<SysUser> findUserByTop(TopSelectParam param);
}
