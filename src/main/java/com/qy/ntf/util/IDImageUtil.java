package com.qy.ntf.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IDImageUtil {
  public static void main(String[] args) {
    String front =
        getIdInfo(
            "http://39.107.247.125:8090/local/download?path=2022-06-25/593064232203845632.jpg",
            "front");
    System.out.println(front);
  }
  /**
   * @param imageUrl 图片地址
   * @param type 类型 front 身份证钱页 back 背页
   * @return
   */
  public static String getIdInfo(String imageUrl, String type) {
    try {
      String imageBase64 = GetUrlImageToBase64(imageUrl);
      System.out.println(imageBase64);
      return sendLogs(imageBase64, type);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String GetUrlImageToBase64(String url) throws Exception {
    if (url == null || "".equals(url.trim())) return null;
    URL u = new URL(url);
    // 打开图片路径
    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
    // 设置请求方式为GET
    conn.setRequestMethod("GET");
    // 设置超时响应时间为5秒
    conn.setConnectTimeout(5000);
    // 通过输入流获取图片数据
    InputStream inStream = conn.getInputStream();
    // 读取图片字节数组
    byte[] bytes = readInputStream(inStream);
    inStream.close();
    // 对字节数组Base64编码
    BASE64Encoder encoder = new BASE64Encoder();
    // 返回Base64编码过的字节数组字符串
    return encoder.encode(bytes);
  }

  public static byte[] readInputStream(InputStream inStream) throws Exception {
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[10240];
    int len = 0;
    while ((len = inStream.read(buffer)) != -1) {
      outStream.write(buffer, 0, len);
    }
    inStream.close();
    return outStream.toByteArray();
  }

  public static String sendLogs(String data, String type) throws Exception {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost("http://apis.juhe.cn/idimage/verify");
      HttpEntity reqEntity =
          MultipartEntityBuilder.create()
              .addPart(
                  "key",
                  new StringBody(
                      ResourcesUtil.getProperty("jh.ocr.key"), ContentType.MULTIPART_FORM_DATA))
              .addPart("image", new StringBody(data, ContentType.MULTIPART_FORM_DATA))
              .addPart("side", new StringBody(type, ContentType.MULTIPART_FORM_DATA))
              .build();

      httpPost.setEntity(reqEntity);
      BasicResponseHandler handler = new BasicResponseHandler();
      return httpClient.execute(httpPost, handler);
    }
  }
}
