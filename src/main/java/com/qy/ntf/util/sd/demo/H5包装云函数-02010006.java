// package com.qy.ntf.util.sd.demo;
//
// import java.net.URLEncoder;
// import java.security.MessageDigest;
// import java.text.SimpleDateFormat;
// import java.util.Calendar;
// import java.util.LinkedHashMap;
// import java.util.Map;
// import java.util.UUID;
//
/// **
// * @Auther YaoJun Qi @Date 2021/03/05 13:43
// *
// * @description
// */
//
//// TODO 修改商户号，mer_key(key1),key(md5key)
//
// public class GenerateUrl {
//  public static void main(String[] args) {
//    SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
//    Calendar calendar = Calendar.getInstance();
//    String createTime = sdf.format(calendar.getTime());
//    calendar.add(Calendar.HOUR, 1);
//    String endTime = sdf.format(calendar.getTime());
//
//    String version = "10";
//    // 商户号
//    String mer_no = "6888806";
//    // 商户key1
//    String mer_key = "FKfQM/ZIobMEEMc6ZM4arwxHuribPegYgC1cPtNoEkU=";
//    // 订单号
//    UUID uuid = UUID.randomUUID();
//    String mer_order_no = uuid.toString().replaceAll("-", "");
//    // 回调地址
//    String notify_url = "https://www.baidu.com";
//    // 金额
//    String order_amt = "0.11";
//    // 商品名称
//    String goods_name = "测试";
//    // 支付扩展域
//    // 云函数所需参数，resourceAppid：小程序AppID； resourceEnv：云开发环境 ID
//    String pay_extra = "{\"resourceAppid\":\"666666\",\"resourceEnv\":\"hmfsy-1\"}";
//
//    // md5key
//    String key = "4upgQr5SPZLmISC4tJjkxLQK7xl1oiupnsamaDKdckfhrKf/+Nixuz+UKFTiw==";
//
//    Map<String, String> map = new LinkedHashMap<>();
//    map.put("accsplit_flag", "NO");
//    map.put("create_ip", "127_0_0_1");
//    map.put("create_time", createTime);
//
//    map.put("mer_key", mer_key);
//    map.put("mer_no", mer_no);
//    map.put("mer_order_no", mer_order_no);
//    map.put("notify_url", notify_url);
//    map.put("order_amt", order_amt);
//    map.put("pay_extra", pay_extra);
//    map.put("return_url", "");
//    map.put("sign_type", "MD5");
//    map.put("store_id", "000000");
//    map.put("version", version);
//    map.put("key", key);
//
//    //        map.put("expire_time",endTime);
//    //        map.put("goods_name",goods_name);
//    //        map.put("product_code","02010006");
//    //        map.put("clear_cycle","0");
//
//    String signature = "";
//
//    for (String s : map.keySet()) {
//      if (!(map.get(s) == null || map.get(s).equals(""))) {
//        signature += s + "=";
//        signature += map.get(s) + "&";
//      }
//    }
//    signature = signature.substring(0, signature.length() - 1);
//    System.out.println("参与签名字符串：\n" + signature);
//
//    String sign = MD5Util.encode(signature).toUpperCase();
//
//    System.out.println("签名串：\n" + sign);
//
//    // 拼接url
//    String url =
//        "https://sandcash.mixienet.com.cn/pay/h5/applet?"
//            +
//            //     云函数h5： applet  ；支付宝H5：alipay  ； 微信公众号H5：wechatpay   ；
//            // 一键快捷：fastpayment   ；H5快捷 ：unionpayh5    ；支付宝扫码：alipaycode ;快捷充值:quicktopup
//            // 电子钱包【云账户】：cloud
//            "version="
//            + version
//            + ""
//            + "&mer_no="
//            + mer_no
//            + ""
//            + "&mer_key="
//            + URLEncoder.encode(mer_key)
//            + ""
//            + "&mer_order_no="
//            + mer_order_no
//            + ""
//            + "&create_time="
//            + createTime
//            + ""
//            + "&expire_time="
//            + endTime
//            + ""
//            + // endTime
//            "&order_amt=0.11"
//            + "&notify_url="
//            + URLEncoder.encode(notify_url)
//            + ""
//            + "&return_url="
//            + "&create_ip=127_0_0_1"
//            + "&goods_name="
//            + URLEncoder.encode(goods_name)
//            + ""
//            + "&store_id=000000"
//            +
//            // 产品编码: 云函数h5：  02010006  ；支付宝H5：  02020002  ；微信公众号H5：02010002   ；
//            // 一键快捷：  05030001  ；H5快捷：  06030001   ；支付宝扫码：  02020005 ；快捷充值：  06030003
//            // 电子钱包【云账户】：开通账户并支付product_code应为：04010001；消费（C2C）product_code 为：04010003 ; 我的账户页面
//            // product_code 为：00000001
//            "&product_code=02010006"
//            + ""
//            + "&clear_cycle=3"
//            + "&pay_extra="
//            + URLEncoder.encode(pay_extra)
//            + ""
//            +
// "&meta_option=%5B%7B%22s%22%3A%22Android%22,%22n%22%3A%22wxDemo%22,%22id%22%3A%22com.pay.paytypetest%22,%22sc%22%3A%22com.pay.paytypetest%22%7D%5D"
//            + "&accsplit_flag=NO"
//            + "&jump_scheme="
//            + "&sign_type=MD5"
//            + "&sign="
//            + sign
//            + "";
//
//    System.out.println("最终链接：\n\n" + url);
//  }
// }
//
// class MD5Util {
//
//  /** 私有构造方法,将该工具类设为单例模式. */
//  private MD5Util() {}
//
//  private static final String[] hex = {
//    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
//  };
//
//  public static String encode(String password) {
//    try {
//      MessageDigest md5 = MessageDigest.getInstance("MD5");
//      byte[] byteArray = md5.digest(password.getBytes("utf-8"));
//      String passwordMD5 = byteArrayToHexString(byteArray);
//      return passwordMD5;
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return password;
//  }
//
//  public static String encode(String password, String enc) {
//    try {
//      MessageDigest md5 = MessageDigest.getInstance("MD5");
//      byte[] byteArray = md5.digest(password.getBytes(enc));
//      String passwordMD5 = byteArrayToHexString(byteArray);
//      return passwordMD5;
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return password;
//  }
//
//  private static String byteArrayToHexString(byte[] byteArray) {
//    StringBuffer sb = new StringBuffer();
//    for (byte b : byteArray) {
//      sb.append(byteToHexChar(b));
//    }
//    return sb.toString();
//  }
//
//  private static Object byteToHexChar(byte b) {
//    int n = b;
//    if (n < 0) {
//      n = 256 + n;
//    }
//    int d1 = n / 16;
//    int d2 = n % 16;
//    return hex[d1] + hex[d2];
//  }
//
//  public static void main(String[] args) {
//    System.out.println(
//        encode(
//
// "accsplit_flag=NO&activity_no=MP20201228132838216&benefit_amount=0.5&create_ip=172_12_12_12&create_time=20210112161203&mer_key=Xx52CDtWRH1etGu4IfFEB4OeRrnbr+EUd5VO7cBQFCqxfDl5FJcJaUjKJbHapVsyxSODBEbssNk=&mer_no=16938552&mer_order_no=20210112161203087575052491&notify_url=http://sandcash/notify&order_amt=10.0&pay_extra={\"mer_app_id\":\"\",\"openid\":\"\",\"buyer_id\":\"\",\"wx_app_id\":\"\",\"gh_ori_id\":\"gh_8f69bbed2867\",\"path_url\":\"pages/zf/index?\",\"miniProgramType\":\"0\"}&return_url=http://www.baidu.com&sign_type=MD5&store_id=100001&version=1.0&key=\"商户提供\"")
//            .toUpperCase());
//    //        encode("ganxuehui","admin");
//  }
// }
