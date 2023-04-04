package com.qy.ntf.bean.customResult;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.bean.entity.OrderProAfter;
import com.qy.ntf.bean.entity.OrderProduct;
import com.qy.ntf.bean.entity.OrderProductRecord;
import com.qy.ntf.bean.entity.SysAddress;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class AfterSaleResult {
  private OrderProduct orderProduct;
  private OrderProductRecord orderProductRecord;
  private List<OrderProAfter> orderProAfters;
  private SysAddress sysAddress;
  // 用户应继续的操作0退货退款-发货1换货-发货2撤销售后（平台已发货将无法撤销）
  private Integer userFlag;
  // 管理员应继续的操作0审核是否允许退款（退货退款，仅退款）1发货
  private Integer adminFlag;
  private String logisticsOrgName;
  private String logNum;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date turnTime;
}
