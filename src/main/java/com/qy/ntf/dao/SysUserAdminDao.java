package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.SysUserAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 王振读 email 2022-05-31 22:14:55 DESC : 管理员表 Dao
 */
@Mapper
public interface SysUserAdminDao extends BaseMapper<SysUserAdmin> {

  @Select(
      "select * from sys_user_admin where adminAccount = #{adminAccount} and adminPass = #{md5}")
  SysUserAdmin selectListByNameAndPassword(
      @Param("adminAccount") String adminAccount, @Param("md5") String md5);
}
