package com.qy.ntf.config;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Configuration
public class UMSign {
  @Value("${aliyun.sign.appkey}")
  private String AliyunAppKey;

  @Value("${aliyun.sign.appCode}")
  private String AliyunAppCode;

  @Value("${UM.appkey}")
  private String UmAppkey;

  public static void main(String[] s) {
    String token =
        "2eyAgIm8iIDogImlPUyIsICAiayIgOiAiRXVheFdiVVZvc2kxbFdtVksxUkROeFNvT2tZVWdibWxsQ3dmUkl2YTBXV2JtNFJoYmg3T2FZaTBHTzJjQ05PbzZ6UHNIbVRxbDZvYlFSa0RnaGFaS3Q2OEtVbFwvdGtTM2FPVnZxTSs0ckI5Yms4OStBb055MGdNaVNjRkptTVZWc1I4UmNucUREbCtjNnZuR0VNVFhCVFRyYkdwWjY4bmtWMTlvUG5TdlY5Vzk0SUpkKzlLYlUwTFprV1MzbFRmZXdEaWIxOHI3ejNxXC9sNmdLQWZreE84WkowVXlRMlF1YXhRN05aK0NHS1dsRmcxXC9LSkRUNzFmcHN3QTNGblBMdFBTelNTRW1pd25EWUhSR3J0RlJRUmVpaTNJK0RtbkJqYmI4S1ZhSEZMdjFsVVdHWjNrcU9mVGVrTXdPWERtRUdtSmhSeDlJZ0hzb0Y3N3RlT2wxWVwvUT09IiwgICJjIiA6ICJzdFU2ZitvcThBZXlWSVpDNW5NM1NrK1oxc21nSU81TWVJQWwrTHV3dnFnZTBoSExZU1JHOW0rR3B2WitibktRR3ZqNmR0M2JjTklFd1d4Wmk2ZHJEQVwveSt4M1BKVktvMGp6SmZtUnBScmI3clFudlNGVXZMbk8ya1dxY1RDemFoQWdCN0pvWEZsUlk3anhGM01HTktJRUpnY3pxMHdVSFVraHpFdlRiMFhVbk9VTXk3SStUd1NoUG10Yk1YMkdta3JWNmx3aXhoOFN2ZkVOZzg3VHF3MEFzTThZK0Z4R3IyVVdLRUZJZEgydStlOE01VjY4dnlqWmxEQ0FPbW1Bd2JuelwvSFpYcUhFb0JkYUFmYXlTWkFMdWI2OUxqQXBaRlwvNVRNV2xyRXJBTTBxZmh2eTVzRnNNQXoxZkZWanF2OTJcL0dPSGVtUTJcLzRnNUpWNGJ6eG8yd3BITnBzM3pNUlZ1alBrWU1STEpZazB3bnIzN0c5UzM0MlVIa01NWGdQR1RIcGhvMWZBZ3A0S3JmZ1B0ditYRllTM0o1ak9ZNUlaOFJOdTJ2eXdDaXpyZHFXS3JpcHR4ZFArSmIrdjVVbE05VHZhMmZcL01ORm4yY3BIV29wMnRQbU5XbHFnbVA1b3B6dzFUS2hTN0lIV3BzSWtXWTRvVXYzOFFEellyTWhtNzJUUUZKMUVXSFl2R3gzaHZkb2J1aytpWWZVcFhWTHBHM1JQMzVWdnUzUVBGd3BiUEt4Yng3NExldVJSdzcyYnMwVFwvY0RJTUtuR0lHR1huV3VlVWxWNkg5NmhkOVRWeGFjS1RTeHpDWXkwc3Z1M2t6T0RUVldFQ2RrRE9PeXk4XC8zeGJNWFYrNGFsa294MW5GYncwZVR6bVdrbU4yTFlMNk5qZlVNdFQzeW16ZlwvMlNlREttc0lBak81cDJ2bENXSVR2ZVlaaEthMkxycWhXODdPMWR3UzdQUFJ5V3ZXY2gzeWd6UXliaGx1NHlCMU85TW43ZkZWdkw1a1wvc21lY0p1dmlaV0Y4MHpTTkxwemlKcDRMVEJKemRrYmJXMnU4SDFrMGY2OFwvZEE0WU09In0=";
    try {
      new UMSign().getUserMobile(token);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String getUserMobile(String youmengToken) throws RuntimeException {
    // 这是你阿里云上面的appkey
    String APP_KEY = AliyunAppKey;
    // 这是你友盟上面的appkey
    String YOUMENG_APP_KEY = UmAppkey;
    HttpClient httpClient;
    HttpPost postMethod;
    HttpResponse response;
    String reponseContent = null;
    // Body内容
    String body = " {\n \"token\":\"" + youmengToken + "\"\n }";

    Date d = new Date();
    String timestamp = String.valueOf(d.getTime());
    String uuid = UUID.randomUUID().toString();

    try {
      httpClient = HttpClients.createDefault();
      postMethod =
          new HttpPost(
              "https://verify5.market.alicloudapi.com/api/v1/mobile/info?appkey="
                  + YOUMENG_APP_KEY);
      // 设置请求头
      postMethod.addHeader("Content-type", "application/json; charset=UTF-8");
      postMethod.addHeader("Authorization", "APPCODE " + AliyunAppCode);
      postMethod.addHeader("X-Ca-Timestamp", timestamp);
      postMethod.addHeader("gateway_channel", "http");
      postMethod.addHeader("X-Ca-Key", APP_KEY);
      postMethod.addHeader("X-Ca-Nonce", uuid);
      postMethod.addHeader("X-Ca-Request-Mode", "DEBUG");
      postMethod.addHeader("X-Ca-Stage", "RELEASE");
      postMethod.addHeader("Host", "verify5.market.alicloudapi.com");
      postMethod.addHeader("Content-MD5", Base64.encodeBase64String(DigestUtils.md5(body)));
      //      postMethod.addHeader("Content-Type", "application/octet-stream; charset=UTF-8");
      postMethod.addHeader("Accept", "application/json");
      postMethod.addHeader(
          "X-Ca-Signature-Headers",
          "X-Ca-Key,X-Ca-Nonce,X-Ca-Signature,X-Ca-Signature-Headers,X-Ca-Stage,X-Ca-Timestamp");
      postMethod.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
      response = httpClient.execute(postMethod);
      HttpEntity httpEntity = response.getEntity();
      reponseContent = EntityUtils.toString(httpEntity);
      System.out.println("返回：" + reponseContent);
      JSONObject jsonObject = JSONObject.parseObject(reponseContent);
      JSONObject data = jsonObject.getJSONObject("data");
      if (data != null) {
        return data.getString("mobile");
      }
    } catch (Exception e) {
      throw new RuntimeException("一键登录异常请联系管理员：%s" + e.getMessage());
    }
    return null;
  }
  //  public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
  //    String umAppkey = "62a98d7e88ccdf4b7e98dbad";
  //    String appKey = "204079199";
  //    String appSecret = "nwBSaMWe8fYrMY3zSbp6URJsgAeKzgdv";
  //    String token =
  //
  // "eyAgIm8iIDogImlPUyIsICAiayIgOiAiTklObnlnRUlxS0VYXC95YXhVcVJwUUJFc3JiQW4yd1lYSlY1WU5Ed0xjaDVhQVNjUWszOGpaWWhlclNlZUs3RnV5ZmZEOERCSDdlOG5EYmRmTVRcL3lxUU85U09WUjFpclM3QktYRmtMXC9KQmVvdVB1dUFPR3N6OG1LXC9yR1g0ZUJcL3NXbEJqeDc0S1ZFTkFJZTlqenRLSTFvZDYrOHQzMDN0TVZkZ093REw3eDJQNWpad1FTOGRhN0w2K2dnSDNUVnJTcmdFSzJiZ2pYSEpQMGdDcGZXSUJQdUd4Y015VUIxZnROU3ZoMDQ0XC9sUWM5SitpRHJQYUNxVVJrZGN5QThIRmdMamN2SXplSVZTMFZsYVd5OUVTbEw3RlRDQTU5c0IwUUlaZFNybWNLak1LRU9ZVkdnd0gzNThaTGx1Z1VMZFhueElSVDhFYnNWc3JZeFZ3bXh3S0p3PT0iLCAgImMiIDogIm5qU3pLdTk0RXM3bzJEMW9zQU5SUnZQWUhTTlZ4NkNhYnYyRkFuaEcxNHpOYnhqWTRMRWFmOTJQZUxLNEFMazhlOFoyNXVUXC9mU0ZNc2prZ0llSzJQNGRneVhoS3FLdXVVTDRaSno0WnpqRkpwZzIrdVQzc0t5YmFkYzgrVU9FVXhHeUt0dXdpeFVPREo2bElPMllNTmFXQ3NoVUw2d2JWc3hGSE5zYVQ0VzN3RjFRQzR1XC9DN3lsanJwb2tBOVRhYkhEQ0ZtbGUyNkJ6aGtNT1wvM1Y5VTRcLzlEajVaOUlvWVFkU2JxT1dodW5HOVdnVXU1VXRjY2FZQ1wvSWthXC9WSzMrY1NXVUFkK0UwbVpiSWh6cjBKSmZZWEtjZzhXYzArd2lLa1RTMkJtMnlsaEY3eTBONCsxSm4wVDdKNVdTSDRLZ1wvb2lnRG81VXoxU2Z3UkpxcHlxUlB1YTNPczJ2ekp4YlZZbTRyYXBQYWc1dEJPRytFYkg0NmdobXVZT1hybTZlZHJwczBlTG5OV3VWNnltcEhpdzJ1WmEyVGpvVndnY284TzRFQ3J3MVwvcHZxTW9md3plTURvXC9RUnJ2c3ZieEh2NTN4YzBpRW5Xa0FoVGNpQXpVU1Jad1N1dlpoakFiYWdWOGlEUEV3c3BqOWc5enJrbXJIN0ZOTHFHZFpzSllWVmlMeHREemozbVZCUHJZK3M1SWtwV0JcLzNqNGlXcVNzZmIwM1hwalN1RTlUWUVDbWZWK3ljREowNEtCMUc5V040UXZab1ZzMEhpWUlvZ2ZMR1JpM0Fja2VZbkdESGJOdGNIKzFvMUZSY1BNZ29FTnFYUFBmNE9sVVlKeEhnRjk3MEhCbEpXTFZkaFhZM2ZsbE1NaDFTbGFKeXhnVUxueDdcL1BCK0xCZTNMOVU0N3hOd1JNSXJwd25yYWFwXC9TZWRqSHhoUm9McTM4dDdFc085Nlh5b0o0S0NyQzVBQ1RJSXZ2bVFSNEZMRGtkenFteng2Y0w1cDBFWCtPTFZiNEhzUmNQXC9BUUNUbVEwSlo5VnZVeFpDVlgxdXIzZHRTSWk3QXZ0VVBKM0hqZVVrPSJ9";
  //    // 下面的url要和阿里云云市场购买的商品对应
  //    String url = "https://verify5.market.alicloudapi.com/api/v1/mobile/info?appkey=" + umAppkey;
  //    HttpPost httpPost = new HttpPost(url);
  //    /** body */
  //    JSONObject object = new JSONObject();
  //    object.put("token", token);
  //    StringEntity stringEntity = new StringEntity(object.toJSONString(), StandardCharsets.UTF_8);
  //    httpPost.setEntity(stringEntity);
  //    /** header */
  //    httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
  //    httpPost.setHeader("Accept", "application/json");
  //    httpPost.setHeader("X-Ca-Version", "1");
  //    httpPost.setHeader("X-Ca-Signature-Headers",
  // "X-Ca-Version,X-Ca-Stage,X-Ca-Key,X-Ca-Timestamp");
  //    httpPost.setHeader("X-Ca-Stage", "RELEASE");
  //    httpPost.setHeader("X-Ca-Key", appKey);
  //    httpPost.setHeader("X-Ca-Timestamp", String.valueOf(System.currentTimeMillis()));
  //    httpPost.setHeader("X-Ca-Nonce", UUID.randomUUID().toString());
  //    httpPost.setHeader(
  //        "Content-MD5", Base64.encodeBase64String(DigestUtils.md5(object.toJSONString())));
  //    /** sign */
  //    String stringToSign = getSignString(httpPost);
  //    Mac hmacSha256 = Mac.getInstance("HmacSHA256");
  //    byte[] keyBytes = appSecret.getBytes(StandardCharsets.UTF_8);
  //    hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
  //    String sign =
  //        new String(
  //
  // Base64.encodeBase64(hmacSha256.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8))));
  //    httpPost.setHeader("X-Ca-Signature", sign);
  //    /** execute */
  //    CloseableHttpClient httpclient = HttpClients.createDefault();
  //    try {
  //      CloseableHttpResponse response = httpclient.execute(httpPost);
  //      System.out.println(response);
  //    } catch (Exception e) {
  //      e.printStackTrace();
  //    }
  //  }

  //  private static String getSignString(HttpPost httpPost) {
  //    Header[] headers = httpPost.getAllHeaders();
  //    Map<String, String> map = new HashMap<>();
  //    for (Header header : headers) {
  //      map.put(header.getName(), header.getValue());
  //    }
  //    return httpPost.getMethod()
  //        + "\n"
  //        + map.get("Accept")
  //        + "\n"
  //        + map.get("Content-MD5")
  //        + "\n"
  //        + map.get("Content-Type")
  //        + "\n\n"
  //        + "X-Ca-Key:"
  //        + map.get("X-Ca-Key")
  //        + "\n"
  //        + "X-Ca-Stage:"
  //        + map.get("X-Ca-Stage")
  //        + "\n"
  //        + "X-Ca-Timestamp:"
  //        + map.get("X-Ca-Timestamp")
  //        + "\n"
  //        + "X-Ca-Version:"
  //        + map.get("X-Ca-Version")
  //        + "\n"
  //        + httpPost.getURI().getPath()
  //        + "?"
  //        + httpPost.getURI().getQuery();
  //  }
}
