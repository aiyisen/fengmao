package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class InsertLianLianOrder {
  private String api_version = "1.0";
  private String sign;
  private String sign_type = "RSA";
  private String time_stamp;
  /** 商户编号是商户在连连支付支付平台上开设的商户号码，为18位数字，如：201304121000001004。测试阶段可以先用测试商户号测试 */
  private String oid_partner;
  /** 用户编号。 商户系统内对用户的唯一编码，可以为自定义字符串，加密密文或者邮箱等可以唯一定义用户的标识。 */
  private String user_id;
  /** 虚拟商品销售：101001。 实物商品销售：109001。当busi_partner与您的商户号的业务属性不相符时， 该次请求将返回请求无效 */
  private String busi_partner;
  /**
   * 商户订单号。 为商户系统内对订单的唯一编号，保证唯一。 连连会根据no_order 创建连连订单号 oid_paybill， 如no_order已有对应连连订单号 oid_paybill，
   * 则将请求视为重复订单请求。 重复发起订单请求时， 请求中的参数信息需与原创单时一致
   */
  private String no_order;
  /** 商户订单时间。格式为yyyyMMddHHmmss， HH以24小时为准，如 20180130161010。 */
  private String dt_order;
  /** 交易金额。请求no_order对应的订单总金额，单位为元，精确到小数点后两位，小数点计入字符长度。 取值范围为 0.01 ~ 99999999。初始额度：50元 */
  private String money_order;
  /**
   * 接收异步通知的线上地址。连连支付支付平台在用户支付成功后通知商户服务端的地址。如 http://test.lianlianpay.com.cn/help/notify.php
   * 。异步通知处理规则及详情见异步通知。
   */
  private String notify_url;
  /** 风险控制参数。连连风控部门要求商户统一传入风险控制参数字段，字段值为json 字符串的形式 */
  private String risk_item;
  /** 支付产品标识。 0， 快捷收款。 1， 认证收款。 2， 网银收款。 5， 新认证收款。 12， 手机网银收款 。 */
  private String flag_pay_product;
  /** 应用渠道标识。 0， App-Android。 1， App-iOS。 2， Web。 3， H5 */
  private String flag_chnl;

  private String id_type = "0";
  /** 证件号码。 对应id_type的相关证件号码。 */
  private String id_no;
  /** 用户姓名，为用户在银行预留的姓名信息，中文。 对于少数民族，间隔号以「·」（U+00B7 MIDDLE DOT）为准 */
  private String acct_name;
  /** 用户银行卡卡号。 历次支付时，传入no_agree则取该协议号对应的用户银行卡号， 两者都传时， 以no_agree为准 */
  private String card_no;
  /** 签约协议编号。签约规则及详情见签约说明。 */
  private String no_agree;
  /** 银行编码。手机银行必填。指定银行编号后，可直接跳转指定银行网银或手机银行APP收银台(网银支付若此参数为非空，则card_type必填)。可参考银行编码表进行查询 */
  private String bank_code;
  /** 卡类型。bank_code非空时必传， 适用于网银支付。 0， 借记卡。 1，信用卡。 2， 企业网银 */
  private String card_type;

  @Data
  public static class RiskItem {
    /** 商品类目。 详见商品类目表。 */
    private String frms_ware_category;
    /** 商户用户唯一标识。 商户系统中对用户的唯一编号， 可与支付请求时的user_id相同。 */
    private String user_info_mercht_userno;
    /** 用户绑定手机号。 如商户确实没有收集的， 需要与连连技术支持特殊说明。（接入微信支付的商户，若未强制绑定手机号，在商户无法获取用户手机号的情况下，该参数可不传） */
    private String user_info_bind_phone;
    /** 注册时间。用户在商户系统中的注册时间， 格式须为YYYYMMDDHHMMSS， 24小时制。 */
    private String user_info_dt_register;
    /** 商品名称。 */
    private String goods_name;
    /** 业务来源。 0 - 商户代扣模式 10 - APP。 13 - PC。 16 - H5。 18 - IVR。 */
    private String frms_client_chnl;
    /** 用户交易请求IP，frms_client_chnl=0可不传 */
    private String frms_ip_addr;
    /** 用户授权标记。 0 - 否 1 - 是 该字段指开通API的商户，用户有无勾选连连支付服务协议。（商户接入微信扫码支付时，该字段无需传输） */
    private String user_auth_flag;
    /** 可选 代收授权标记。 0：否 1：是 协议代扣场景必传，该字段指开通代扣功能的商户，用户是否有授权商户委托第三方支付扣款，且协议在有效期内。 */
    private String repay_auth_flag;
  }
}
