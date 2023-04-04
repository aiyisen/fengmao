package com.qy.ntf.util;

import com.qy.ntf.bean.entity.StoreTreasure;
import com.qy.ntf.bean.entity.StoreTreasureRecord;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.entity.SysUserBackpack;
import com.qy.ntf.util.wenchang.DDCSdkClient;
import com.qy.ntf.util.wenchang.util.SignEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.math.BigInteger;
import java.util.UUID;

@Slf4j
public class WenchangDDC {
  DDCSdkClient client =
      new DDCSdkClient.Builder()
          .setAuthorityLogicAddress("0xFa1d2d3EEd20C4E4F5b927D9730d9F4D56314B29")
          .setChargeLogicAddress("0x0B8ae0e1b4a4Eb0a0740A250220eE3642d92dc4D")
          .setDDC721Address("0x354c6aF2cB870BEFEA8Ea0284C76e4A46B8F2870")
          .setDDC1155Address("0x0E762F4D11439B1130D402995328b634cB9c9973")
          .setGasLimit("300000")
          .setGasPrice("1")
          .setSignEventListener(new SignEvent())
          .init();
  //
  //  {
  //    client.setGatewayUrl(
  //        "https://opbningxia.bsngate.com:18602/api/ba851d44d7524798bc5d3218a85df6fd/evmrpc");
  //  }
  //
  //  BaseService baseService = client.getChargeService();

  //  Account CreateAccountHex() {
  //    client.setGatewayUrl(
  //        "https://opbningxia.bsngate.com:18602/api/ba851d44d7524798bc5d3218a85df6fd/evmrpc");
  //    client.setGatewayApiKey("568d46b0b4d94f7db267a433e1c42b31");
  //    Account accountHex = baseService.createAccountHex();
  //    System.out.println(accountHex.getAddress());
  //    System.out.println(accountHex.getPrivateKey());
  //    System.out.println(accountHex.getKeyseed());
  //    System.out.println(accountHex.getPublicKey());
  //    return accountHex;
  //  }
  //
  //  String AccoutHexToBech32(String address) {
  //    client.setGatewayUrl(
  //        "https://opbningxia.bsngate.com:18602/api/ba851d44d7524798bc5d3218a85df6fd/evmrpc");
  //    String s = baseService.accountHexToBech32(address);
  //    return s;
  //  }

  //  public Account createCount() {
  //    Account account = CreateAccountHex();
  //    log.info("原账户信息：" + JSONObject.toJSONString(account));
  //    account.setAddress(AccoutHexToBech32(account.getAddress()));
  //    log.info("账户地址 hexToBen32:" + JSONObject.toJSONString(account));
  //    return account;
  //  }

  //  public SysUser userAddGas(String address, SysUser sysUser) {
  //    JSONObject param = new JSONObject();
  //    param.put("businessMoney", 1);
  //    param.put("opbChainClientAddress", address);
  //    param.put("opbChainId", 2);
  //    param.put(
  //        "proof",
  //
  // "{\"claim\":{\"entName\":\"福州银聚网络科技发展有限公司\",\"domain\":\"http://web.11touch.net/\",\"loginName\":\"fzyjwl123\",\"did\":\"did:bsn:46K1qscPJMKmBwk1eBHsHyofXwCq\"},\"context\":\"https://www.w3.org/2018/credentials/v1\",\"cptId\":\"882201251518080013\",\"created\":\"2022-08-31 06:15:18\",\"expirationDate\":\"2042-08-31\",\"id\":\"1564859356577665024\",\"issuerDid\":\"did:bsn:2unHZEGYWU5TrcmVnFQ8YGKnLV2h\",\"proof\":{\"creator\":\"did:bsn:2unHZEGYWU5TrcmVnFQ8YGKnLV2h\",\"type\":\"Secp256k1\",\"signatureValue\":\"TIYnNbE+Ei60DmQhp8kXI84C4fWED/ZUtuwM/n45hK4X4m8VPhaPo/COLHdFrcbiQZeWTZ2d4LiAP/eRRtMcNgE=\"},\"shortDesc\":\"DDC业务凭证模板\",\"type\":\"Proof\",\"userDid\":\"did:bsn:46K1qscPJMKmBwk1eBHsHyofXwCq\"}");
  //    param.put("reqTransactionSn", UUID.randomUUID().toString().replaceAll("-", ""));
  //
  //    HttpClient httpClient = HttpClientFactory.getThreadSafeHttpClient();
  //    HttpPost httpPost =
  //        new HttpPost("https://openapi-ddc.bsnbase.com/ddcoai/sys/v1/opb/account/gas/save");
  //    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
  //    httpPost.setHeader("apitoken", ResourcesUtil.getProperty("bsn_api_token"));
  //    HttpResponse response = null;
  //    HttpEntity entity = null;
  //    StringEntity stringEntiry = null;
  //    try {
  //      stringEntiry = new StringEntity(param.toJSONString(), "utf-8");
  //      stringEntiry.setContentType("application/json");
  //      httpPost.setEntity(stringEntiry);
  //      log.info("创建链账户请求参数：" + stringEntiry.toString());
  //      response = httpClient.execute(httpPost);
  //      // 获取响应内容
  //      String retData = null;
  //      entity = response.getEntity();
  //      retData =
  //          StringEscapeUtils.unescapeJava(StringUtils.trim(EntityUtils.toString(entity,
  // "utf-8")));
  //      JSONObject result = JSONObject.parseObject(retData, JSONObject.class);
  //      log.info("新建链账户充值能量：" + result);
  //      if (result.toJSONString().contains("MSG_10021027")) {
  //        // 重新创建链账户地址
  //        Account count = createCount();
  //        create(sysUser.getPhone() + (Math.random()), count);
  //        sysUser.setLinkAddress("0x" + accountBech32ToHex(count.getAddress()));
  //        log.info("用户地址失效重新生成-地址：" + sysUser.getLinkAddress());
  //        sysUser.setPublicKey(count.getPublicKey());
  //        sysUser.setPrivateKey(count.getPrivateKey());
  //        sysUser.setMnemonic(count.getKeyseed());
  //        return sysUser;
  //      }
  //    } catch (Exception e) { // 读取响应错误
  //      System.out.println("BSN 生成链账户异常：" + e.getMessage());
  //    } finally {
  //      try {
  //        if (null != stringEntiry) EntityUtils.consume(stringEntiry);
  //        httpPost.abort(); // 释放连接资源
  //      } catch (Exception ignored) {
  //      }
  //    }
  //    return null;
  //  }

  public String safeTransferFrom(
      String from, String to, BigInteger ddcId, BigInteger amount, byte[] data) throws Exception {
    //    if (ddcId != null && Strings.isNotEmpty(from) && Strings.isNotEmpty(to)) {
    //      return client.getDDC1155Service().safeTransferFrom("", from, to, ddcId, amount, data);
    //    } else {
    //      log.info("转移参数异常：from: " + from + " to: " + to + " ddcId: " + ddcId);
    //    }
    return null;
  }

  public void transDDc(
      String from,
      String to,
      SysUser sysUser,
      StoreTreasure storeTreasure,
      SysUserBackpack sysUserBackpack,
      StoreTreasureRecord storeTreasureRecord) {
    log.info(
        "==========ddc转移开始： "
            + " to: "
            + sysUser.getLinkAddress()
            + " streaId："
            + storeTreasure.getId()
            + " sysUserBackId:"
            + sysUserBackpack.getId()
            + "storeTreasureRecord:"
            + storeTreasureRecord.getId());
    if (Strings.isNotEmpty(storeTreasureRecord.getNftId())) {
      try {
        UUID uuid = UUID.randomUUID();
        sysUserBackpack.setTransationId(uuid.toString());
        AvataUtil.trans(from, storeTreasureRecord.getNftId(), uuid.toString(), to);
        log.info("==========ddc 转移结束： 交易hash: " + sysUserBackpack.getTransationId());
      } catch (Exception e) {
        log.info("==========ddc转移异常：" + e.getMessage());
        e.printStackTrace();
      }
      sysUserBackpack.setDdcId(storeTreasure.getDdcId());
      sysUserBackpack.setDdcUrl(storeTreasure.getDdcUrl());
    }
  }
  /** 根据指定手机号创建链账户 */
  //  public String create(String mobile, Account account) {
  //    String userId = mobile + ResourcesUtil.getProperty("bsn_user_alia");
  //    BsnCreateUserParam bsnCreateUserParam = new BsnCreateUserParam();
  //    bsnCreateUserParam.setOpbChainClientName(userId);
  //    bsnCreateUserParam.setOpbChainClientType(2);
  //    bsnCreateUserParam.setOpbChainId(2);
  //    bsnCreateUserParam.setOpbClientAddress(account.getAddress()); // 账户地址
  //    bsnCreateUserParam.setOpbPublicKey(account.getPublicKey()); // 公钥
  //    bsnCreateUserParam.setOpenDdc(5);
  //    bsnCreateUserParam.setOpbKeyType(3);
  //    bsnCreateUserParam.setProof(
  //
  // "{\"claim\":{\"entName\":\"福州银聚网络科技发展有限公司\",\"domain\":\"http://web.11touch.net/\",\"loginName\":\"fzyjwl123\",\"did\":\"did:bsn:46K1qscPJMKmBwk1eBHsHyofXwCq\"},\"context\":\"https://www.w3.org/2018/credentials/v1\",\"cptId\":\"882201251518080013\",\"created\":\"2022-08-31 06:15:18\",\"expirationDate\":\"2042-08-31\",\"id\":\"1564859356577665024\",\"issuerDid\":\"did:bsn:2unHZEGYWU5TrcmVnFQ8YGKnLV2h\",\"proof\":{\"creator\":\"did:bsn:2unHZEGYWU5TrcmVnFQ8YGKnLV2h\",\"type\":\"Secp256k1\",\"signatureValue\":\"TIYnNbE+Ei60DmQhp8kXI84C4fWED/ZUtuwM/n45hK4X4m8VPhaPo/COLHdFrcbiQZeWTZ2d4LiAP/eRRtMcNgE=\"},\"shortDesc\":\"DDC业务凭证模板\",\"type\":\"Proof\",\"userDid\":\"did:bsn:46K1qscPJMKmBwk1eBHsHyofXwCq\"}");
  //    HttpClient httpClient = HttpClientFactory.getThreadSafeHttpClient();
  //    HttpPost httpPost =
  //        new HttpPost("https://openapi-ddc.bsnbase.com/ddcoai/sys/v2/opb/account/create/save");
  //    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
  //    httpPost.setHeader("apitoken", ResourcesUtil.getProperty("bsn_api_token"));
  //    HttpResponse response = null;
  //    HttpEntity entity = null;
  //    StringEntity stringEntiry = null;
  //    try {
  //      stringEntiry = new StringEntity(JSONObject.toJSONString(bsnCreateUserParam), "utf-8");
  //      stringEntiry.setContentType("application/json");
  //      httpPost.setEntity(stringEntiry);
  //      log.info("创建链账户请求参数：" + stringEntiry.toString());
  //      response = httpClient.execute(httpPost);
  //      // 获取响应内容
  //      String retData = null;
  //      entity = response.getEntity();
  //      retData =
  //          StringEscapeUtils.unescapeJava(
  //              com.aliyun.oss.common.utils.StringUtils.trim(EntityUtils.toString(entity,
  // "utf-8")));
  //      JSONObject result = JSONObject.parseObject(retData, JSONObject.class);
  //      log.info("创建链账户返回信息：" + result);
  //      return result.getJSONObject("data").getString("resAsyncSn");
  //
  //    } catch (Exception e) { // 读取响应错误
  //      e.printStackTrace();
  //      System.out.println("BSN 生成链账户异常：" + e.getMessage());
  //      System.out.println(
  //          "BSN 生成链账户异常 用户信息：" + JSONObject.toJSONString(bsnCreateUserParam) + " 系统用户手机号：" +
  // mobile);
  //    } finally {
  //      try {
  //        if (null != stringEntiry) EntityUtils.consume(stringEntiry);
  //        httpPost.abort(); // 释放连接资源
  //      } catch (Exception ignored) {
  //      }
  //    }
  //
  //    return null;
  //  }

  /**
   * 根据创建链账户返回的code查询创建账户是否正常
   *
   * @param resAsyncSn
   */
  //  public void checkAccountCreateStatus(String resAsyncSn) {
  //    HttpClient httpClient = HttpClientFactory.getThreadSafeHttpClient();
  //    HttpPost httpPost =
  //        new HttpPost(
  //            "https://openapi-ddc.bsnbase.com/ddcoai/sys/v2/opb/account/async/result/searches");
  //    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
  //    httpPost.setHeader("apitoken", ResourcesUtil.getProperty("bsn_api_token"));
  //    HttpResponse response = null;
  //    HttpEntity entity = null;
  //    StringEntity stringEntiry = null;
  //    try {
  //      JSONObject param = new JSONObject();
  //      param.put("resAsyncSn", resAsyncSn);
  //      stringEntiry = new StringEntity(param.toJSONString(), "utf-8");
  //      httpPost.setEntity(stringEntiry);
  //      response = httpClient.execute(httpPost);
  //      // 获取响应内容
  //      String retData = null;
  //      entity = response.getEntity();
  //      retData =
  //          StringEscapeUtils.unescapeJava(StringUtils.trim(EntityUtils.toString(entity,
  // "utf-8")));
  //      System.out.println("异步交易查询结果：" + retData);
  //      JSONObject result = JSONObject.parseObject(retData, JSONObject.class);
  //      if (result.getInteger("code") == 0) {
  //        System.out.println("创建链账户正常：" + retData);
  //      } else {
  //        throw new RuntimeException("创建链账户异常，请联系管理员");
  //      }
  //    } catch (Exception e) { // 读取响应错误
  //      log.info("BSN 生成链账户异常：" + e.getMessage());
  //
  //    } finally {
  //      try {
  //        if (null != stringEntiry) EntityUtils.consume(stringEntiry);
  //        httpPost.abort(); // 释放连接资源
  //      } catch (Exception ignored) {
  //
  //      }
  //    }
  //  }
  //
  //  private String sender = ResourcesUtil.getProperty("bsn_plate_addr");
  //
  ////  public String safeMint(String ddicUrl, BigInteger bigInteger, String dataStr) {
  ////    DDC1155Service ddc1155Service = client.getDDC1155Service();
  ////    String flag = null;
  ////    client.setGatewayUrl(
  ////        "https://opbningxia.bsngate.com:18602/api/ba851d44d7524798bc5d3218a85df6fd/evmrpc");
  ////    try {
  ////      return ddc1155Service.safeMint(sender, sender, bigInteger, ddicUrl, dataStr.getBytes());
  ////    } catch (Exception e) {
  ////      e.printStackTrace();
  ////      if (e.getMessage() != null && e.getMessage().contains("failed to check sender balance"))
  // {
  ////        flag = "能量值或业务费不足";
  ////      }
  ////    }
  ////    throw new RuntimeException(flag);
  ////  }
  //
  ////  public String getDdcUrl(BigInteger ddcidByTxHash4) throws Exception {
  ////    try {
  ////      return client.getDDC1155Service().ddcURI(ddcidByTxHash4);
  ////    } catch (Exception e) {
  ////      System.out.println(e.getMessage());
  ////      return "";
  ////    }
  ////  }
  //
  ////  public void addBalance() {
  ////    try {
  ////      client.getChargeService().selfRecharge(sender, new BigInteger(1000 + ""));
  ////    } catch (Exception e) {
  ////      throw new RuntimeException("平台账户充值账户异常，异常信息：" + e.getMessage());
  ////    }
  ////  }
  //
  ////  public BigInteger getDdcIdFromTxHash(String hash) throws Exception {
  ////    client.setGatewayUrl(
  ////        "https://opbningxia.bsngate.com:18602/api/ba851d44d7524798bc5d3218a85df6fd/evmrpc");
  ////    BigInteger ddcId = null;
  ////    List<JSONObject> ddcIds = null;
  ////    BlockEventService blockEventService = new BlockEventService();
  ////    ArrayList<BaseEventResponse> baseEventResponsesList =
  ////        blockEventService.analyzeEventsByTxHash(hash);
  ////    if (baseEventResponsesList != null && baseEventResponsesList.size() > 0) {
  ////      for (int i = 0; i < baseEventResponsesList.size(); i++) {
  ////        BaseEventResponse baseEventResponse = baseEventResponsesList.get(i);
  ////        if (baseEventResponse instanceof DDC721.TransferEventResponse) {
  ////          ddcId =
  ////              new BigInteger(
  ////                  String.valueOf(((DDC721.TransferEventResponse) baseEventResponse).ddcId));
  ////        }
  ////        if (baseEventResponse instanceof DDC1155.TransferSingleEventResponse) {
  ////          ddcId =
  ////              new BigInteger(
  ////                  String.valueOf(((DDC1155.TransferSingleEventResponse)
  // baseEventResponse).ddcId));
  ////        }
  ////        if (baseEventResponse instanceof DDC1155.TransferBatchEventResponse) {
  ////          ddcIds =
  ////              JSONObject.parseObject(
  ////                  JSONObject.toJSONString(
  ////                      ((DDC1155.TransferBatchEventResponse) baseEventResponse).ddcIds),
  ////                  List.class);
  ////        }
  ////      }
  ////    }
  ////    return ddcId;
  ////  }
  //
  //  public String accountBech32ToHex(String linkAddress) {
  //    return baseService.accountBech32ToHex(linkAddress);
  //  }
}
