package com.qy.ntf.bean.customResult;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CellTotal {
  private BigDecimal todayCount;
  private BigDecimal monthCount;
  private BigDecimal totalCount;
  private List<CellResult> cellResultList;
}
