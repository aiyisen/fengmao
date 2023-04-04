package com.qy.ntf.bean.param;

import lombok.Data;

import java.util.List;

@Data
public class AddDropParam {
  private List<Long> userIds;
  private Long integral;
  private Long id;
  private Integer count;
}
