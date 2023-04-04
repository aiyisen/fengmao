package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 邀请记录 实体
 */
@TableName("sys_user_invite_link")
@Data
public class SysUserInviteLink {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 用户id */
  private Long uId;

  /** 被邀请用户id */
  private Long seUId;

  private Long integralCount;
}
