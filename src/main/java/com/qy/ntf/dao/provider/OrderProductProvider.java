package com.qy.ntf.dao.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.dto.OrderProductSelectDto;
import com.qy.ntf.bean.entity.OrderProduct;
import com.qy.ntf.util.PageSelectParam;
import org.apache.ibatis.annotations.Param;

public class OrderProductProvider {

  public String listByPage(
      Page<OrderProduct> orderProductPage,
      @Param("userId") Long userId,
      PageSelectParam<OrderProductSelectDto> param) {
    String res = "select * from order_product where state =1";
    if (userId != null) {
      res = res + " and uId = " + userId;
    }
    if (param.getSelectParam().getOrderFlag() != null) {
      res = res + " and orderFlag = " + param.getSelectParam().getOrderFlag();
    }
    res = res + " order by createTime desc";
    return res;
  }

  public String saleAfterOrders(Page page, Integer selectParam, Long id) {

    String res =
        " select a.* from (select order_product.id as orderId, "
            + "order_product.productId as proId,\n"
            + " order_product.proCount as  productCount,"
            + "order_product.totalPrice as orderTotalPrice, \n"
            + "order_pro_after.orderType as afterType, \n"
            + "order_pro_after.orderAfterState as afterState, \n"
            + " order_product.orderFlag as orderState , "
            + " order_pro_after.createTime AS createTime   "
            + "from order_product left join order_pro_after on order_pro_after.orderProductId = order_product.id where order_product.state =1 and order_product.saleAfterActive = 1 ";
    if (id != null && id != -1) {
      res =
          res
              + " and order_product.uId = "
              + id
              + " ORDER BY\n"
              + "\torder_pro_after.createTime DESC \n"
              + "\tlimit 111111\n"
              + "\t) AS a \n"
              + "GROUP BY\n"
              + "\ta.orderId order by createTime desc";
    } else {
      res =
          res
              + " ORDER BY\n"
              + "\torder_pro_after.createTime DESC \n"
              + "\tlimit 111111\n"
              + "\t) AS a \n"
              + "GROUP BY\n"
              + "\ta.orderId order by createTime desc ";
    }
    //    if (selectParam != null) {
    //      res = res + " and order_pro_after.orderAfterState = 5";
    //    }

    return res;
  }
}
