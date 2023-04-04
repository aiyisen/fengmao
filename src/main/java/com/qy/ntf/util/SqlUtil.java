package com.qy.ntf.util;

import com.qy.ntf.base.BaseEntity;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author songYu Created on 2019/3/29 13:40 DESC : sql 工具
 */
public class SqlUtil {

  /**
   * 获取sql
   *
   * @return
   */
  public static SQL getSql() {
    return new SQL();
  }

  /**
   * 单表获取公共查询字段
   *
   * @return
   */
  public static SQL getCommonSelectColumn() {
    return getCommonSelectColumn(null);
  }

  /**
   * 多表获取公共字段
   *
   * @param tableName
   * @return
   */
  public static SQL getCommonSelectColumn(String tableName) {
    if (null != tableName && !"".equals(tableName)) {
      tableName = tableName + ".";
    } else {
      tableName = "";
    }
    SQL sql =
        getSql()
            .SELECT(tableName + "id AS id")
            .SELECT(tableName + "create_by AS createBy")
            .SELECT(tableName + "create_time AS createTime")
            .SELECT(tableName + "update_by AS updateBy")
            .SELECT(tableName + "update_time AS updateTime")
            .SELECT(tableName + "state AS state")
            .SELECT(tableName + "organization_code AS organizationCode");
    return sql;
  }

  /**
   * 单表设置公共查询条件
   *
   * @param sql
   * @param paramName
   * @param dto
   * @return
   */
  public static SQL setCommonSelectWhere(SQL sql, String paramName, BaseEntity dto) {
    return setCommonSelectWhere(sql, paramName, null, dto);
  }

  /**
   * 多表设置公共查询条件
   *
   * @param sql
   * @param paramName 参数名
   * @param tableName 表名或者别名
   * @param dto
   * @return
   */
  public static SQL setCommonSelectWhere(
      SQL sql, String paramName, String tableName, BaseEntity dto) {
    if (null != tableName && !"".equals(tableName)) {
      tableName = tableName + ".";
    } else {
      tableName = "";
    }
    //        if (StringUtil.notNullOrEmpty(dto.getCreateBy())) {
    //            sql.WHERE(tableName + "create_by LIKE CONCAT('%',#{" + paramName +
    // ".createBy},'%')");
    //        }
    //        if (null != dto.getCreateStartTime()) {
    //            sql.WHERE(tableName + "create_time >= #{" + paramName + ".createStartTime}");
    //        }
    //        if (null != dto.getCreateEndTime()) {
    //            sql.WHERE(tableName + "create_time <= #{" + paramName + ".createStartTime}");
    //        }
    //        if (StringUtil.notNullOrEmpty(dto.getUpdateBy())) {
    //            sql.WHERE(tableName + "update_by LIKE CONCAT('%',#{" + paramName +
    // ".updateBy},'%')");
    //        }
    //        if (null != dto.getUpdateStartTime()) {
    //            sql.WHERE(tableName + "update_time >= #{" + paramName + ".updateStartTime}");
    //        }
    //        if (null != dto.getUpdateEndTime()) {
    //            sql.WHERE(tableName + "update_time <= #{" + paramName + ".updateStartTime}");
    //        }
    if (null != dto.getState()) {
      sql.WHERE(tableName + "state = #{" + paramName + ".state}");
    }
    return sql;
  }

  /**
   * 设置公共分组字段
   *
   * @param sql
   * @param tableName
   * @return
   */
  public static SQL setCommonGroupBy(SQL sql, String tableName) {
    if (StringUtil.notNullOrEmpty(tableName)) {
      tableName = tableName + ".";
    } else {
      tableName = "";
    }
    sql.GROUP_BY(tableName + "id")
        .GROUP_BY(tableName + "create_by")
        .GROUP_BY(tableName + "create_time")
        .GROUP_BY(tableName + "update_by")
        .GROUP_BY(tableName + "update_time")
        .GROUP_BY(tableName + "state")
        .GROUP_BY(tableName + "organization");
    return sql;
  }
}
