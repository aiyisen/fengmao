package com.qy.ntf.bean.entity;

import com.qy.ntf.base.BaseEntity;
import lombok.Data;

@Data
public class StoreTreasureRecord extends BaseEntity {
  private Long id;
  private Long strId;
  private Long userId;
  private String orderFingerprint;
  private String strNum;
  private String nftId;
}
