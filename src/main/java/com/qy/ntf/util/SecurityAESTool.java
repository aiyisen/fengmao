package com.qy.ntf.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SecurityAESTool {

  /**
   * AES加密
   *
   * @param str 明文
   * @param key 秘钥
   * @return
   */
  public static String encrypt(String str, String key) {
    byte[] crypted = null;
    try {
      SecretKeySpec skey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, skey);
      String enStr = str;
      crypted = cipher.doFinal(enStr.getBytes("UTF-8"));
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    String body = new String(Base64.encodeBase64(crypted));
    return body;
  }

  /**
   * AES解密
   *
   * @param input
   * @param key
   * @return
   */
  public static String decrypt(String input, String key) {
    byte[] output = null;
    String body = null;
    if (input == null || key == null) {
      return null;
    }
    try {
      SecretKeySpec skey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, skey);
      byte[] b = Base64.decodeBase64(input);
      // 解密
      output = cipher.doFinal(b);
      body = new String(output, "UTF-8");
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return body;
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
    String key = "1111111111111111"; // 密钥
    String data = "123456"; // 明文

    String enStr = SecurityAESTool.encrypt(data, key);
    System.out.println(enStr);
    System.out.println(URLEncoder.encode(enStr, "UTF-8"));
  }
}
