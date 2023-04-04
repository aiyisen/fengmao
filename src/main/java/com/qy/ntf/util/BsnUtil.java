package com.qy.ntf.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.common.utils.StringUtils;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.param.BsnCreateUserParam;
import com.qy.ntf.util.llPay.HttpClientFactory;
import com.reddate.wuhanddc.DDCSdkClient;
import com.reddate.wuhanddc.dto.ddc.*;
import com.reddate.wuhanddc.listener.SignEventListener;
import com.reddate.wuhanddc.net.DDCWuhan;
import com.reddate.wuhanddc.service.BlockEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;
import org.web3j.utils.Strings;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
public class BsnUtil {
  // sign event listener
  SignEventListener signEventListener =
      event -> transactionSignature(event.getSender(), event.getRawTransaction());

  // ddcSdkClient instantiation
  DDCSdkClient ddcSdk = new DDCSdkClient().instance(signEventListener);

  // set gateway url
  static {
    DDCWuhan.setGatewayUrl(
        "https://opbningxia.bsngate.com:18602/api/99e14f719e1c4cf1a4d0f21695818615/rpc");
  }

  //  The address the transaction is send from.
  public String sender = ResourcesUtil.getProperty("bsn_plate_addr");

  private static String transactionSignature(String sender, RawTransaction transaction) {
    // sender: Obtain the privateKey according to the sender and complete its signature

    // sender privateKey
    String privateKey = ResourcesUtil.getProperty("bsn_plate_pri_key");
    Credentials credentials = Credentials.create(privateKey);

    /**
     * 5555 wuhanchain id,call example curl -X POST --data
     * '{"jsonrpc":"2.0","method":"eth_chainId","params":[],"id":1}'
     */
    byte[] signedMessage = TransactionEncoder.signMessage(transaction, 5555, credentials);
    return Numeric.toHexString(signedMessage);
  }
  // 创建链账户
  public Account createUser(String mobile) {
    Account account = ddcSdk.accountService.createAccount();
    String userId = mobile + ResourcesUtil.getProperty("bsn_user_alia");
    BsnCreateUserParam bsnCreateUserParam = new BsnCreateUserParam();
    bsnCreateUserParam.setOpbChainClientName(userId);
    bsnCreateUserParam.setOpbChainClientType(2);
    bsnCreateUserParam.setOpbChainId(4);
    bsnCreateUserParam.setOpbClientAddress(account.getAddress());
    bsnCreateUserParam.setOpbPublicKey(account.getPublicKey());
    bsnCreateUserParam.setOpenDdc(5);
    bsnCreateUserParam.setOpbKeyType(3);
    //    bsnCreateUserParam.setProof(ResourcesUtil.getProperty("bsn_api_access_token"));
    bsnCreateUserParam.setProof(
        "{\"claim\":{\"entName\":\"宝龄国际数字科技发展（北京）有限公司\",\"domain\":\"http://xskymeta.com\",\"loginName\":\"Baoling\",\"did\":\"did:bsn:2VUVWdf2m1WTQGrqkxGjimMZb8qm\"},\"context\":\"https://www.w3.org/2018/credentials/v1\",\"cptId\":\"882201251518080013\",\"created\":\"2022-07-15 07:02:19\",\"expirationDate\":\"2042-07-15\",\"id\":\"1547838958803750912\",\"issuerDid\":\"did:bsn:2unHZEGYWU5TrcmVnFQ8YGKnLV2h\",\"proof\":{\"creator\":\"did:bsn:2unHZEGYWU5TrcmVnFQ8YGKnLV2h\",\"type\":\"Secp256k1\",\"signatureValue\":\"JV8oUGnc8MQ2Vpj6dSYztddMjoE/1FEj1kN3Skh8/HMfxDq28GFs1j5IpRzjdwYC5lFIpQGfoMB7utmSmfs3AwE=\"},\"shortDesc\":\"DDC业务凭证模板\",\"type\":\"Proof\",\"userDid\":\"did:bsn:2VUVWdf2m1WTQGrqkxGjimMZb8qm\"}");

    HttpClient httpClient = HttpClientFactory.getThreadSafeHttpClient();
    HttpPost httpPost =
        new HttpPost("https://openapi-ddc.bsnbase.com/ddcoai/sys/v2/opb/account/create/save");
    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
    httpPost.setHeader("apitoken", ResourcesUtil.getProperty("bsn_api_token"));
    HttpResponse response = null;
    HttpEntity entity = null;
    StringEntity stringEntiry = null;
    try {
      stringEntiry = new StringEntity(JSONObject.toJSONString(bsnCreateUserParam), "utf-8");
      httpPost.setEntity(stringEntiry);
      response = httpClient.execute(httpPost);
      // 获取响应内容
      String retData = null;
      entity = response.getEntity();
      retData =
          StringEscapeUtils.unescapeJava(StringUtils.trim(EntityUtils.toString(entity, "utf-8")));
      log.info("生成链账户响应值：" + retData + "系统用户手机号：" + mobile);
    } catch (Exception e) { // 读取响应错误
      log.error("BSN 生成链账户异常：" + e.getMessage());
      log.error(
          "BSN 生成链账户异常 用户信息：" + JSONObject.toJSONString(bsnCreateUserParam) + " 系统用户手机号：" + mobile);
    } finally {
      try {
        if (null != stringEntiry) EntityUtils.consume(stringEntiry);
        httpPost.abort(); // 释放连接资源
      } catch (Exception ignored) {

      }
    }

    return account;
  }

  public void addBalance() {
    try {
      ddcSdk.chargeService.selfRecharge(sender, new BigInteger(1000 + ""));
    } catch (Exception e) {
      throw new RuntimeException("平台账户充值账户异常，异常信息：" + e.getMessage());
    }
  }
  /**
   * 生成721 DDC 拥有者：0x90bd9eb2a3cc6f400b37b9554f3895bb14ad7e77
   * 返回交易Hash：0xe8eb995dba3b8c9cfacfd3e263ed9ebbae9cd6b031486310891b68cc526025dd @Param address
   * 接收账户地址
   */
  public String mint721(String address, String ddicUrl) throws Exception {
    return ddcSdk.ddc721Service.mint(sender, address, "{\"url\":\"" + ddicUrl + "\"}");
  }
  /**
   * 生成1155 DDC 拥有者：0x90bd9eb2a3cc6f400b37b9554f3895bb14ad7e77
   * 返回交易Hash：0xe8eb995dba3b8c9cfacfd3e263ed9ebbae9cd6b031486310891b68cc526025dd @Param address
   * 接收账户地址 address bigInteger 数量
   */
  public String mint1155(String address, String ddicUrl, BigInteger bigInteger, String dataStr)
      throws Exception {
    return ddcSdk.ddc1155Service.safeMint(sender, address, bigInteger, ddicUrl, dataStr.getBytes());
  }

  @Test
  public void fun() {
    try {
      //      Account user = createUser("15100000003");
      //      System.out.println(JSONObject.toJSONString(user));
      // {"address":"0x92855ae99c7760fb8b98988968b89206d21208fc","mnemonic":"[version, adult,
      // differ, rug, rib, old, almost, mistake, joke, hold, middle,
      // simple]","privateKey":"0xb7c19f6a3a0dd14ea2cdcbbd17eeb0ba83ee040e8c67ec212b6bf76288ece381","publicKey":"0xfa86a97e733868424fcca59c1128dccfebc8ad86319aea7b8649f095a3a73d87dedde26afaea87ad5ac8ea81a77d9df490ec21b59dd5e2005e12a582d5c8ba9d"}
      //      String s =
      //          mint1155(
      //              "0x92855ae99c7760fb8b98988968b89206d21208fc",
      //              "https://xskymeta.com/#/",
      //              new BigInteger(3 + ""),
      //              "");
      //      log.info("mint1155返回数据：" + JSONObject.toJSONString(s));
      // 交易信息：mint1155返回数据："0x4e8c4c0df1e98edc103283f83936e67f8d0d2c98ae2e65fa57b9295878a1d673"
      // 获取交易信息
      //
      // getDDCIDByTxHash4("0x4e8c4c0df1e98edc103283f83936e67f8d0d2c98ae2e65fa57b9295878a1d673");
      //      TransactionsBean transactionByHash =
      //          ddcSdk.ddc1155Service.getTransactionByHash(
      //              "0x4e8c4c0df1e98edc103283f83936e67f8d0d2c98ae2e65fa57b9295878a1d673");
      //      System.out.println(JSONObject.toJSONString(transactionByHash));
      //      ddcSdk.ddc1155Service.getTransactionReceipt(
      //          "0x4e8c4c0df1e98edc103283f83936e67f8d0d2c98ae2e65fa57b9295878a1d673");
      // 交易信息：{"blockHash":"0xf60b98c5c59bb67bff628bef08f637fe0b3c05b6a9a1e7e0b5870f23d86ba4ea","blockNumber":"0x37a1c3","from":"0x04f85b5015a3df6e112c585cff7d47066673b8bc","gas":"0x3ded5","gasPrice":"0x59682f00","hash":"0x4e8c4c0df1e98edc103283f83936e67f8d0d2c98ae2e65fa57b9295878a1d673","input":"0xb55bc61700000000000000000000000092855ae99c7760fb8b98988968b89206d21208fc0000000000000000000000000000000000000000000000000000000000000003000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000000000000000e0000000000000000000000000000000000000000000000000000000000000002b7b2275726c223a227b2275726c223a2268747470733a2f2f78736b796d6574612e636f6d2f232f227d227d000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000a5b42403137383231336200000000000000000000000000000000000000000000","nonce":"0xc","r":"0xc2a9eb7af1f62ac03d3c29feefdda26a271000e824f20d9fa85e8689219746c5","s":"0x56cf996a87f7e2586b01a05bd85721dd2d6433b4427bc1cabb8fefe42e971641","to":"0x061e59c74815994dab4226a0d344711f18e0f418","transactionIndex":"0xb","type":"0x0","v":"0x2b89","value":"0x0"}
      // 0x415335ece3896c61fbe0f86798b3877f6a78a67b8ca0ad27e47d4851bb59feb3
      //      String ddcidByTxHash4 =
      //
      //
      // getDDCIDByTxHash4("0x4e8c4c0df1e98edc103283f83936e67f8d0d2c98ae2e65fa57b9295878a1d673");
      String ddcidByTxHash4 =
          getDDCIDByTxHash4("0x847720e9763c0bd8f9a43a68d63d0d2d133c2b91cd46a85a7ad47f79a5445331");
      System.out.println(ddcidByTxHash4);
      // ddcId =53299
      String ddcURI = ddcSdk.ddc1155Service.ddcURI(new BigInteger(ddcidByTxHash4));
      System.out.println(ddcURI);

    } catch (Exception e) {
      log.error("异常信息：" + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Test
  public void transferFromTest() {
    try {
      String s =
          safeTransferFrom(
              "0xebb5e1d527f0f92b7160d443ebeb4f9724a27cf3",
              new BigInteger(53307 + ""),
              new BigInteger(1 + ""),
              "".getBytes());
      log.info("交易結果：" + s);
    } catch (Exception e) {
      e.getMessage();
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * DDC的转移
   *
   * @param to 接收者账户
   * @param ddcId DDCID
   * @param amount 数量
   * @param data 附加数据
   * @return 转移结果
   * @throws Exception Exception
   */
  public String safeTransferFrom(String to, BigInteger ddcId, BigInteger amount, byte[] data)
      throws Exception {
    return ddcSdk.ddc1155Service.safeTransferFrom(sender, sender, to, ddcId, amount, data, null);
  }
  /** 根据交易hash 获取DDCID 115176 */
  public String getDDCIDByTxHash4(String transactionHash) throws Exception {
    TransactionReceipt transactionRecepitBean =
        ddcSdk.baseService.getTransactionReceipt(transactionHash);
    if (transactionRecepitBean != null) {
      BlockEventService blockEventService = new BlockEventService();
      ArrayList<BaseEventBean> blockEvent =
          blockEventService.getBlockEvent(transactionRecepitBean.getBlockNumber());
      BaseEventBean baseEventBean =
          blockEvent.stream()
              .filter(
                  eventBean ->
                      eventBean.getBlockHash().equals(transactionHash)
                          && (eventBean instanceof DDC721TransferEventBean
                              || eventBean instanceof DDC1155TransferSingleEventBean
                              || eventBean instanceof DDC1155TransferBatchEventBean))
              .findFirst()
              .get();
      log.info(
          "生成ddc查询交易结果：" + JSONObject.toJSONString(baseEventBean) + " 交易hash: " + transactionHash);
      if (baseEventBean instanceof DDC721TransferEventBean) {
        return ((DDC721TransferEventBean) baseEventBean).getDdcId() + "";
      } else if (baseEventBean instanceof DDC1155TransferSingleEventBean) {
        return ((DDC1155TransferSingleEventBean) baseEventBean).getDdcId() + "";
      } else if (baseEventBean instanceof DDC1155TransferBatchEventBean) {
        return Strings.join(
            ((DDC1155TransferBatchEventBean) baseEventBean)
                .getDdcIds().stream().map(o -> o + "").collect(Collectors.toList()),
            ";");
      }
    }
    return "";
  }

  public String getDdcUrl(String ddcidByTxHash4) throws Exception {
    return ddcSdk.ddc1155Service.ddcURI(new BigInteger(ddcidByTxHash4));
  }

  @Test
  public void createUser() {
    BsnUtil bsnUtil = new BsnUtil();
    Account user = bsnUtil.createUser("1514000882011");
    System.out.println(JSONObject.toJSONString(user));
  }

  public void checkUserAddress(SysUser sysUser) {
    ddcSdk.ddc721Service.checkTo(sysUser.getLinkAddress());
  }
}
