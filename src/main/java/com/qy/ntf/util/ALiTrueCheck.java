package com.qy.ntf.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ALiTrueCheck {
  public static JSONObject getInfo(String url) {

    // API产品路径
    String requestUrl = "https://personcard.market.alicloudapi.com/ai-market/ocr/personid";
    // 阿里云APPCODE
    String appcode = "79f6aeb33c4e48f2ac5d26b3df5824b2";

    CloseableHttpClient httpClient = null;
    try {
      httpClient = HttpClients.createDefault();
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      // 装填参数

      // 启用URL方式进行识别
      // 内容数据类型是图像文件URL链接
      params.add(new BasicNameValuePair("AI_IDCARD_IMAGE", url));
      params.add(new BasicNameValuePair("AI_IDCARD_IMAGE_TYPE", "1"));
      params.add(new BasicNameValuePair("AI_IDCARD_SIDE", "FRONT"));

      // 创建一个HttpPost实例
      HttpPost httpPost = new HttpPost(requestUrl);
      httpPost.addHeader("Authorization", "APPCODE " + appcode);
      httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
      // 设置请求参数
      httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
      // 发送POST请求
      HttpResponse execute = httpClient.execute(httpPost);
      // 获取状态码
      int statusCode = execute.getStatusLine().getStatusCode();
      System.out.println(statusCode);

      // 获取结果
      HttpEntity entity = execute.getEntity();
      String result = EntityUtils.toString(entity, "utf-8");
      JSONObject tmpResult = JSONObject.parseObject(result);
      return tmpResult.getJSONObject("PERSON_ID_ENTITY");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (httpClient != null) {
        try {
          httpClient.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public static Boolean checkSec(String url) {

    // API产品路径
    String requestUrl = "https://personcard.market.alicloudapi.com/ai-market/ocr/personid";
    // 阿里云APPCODE
    String appcode = "79f6aeb33c4e48f2ac5d26b3df5824b2";

    CloseableHttpClient httpClient = null;
    try {
      httpClient = HttpClients.createDefault();
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      // 装填参数

      // 启用URL方式进行识别
      // 内容数据类型是图像文件URL链接
      params.add(new BasicNameValuePair("AI_IDCARD_IMAGE", url));
      params.add(new BasicNameValuePair("AI_IDCARD_IMAGE_TYPE", "1"));
      params.add(new BasicNameValuePair("AI_IDCARD_SIDE", "FRONT"));

      // 创建一个HttpPost实例
      HttpPost httpPost = new HttpPost(requestUrl);
      httpPost.addHeader("Authorization", "APPCODE " + appcode);
      httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
      // 设置请求参数
      httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
      // 发送POST请求
      HttpResponse execute = httpClient.execute(httpPost);
      // 获取状态码
      int statusCode = execute.getStatusLine().getStatusCode();
      System.out.println(statusCode);

      // 获取结果
      HttpEntity entity = execute.getEntity();
      String result = EntityUtils.toString(entity, "utf-8");
      JSONObject tmpResult = JSONObject.parseObject(result);
      JSONObject person_id_side_entity = tmpResult.getJSONObject("PERSON_ID_SIDE_ENTITY");
      if (person_id_side_entity == null) {
        return false;
      } else {
        if (person_id_side_entity.getString("PERSON_ID_BACK_SIDE_AMOUNT") != null) {
          return person_id_side_entity.getString("PERSON_ID_BACK_SIDE_AMOUNT").equals("1");
        }
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (httpClient != null) {
        try {
          httpClient.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }

  public static void main(String[] arggs) {
    System.out.println(
        checkFourParam("211321199509274768", "毛鸿妲", "13654974713", "6230810345508247"));
  }

  public static Boolean checkParam(String idCardNo, String name) {

    // API产品路径
    String requestUrl = "https://jmidcardv1.market.alicloudapi.com/idcard/validate";
    // 阿里云APPCODE
    String appcode = "79f6aeb33c4e48f2ac5d26b3df5824b2";

    CloseableHttpClient httpClient = null;
    try {
      httpClient = HttpClients.createDefault();
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      // 装填参数

      // 启用URL方式进行识别
      // 内容数据类型是图像文件URL链接
      params.add(new BasicNameValuePair("idCardNo", idCardNo));
      params.add(new BasicNameValuePair("name", name));

      // 创建一个HttpPost实例
      HttpPost httpPost = new HttpPost(requestUrl);
      httpPost.addHeader("Authorization", "APPCODE " + appcode);
      httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
      // 设置请求参数
      httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
      // 发送POST请求
      HttpResponse execute = httpClient.execute(httpPost);
      // 获取状态码
      int statusCode = execute.getStatusLine().getStatusCode();
      System.out.println(statusCode);

      // 获取结果
      HttpEntity entity = execute.getEntity();
      String result = EntityUtils.toString(entity, "utf-8");
      JSONObject tmpResult = JSONObject.parseObject(result);
      System.out.println("实名认证：" + tmpResult.toJSONString());
      if (tmpResult.getJSONObject("data") != null) {
        return tmpResult.getJSONObject("data").getInteger("result") == 0;
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (httpClient != null) {
        try {
          httpClient.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }

  public static Boolean checkFourParam(
      String idCardNo, String name, String phone, String bankCardNo) {

    // API产品路径
    String requestUrl = "http://yhkys.market.alicloudapi.com/communication/personal/1887";
    // 阿里云APPCODE
    String appcode = "66872008786042e6b7b922f876360c44";

    CloseableHttpClient httpClient = null;
    try {
      httpClient = HttpClients.createDefault();
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      // 装填参数

      // 启用URL方式进行识别
      // 内容数据类型是图像文件URL链接
      params.add(new BasicNameValuePair("acc_no", bankCardNo));
      params.add(new BasicNameValuePair("idcard", idCardNo));
      params.add(new BasicNameValuePair("mobile", phone));
      params.add(new BasicNameValuePair("name", name));

      // 创建一个HttpPost实例
      HttpPost httpPost = new HttpPost(requestUrl);
      httpPost.addHeader("Authorization", "APPCODE " + appcode);
      httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
      // 设置请求参数
      httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
      // 发送POST请求
      HttpResponse execute = httpClient.execute(httpPost);
      // 获取状态码
      int statusCode = execute.getStatusLine().getStatusCode();
      System.out.println(statusCode);

      // 获取结果
      HttpEntity entity = execute.getEntity();
      String result = EntityUtils.toString(entity, "utf-8");
      JSONObject tmpResult = JSONObject.parseObject(result);
      System.out.println("实名认证：" + tmpResult.toJSONString());
      if (tmpResult.getJSONObject("data") != null) {
        return "1".equals(tmpResult.getJSONObject("data").getString("state"));
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (httpClient != null) {
        try {
          httpClient.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }
}
