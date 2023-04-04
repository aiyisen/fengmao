package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.SysUserBackpack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 用户背包（记录虚拟商品购买订单标识） Dao
 */
@Mapper
public interface SysUserBackpackDao extends BaseMapper<SysUserBackpack> {
  @Select(
      "select * from sys_user_backpack where orderFingerprint = #{orderFingerprint} and uId = #{id} order by createTime desc")
  List<SysUserBackpack> selectByOrderFigerprintAndUserId(
      @Param("orderFingerprint") String orderFingerprint, @Param("id") Long id);

  @Update("update sys_user_backpack set state=-1 where uId=#{uId}")
  void delectByUId(@Param("uId") Long id);
}
