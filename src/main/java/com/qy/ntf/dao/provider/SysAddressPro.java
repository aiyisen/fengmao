package com.qy.ntf.dao.provider;

import java.util.List;

public class SysAddressPro {
  public String selectDetailByIds(List<Long> collect) {
    String res =
        "SELECT\n"
            + "\tCONCAT_WS('',sys_address.`provName`,sys_address.`cityName`,sys_address.`conName`,sys_address.detail) as detail,\n"
            + "\tsys_address.*\n"
            + "FROM\n"
            + "\tsys_address  where sys_address.id in ( ";
    for (Long aLong : collect) {
      res = res + aLong + ",";
    }
    res = res.substring(0, res.length() - 1);
    res += ")";
    return res;
  }
}
