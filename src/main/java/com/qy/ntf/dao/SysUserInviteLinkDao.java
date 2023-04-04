package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.SysUserInviteLink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 邀请记录 Dao
 */
@Mapper
public interface SysUserInviteLinkDao extends BaseMapper<SysUserInviteLink> {
  @Update("DELETE FROM sys_user_invite_link where  seUId = #{uId}")
  void deleteByUId(@Param("uId") Long id);
}
