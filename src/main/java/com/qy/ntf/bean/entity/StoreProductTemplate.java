package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 特权商品材料模板 实体
 */
@TableName("store_product_template")
@Data
public class StoreProductTemplate extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 特权商品id */
  @TableField("productId")
  private Long productId;

  /** 所需商品id */
  @TableField("needId")
  private Long needId;

  /** 所需商品类型0虚拟商品1实物商品 */
  @TableField("needType")
  private Integer needType;
  /** 所需商品数量 */
  @TableField("needCount")
  private Integer needCount;
}
