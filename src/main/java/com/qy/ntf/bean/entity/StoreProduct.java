package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 权益商城-普通专区/特权专区/兑换专区 主体 实体
 */
@TableName("store_product")
@Data
public class StoreProduct extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 商品类型0普通商品1特权专区2兑换专区 */
  private Integer proType;

  /** 商品标题 */
  private String proTitle;

  /** 原价 */
  private BigDecimal oldPrice;

  /** 现价 */
  private BigDecimal curPrice;

  /** 详情“;”按序拼接 */
  private String proDetail;

  /** 头部图片 */
  private String headerIndex;

  private String banners;

  /** 总量 */
  private Integer totalCount;

  /** 剩余数量 */
  private Integer surplusCount;

  /** 运费 */
  private BigDecimal feight;

  /** 冻结数量/已报名数量（超前申购属性名称） */
  private Integer frostCount;
  /** vip折扣 */
  private BigDecimal vipPercent;
}
