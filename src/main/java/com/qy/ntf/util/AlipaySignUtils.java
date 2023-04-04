package com.qy.ntf.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.StreamUtil;
import com.alipay.api.internal.util.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AlipaySignUtils @Description @Author wjx @Date 2019/2/19 15:38 @Version 1.0
 */
public class AlipaySignUtils {
  private static Logger log = LoggerFactory.getLogger(AlipaySignUtils.class);
  /**
   * @return boolean @Author wjx @Description 验签 Date 2019/2/20 15:43 @Param []
   */
  public static boolean veritySign(JSONObject params, String publicKey, String charset)
      throws AlipayApiException, AlipayApiException {
    return true;
  }

  public static boolean veritySign(
      Map<String, String> params, String publicKey, String charset, String signType)
      throws AlipayApiException {
    String sign = params.get("sign");
    String content = getSignCheckContentV1(params);

    return rsaCheck(content, sign, publicKey, charset, signType);
  }

  public static boolean rsaCheck(
      String content, String sign, String publicKey, String charset, String signType)
      throws AlipayApiException {
    if (AlipayConstants.SIGN_TYPE_RSA.equals(signType)) {
      return rsaCheckContent(content, sign, publicKey, charset);
    } else if (AlipayConstants.SIGN_TYPE_RSA2.equals(signType)) {
      return rsa256CheckContent(content, sign, publicKey, charset);
    } else {
      throw new AlipayApiException("Sign Type is Not Support : signType=" + signType);
    }
  }

  public static boolean rsa256CheckContent(
      String content, String sign, String publicKey, String charset) throws AlipayApiException {
    try {
      PublicKey pubKey =
          getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
      java.security.Signature signature =
          java.security.Signature.getInstance(AlipayConstants.SIGN_SHA256RSA_ALGORITHMS);
      signature.initVerify(pubKey);
      if (StringUtils.isEmpty(charset)) {
        signature.update(content.getBytes());
      } else {
        signature.update(content.getBytes(charset));
      }
      return signature.verify(Base64.decodeBase64(sign.getBytes()));
    } catch (Exception e) {
      throw new AlipayApiException(
          "RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
    }
  }

  public static String getSignCheckContentV1(Map<String, String> params) {
    if (params == null) {
      return null;
    }

    params.remove("sign");
    params.remove("sign_type");

    StringBuffer content = new StringBuffer();
    List<String> keys = new ArrayList<String>(params.keySet());
    Collections.sort(keys);

    for (int i = 0; i < keys.size(); i++) {
      String key = keys.get(i);
      String value = params.get(key);
      content.append((i == 0 ? "" : "&") + key + "=" + value);
    }

    return content.toString();
  }

  public static boolean rsaCheckContent(
      String content, String sign, String publicKey, String charset) throws AlipayApiException {
    try {
      PublicKey pubKey =
          getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));

      java.security.Signature signature =
          java.security.Signature.getInstance(AlipayConstants.SIGN_ALGORITHMS);

      signature.initVerify(pubKey);

      if (StringUtils.isEmpty(charset)) {
        signature.update(content.getBytes());
      } else {
        signature.update(content.getBytes(charset));
      }

      return signature.verify(Base64.decodeBase64(sign.getBytes()));
    } catch (Exception e) {
      throw new AlipayApiException(
          "RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
    }
  }

  public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

    StringWriter writer = new StringWriter();
    StreamUtil.io(new InputStreamReader(ins), writer);

    byte[] encodedKey = writer.toString().getBytes();

    encodedKey = Base64.decodeBase64(encodedKey);

    return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
  }
}
