package com.qy.ntf.dao.provider;

import com.qy.ntf.bean.param.TopSelectParam;

public class SysUserProvider {
  public String findUserByTop(TopSelectParam param) {
    String sb =
        "select a.* from ("
            + "SELECT\n"
            + "\tsys_user.username,\n"
            + "\tsys_user.id as  id,\n"
            + " sys_user.headImg, "
            + " sys_user.realName AS realName,"
            + " sys_user.phone as phone, "
            + " sys_user.metaCount as metaCount,"
            + " sys_user.balance as balance,"
            + "\tcount( sys_user_invite_link.uId ) as total\n"
            + "FROM\n"
            + "\tsys_user_invite_link\n"
            + "\tLEFT JOIN sys_user ON sys_user.id\t= sys_user_invite_link.uId where 1=1\n"
            + " and UNIX_TIMESTAMP(sys_user_invite_link.createTime) > "
            + param.getStartTime().getTime() / 1000
            + " and UNIX_TIMESTAMP(sys_user_invite_link.createTime) < "
            + param.getEndTime().getTime() / 1000
            + " GROUP BY sys_user_invite_link.uId "
            + ") as a order by a.total desc";

    return sb;
  }
}
