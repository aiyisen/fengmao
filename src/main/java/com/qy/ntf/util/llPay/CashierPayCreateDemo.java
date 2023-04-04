package com.qy.ntf.util.llPay;

import com.alibaba.fastjson.JSON;
import com.qy.ntf.bean.dto.OrderTreasurePoolAddDto;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.util.LLianPayDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

/** 账户+收银台 Demo */
public class CashierPayCreateDemo {
  public static void main(String[] args) {
    //        // 普通消费
    //    generalConsume();

    // 用户充值
    //        userTopup();
  }

  /** 用户充值 */
  public static void userTopup() {
    CashierPayCreateParams params = new CashierPayCreateParams();
    String timestamp = LLianPayDateUtils.getTimestamp();
    params.setTimestamp(timestamp);
    params.setOid_partner(LLianPayConstant.OidPartner);
    // 用户充值
    params.setTxn_type("USER_TOPUP");
    params.setUser_id("LLianPayTest-In-User-12345");
    /*
    用户类型。默认：注册用户。
    注册用户：REGISTERED
    匿名用户：ANONYMOUS
     */
    params.setUser_type("REGISTERED");
    params.setNotify_url("https://test.lianlianpay/notify");
    params.setReturn_url("https://open.lianlianpay.com?jump=page");
    // 交易发起渠道设置
    params.setFlag_chnl("H5");
    // 测试风控参数
    params.setRisk_item(
        "{\"frms_ware_category\":\"4007\",\"goods_name\":\"用户充值\",\"user_info_mercht_userno\":\"\",\"user_info_dt_register\":\"20220823101239\",\"user_info_bind_phone\":\"13208123456\",\"user_info_full_name\":\"连连测试\",\"user_info_id_no\":\"\",\"user_info_identify_state\":\"0\",\"user_info_identify_type\":\"4\",\"user_info_id_type\":\"0\",\"frms_client_chnl\":\" H5\",\"frms_ip_addr\":\"127.0.0.1\",\"user_auth_flag\":\"1\"}");

    // 设置商户订单信息
    CashierPayCreateOrderInfo orderInfo = new CashierPayCreateOrderInfo();
    orderInfo.setTxn_seqno("LLianPayTest" + timestamp);
    orderInfo.setTxn_time(timestamp);
    orderInfo.setTotal_amount(100.00);
    orderInfo.setGoods_name("用户充值");
    params.setOrderInfo(orderInfo);

    // 设置付款方信息
    CashierPayCreatePayerInfo payerInfo = new CashierPayCreatePayerInfo();
    payerInfo.setPayer_id("LLianPayTest-In-User-12345");
    payerInfo.setPayer_type("USER");
    params.setPayerInfo(payerInfo);

    // 测试环境URL
    String url = "https://accpgw.lianlianpay.com/v1/cashier/paycreate";
    LLianPayClient lLianPayClient = new LLianPayClient();
    String resultJsonStr = lLianPayClient.sendRequest(url, JSON.toJSONString(params));
    CashierPayCreateResult cashierPayCreateResult =
        JSON.parseObject(resultJsonStr, CashierPayCreateResult.class);
    System.out.println(cashierPayCreateResult);
  }

  private static Logger log = LoggerFactory.getLogger(LLianPayClient.class);
  /** 普通消费 */
  public static CashierPayCreateResult recharge(
      BigDecimal consignment_fee,
      SysUser saler,
      OrderTreasurePoolAddDto tmpDto,
      String mobile,
      String id,
      String amount,
      String urls,
      String title,
      SysUser user,
      String ip) {
    log.info(
        "============consignment_fee:{}, tmpDto:{}, mobile:{}, id:{},amount:{}, urls:{},title:{},ip:{},user:{}",
        consignment_fee,
        tmpDto,
        mobile,
        id,
        amount,
        urls,
        title,
        ip,
        user);
    CashierPayCreateParams params = new CashierPayCreateParams();
    String timestamp = LLianPayDateUtils.getTimestamp();
    params.setTimestamp(timestamp);
    params.setOid_partner(LLianPayConstant.OidPartner);
    SimpleDateFormat sp = new SimpleDateFormat("yyyyMMddHHmmss");
    // 普通消费
    params.setTxn_type("GENERAL_CONSUME");
    params.setUser_id(mobile);
    /*
    用户类型。默认：注册用户。
    注册用户：REGISTERED
    匿名用户：ANONYMOUS
     */
    params.setUser_type("REGISTERED");
    params.setNotify_url(urls);
    params.setReturn_url("http://www.fmcangping.com/#/pages/mine/wodegoumai/wodegoumai?jump=page");
    // 交易发起渠道设置
    params.setFlag_chnl("H5");
    // 测试风控参数
    params.setRisk_item(
        "{\"frms_ware_category\":\"4007\",\"goods_name\":\""
            + title
            + "\",\"user_info_mercht_userno\":\""
            + mobile
            + "\",\"user_info_dt_register\":\""
            + sp.format(user.getCreateTime())
            + "\",\"user_info_bind_phone\":\""
            + mobile
            + "\",\"user_info_full_name\":\""
            + user.getRealName()
            + "\",\"user_info_id_no\":\""
            + user.getIdCard()
            + "\",\"user_info_identify_state\":\"1\",\"user_info_identify_type\":\"1\",\"user_info_id_type\":\"0\",\"frms_client_chnl\":\"16\",\"frms_ip_addr\":\""
            + ip
            + "\",\"user_auth_flag\":\"1\"}");
    log.info("==========params0:{}", params);
    // 设置商户订单信息
    CashierPayCreateOrderInfo orderInfo = new CashierPayCreateOrderInfo();
    orderInfo.setTxn_seqno(id);
    orderInfo.setTxn_time(timestamp);
    orderInfo.setTotal_amount(new BigDecimal(amount).doubleValue());
    orderInfo.setGoods_name(title);
    params.setOrderInfo(orderInfo);
    if (tmpDto.getItemType() == 0) {
      // 藏品首发
      // 设置收款方信息，消费分账场景支持上送多收款方（最多10个），收款总金额必须和订单总金额相等
      CashierPayCreatePayeeInfo mPayeeInfo = new CashierPayCreatePayeeInfo();
      mPayeeInfo.setPayee_id(LLianPayConstant.OidPartner);
      mPayeeInfo.setPayee_type("MERCHANT");
      mPayeeInfo.setPayee_amount(amount);
      mPayeeInfo.setPayee_memo("");

      params.setPayeeInfo(new CashierPayCreatePayeeInfo[] {mPayeeInfo});
    } else if (tmpDto.getItemType() == 2) {
      // 流转中心
      // 设置收款方信息，消费分账场景支持上送多收款方（最多10个），收款总金额必须和订单总金额相等
      CashierPayCreatePayeeInfo mPayeeInfo2 = new CashierPayCreatePayeeInfo();
      mPayeeInfo2.setPayee_id(saler.getPhone());
      mPayeeInfo2.setPayee_type("USER");
      mPayeeInfo2.setPayee_accttype("USEROWN");
      mPayeeInfo2.setPayee_amount(
          new BigDecimal(amount)
                  .multiply(BigDecimal.ONE.subtract(consignment_fee))
                  .setScale(2, RoundingMode.DOWN)
                  .doubleValue()
              + "");
      mPayeeInfo2.setPayee_memo("");
      log.info("==========params1:{}", params);
      CashierPayCreatePayeeInfo mPayeeInfo = new CashierPayCreatePayeeInfo();
      mPayeeInfo.setPayee_id(LLianPayConstant.OidPartner);
      mPayeeInfo.setPayee_type("MERCHANT");
      mPayeeInfo.setPayee_amount(
          new BigDecimal(amount)
                  .subtract(new BigDecimal(mPayeeInfo2.getPayee_amount()))
                  .setScale(2, RoundingMode.DOWN)
                  .doubleValue()
              + "");
      log.info("==========params2:{}", params);
      params.setPayeeInfo(new CashierPayCreatePayeeInfo[] {mPayeeInfo, mPayeeInfo2});
      log.info("==========params3:{}", params);
    } else if (tmpDto.getItemType() == 3) {
      // 盲盒下单
      // 设置收款方信息，消费分账场景支持上送多收款方（最多10个），收款总金额必须和订单总金额相等
      CashierPayCreatePayeeInfo mPayeeInfo = new CashierPayCreatePayeeInfo();
      mPayeeInfo.setPayee_id(LLianPayConstant.OidPartner);
      mPayeeInfo.setPayee_type("MERCHANT");
      mPayeeInfo.setPayee_amount(amount);
      mPayeeInfo.setPayee_memo("");

      params.setPayeeInfo(new CashierPayCreatePayeeInfo[] {mPayeeInfo});
    }

    // 设置付款方信息
    CashierPayCreatePayerInfo payerInfo = new CashierPayCreatePayerInfo();
    payerInfo.setPayer_id(mobile);
    payerInfo.setPayer_type("USER");
    params.setPayerInfo(payerInfo);

    // 测试环境URL
    String url = "https://accpgw.lianlianpay.com/v1/cashier/paycreate";
    LLianPayClient lLianPayClient = new LLianPayClient();
    String resultJsonStr = lLianPayClient.sendRequest(url, JSON.toJSONString(params));
    log.info("===========下单连连请求参数：{}", params);
    log.info("==========下单连连返回参数：{}", resultJsonStr);
    CashierPayCreateResult cashierPayCreateResult =
        JSON.parseObject(resultJsonStr, CashierPayCreateResult.class);
    return cashierPayCreateResult;
  }

  public static CashierPayCreateResult realRecharge(
      String mobile, String id, String amount, String urls, String title, SysUser user, String ip) {

    CashierPayCreateParams params = new CashierPayCreateParams();
    String timestamp = LLianPayDateUtils.getTimestamp();
    params.setTimestamp(timestamp);
    params.setOid_partner(LLianPayConstant.OidPartner);
    SimpleDateFormat sp = new SimpleDateFormat("yyyyMMddHHmmss");
    // 普通消费
    params.setTxn_type("USER_TOPUP");
    params.setUser_id(mobile);
    /*
    用户类型。默认：注册用户。
    注册用户：REGISTERED
    匿名用户：ANONYMOUS
     */
    params.setUser_type("REGISTERED");
    params.setNotify_url(urls);
    params.setReturn_url("http://www.fmcangping.com/#/pages/mine/wodegoumai/wodegoumai?jump=page");
    // 交易发起渠道设置
    params.setFlag_chnl("H5");
    // 测试风控参数
    params.setRisk_item(
        "{\"frms_ware_category\":\"4007\",\"goods_name\":\""
            + title
            + "\",\"user_info_mercht_userno\":\""
            + mobile
            + "\",\"user_info_dt_register\":\""
            + sp.format(user.getCreateTime())
            + "\",\"user_info_bind_phone\":\""
            + mobile
            + "\",\"user_info_full_name\":\""
            + user.getRealName()
            + "\",\"user_info_id_no\":\""
            + user.getIdCard()
            + "\",\"user_info_identify_state\":\"1\",\"user_info_identify_type\":\"4\",\"user_info_id_type\":\"1\",\"frms_client_chnl\":\"16\",\"frms_ip_addr\":\""
            + ip
            + "\",\"user_auth_flag\":\"1\"}");

    // 设置商户订单信息
    CashierPayCreateOrderInfo orderInfo = new CashierPayCreateOrderInfo();
    orderInfo.setTxn_seqno(id);
    orderInfo.setTxn_time(timestamp);
    orderInfo.setTotal_amount(new BigDecimal(amount).doubleValue());
    orderInfo.setGoods_name(title);
    params.setOrderInfo(orderInfo);
    // 藏品首发
    // 设置收款方信息，消费分账场景支持上送多收款方（最多10个），收款总金额必须和订单总金额相等
    //    CashierPayCreatePayeeInfo mPayeeInfo = new CashierPayCreatePayeeInfo();
    //    mPayeeInfo.setPayee_id(LLianPayConstant.OidPartner);
    //    mPayeeInfo.setPayee_type("MERCHANT");
    //    mPayeeInfo.setPayee_amount(amount);
    //    mPayeeInfo.setPayee_memo("");
    //
    //    params.setPayeeInfo(new CashierPayCreatePayeeInfo[] {mPayeeInfo});

    // 设置付款方信息
    CashierPayCreatePayerInfo payerInfo = new CashierPayCreatePayerInfo();
    payerInfo.setPayer_id(mobile);
    payerInfo.setPayer_type("USER");
    params.setPayerInfo(payerInfo);

    // 测试环境URL
    String url = "https://accpgw.lianlianpay.com/v1/cashier/paycreate";
    LLianPayClient lLianPayClient = new LLianPayClient();
    String resultJsonStr = lLianPayClient.sendRequest(url, JSON.toJSONString(params));
    CashierPayCreateResult cashierPayCreateResult =
        JSON.parseObject(resultJsonStr, CashierPayCreateResult.class);
    return cashierPayCreateResult;
  }
}
