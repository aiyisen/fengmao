package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.dao @ClassName: UserMapper @Author:
 * 王振读 @Description: ${description} @Date: 2021/11/29 9:19 @Version: 1.0
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
  @Update("update sys_user set state = ${state} where id = #{id}")
  void updateUserState(@Param("id") Long id, @Param("state") Integer state);

  @Select(
      "select id, username, phone, email,pass, realName, organizationCode, state from sys_user where username = #{username} and pass = #{pass}")
  List<SysUser> selectListByNameAndPassword(
      @Param("username") String username, @Param("pass") String pas);

  // 用户是否有角色关联
  @Select("select count(1) from sys_user_role where userId = #{userId}")
  Integer getRoleCount(@Param("userId") Long userId);

  @Update("update sys_user set pass = #{password} where id = #{id}")
  void resetPassword(@Param("id") Long id, @Param("password") String password);
}
