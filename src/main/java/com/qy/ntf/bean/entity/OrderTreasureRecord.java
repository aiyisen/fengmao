package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品快照 实体
 */
@TableName("order_treasure_record")
@Data
public class OrderTreasureRecord extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  private Long treasureId;

  /** 订单id */
  private Long orderTreasurePoolId;

  /** 宝贝类型0藏品首发1超前申购 */
  private Integer tType;

  /** 宝贝标题 */
  private String treasureTitle;

  /** 主图 */
  private String indexImgPath;

  /** 头部图片 */
  private String headImgPath;

  private String authInfo;

  /** 总量 */
  private Integer totalCount;

  /** 剩余数量 */
  private Integer surplusCount;

  /** 冻结数量/已报名数量（超前申购属性名称） */
  private Integer frostCount;

  /** 价格(0RMB1META) */
  private BigDecimal price;

  @TableField("s_t_r_i_p_id")
  private Long stripId;

  /** 作品介绍 */
  private String introduce;

  /** 作品意义 */
  private String sense;

  /** 购买须知 */
  private String needKnow;

  /** 开售时间（藏品首发特有属性 */
  private Date saleTime;

  /** 报名时间（超前申购特有属性） */
  private Date upTime;

  /** 抽奖时间（超前申购特有属性） */
  private Date checkTime;

  private String tNum;
  private BigDecimal turnMaxPrice;
  private BigDecimal turnMinPrice;
}
