package com.qy.ntf.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
/**
 * @author jefferson
 */
public class idcard_103 {

  // 设置超时时间为5秒
  public static RequestConfig config =
      RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
  // 配置您申请的KEY,在个人中心->我的数据,接口名称上方查看
  public static final String APPKEY = ResourcesUtil.getProperty("jh.ocr.check.key");
  // 明文查询地址
  public static String query_url = "http://op.juhe.cn/idcard/query?key=" + APPKEY;
  // 加密查询地址
  public static String queryEncry_url = "http://op.juhe.cn/idcard/queryEncry?key=" + APPKEY;

  // 主方法
  public static String check(String name, String idNum) {

    try {
      String realname = name; // 姓名
      String idcard = idNum; // 身份证
      String openid = ResourcesUtil.getProperty("jh.ocr.check.openId"); // 个人中心查询
      String key = MD5(openid).substring(0, 16); // 取前16位作为加密密钥
      int type = 2; // 加密版本
      realname = SecurityAESTool.encrypt(realname, key); // 加密姓名
      idcard = SecurityAESTool.encrypt(idcard, key); // 加密身份证
      Map<String, Object> params = new HashMap<>(); // 组合参数
      params.put("realname", realname);
      params.put("idcard", idcard);
      // 请求接口
      return queryResult(params, type);
    } catch (Exception e) {
      e.printStackTrace();
      return "{}";
    }

    // 打印结果
  }

  /**
   * 请求接口查询数据
   *
   * @param params 参数
   * @param type 类型，1明文查询（默认），2加密版
   * @return 结果
   * @throws Exception
   */
  public static String queryResult(Map<String, Object> params, int type) throws Exception {

    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response = null;
    String result = null;
    String url = query_url;
    if (type == 2) {
      url = queryEncry_url;
    }
    try {
      url = url + "&" + urlencode(params);
      HttpGet httpget = new HttpGet(url);
      httpget.setConfig(config);
      BasicResponseHandler handler = new org.apache.http.impl.client.BasicResponseHandler();
      result = httpClient.execute(httpget, handler);

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (response != null) {
        response.close();
      }
      httpClient.close();
    }
    return result;
  }

  // 将map型转为请求参数型

  public static String urlencode(Map<String, ?> data) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, ?> i : data.entrySet()) {
      try {
        sb.append(i.getKey())
            .append("=")
            .append(URLEncoder.encode(i.getValue() + "", "UTF-8"))
            .append("&");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    String result = sb.toString();
    result = result.substring(0, result.lastIndexOf("&"));
    return result;
  }

  /**
   * md5加密
   *
   * @param data
   * @return 加密结果
   */
  public static String MD5(String data) {
    StringBuffer md5str = new StringBuffer();
    byte[] input = data.getBytes();
    try {
      // 创建一个提供信息摘要算法的对象，初始化为md5算法对象
      MessageDigest md = MessageDigest.getInstance("MD5");
      // 计算后获得字节数组
      byte[] buff = md.digest(input);
      // 把数组每一字节换成16进制连成md5字符串
      int digital;
      for (int i = 0; i < buff.length; i++) {
        digital = buff[i];
        if (digital < 0) {
          digital += 256;
        }
        if (digital < 16) {
          md5str.append("0");
        }
        md5str.append(Integer.toHexString(digital));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return md5str.toString();
  }
}
