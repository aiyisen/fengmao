package com.qy.ntf.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class CollectRecord {
  private Long strId;
  private String strTitle;
  private List<TmpUserInfo> tmpUserInfoList;
}
