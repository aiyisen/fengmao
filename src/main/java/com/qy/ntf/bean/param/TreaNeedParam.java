package com.qy.ntf.bean.param;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class TreaNeedParam {
  private Long id;

  @Min(value = 0, message = "数量最小为0")
  private Integer count;
}
