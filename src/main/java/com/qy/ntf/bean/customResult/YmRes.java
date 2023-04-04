package com.qy.ntf.bean.customResult;

import lombok.Data;

@Data
public class YmRes {
  private String success;
  private Integer code;
  private String message;
  private String requestId;
  private Item data;

  @Data
  public static class Item {
    private String mobile;
    private Double score;
    private Double activeScore;
  }
}
