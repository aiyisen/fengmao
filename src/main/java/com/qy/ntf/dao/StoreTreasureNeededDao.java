package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.customResult.TreaTrading;
import com.qy.ntf.bean.dto.StoreTreasureDto;
import com.qy.ntf.bean.entity.StoreTreasureNeeded;
import com.qy.ntf.dao.provider.StoreTreasureNeededProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author 王振读 email 2022-05-28 15:09:30 DESC : Dao
 */
@Mapper
public interface StoreTreasureNeededDao extends BaseMapper<StoreTreasureNeeded> {

  @SelectProvider(type = StoreTreasureNeededProvider.class, method = "tradingPage")
  IPage<TreaTrading> tradingPage(
      Page<TreaTrading> IPage,
      @Param("selectParam") Integer selectParam,
      @Param("userId") Long userId);

  @Select(
      "select  store_treasure.* from store_treasure where couldCompound = 1 and tType=2 and fromUser != 1 and state =1 order by createTime desc")
  List<StoreTreasureDto> getNeededList();

  @Select(
      "SELECT\n"
          + "\tstore_treasure_needed.* \n"
          + "FROM\n"
          + "\tstore_treasure_needed\n"
          + "\tLEFT JOIN store_treasure ON store_treasure.id = store_treasure_needed.needStoreTreasureId \n"
          + "WHERE\n"
          + "\tstore_treasure_needed.storeTreasureId = #{id} \n"
          + "\tAND store_treasure_needed.state =1\n"
          + "\t")
  List<StoreTreasureNeeded> selectByStoreTreasureId(@Param("id") Long id);
}
