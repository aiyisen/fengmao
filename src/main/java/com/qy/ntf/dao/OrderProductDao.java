package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.customResult.CellResult;
import com.qy.ntf.bean.customResult.SaleAfterOrder;
import com.qy.ntf.bean.dto.OrderProductDto;
import com.qy.ntf.bean.dto.OrderProductSelectDto;
import com.qy.ntf.bean.entity.OrderProduct;
import com.qy.ntf.dao.provider.OrderProductProvider;
import com.qy.ntf.util.PageSelectParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 实物商品订单 Dao
 */
@Mapper
public interface OrderProductDao extends BaseMapper<OrderProduct> {
  @SelectProvider(type = OrderProductProvider.class, method = "listByPage")
  IPage<OrderProductDto> listByPage(
      Page<OrderProduct> orderProductPage,
      @Param("userId") Long userId,
      PageSelectParam<OrderProductSelectDto> param);

  @Select(
      "select DATE_FORMAT(order_product.createTime,'%Y-%m-%d') as date, count(id) as `count` from order_product where orderFlag > 0 GROUP BY `date`")
  List<CellResult> getCollectLine();

  @SelectProvider(type = OrderProductProvider.class, method = "saleAfterOrders")
  IPage<SaleAfterOrder> saleAfterOrders(Page page, Integer selectParam, Long id);

  @Update("update order_product set state=-1  where uId = #{uId}")
  void deleteByUId(@Param("uId") Long id);
}
