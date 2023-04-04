package com.qy.ntf.dao.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.entity.OrderVip;
import com.qy.ntf.bean.param.OrderVipSelectParam;

public class OrderVipDaoProvider {
  public String orderCollectData(Page<OrderVip> page, OrderVipSelectParam selectParam) {
    String s =
        "SELECT\n"
            + "\torder_vip.payTotal,\n"
            + "\torder_vip.createTime,\n"
            + "\torder_vip.payType,\n"
            + "\tsys_user.realName,\n"
            + "\tsys_user.phone  from order_vip\n"
            + "\tLEFT JOIN sys_user ON sys_user.id = order_vip.uId where order_vip.orderFlag = 1";
    if (selectParam.getStartTime() != null) {
      s =
          s
              + " and UNIX_TIMESTAMP(order_vip.createTime) > "
              + selectParam.getStartTime().getTime() / 1000;
    }
    if (selectParam.getEndTime() != null) {
      s =
          s
              + " and UNIX_TIMESTAMP(order_vip.createTime) < "
              + selectParam.getEndTime().getTime() / 1000;
    }

    return s;
  }
}
