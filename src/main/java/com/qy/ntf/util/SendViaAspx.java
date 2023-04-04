package com.qy.ntf.util;

import com.qy.ntf.bean.param.SendVerfiBack;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SendViaAspx {
  private static CloseableHttpClient httpclient;

  public static void main(String[] a) {
    sendCode("15542914939", "826927");
  }

  public static SendVerfiBack sendCode(String phone, String code) {
    HttpEntity entity = null;
    String returnString = null;
    try {
      httpclient = createSSLClientDefault();
      String url = "https://dx.ipyy.net/sms.aspx"; // 接口地址
      String userid = "69517"; // 用户ID。
      String account = "OT00672"; // 用户账号名
      String password = "k7fvb8td"; // 接口密码
      String text = "【X星云】您正在登录验证，验证码" + code + "，切勿将验证码泄露于他人。";
      String sendTime = "";
      String extno = ""; // 扩展号，没有请留空
      HttpPost post = new HttpPost(url);
      post.setHeader("Content-type", "application/x-www-form-urlencoded");
      List<BasicNameValuePair> nvps = new ArrayList<>();
      nvps.add(new BasicNameValuePair("action", "send"));
      nvps.add(new BasicNameValuePair("userid", userid));
      nvps.add(new BasicNameValuePair("account", account));
      nvps.add(new BasicNameValuePair("password", encode(password)));
      nvps.add(new BasicNameValuePair("mobile", phone));
      nvps.add(new BasicNameValuePair("content", text));
      nvps.add(new BasicNameValuePair("sendTime", sendTime));
      nvps.add(new BasicNameValuePair("extno", extno));
      post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
      HttpResponse response = httpclient.execute(post);
      entity = response.getEntity();
      returnString = EntityUtils.toString(entity);
      return randerObject(returnString);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        EntityUtils.consume(entity);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static SendVerfiBack randerObject(String arg) {
    try {
      JAXBContext context = JAXBContext.newInstance(SendVerfiBack.class);
      // 进行将Xml转成对象的核心接口
      Unmarshaller unmarshaller = context.createUnmarshaller();
      StringReader sr = new StringReader(arg);
      SendVerfiBack xmlObject = (SendVerfiBack) unmarshaller.unmarshal(sr);
      return xmlObject;
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  public static CloseableHttpClient createSSLClientDefault() {
    try {
      // 信任所有
      SSLContext sslContext =
          new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
      return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    } catch (KeyManagementException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (KeyStoreException e) {
      e.printStackTrace();
    }
    return HttpClients.createDefault();
  }
  /*
   * 加密算法
   */
  public static String encode(String text) {

    try {
      MessageDigest digest = MessageDigest.getInstance("md5");
      byte[] result = digest.digest(text.getBytes());
      StringBuilder sb = new StringBuilder();
      for (byte b : result) {
        int number = b & 0xff;
        String hex = Integer.toHexString(number);
        if (hex.length() == 1) {
          sb.append("0" + hex);
        } else {
          sb.append(hex);
        }
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return "";
  }
}
