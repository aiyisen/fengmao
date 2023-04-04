package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统信息 实体
 */
@TableName("sys_message")
@Data
public class SysMessage extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 消息类型0系统通知1推送消息 */
  private Integer msgType;

  /** 消息内容 */
  @TableField(value = "message")
  private String message;

  /** 消息标题 */
  private String msgTitle;
}
