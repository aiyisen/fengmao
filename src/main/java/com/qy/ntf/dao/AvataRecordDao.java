package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.AvataRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 充值订单表 Dao
 */
@Mapper
public interface AvataRecordDao extends BaseMapper<AvataRecord> {
  @Select("select strId from avata_record where avata_record.tmpId = #{tmpTreaId} limit 1")
  String findStrIdByTmpId(String tmpTreaId);
}
