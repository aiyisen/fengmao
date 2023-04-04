package com.qy.ntf.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourcesUtil {

  public static final String WX_PARTNER_ID = ResourcesUtil.getProperty("wx_partner_id");
  ;
  public static String MCH_ID = ResourcesUtil.getProperty("mch_id");
  public static final String WX_APP_ID = ResourcesUtil.getProperty("wx_appid");
  public static String WX_API_KEY = ResourcesUtil.getProperty("wx_api_key");
  public static final String WX_API_CLIENT = ResourcesUtil.getProperty("wx_api_clent");
  public static final String WEIXIN_ORDER_PRODUCT_NOTIFY_URL =
      ResourcesUtil.getProperty("weixin_order_product_notify");
  public static final String WEIXIN_ORDER_TREASURE_NOTIFY_URL =
      ResourcesUtil.getProperty("weixin_order_treasure_notify");
  public static final String WEIXIN_VIP_ORDER_TREASURE_NOTIFY_URL =
      ResourcesUtil.getProperty("weixin_vip_order_treasure_notify");
  public static final String WEIXIN_BALANCE_TREASURE_NOTIFY_URL =
      ResourcesUtil.getProperty("weixin_balance_treasure_notify");

  public static String ALI_APP_ID = ResourcesUtil.getProperty("ali_appid");
  public static String ALI_PRIVATE_KEY = ResourcesUtil.getProperty("private_key");
  public static String ALI_PUBLIC_KEY = ResourcesUtil.getProperty("alipay_public_key");
  public static String ALI_PAY_URL = ResourcesUtil.getProperty("alipay_url");
  public static final String ALIPAY_ORDER_PRODUCT_NOTIFY_URL =
      ResourcesUtil.getProperty("alipay_order_product_notify");
  public static final String ALIPAY_ORDER_TREASURE_NOTIFY_URL =
      ResourcesUtil.getProperty("alipay_order_treasure_notify");
  public static final String ALIPAY_VIP_ORDER_TREASURE_NOTIFY_URL =
      ResourcesUtil.getProperty("alipay_vip_order_treasure_notify");
  public static final String ALIPAY_BALANCE_TREASURE_NOTIFY_URL =
      ResourcesUtil.getProperty("alipay_balance_treasure_notify");

  public static String SUCCESS = "SUCCESS";

  public static final String ALICHART3 = "ISO-8859-1";
  public static final String ALICHART = "UTF-8";

  /** 根据键从config.properties获取值 */
  public static String getProperty(String key) {
    Properties appResources = getResources("conf.properties");
    return appResources.getProperty(key);
  }
  /**
   * 根据资源文件名称获取Properties对象,可通过ps.getProperty(key)获取属性值
   *
   * @param fileName 文件名
   * @return properties对象
   */
  private static Properties getResources(String fileName) {
    InputStream is = null;
    Properties ps = null;
    try {
      if (fileName != null && !"".equals(fileName)) {
        is = ResourcesUtil.class.getClassLoader().getResourceAsStream(fileName);
        ps = new Properties();
        ps.load(is);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (is != null) {
          is.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return ps;
  }
}
