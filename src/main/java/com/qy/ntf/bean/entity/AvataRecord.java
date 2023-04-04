package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("avata_record")
@Data
public class AvataRecord {
  private Long strId;
  private String tmpId;
}
