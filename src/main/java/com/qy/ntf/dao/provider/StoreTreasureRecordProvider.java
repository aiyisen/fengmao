package com.qy.ntf.dao.provider;

import com.qy.ntf.bean.entity.StoreTreasureRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StoreTreasureRecordProvider {
  public String insertBatch(List<StoreTreasureRecord> allRecord) {
    StringBuilder s =
        new StringBuilder(
            "INSERT INTO store_treasure_record ( strId, strNum, createId, createTime,  state ) VALUES ");
    SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date = sp.format(new Date());
    for (StoreTreasureRecord re : allRecord) {
      s =
          s.append(
              "("
                  + re.getStrId()
                  + ","
                  + re.getStrNum()
                  + ","
                  + re.getCreateId()
                  + ","
                  + "'"
                  + date
                  + "'"
                  + ", 1),");
    }
    s = new StringBuilder(s.substring(0, s.toString().length() - 1));
    return s.toString();
  }
}
