package com.qy.ntf.dao.provider;

import java.util.List;

public class StoreProductTemplateProvider {
  public String selectNeedCount(List<Long> collect) {
    String res =
        "SELECT\n"
            + "\tsum( a.treaCount ) as treaCount,\n"
            + "\ta.* \n"
            + "FROM\n"
            + "\t(\n"
            + "SELECT\n"
            + "\tstore_product_template.productId AS id,\n"
            + "\tcount( * ) AS treaCount,\n"
            + "\tstore_product_template.needId \n"
            + "FROM\n"
            + "\tstore_product_template left join store_treasure  on store_product_template.needId= store_treasure.id \n"
            + "WHERE\n"
            + "\tstore_treasure.state = 1  ";
    res = res + " and productId in ( ";
    for (Long aLong : collect) {
      res = res + aLong + ",";
    }
    res = res.substring(0, res.length() - 1);
    res = res + ") \n" + "GROUP BY\n" + "\tid \n" + "\t) AS a \n" + "GROUP BY\n" + "\ta.id";
    return res;
  }
}
