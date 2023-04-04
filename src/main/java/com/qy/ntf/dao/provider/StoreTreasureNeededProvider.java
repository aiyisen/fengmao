package com.qy.ntf.dao.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.customResult.TreaTrading;
import org.apache.ibatis.annotations.Param;

public class StoreTreasureNeededProvider {
  public String tradingPage(
      Page<TreaTrading> IPage,
      @Param("selectParam") Integer selectParam,
      @Param("userId") Long userId) {
    String res =
        "SELECT\n"
            + "IF\n"
            + "\t( sys_user_backpack.treasureFrom = 0, store_treasure.treasureTitle, store_pro_pool.proPoolTitle ) title,\n"
            + "\tsys_user_backpack.finType AS type,\n"
            + "IF\n"
            + "\t( sys_user_backpack.treasureFrom = 0, store_treasure.headImgPath, store_pro_pool.indexPath ) AS imgPath,\n"
            + "IF\n"
            + "\t( sys_user_backpack.treasureFrom = 0, store_treasure.price, store_pro_pool.proPrice ) AS price,\n"
            + "\tbeforUser.username AS beforeUser,\n"
            + "\tafterUser.username AS afterUser \n"
            + "FROM\n"
            + "\tsys_user_backpack\n"
            + "\tLEFT JOIN sys_user beforUser ON sys_user_backpack.beforeUserId = beforUser.id\n"
            + "\tLEFT JOIN sys_user afterUser ON sys_user_backpack.afterUserId = afterUser.id\n"
            + "\tLEFT JOIN store_treasure ON store_treasure.id = sys_user_backpack.sTreasureId\n"
            + "\tLEFT JOIN store_pro_pool ON store_pro_pool.id = sys_user_backpack.sTreasureId"
            + " where sys_user_backpack.state = 1 and sys_user_backpack.uId = #{userId} ";
    if (selectParam != null) {
      //      -2收入-1支出1赠送2获赠3出售4购买
      switch (selectParam) {
        case -1:
          res = res + " and sys_user_backpack.finType in (2,4)";
        case -2:
          res = res + " and sys_user_backpack.finType in (1,3)";
        case 1:
          res = res + " and sys_user_backpack.finType =1";
        case 2:
          res = res + " and sys_user_backpack.finType =2";
        case 3:
          res = res + " and sys_user_backpack.finType =3";
        case 4:
          res = res + " and sys_user_backpack.finType =4";
        default:
          break;
      }
    }
    res = res + " order by sys_user_backpack.createTime desc ";
    return res;
  }
}
