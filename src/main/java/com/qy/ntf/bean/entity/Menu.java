package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_menu")
@Data
public class Menu {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long pid;
  private Integer type;
  private String name;
  private String path;
  private String icon;
  private String description;
  private Integer sortNumber;
  private Integer state;

  @Override
  public String toString() {
    return id + ", " + name + ", type=" + type + ", pid=" + pid;
  }
}
