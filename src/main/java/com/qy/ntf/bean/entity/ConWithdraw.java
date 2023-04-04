package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 提现订单表 实体
 */
@TableName("con_withdraw")
@Data
public class ConWithdraw extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 用户id */
  private Long uId;

  /** 订单指纹唯一标识 */
  private String orderFingerprint;

  /** 提现总额 */
  private BigDecimal withdrawTotal;

  private BigDecimal realPay;

  /** 提现状态0：待审核1审核通过2审核驳回 */
  private Integer applyFlag;

  /** 拨付状态0未拨付1已拨付 */
  private Integer allocatedState;

  private String aliPayIdentity;
  private String aliPayName;

  /** 原因 */
  private String applyCause;

  /** 审核时间 */
  private Date applyTime;

  private String token;
}
