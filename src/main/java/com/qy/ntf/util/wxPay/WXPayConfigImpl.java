package com.qy.ntf.util.wxPay;

import com.github.wxpay.sdk.WXPayConfig;
import com.qy.ntf.util.ResourcesUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WXPayConfigImpl implements WXPayConfig {

  private static WXPayConfigImpl INSTANCE;

  private static byte[] certData;

  private WXPayConfigImpl() throws Exception {
    // 从微信商户平台下载的安全证书存放的目录
    String certPath = ResourcesUtil.WX_API_CLIENT;

    File file = new File(certPath);
    InputStream certStream = new FileInputStream(file);
    certData = new byte[(int) file.length()];
    certStream.read(certData);
    certStream.close();
  }

  public static WXPayConfigImpl getInstance() throws Exception {
    if (INSTANCE == null) {
      synchronized (WXPayConfigImpl.class) {
        if (INSTANCE == null) {
          INSTANCE = new WXPayConfigImpl();
        }
      }
    }
    return INSTANCE;
  }

  /** 作者:WZD 2017年8月16日 上午9:04:21 获取appId */
  @Override
  public String getAppID() {
    return ResourcesUtil.WX_APP_ID;
  }

  /** 作者:WZD 2017年8月16日 上午9:04:21 获取证书内容 */
  @Override
  public InputStream getCertStream() {
    return new ByteArrayInputStream(certData);
  }

  /** 作者:WZD 2017年8月16日 上午9:04:21 设置请求超时时间 */
  @Override
  public int getHttpConnectTimeoutMs() {
    return 600000;
  }

  /** 作者:WZD 2017年8月16日 上午9:04:21 读取时间 */
  @Override
  public int getHttpReadTimeoutMs() {
    return 600000;
  }

  /** 作者:WZD 2017年8月16日 上午9:04:21 apiKey */
  @Override
  public String getKey() {
    return ResourcesUtil.WX_API_KEY;
  }

  /** 作者:WZD 2017年8月16日 上午9:04:21 商户号 */
  @Override
  public String getMchID() {
    return ResourcesUtil.MCH_ID;
  }
}
