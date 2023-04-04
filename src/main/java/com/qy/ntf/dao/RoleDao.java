package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleDao extends BaseMapper<Role> {

  // 角色下是否有菜单
  @Select("select count(1) from sys_role_menu where roleId = #{roleId}")
  Integer getMenuCount(@Param("roleId") Long roleId);

  @Delete("delete from sys_user_role where userId = #{userId}")
  void deleteUserRole(Long userId);

  @Insert("insert into sys_user_role(userId, roleId) values(#{userId}, #{roleId})")
  void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

  @Select(
      "select r.* from sys_user_role ur, sys_role r where ur.userId = #{userId} and ur.roleId = r.id order by r.id")
  List<Role> getUserRoles(@Param("userId") Long userId);

  @Update("update sys_role set state = ${state} where id = ${id}")
  void updateState(@Param("id") Long id, @Param("state") Integer state);
}
