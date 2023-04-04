package com.qy.ntf.bean.customResult;

import lombok.Data;

import java.util.List;

@Data
public class IndexData {
  private List<CellResult> cellList;
  private List<CellResult> orderList;
}
