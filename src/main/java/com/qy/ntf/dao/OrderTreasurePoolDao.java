package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.customResult.CellResult;
import com.qy.ntf.bean.customResult.StorePoolPriceRecord;
import com.qy.ntf.bean.dto.OrderTreasurePoolDto;
import com.qy.ntf.bean.dto.TopItem;
import com.qy.ntf.bean.entity.OrderTreasurePool;
import com.qy.ntf.bean.entity.StoreTreasure;
import com.qy.ntf.dao.provider.OrderTreasurePoolProvider;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 聚合池藏品订单 Dao
 */
@Mapper
public interface OrderTreasurePoolDao extends BaseMapper<OrderTreasurePool> {
  @Select(
      "select sys_user.username , order_treasure_pool.curPrice as price, order_treasure_pool.createTime "
          + "from order_treasure_pool left join sys_user on sys_user.id = order_treasure_pool.createId where order_treasure_pool.createTime "
          + "> #{time} and order_treasure_pool.teaPoId=#{id} and order_treasure_pool.itemType = 2")
  List<StorePoolPriceRecord> getPoolPriceRecord(@Param("id") Long id, @Param("time") Long time);

  @Select(
      "select sys_user.username , order_treasure_pool.curPrice as price, order_treasure_pool.createTime "
          + "from order_treasure_pool left join sys_user on sys_user.id = order_treasure_pool.createId where  "
          + " order_treasure_pool.teaPoId=#{id} and order_treasure_pool.itemType = 2")
  IPage<StorePoolPriceRecord> getPoolPriceRecordByPage(
      Page<StorePoolPriceRecord> page, @Param("id") Long id);

  @SelectProvider(type = OrderTreasurePoolProvider.class, method = "listByPage")
  IPage<OrderTreasurePoolDto> listByPage(
      Page<OrderTreasurePool> orderProductPage,
      @Param("userId") Long userId,
      @Param("orderFlag") Integer orderFlag,
      @Param("itemType") Integer itemType);

  @SelectProvider(type = OrderTreasurePoolProvider.class, method = "listByPageWithNoCreateId")
  IPage<OrderTreasurePoolDto> listByPageWithNoCreateId(
      Page<OrderTreasurePool> orderProductPage,
      @Param("userId") Long userId,
      @Param("orderFlag") Integer orderFlag,
      @Param("itemType") Integer itemType);

  @SelectProvider(type = OrderTreasurePoolProvider.class, method = "getReResTreasure")
  List<StoreTreasure> getReResTreasure(
      @Param("orderFins") List<String> orderFins, @Param("itemType") int itemType);

  //  @SelectProvider(type = OrderTreasurePoolProvider.class, method = "getReProPool")
  //  List<StoreProPool> getReProPool(
  //      @Param("orderFins") List<String> orderFins, @Param("itemType") int itemType);

  @Select(
      "SELECT\n"
          + "\tcount( a.id ) AS `count`,\n"
          + "\ta.date \n"
          + "FROM\n"
          + "\t( SELECT DATE_FORMAT( createTime, '%Y-%m-%d' ) AS date, order_treasure_pool.id FROM order_treasure_pool WHERE `orderFlag` > 0 ) AS a \n"
          + "GROUP BY\n"
          + "\ta.`date`")
  List<CellResult> getCollectLine(@Param("type") int type);

  @Select(
      "select * from order_treasure_pool where order_treasure_pool.orderFingerprint = #{orderFingerprint} and orderFlag = 2 order by createTime desc limit 1")
  OrderTreasurePool selectByOrderFingerpint(String orderFingerprint);

  @Update("update order_treasure_pool set state=-1 where order_treasure_pool.createId =  #{uId}")
  void delectByUId(@Param("uId") Long uId);

  @Select(
      "select count(*) from order_treasure_pool where createId = #{id} and orderFlag > 0 and state =1")
  Integer getCountByUserId(@Param("id") Long id);

  @SelectProvider(type = OrderTreasurePoolProvider.class, method = "consumptionCollect")
  IPage<TopItem> consumptionCollect(Page page, @Param("type") Integer type);

  @SelectProvider(type = OrderTreasurePoolProvider.class, method = "inviteCollect")
  IPage<TopItem> inviteCollect(Page page, @Param("type") Integer selectParam);

  @SelectProvider(type = OrderTreasurePoolProvider.class, method = "selectUnDoOrder")
  List<OrderTreasurePool> selectUnDoOrder(Long id, Date parse);

  @SelectProvider(type = OrderTreasurePoolProvider.class, method = "selectByDdcIds")
  Set<Long> selectByDdcIds(List<String> ddcIds);

  @SelectProvider(type = OrderTreasurePoolProvider.class, method = "selectByTitles")
  Set<Long> selectByTitles(List<String> titles);
}
