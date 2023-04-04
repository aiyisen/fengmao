package com.qy.ntf.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class CalendarResult {
  private String date;
  private List<StoreTreasureDto> storeTreasureDtoList;
}
