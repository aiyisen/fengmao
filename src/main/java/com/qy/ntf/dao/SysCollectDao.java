package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.customResult.CellResult;
import com.qy.ntf.bean.entity.SysCollect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 王振读 email 2022-06-01 21:24:13 DESC : Dao
 */
@Mapper
public interface SysCollectDao extends BaseMapper<SysCollect> {

  @Select("select date, totalCount as `count` from sys_collect order by `date` desc limit 0,100")
  List<CellResult> selectListByCount();

  @Select("select date, totalCount as `count` from sys_collect order by `date` desc limit 0,30")
  List<CellResult> selectListByMonth();
}
