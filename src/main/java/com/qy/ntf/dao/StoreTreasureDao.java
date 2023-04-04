package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.dto.StoreTreasureDto;
import com.qy.ntf.bean.entity.StoreTreasure;
import com.qy.ntf.bean.param.TurnParam;
import com.qy.ntf.dao.provider.StoreTreasureProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 藏品首发/超前申购 主体 Dao
 */
@Mapper
public interface StoreTreasureDao extends BaseMapper<StoreTreasure> {
  @Select(
      "select store_treasure.*, store_treasure_needed.needCount as needCount "
          + "from store_treasure_needed left join store_treasure on store_treasure_needed.needStoreTreasureId = store_treasure.id "
          + "where store_treasure_needed.state =1 and store_treasure_needed.storeTreasureId = #{id}")
  List<StoreTreasureDto> getCouldCompById(@Param("id") Long id);

  @Select(
      "select store_treasure.*, store_treasure.s_t_r_i_p_id as stripId  from store_treasure where id in (select order_treasure_pool.teaPoId from order_treasure_pool where order_treasure_pool.isJoin=1 and order_treasure_pool.createId=#{userId}) order by store_treasure.createTime desc")
  IPage<StoreTreasureDto> getMyJoin(Page page, Long userId);

  @Update("update store_treasure set state =-1 where id = #{id}")
  void removeById(@Param("id") Long id);

  @Select("select * from store_treasure ${ew.customSqlSegment}")
  Page<StoreTreasureDto> selectByQue(
      Page page, @Param(Constants.WRAPPER) LambdaQueryWrapper<StoreTreasure> que);

  @SelectProvider(type = StoreTreasureProvider.class, method = "turnPage")
  Page<StoreTreasureDto> turnPage(Page<Object> objectPage, TurnParam param);

  @SelectProvider(type = StoreTreasureProvider.class, method = "turnAndTurnPage")
  IPage<StoreTreasureDto> turnAndTurnPage(Page<Object> objectPage, @Param("ti") String ti);

  @Select(
      "select store_treasure.* from store_treasure where pId=#{id} and state =1 and fromUser !=1")
  List<StoreTreasure> selectByPid(@Param("id") Long id);

  @Select(
      "select store_treasure.* from store_treasure  where  ddcId =#{ddcId} and fromUser !=1 limit 1")
  StoreTreasure selectPlateTreaByDDCID(@Param("ddcId") String ddcId);
}
