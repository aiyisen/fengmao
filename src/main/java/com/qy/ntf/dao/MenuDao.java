package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.dto.ButtonPermissionsDto;
import com.qy.ntf.bean.dto.DynamicRoutingDto;
import com.qy.ntf.bean.entity.Menu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MenuDao extends BaseMapper<Menu> {

  // 获取指定角色所有菜单
  @Select(
      "select m.* from sys_role_menu rm, sys_menu m where rm.menuId = m.id and rm.roleId = #{roleId} and m.state=1 order by sortNumber asc ,`name` ASC ")
  List<Menu> getRoleMenus(@Param("roleId") Long roleId);

  // 取指定用户的所有菜单
  @Select(
      "select distinct m.* "
          + "from sys_user_role ur, sys_role_menu rm, sys_menu m "
          + "where ur.userId=#{userId} and ur.roleId = rm.roleId and rm.menuId = m.id "
          + "order by m.sortNumber,`name` ASC")
  List<Menu> getUserMenus(@Param("userId") Long userId);

  @Select(
      "select m.* "
          + "from sys_menu m, sys_role_menu rm, sys_user_role ur "
          + "where m.pid = #{pid} and m.id = rm.menuId and rm.roleId = ur.roleId and ur.userId = #{userId} "
          + "order by m.sortNumber")
  List<Menu> getTenantMenu(@Param("userId") Long userId, @Param("pid") Long pid);

  @Delete("delete from sys_role_menu where roleId = #{roleId}")
  void deleteRoleMenu(Long roleId);

  @Insert("insert into sys_role_menu(roleId, menuId) values(#{roleId}, #{menuId})")
  void insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

  @Update("update sys_menu set sortNumber = #{sortNumber} where id = #{id}")
  int updateMenuSortNumber(@Param("id") Long id, @Param("sortNumber") Integer sortNumber);

  @Update("update sys_menu set state = ${state} where id = ${id}")
  void updateState(@Param("id") Long id, @Param("state") Integer state);
  /**
   * liz 通过username获得动态路由
   *
   * @return SELECT m.`name`, m.`path`, m.`icon`, e.`name` AS pname, e.`path` AS
   *     ppath,e.`id`,e.`icon` AS picon FROM `sys_menu` m, `sys_user_role` u, `sys_role_menu` r,
   *     `sys_user` s, `sys_menu` e, `sys_role` o WHERE m.`id` = r.`menu_id` AND u.`role_id` =
   *     r.`role_id` AND u.`user_id` = s.`id` AND m.`pid` = e.`id` AND u.`role_id` = o.`id` AND
   *     s.`username` = 'zhangsan' AND o.`state` = 1 AND m.`type` <> 3 AND e.`pid` = 43 AND e.`name`
   *     = '菜单管理' AND ORDER BY m.`pid` ;
   *     <p>SELECT m.`name`, m.`path`, m.`icon`, e.`name` AS pname, e.`path` AS
   *     p_path,e.`id`,e.`icon` AS picon FROM `sys_menu` m, `sys_user_role` u, `sys_role_menu` r,
   *     `sys_user` s, `sys_menu` e, `sys_role` o WHERE m.`id` = r.`menu_id` AND u.`role_id` =
   *     r.`role_id` AND u.`user_id` = s.`id` AND m.`pid` = e.`id` AND u.`role_id` = o.`id` AND
   *     s.`username` = 'zhangsan' AND o.`state` = 1 AND e.`pid` = 0 AND e.`name` = '菜单管理' ORDER BY
   *     m.`pid` ;
   */
  @Select(
      "SELECT DISTINCT  m.`name`, m.`path`, m.`icon`, e.`name` AS pname, e.`path` AS ppath,e.`id`,e.`icon` AS picon "
          + " FROM `sys_menu` m, `sys_user_role` u, `sys_role_menu` r, `sys_user_admin` s, `sys_menu` e, `sys_role` o "
          + " WHERE m.`id` = r.`menuId` AND u.`roleId` = r.`roleId` AND u.`userId` = s.`id` AND m.`pid` = e.`id`  "
          + "  AND u.`roleId` = o.`id` AND s.`id` = #{adminId} AND o.`state` = 1 AND m.`type` <> 3 AND e.`pid` = ${pid} ORDER BY e.sortNumber,e.`name`,m.`sortNumber` ")
  List<DynamicRoutingDto> getDynamicRoutingList(
      @Param("adminId") Long adminId, @Param("pid") Integer pid);

  /**
   * liz 通过username获得动态路由
   *
   * @return
   */
  @Select(
      "SELECT  DISTINCT m.`name`, m.`path`, m.`icon`, e.`name` AS pname, e.`path` AS ppath,e.`id`,e.`icon` AS picon "
          + " FROM `sys_menu` m, `sys_user_role` u, `sys_role_menu` r, `sys_user_admin` s, `sys_menu` e, `sys_role` o "
          + " WHERE m.`id` = r.`menuId` AND u.`roleId` = r.`roleId` AND u.`userId` = s.`id` AND m.`pid` = e.`id` "
          + "  AND u.`roleId` = o.`id` AND s.`id` = #{adminId} AND o.`state` = 1 AND m.`type` <> 3 AND e.`pid` = ${pid} AND e.`name` = #{name} ORDER BY e.sortNumber,m.`sortNumber` ")
  List<DynamicRoutingDto> getDynamicRoutingListByName(
      @Param("adminId") Long adminId, @Param("pid") Integer pid, @Param("name") String name);
  /**
   * 通过userid查着菜单
   *
   * @param userId
   * @return
   */
  @Select(
      "SELECT m.name AS bname,m.`path` AS bpath,e.path AS mpath,e.name AS mname "
          + "FROM `sys_menu` m,`sys_user_role` ur,`sys_role_menu` rm,`sys_menu` e  "
          + "WHERE m.`id` = rm.`menuId` AND ur.`roleId` = rm.`roleId` AND ur.`userId` = ${userId} "
          + "AND m.`type` = 3 AND  m.`pid` = e.id ORDER BY e.path ,m.`path`")
  List<ButtonPermissionsDto> getButtonPermissions(@Param("userId") Long userId);
}
