package com.qy.ntf.bean.param;

import lombok.Data;

import java.util.List;

@Data
public class AddCouldCompParam {
  private Long stId;
  private List<Item> items;

  @Data
  public static class Item {
    private Long needId;
    private Integer needCount;
  }
}
