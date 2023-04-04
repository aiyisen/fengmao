package com.qy.ntf.dao.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.entity.OrderProduct;
import org.apache.ibatis.annotations.Param;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderTreasurePoolProvider {
  public String selectByDdcIds(List<String> ddcIds) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        "SELECT\n"
            + "\t DISTINCT sys_user_backpack.createId\n"
            + "FROM\n"
            + "\tsys_user_backpack\n"
            + "\tLEFT JOIN store_treasure ON store_treasure.id = sys_user_backpack.sTreasureId \n"
            + "WHERE\n"
            + "\tfinType IN ( 4,6, 8 ) \n"
            + "\tAND sys_user_backpack.orderFingerprint NOT IN ( SELECT sys_user_backpack.orderFingerprint FROM sys_user_backpack WHERE sys_user_backpack.finType IN ( 7, 3 ) ) \n"
            + "\t\n"
            + "\tAND store_treasure.ddcId in ( ");
    for (int i = 0; i < ddcIds.size(); i++) {
      if (i < ddcIds.size() - 1) {
        sb.append("'").append(ddcIds.get(i)).append("'").append(" , ");
      } else {
        sb.append("'")
            .append(ddcIds.get(i))
            .append("'")
            .append(" ) ORDER BY\n")
            .append("\tsys_user_backpack.id ASC;");
      }
    }
    return sb.toString();
  }

  public String selectByTitles(List<String> titles) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        "SELECT\n"
            + "\t DISTINCT sys_user_backpack.createId\n"
            + "FROM\n"
            + "\tsys_user_backpack\n"
            + "\tLEFT JOIN store_treasure ON store_treasure.id = sys_user_backpack.sTreasureId \n"
            + "WHERE\n"
            + "\tfinType IN ( 4,6, 8 ) \n"
            + "\tAND sys_user_backpack.orderFingerprint NOT IN ( SELECT sys_user_backpack.orderFingerprint FROM sys_user_backpack WHERE sys_user_backpack.finType IN ( 7, 3 ) ) \n"
            + "\t\n"
            + "\tAND store_treasure.treasureTitle in ( ");
    for (int i = 0; i < titles.size(); i++) {
      if (i < titles.size() - 1) {
        sb.append("'").append(titles.get(i)).append("'").append(" , ");
      } else {
        sb.append("'")
            .append(titles.get(i))
            .append("'")
            .append(" ) ORDER BY\n")
            .append("\tsys_user_backpack.id ASC;");
      }
    }
    return sb.toString();
  }

  public String selectUnDoOrder(Long id, Date parse) {
    return "SELECT\n"
        + "\tid,\n"
        + "\ttradeNo,\n"
        + "\torderFingerprint,\n"
        + "\tteaPoId,\n"
        + "\titemType,\n"
        + "\tcurPrice,\n"
        + "\ttotalPrice,\n"
        + "\torderFlag,\n"
        + "\torderNum,\n"
        + "\tcheckFlag,\n"
        + "\ttotalCount,\n"
        + "\tpayType,\n"
        + "\tisJoin,\n"
        + "\tpayEndTime,\n"
        + "\tpayTime,\n"
        + "\tcreateId,\n"
        + "\tcreateTime,\n"
        + "\tupdateId,\n"
        + "\tupdateTime,\n"
        + "\tstate \n"
        + "FROM\n"
        + "\torder_treasure_pool \n"
        + "WHERE\n"
        + "\t( createId = "
        + id
        + "  AND orderFlag IN ( 0,-1 ) AND  UNIX_TIMESTAMP(createTime) > "
        + parse.getTime() / 1000
        + " )";
  }

  public String inviteCollect(Page page, @Param("type") Integer type) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        "SELECT\n"
            + "\tsys_user.username,\n"
            + "\tsys_user.id as  userId,\n"
            + " sys_user.headImg, "
            + "\tcount( sys_user_invite_link.uId ) as total\n"
            + "FROM\n"
            + "\tsys_user_invite_link\n"
            + "\tLEFT JOIN sys_user ON sys_user.id\t= sys_user_invite_link.uId where 1=1\n");
    Calendar instance = Calendar.getInstance();
    switch (type) {
      case 0:
        instance.set(
            instance.get(Calendar.YEAR),
            instance.get(Calendar.MONTH),
            instance.get(Calendar.DAY_OF_MONTH),
            0,
            0,
            0);
        sb.append(" and  UNIX_TIMESTAMP(sys_user_invite_link.createTime) > ")
            .append(instance.getTime().getTime() / 1000);
        break;
      case 1:
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = instance.get(Calendar.DAY_OF_WEEK); // 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
          instance.add(Calendar.DAY_OF_MONTH, -1);
        }
        instance.setFirstDayOfWeek(Calendar.MONDAY); // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = instance.get(Calendar.DAY_OF_WEEK); // 获得当前日期是一个星期的第几天
        instance.add(
            Calendar.DATE, instance.getFirstDayOfWeek() - day); // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

        sb.append(" and  UNIX_TIMESTAMP(sys_user_invite_link.createTime) > ")
            .append(instance.getTime().getTime() / 1000);
        break;
      case 2:
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int days = instance.get(Calendar.DAY_OF_MONTH); // 获得当前日期是一个星期的第几天
        instance.add(
            Calendar.DATE, instance.getFirstDayOfWeek() - days); // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

        sb.append(" and  UNIX_TIMESTAMP(sys_user_invite_link.createTime) > ")
            .append(instance.getTime().getTime() / 1000);
        break;
      case 3:
        break;
      default:
        break;
    }
    sb.append("\tgroup by sys_user_invite_link.uId\n" + "\t\torder by total desc");
    return sb.toString();
  }

  public String consumptionCollect(Page page, @Param("type") Integer type) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        "SELECT\n"
            + "\tsys_user.username,\n"
            + " sys_user.headImg, "
            + "\tsys_user.id as  userId,\n"
            + "\tsum( order_treasure_pool.totalPrice ) AS total \n"
            + "FROM\n"
            + "\torder_treasure_pool\n"
            + "\tLEFT JOIN sys_user ON sys_user.id = order_treasure_pool.createId where 1=1 \n");
    Calendar instance = Calendar.getInstance();
    // 0日榜1周榜2月榜3总榜
    switch (type) {
      case 0:
        instance.set(
            instance.get(Calendar.YEAR),
            instance.get(Calendar.MONTH),
            instance.get(Calendar.DAY_OF_MONTH),
            0,
            0,
            0);
        sb.append(" and UNIX_TIMESTAMP(order_treasure_pool.createTime) > ")
            .append(instance.getTime().getTime() / 1000);
        break;
      case 1:
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = instance.get(Calendar.DAY_OF_WEEK); // 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
          instance.add(Calendar.DAY_OF_MONTH, -1);
        }
        instance.setFirstDayOfWeek(Calendar.MONDAY); // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = instance.get(Calendar.DAY_OF_WEEK); // 获得当前日期是一个星期的第几天
        instance.add(
            Calendar.DATE, instance.getFirstDayOfWeek() - day); // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

        sb.append(" and  UNIX_TIMESTAMP(order_treasure_pool.createTime) > ")
            .append(instance.getTime().getTime() / 1000);
        break;
      case 2:
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int days = instance.get(Calendar.DAY_OF_MONTH); // 获得当前日期是一个星期的第几天
        instance.add(
            Calendar.DATE, instance.getFirstDayOfWeek() - days); // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

        sb.append(" and  UNIX_TIMESTAMP(order_treasure_pool.createTime) > ")
            .append(instance.getTime().getTime() / 1000);
        break;
      case 3:
        break;
      default:
        break;
    }
    sb.append(" and orderFlag > 0 ");
    sb.append("GROUP BY order_treasure_pool.createId order by total desc");
    return sb.toString();
  }

  public String listByPage(
      Page<OrderProduct> orderProductPage,
      @Param("userId") Long userId,
      @Param("orderFlag") Integer orderFlag,
      @Param("itemType") Integer itemType) {
    String res = "select * from order_treasure_pool where payType != -1";
    if (userId != null) {
      res = res + " and createId = " + userId;
    }
    //    if (itemType == null) {
    //      res = res + "  and order_treasure_pool.itemType!=4 ";
    //    } else if (itemType == 4) {
    //      res = res + "  and order_treasure_pool.itemType ==4 ";
    //    }
    if (orderFlag != null) {
      switch (orderFlag) {
        case -1:
          res = res + " and orderFlag = -1 ";
          break;
        case 0:
          res = res + " and orderFlag = 0 ";
          break;
        case 1:
          res = res + " and orderFlag = 2   ";
          break;
        case 2:
          res = res + " and orderFlag = 2 ";
          break;

        default:
          break;
      }
    }
    return res + " order by order_treasure_pool.createTime desc";
  }

  public String listByPageWithNoCreateId(
      Page<OrderProduct> orderProductPage,
      @Param("userId") Long userId,
      @Param("orderFlag") Integer orderFlag,
      @Param("itemType") Integer itemType) {
    String res = "select * from order_treasure_pool where  ";

    res = res + "  1 = 1";

    //    if (itemType == null) {
    //      res = res + "  and order_treasure_pool.itemType!=4 ";
    //    } else if (itemType == 4) {
    //      res = res + "  and order_treasure_pool.itemType ==4 ";
    //    }
    if (orderFlag != null) {
      switch (orderFlag) {
        case -1:
          res = res + " and state =1 and payType != -1 and orderFlag = -1 ";
          break;
        case 0:
          res = res + " and  state =1 and payType != -1 and orderFlag = 0 ";
          break;
        case 1:
          res =
              res
                  + " and  ((state =1 and payType != -1 and orderFlag = 2)or (payType=-1 and state=-1)  )and order_treasure_pool.orderFingerprint in (select store_treasure.orderFingerprint from store_treasure where store_treasure.state=1) ";
          break;
        case 2:
          res = res + " and state =1 and payType != -1 and orderFlag = 2 ";
          break;

        default:
          break;
      }
    }
    return res + " order by order_treasure_pool.createTime desc";
  }

  public String getReResTreasure(
      @Param("orderFins") List<String> orderFins, @Param("itemType") int itemType) {
    StringBuilder res = new StringBuilder();
    res =
        new StringBuilder(
            "select order_treasure_pool.id, "
                + " order_treasure_pool.createTime,"
                + " store_treasure.treasureTitle, "
                + " store_treasure.headImgPath, "
                + " order_treasure_pool.orderFingerprint from order_treasure_pool "
                + "left join store_treasure on store_treasure.id = order_treasure_pool.teaPoId where order_treasure_pool.orderFlag = 2 ");
    if (orderFins.size() > 0) {
      res.append(" and orderFingerprint in ( ");
      for (String orderFin : orderFins) {
        res.append(" '").append(orderFin).append("',");
      }
      res = new StringBuilder(res.substring(0, res.length() - 1));
      res.append(" ) ");
    }
    return res.toString();
  }

  public String getReProPool(
      @Param("orderFins") List<String> orderFins, @Param("itemType") int itemType) {
    StringBuilder res = new StringBuilder();
    res =
        new StringBuilder(
            "select order_treasure_pool.id,"
                + " order_treasure_pool.createTime,"
                + " store_pro_pool.proPoolTitle, "
                + " store_pro_pool.indexPath, "
                + " order_treasure_pool.orderFingerprint"
                + " from order_treasure_pool left join store_pro_pool on store_pro_pool.id = order_treasure_pool.teaPoId"
                + " where  ");
    if (orderFins.size() > 0) {
      res.append(" and order_treasure_pool.orderFingerprint in ( ");
      for (String orderFin : orderFins) {
        res.append(" '").append(orderFin).append("',");
      }
      res = new StringBuilder(res.substring(0, res.length() - 1));
      res.append(" ) ");
    }
    return res.toString();
  }
}
