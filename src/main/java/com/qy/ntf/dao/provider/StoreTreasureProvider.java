package com.qy.ntf.dao.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.param.TurnParam;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.Strings;

public class StoreTreasureProvider {
  public String turnPage(Page<Object> objectPage, TurnParam param) {
    StringBuilder sb = new StringBuilder();
    sb.append("select * from store_treasure where fromUser =1 and state =1 and surplusCount > 0");
    if (param != null && param.getSysCateId() != null && param.getSysCateId() != 0) {
      sb.append(" and sysCateId = ").append(param.getSysCateId());
    }
    if (param != null && Strings.isNotEmpty(param.getKeyWord())) {
      sb.append(" and treasureTitle like '%").append(param.getKeyWord()).append("%'");
    }
    sb.append(" order by ");
    if (param != null && param.getPriceOrder() == null && param.getTimeOrder() == null) {
      sb.append(" createTime desc  ");
    } else {
      // 价格升序0 价格降序1
      if (param != null && param.getPriceOrder() != null && param.getPriceOrder() == 1) {
        sb.append(" price desc, ");
      } else if (param != null && param.getPriceOrder() != null) {
        sb.append(" price asc, ");
      }
      // 时间升序0 时间降序1
      if (param != null && param.getTimeOrder() != null && param.getTimeOrder() == 1) {
        sb.append(" createTime desc, ");
      } else if (param != null && param.getTimeOrder() != null) {
        sb.append(" createTime asc, ");
      }
      String s = sb.toString();
      return s.substring(0, s.length() - 2);
    }
    return sb.toString();
  }

  public String turnAndTurnPage(Page<Object> objectPage, @Param("ti") String selectParam) {
    String sql =
        "SELECT\n"
            + "\tstore_treasure.* ,be.username as givingUser, af.username as reciveUser\n"
            + "FROM\n"
            + "\tsys_user_backpack\n"
            + "\tLEFT JOIN store_treasure ON store_treasure.id = sys_user_backpack.sTreasureId \n"
            + "\tleft join sys_user be on be.id = sys_user_backpack.beforeUserId\n"
            + "\tleft join sys_user af on af.id = sys_user_backpack.afterUserId\n"
            + "WHERE\n"
            + "\tsys_user_backpack.finType =1 ";
    if (Strings.isNotEmpty(selectParam)) {
      sql = sql + " and store_treasure.treasureTitle like '%" + selectParam + "%'";
    }
    return sql;
  }
}
