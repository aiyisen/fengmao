package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class TurnParam {
  // 价格升序1 价格降序0
  private Integer priceOrder;
  // 时间升序1 时间降序0
  private Integer timeOrder;
  private Long sysCateId;
  private String keyWord;
}
