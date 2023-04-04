package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 反馈建议 实体
 */
@TableName("sys_idea")
@Data
public class SysIdea extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 用户id */
  private Long uId;

  /** 意见反馈内容 */
  private String ideaContent;
}
