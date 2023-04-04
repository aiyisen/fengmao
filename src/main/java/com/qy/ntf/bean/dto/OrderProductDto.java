package com.qy.ntf.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.entity.OrderProductRecord;
import com.qy.ntf.bean.entity.SysAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 实物商品订单 添加参数
 */
@Data
@ApiModel(value = "实物商品订单 添加参数", description = "实物商品订单 添加参数")
public class OrderProductDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 订单指纹唯一标识 */
  @ApiModelProperty(value = "订单指纹唯一标识", example = "订单指纹唯一标识", required = true)
  private String orderFingerprint;

  private String productId;
  /** 用户id、 */
  @ApiModelProperty(value = "用户id、", example = "用户id、", required = true)
  private Long uId;

  /** 总价 */
  @ApiModelProperty(value = "总价", example = "总价", required = true)
  private BigDecimal totalPrice;

  /** 订单中商品数量 */
  @ApiModelProperty(value = "订单中商品数量", example = "订单中商品数量", required = true)
  private Integer proCount;

  /** 商品单价 */
  @ApiModelProperty(value = "商品单价", example = "商品单价", required = true)
  private BigDecimal proPrice;

  /** 收货地址id */
  @ApiModelProperty(value = "收货地址id", example = "收货地址id", required = true)
  private Long sysAddressId;

  /** 订单状态：-1已取消0待付款1代发货2待收货3已完成 */
  @ApiModelProperty(
      value = "订单状态：-1已取消0待付款1代发货2待收货3已完成",
      example = "订单状态：-1已取消0待付款1代发货2待收货3已完成",
      required = true)
  private Integer orderFlag;

  /** 运费 */
  @ApiModelProperty(value = "运费", example = "运费", required = true)
  private BigDecimal freight;

  /** 支付方式0微信1支付宝2applePay */
  @ApiModelProperty(
      value = "支付方式0微信1支付宝2applePay",
      example = "支付方式0微信1支付宝2applePay",
      required = true)
  private Integer payType;

  /** 支付截止时间 */
  @ApiModelProperty(value = "支付截止时间", example = "支付截止时间", required = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date payEndTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date payTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date saveTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date sendTime;

  /** 物流单号 */
  @ApiModelProperty(value = "物流单号", example = "物流单号", required = true)
  private String logisticsOrder;

  /** 订单备注 */
  @ApiModelProperty(value = "订单备注", example = "订单备注", required = true)
  private String orderRemark;

  @ApiModelProperty("订单商品快照")
  private OrderProductRecord orderProductRecord;

  private SysAddress sysAddress;
  private Integer saleAfterActive;
  private List<OrderProAfterDto> orderProAfterDto;
}
