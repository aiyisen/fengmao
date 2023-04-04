package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 帮助中心 实体
 */
@TableName("sys_help")
@Data
public class SysHelp extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 标题 */
  private String queTitle;

  /** 内容（富文本） */
  private String queAns;
}
