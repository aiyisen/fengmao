package com.qy.ntf.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Optional;

@ApiModel(description = "API调用结果")
public class TopResponse<T> {

  private BigDecimal total;
  private Integer topNum;
  public static final int RESPONSE_SUCCESS = 0;
  public static final int RESPONSE_FAILURE = -1;
  public static final int LOGIN_FAILURE = -2;

  @ApiModelProperty("结果代码 0: 成功 -1: 失败 -2:登录失效")
  private int code;

  @ApiModelProperty("如果成功，忽略此字段，如果失败： 描述失败原因")
  private String msg;

  @ApiModelProperty("成功时返回的数据， null表示无数据返回")
  private T data;

  public TopResponse() {}

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public Integer getTopNum() {
    return topNum;
  }

  public void setTopNum(Integer topNum) {
    this.topNum = topNum;
  }

  public TopResponse(int code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  // 用于不需要返回数据的情况
  public static <T> TopResponse<T> success() {
    return new TopResponse<T>(RESPONSE_SUCCESS, "ok", null);
  }

  // 成功，返回数据
  public static <T> TopResponse<T> success(T data) {
    return new TopResponse<T>(RESPONSE_SUCCESS, "ok", data);
  }

  // 失败
  public static <T> TopResponse<T> fail(String error) {
    return new TopResponse<T>(RESPONSE_FAILURE, error, null);
  }

  public static <T> TopResponse<T> fail(String error, Integer state) {
    return new TopResponse<T>(state, error, null);
  }

  // 根据是否空值判断调用成功，还是失败函数
  public static <T> TopResponse<T> response(Optional<T> data) {
    if (data.isPresent()) {
      return success(data.get());
    }
    return fail("no data");
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
