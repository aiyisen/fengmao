package com.qy.ntf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.lianpay.api.util.TraderRSAUtil;
import com.qy.ntf.bean.dto.ConRechargeAddDto;
import com.qy.ntf.bean.dto.ConRechargeDto;
import com.qy.ntf.bean.dto.ConRechargeUpdateDto;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.entity.ConRecharge;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.dao.ConRechargeDao;
import com.qy.ntf.dao.SysUserDao;
import com.qy.ntf.service.ConRechargeService;
import com.qy.ntf.util.PayUtils;
import com.qy.ntf.util.ResourcesUtil;
import com.qy.ntf.util.llPay.CashierPayCreateDemo;
import com.qy.ntf.util.sd.demo.SDPay;
import com.qy.ntf.util.wxPay.WXPayConfigImpl;
import com.qy.ntf.util.wxPay.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 充值订单表 service服务实现
 */
@Slf4j
@Service("conRechargeService")
public class ConRechargeServiceImpl implements ConRechargeService {

  @Autowired private ConRechargeDao conRechargeDao;

  @Override
  public BaseMapper<ConRecharge> getDao() {
    return conRechargeDao;
  }

  @Override
  public IPage<ConRechargeDto> getListByPage(
      Class<ConRechargeDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<ConRecharge> queryWrapper) {
    IPage<ConRechargeDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public ConRechargeDto getConRechargeById(Long id) {
    Optional<ConRechargeDto> optional = selectDataById(ConRechargeDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Autowired private SysUserDao sysUserDao;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Object save(ConRechargeAddDto tmpDto, UserDto userData, String ip) {
    SysUser sysUser = sysUserDao.selectById(userData.getId());
    ConRecharge conRecharge = new ConRecharge();
    conRecharge.setUId(userData.getId());
    conRecharge.setOrderFingerprint(UUID.randomUUID().toString().replaceAll("-", ""));
    conRecharge.setPayType(tmpDto.getPayType());
    conRecharge.setOrderFlag(0);
    conRecharge.setTotalPrice(tmpDto.getTotalPrice());
    conRecharge.setCreateId(userData.getId());
    conRecharge.setCreateTime(new Date());
    conRechargeDao.insert(conRecharge);
    switch (tmpDto.getPayType()) {
      case 0:
        //        if (tmpDto.getFlagChnl() != null && "3".equals(tmpDto.getFlagChnl())) {
        //          return getWeixinPayH5Response(conRecharge, sysUser.getPhone());
        //        } else {
        //          return getWeixinPayResponse(conRecharge, sysUser.getPhone());
        //        }
        throw new RuntimeException("支付方式错误");
      case 1:
        //        if (tmpDto.getFlagChnl() != null && "3".equals(tmpDto.getFlagChnl())) {
        //          return getAlipayTradeH5PayResponse(conRecharge, sysUser.getPhone());
        //        } else {
        //          return getAlipayTradeAppPayResponse(conRecharge, sysUser.getPhone());
        //        }
        throw new RuntimeException("支付方式错误");
      case 2:
        //        return SDPayOrder(tmpDto, conRecharge, "充值订单", sysUser);
        return CashierPayCreateDemo.realRecharge(
            userData.getPhone(),
            conRecharge.getOrderFingerprint(),
            conRecharge.getTotalPrice().doubleValue() + "",
            ResourcesUtil.getProperty("lianlianpay_balance_order_notify"),
            "充值",
            sysUser,
            ip);
      default:
        throw new RuntimeException("支付方式错误");
    }
  }

  private Object SDPayOrder(
      ConRechargeAddDto tmpDto, ConRecharge orderProduct, String title, SysUser user) {
    return SDPay.pay(
        ResourcesUtil.getProperty("lianlianpay_balance_order_notify"),
        tmpDto.getFlagChnl().equals("0") || tmpDto.getFlagChnl().equals("1")
            ? "05030001"
            : "06030001",
        title,
        orderProduct.getTotalPrice().doubleValue() + "",
        orderProduct.getCreateId(),
        1,
        orderProduct.getOrderFingerprint());
    //    SimpleDateFormat sp = new SimpleDateFormat("yyyyMMddHHmmss");
    //    InsertLianLianOrder.RiskItem riskItem = new InsertLianLianOrder.RiskItem();
    //    riskItem.setFrms_ware_category("1001"); // 虚拟卡销售 1001  5999
    //    riskItem.setUser_info_mercht_userno(orderProduct.getCreateId() + "");
    //    riskItem.setUser_info_bind_phone(user.getPhone());
    //    riskItem.setUser_info_dt_register(
    //        sp.format(user.getCreateTime() == null ? new Date() : user.getCreateTime()));
    //    riskItem.setGoods_name(title);
    //    riskItem.setFrms_client_chnl(
    //        tmpDto.getFlagChnl().equals("0") || tmpDto.getFlagChnl().equals("1") ? "10" : "16");
    //    riskItem.setFrms_ip_addr(PayUtils.getIp());
    //    riskItem.setUser_auth_flag("1");
    //    riskItem.setRepay_auth_flag("1");
    //
    //    String url = "https://payserverapi.lianlianpay.com/v1/paycreatebill";
    //
    //    InsertLianLianOrder order = new InsertLianLianOrder();
    //    order.setTime_stamp(sp.format(new Date()));
    //    order.setOid_partner(ResourcesUtil.getProperty("lianlian_oid"));
    //    //        order.setOid_partner("202207080003331008");
    //    order.setUser_id(orderProduct.getCreateId() + "");
    //    order.setBusi_partner("109001");
    //    order.setNo_order(orderProduct.getOrderFingerprint());
    //
    //    order.setDt_order(sp.format(new Date()));
    //    order.setMoney_order(orderProduct.getTotalPrice().doubleValue() + "");
    //    order.setNotify_url(ResourcesUtil.getProperty("lianlianpay_balance_order_notify"));
    //    order.setRisk_item(JSONObject.toJSONString(riskItem));
    //    //    order.setFlag_pay_product(Strings.isEmpty(tmpDto.getNoAgree()) ? "5" : "0");
    //    order.setFlag_pay_product("0");
    //    order.setFlag_chnl(tmpDto.getFlagChnl());
    //    order.setId_no(tmpDto.getIdNo());
    //    order.setAcct_name(tmpDto.getAcctName());
    //    order.setCard_no(tmpDto.getCardNo());
    //    order.setSign(genSign(JSON.parseObject(JSON.toJSONString(order))));
    //    String reqJson = JSON.toJSONString(order);
    //    String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, url);
    //    return JSONObject.parseObject(resJson);
  }

  private static String genSign(JSONObject reqObj) {
    // // 生成待签名串
    String sign_src = genSignData(reqObj);
    return getSignRSA(sign_src);
  }
  /**
   * RSA签名验证
   *
   * @return
   */
  public static String getSignRSA(String sign_src) {
    return TraderRSAUtil.sign(ResourcesUtil.getProperty("lianlian_private_key"), sign_src);
  }
  /**
   * 生成待签名串
   *
   * @return
   */
  public static String genSignData(JSONObject jsonObject) {
    StringBuilder content = new StringBuilder();

    // 按照key做首字母升序排列
    List<String> keys = new ArrayList<String>(jsonObject.keySet());
    keys.sort(String.CASE_INSENSITIVE_ORDER);
    for (int i = 0; i < keys.size(); i++) {
      String key = (String) keys.get(i);
      // sign 和ip_client 不参与签名
      if ("sign".equals(key)) {
        continue;
      }
      String value = (String) jsonObject.getString(key);
      // 空串不参与签名
      if (null == value) {
        continue;
      }
      content.append(i == 0 ? "" : "&").append(key).append("=").append(value);
    }
    String signSrc = content.toString();
    if (signSrc.startsWith("&")) {
      signSrc = signSrc.replaceFirst("&", "");
    }
    return signSrc;
  }

  private Object getAlipayTradeAppPayResponse(ConRecharge orderProduct, String title) {
    try {
      String appId = ResourcesUtil.ALI_APP_ID;
      AlipayConfig alipayConfig = new AlipayConfig();
      alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
      alipayConfig.setAppId(appId);
      alipayConfig.setPrivateKey(ResourcesUtil.getProperty("ali_75private_key"));
      alipayConfig.setAppCertPath("/nginx/cert/appCertPublicKey_2021003146630241.crt");
      alipayConfig.setAlipayPublicCertPath("/nginx/cert/alipayCertPublicKey_RSA2.crt");
      alipayConfig.setRootCertPath("/nginx/cert/alipayRootCert.crt");
      alipayConfig.setFormat("json");
      alipayConfig.setCharset("UTF-8");
      alipayConfig.setSignType("RSA2");
      DefaultAlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
      // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
      AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
      // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
      AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
      model.setBody(orderProduct.getOrderFingerprint());
      model.setSubject(title);
      model.setOutTradeNo(orderProduct.getOrderFingerprint());
      model.setTimeoutExpress("30m");
      model.setTotalAmount(String.valueOf(orderProduct.getTotalPrice()));
      model.setProductCode("QUICK_MSECURITY_PAY");
      request.setBizModel(model);
      request.setNotifyUrl(ResourcesUtil.ALIPAY_BALANCE_TREASURE_NOTIFY_URL);
      try {
        // 这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        System.out.println(response.getBody()); // 就是orderString 可以直接给客户端请求，无需再做处理。
        return response.getBody();
      } catch (AlipayApiException e) {
        e.printStackTrace();
      }
      return null;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Object getAlipayTradeH5PayResponse(ConRecharge orderProduct, String title) {
    try {
      String appId = ResourcesUtil.ALI_APP_ID;
      AlipayConfig alipayConfig = new AlipayConfig();
      alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
      alipayConfig.setAppId(appId);
      alipayConfig.setPrivateKey(ResourcesUtil.getProperty("ali_75private_key"));
      alipayConfig.setAppCertPath("/nginx/cert/appCertPublicKey_2021003146630241.crt");
      alipayConfig.setAlipayPublicCertPath("/nginx/cert/alipayCertPublicKey_RSA2.crt");
      alipayConfig.setRootCertPath("/nginx/cert/alipayRootCert.crt");
      alipayConfig.setFormat("json");
      alipayConfig.setCharset("UTF-8");
      alipayConfig.setSignType("RSA2");
      DefaultAlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
      AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
      request.setNotifyUrl(ResourcesUtil.ALIPAY_BALANCE_TREASURE_NOTIFY_URL);
      //      request.setReturnUrl("");
      JSONObject bizContent = new JSONObject();
      bizContent.put("out_trade_no", orderProduct.getOrderFingerprint());
      bizContent.put("total_amount", orderProduct.getTotalPrice());
      bizContent.put("subject", title);
      bizContent.put("product_code", "QUICK_WAP_WAY");
      bizContent.put("return_url", "https://web.11touch.net/#/pages/mine/qianbao/qianbao");
      request.setBizContent(bizContent.toString());
      AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
      if (response.isSuccess()) {
        System.out.println("调用成功");
        return response.getBody();
      } else {
        System.out.println("调用失败");
        return null;
      }
      //      String appId = ResourcesUtil.ALI_APP_ID;
      //      // 公钥
      //      //
      // MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlN0HXsbwxiRsnZfbefFiNpEeLcE5b4nSiDJ7A2Rt0pS2tvJsHZ4UnlUim/6UzxLKAzxn6VpLyKNc3INUctr1ZPtMbLHnDt1vBmXRqeBjear+Vnw6G2Xr4ixoWqRzZAmvlOuOkUJ+G60qU//Kz9YoIm3MnvA3becF8nnLylfkg+pM05+yZkK+3sjXzsS9dGfs3Zs2+jE9ePyRNtYviYXWu9RuOSSGDY06HKVNxHGQUi7TqUZ+W6xRhP0VAfnK5pMBA2CAwywsCv/GRjg6naOUo7wzohqhxBX3WsasqdUBfRkWg2ozkQa77ucPZAWPDRngHXbL2PdbiPSx3SUpL+nZxQIDAQAB
      //      String aliPrivateKey =
      //
      // "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCU3QdexvDGJGydl9t58WI2kR4twTlvidKIMnsDZG3SlLa28mwdnhSeVSKb/pTPEsoDPGfpWkvIo1zcg1Ry2vVk+0xssecO3W8GZdGp4GN5qv5WfDobZeviLGhapHNkCa+U646RQn4brSpT/8rP1igibcye8Ddt5wXyecvKV+SD6kzTn7JmQr7eyNfOxL10Z+zdmzb6MT14/JE21i+Jhda71G45JIYNjTocpU3EcZBSLtOpRn5brFGE/RUB+crmkwEDYIDDLCwK/8ZGODqdo5SjvDOiGqHEFfdaxqyp1QF9GRaDajORBrvu5w9kBY8NGeAddsvY91uI9LHdJSkv6dnFAgMBAAECggEACyOe8ZChY7JGDmTWn4FYgAzL3VCgI6CEiHx+h/pz3VYTdg0d2fmCQXbNaC7co8IcK7HRdLy0/wZ6ZGXPY+jOhAfp6BhH2ezn6eqkjbmkt+37qi0RjAtMY1g/VskHeWzgHpyhxmzbUubaS/7QBk1YI3tj3GDNRQQMheBnR3TcPKKlufR7ri8umRQeow5v41YD1R/AYDU3zX0UxEtsXUNWz6PmqaULlLBB31np5TNTfgshBREgvy/0DVsHtIHcWxNkrwkMapAHaEuR9q9rDsIDpVfRtJxv/aGxIqhYVkDz94MjDsPNUrw7tbcz74R058giXTTMsx8ZPUSAL1UKb5VT4QKBgQD0J8zIacWdL1exDUNKv94xiUAqxB2j0RdN9eUxdO8cSfb9AJxHypNscB2pPsDc0u29R53Za4GkoSihhZ+JSiU8QrZW17nB3JiGoyO4C6NDyQ2g2Jb0am5zEMH/5gp9svyInPVmqd6EJTujXUiAsG2RcY8A+QA0Oh3ozPF68sIhjwKBgQCcFciY1D999yFmscn1vquaLH6s/xOZg7m2ccr0cHrz66r/wbwsNqodCrbEuZ61GJeVU8PnUrAk4wrlGiFqR6pV9PxBHioQYFywsZ/nT8C2XnfqQXe+XVM45ArIZphI4YuBsKSc9AWjA5EGnKSadeJ8VKq4gPfC2gYb+B/10+19awKBgQCYKpnxqiJUVaY0nYx78Nq9SsooHTRP3cfFeeRPD47atapugkvkXnfFFJcX3Rl8RyWOWy0gzWTuQta83DfS69gLF5TmyOpnzWFuQAzJ7s7hN1P8FCD40cBmjGIsZ6XQM5Y6WoCDbIlXGJFzvnaqZcrT4895jra21iW/6sLxmoytNwKBgFsNXhrBXlSGUObOeikwVHy4ziDvICjirfifQyz7XM5kQTm3c6U7MluEv3/dZJbyRKMo3VRZaUXraJSjfLC6I8THCEyYYyNwg0HULJrMbHg2fa+bB1Z2rLC4Xw3uw5FoeXBrcmvY8lzZHMYFpQQyFrA+1+SP33i8pOm2AjburVEHAoGBAKOKnAxaCLiqeDFYeuK4SzjdMbtXDIcHaSQ/hH2P0tnquJquBMIDqVCOEn0o7Oe0pFZ6wD8W2aHPx6fywbaJD7Vz2HBk0cdEHVmSrD5vlSAE2qeDzHNRd397e4GRyXaCwPtZZaGd9dpNB0t9K8+PYi0EYz2Dt/as2ynycXvvcKHh";
      //      // 构造client
      //      CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
      //      // 设置网关地址
      //      certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
      //      // 设置应用Id
      //      certAlipayRequest.setAppId(appId);
      //      // 设置应用私钥
      //      certAlipayRequest.setPrivateKey(aliPrivateKey);
      //      // 设置请求格式，固定值json
      //      certAlipayRequest.setFormat("json");
      //      // 设置字符集
      //      certAlipayRequest.setCharset("utf-8");
      //      // 设置签名类型
      //      certAlipayRequest.setSignType("RSA2");
      //      // 设置应用公钥证书路径
      //      certAlipayRequest.setCertPath(ResourcesUtil.getProperty("ali_CertPath"));
      //      certAlipayRequest.setAlipayPublicCertPath(
      //          ResourcesUtil.getProperty("ali_AlipayPublicCertPath"));
      //      certAlipayRequest.setRootCertPath(ResourcesUtil.getProperty("ali_RootCertPath"));
      //      // 构造client
      //      AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
      //      // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
      //      AlipayTradePayRequest request = new AlipayTradePayRequest();
      //      //
      // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
      //      AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
      //      model.setBody(orderProduct.getOrderFingerprint());
      //      model.setSubject(title);
      //      model.setOutTradeNo(orderProduct.getOrderFingerprint());
      //      model.setTimeoutExpress("30m");
      //      model.setTotalAmount(String.valueOf(orderProduct.getTotalPrice()));
      //      model.setProductCode("QUICK_MSECURITY_PAY");
      //      request.setBizModel(model);
      //      request.setNotifyUrl(ResourcesUtil.ALIPAY_BALANCE_TREASURE_NOTIFY_URL);
      //      try {
      //        // 这里和普通的接口调用不同，使用的是sdkExecute
      //        AlipayTradePayResponse response = alipayClient.pageExecute(request);
      //        System.out.println(response.getBody()); // 就是orderString 可以直接给客户端请求，无需再做处理。
      //        return response.getBody();
      //      } catch (AlipayApiException e) {
      //        e.printStackTrace();
      //      }
      //      return null;
    } catch (AlipayApiException e) {
      throw new RuntimeException(e);
    }
  }

  private Object getWeixinPayResponse(ConRecharge orderProduct, String title) {
    // 微信支付
    HashMap<String, String> payParam = new HashMap<>(16);
    payParam.put("body", title);
    payParam.put("out_trade_no", orderProduct.getOrderFingerprint());
    payParam.put(
        "total_fee", orderProduct.getTotalPrice().multiply(new BigDecimal(100)).longValue() + "");
    payParam.put("spbill_create_ip", PayUtils.getIp());
    payParam.put("notify_url", ResourcesUtil.WEIXIN_BALANCE_TREASURE_NOTIFY_URL);
    payParam.put("trade_type", "APP");
    // 附加数据包，异步通知中使用
    payParam.put("attach", orderProduct.getId() + "");

    Map<String, String> r;
    try {
      WXPayConfig config = WXPayConfigImpl.getInstance();
      WXPay wxPay = new WXPay(config);
      r = wxPay.unifiedOrder(payParam);
      Map<String, String> appResult = new HashMap<>(16);
      appResult.put("appid", ResourcesUtil.WX_APP_ID);
      appResult.put("partnerid", ResourcesUtil.WX_PARTNER_ID);
      appResult.put("prepayid", r.get("prepay_id"));
      appResult.put("package", "Sign=WXPay");
      appResult.put("noncestr", WXPayUtil.generateNonceStr());
      appResult.put("timestamp", System.currentTimeMillis() / 1000 + "");
      String sign = WXPayUtil.generateSignature(appResult, ResourcesUtil.WX_API_KEY);
      appResult.put("sign", sign);
      return appResult;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private Object getWeixinPayH5Response(ConRecharge orderProduct, String title) {
    // 微信支付
    Map<String, String> r;
    try {
      // 微信支付
      HashMap<String, String> payParam = new HashMap<>(16);
      payParam.put("appid", ResourcesUtil.WX_APP_ID);
      payParam.put("mch_id", ResourcesUtil.WX_PARTNER_ID);
      payParam.put("nonce_str", WXPayUtil.generateNonceStr());
      payParam.put("out_trade_no", orderProduct.getOrderFingerprint());
      payParam.put(
          "total_fee", orderProduct.getTotalPrice().multiply(new BigDecimal(100)).longValue() + "");
      payParam.put("spbill_create_ip", PayUtils.getIp());
      payParam.put("notify_url", ResourcesUtil.WEIXIN_BALANCE_TREASURE_NOTIFY_URL);
      payParam.put("trade_type", "MWEB");
      payParam.put(
          "scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://xskymeta.com\"}}");
      payParam.put("body", title);
      String sign = WXPayUtil.generateSignature(payParam, ResourcesUtil.WX_API_KEY);
      payParam.put("sign", sign);
      // 附加数据包，异步通知中使用
      //    payParam.put("attach", JSONObject.toJSONString(orderProduct));

      WXPayConfig config = WXPayConfigImpl.getInstance();
      WXPay wxPay = new WXPay(config);
      r = wxPay.unifiedOrder(payParam);
      System.out.println("微信支付返回值：" + JSONObject.toJSONString(r));
      return r;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(ConRechargeUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    ConRechargeDto dto = new ConRechargeDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(ConRechargeDto dto) {
    ConRechargeDto conRecharge = getConRechargeById(dto.getId());
    if (conRecharge.getState() == 1) {
      conRecharge.setState(2);
    } else {
      conRecharge.setState(1);
    }
    conRecharge.setUpdateTime(new Date());
    conRecharge.setUpdateId(dto.getUpdateId());
    updateDataById(conRecharge.getId(), conRecharge);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<ConRechargeDto> list(ConRechargeDto dto, UserDto userData) {
    LambdaQueryWrapper<ConRecharge> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ConRecharge::getUId, userData.getId()).eq(ConRecharge::getOrderFlag, 1);
    return selectList(ConRechargeDto.class, queryWrapper);
  }
}
