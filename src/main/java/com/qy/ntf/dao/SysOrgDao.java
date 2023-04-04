package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.SysOrg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 王振读 email 2022-07-05 20:10:31 DESC : 发行方 Dao
 */
@Mapper
public interface SysOrgDao extends BaseMapper<SysOrg> {

  @Select(
      "select sys_org.* from sys_org left join sys_series on sys_series.sysOrgId = sys_org.id where sys_series.id = #{id}")
  SysOrg selectBySeriesId(@Param("id") Long sysSeriesId);
}
