package com.qy.ntf.util;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页参数
 *
 * @param <T>
 */
@ApiModel(value = "分页查询参数", description = "分页查询参数")
public class PageSelectParam<T> extends BaseEntity {

  /** 开始页 */
  @ApiModelProperty(
      value = "开始页",
      name = "pageNum",
      example = "1",
      dataType = "Integer",
      required = false)
  private Integer pageNum = PageParam.pageNum;
  /** 每页尺寸 */
  @ApiModelProperty(
      value = "每页尺寸",
      name = "pageSize",
      example = "10",
      dataType = "Integer",
      required = false)
  private Integer pageSize = PageParam.pageSize;
  /** 排序 */
  @ApiModelProperty(
      value = "排序",
      name = "orderBy",
      example = "id desc",
      dataType = "String",
      required = false)
  private String orderBy = PageParam.orderBy;

  /** 查询条件 */
  @ApiModelProperty(value = "查询条件", required = false)
  private T selectParam;

  public PageSelectParam() {}

  public PageSelectParam(T selectParam) {
    this.selectParam = selectParam;
  }

  public PageSelectParam(Integer pageNum, Integer pageSize, String orderBy, T selectParam) {
    this.pageNum = pageNum;
    this.pageSize = pageSize;
    this.orderBy = orderBy;
    this.selectParam = selectParam;
  }

  public void setOrderBy(String orderBy) {
    if (null != orderBy && !"".equals(orderBy)) {
      this.orderBy = orderBy;
    }
  }

  public String getOrderBy(String tableName) {
    return tableName + "." + this.orderBy;
  }

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public Integer getPageSize() {
    return pageSize == null ? 10 : pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public T getSelectParam() {
    return selectParam;
  }

  public void setSelectParam(T selectParam) {
    this.selectParam = selectParam;
  }
}
