package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.SysDictonary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 王振读 email 2022-05-27 21:29:38 DESC : Dao
 */
@Mapper
public interface SysDictonaryDao extends BaseMapper<SysDictonary> {

  @Select("SELECT threshold from sys_dictonary where alias = #{alias} limit 1")
  String selectByAlias(@Param("alias") String alias);

  @Select("SELECT threshold from sys_dictonary where alias = #{alias} ")
  List<String> valueList(String alias);

  @Update("update sys_dictonary set threshold = #{threshold} where alias =#{vip_total_count} ")
  void updateByAlias(String vip_total_count, Integer threshold);
}
