package com.qy.ntf.bean.param;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class BindBoxItemAddParam {
  private Long id;
  private String treasureTitle;

  @Min(value = 0, message = "总量最少为0")
  private Integer totalCount;
}
