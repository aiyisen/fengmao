package com.qy.ntf.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.lianpay.api.util.TraderRSAUtil;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.customResult.LLResult;
import com.qy.ntf.bean.param.LLNotifyParam;
import com.qy.ntf.bean.param.LLOrderBackNotifyParam;
import com.qy.ntf.service.OrderProductService;
import com.qy.ntf.service.OrderTreasurePoolService;
import com.qy.ntf.service.OrderVipService;
import com.qy.ntf.util.ResourcesUtil;
import com.qy.ntf.util.llPay.LLianPayAccpSignature;
import com.qy.ntf.util.sd.SdNotifyBody;
import com.qy.ntf.util.wxPay.WXNotifyParam;
import com.qy.ntf.util.wxPay.WXPayResult;
import com.qy.ntf.util.wxPay.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.controller @ClassName:
 * SystemController @Author: 王振读 @Description: ${description} @Date: 2021/11/26 9:31 @Version: 1.0
 */
@RestController
@RequestMapping("/notify")
@Slf4j
// @CrossOrigin(origins = "*")
@ApiIgnore
public class PayNotifyController extends BaseController {

  @Autowired private OrderProductService orderProductService;
  @Autowired private OrderTreasurePoolService orderTreasurePoolService;
  @Autowired private OrderVipService orderVipService;

  @ApiIgnore
  @PostMapping("/weixin/orderProduct")
  public WXPayResult weixinNotify(@RequestBody WXNotifyParam wxp) {
    WXPayResult wxPayResult = new WXPayResult();
    log.info("==============================>>实物商品订单=》微信异步通知结果：" + wxp);
    Map<String, String> xmlDate = null;
    try {
      xmlDate = WXPayUtil.convertBean(wxp);
      log.info("convertBean-info:{} ", xmlDate);
      boolean b = WXPayUtil.isSignatureValid(xmlDate, ResourcesUtil.WX_API_KEY);
      orderProductService.noifityWXin(wxp, b);
    } catch (Exception e) {
      log.error("weixinNotify-error:{}", e);
    } finally {
      wxPayResult.setReturn_code(ResourcesUtil.SUCCESS);
    }
    return wxPayResult;
  }

  @ApiIgnore
  @PostMapping("/lianlian/orderProduct")
  public LLResult llOrderProduct(@RequestBody LLNotifyParam param) {
    log.info("==============================>>实物商品订单=》连连异步通知结果：" + param);
    try {
      boolean b =
          checkSignRSA(
              JSONObject.parseObject(JSONObject.toJSONString(param)),
              ResourcesUtil.getProperty("lianlian_public_key"));

      if (b) {
        log.info("实物商品订单=》连连异步通知 验签正确");
        orderProductService.noifitylianlian(param);
      } else {
        log.info("实物商品订单=》连连异步通知 验签错误！！！！！！");
      }
    } catch (Exception e) {
      log.error("weixinNotify-error:{}", e);
    }
    return new LLResult();
  }

  @ApiIgnore
  @PostMapping("/alipay/orderProduct")
  public void alipayNotify(HttpServletRequest request, HttpServletResponse response) {
    log.info("==============================>>实物商品订单=》支付宝的异步通知结果：" + request);
    try {
      if (null != request) {
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
          String name = (String) iter.next();
          String[] values = (String[]) requestParams.get(name);
          String valueStr = "";
          for (int i = 0; i < values.length; i++) {
            valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
          }
          // 乱码解决，这段代码在出现乱码时使用。
          // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
          params.put(name, valueStr);
        }
        // 切记alipayPublicCertPath是支付宝公钥证书路径，请去open.alipay.com对应应用下载。
        // boolean AlipaySignature.rsaCertCheckV1(Map<String, String> params, String
        // publicKeyCertPath, String charset,String signType)
        String tradeStatus = (String) params.get("trade_status");
        boolean signVerified =
            AlipaySignature.rsaCertCheckV1(
                params, "/nginx/cert/alipayCertPublicKey_RSA2.crt", "UTF-8", "RSA2");
        if (signVerified && "TRADE_SUCCESS".equals(tradeStatus)) {
          // 验证成功
          String sellerId =
              new String(
                  request.getParameter("seller_id").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          // 商户订单号
          String outtradeno =
              new String(
                  request.getParameter("out_trade_no").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          // 第三方号
          String tradeNo =
              new String(
                  request.getParameter("trade_no").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          log.info("支付宝订单Id" + outtradeno);
          orderProductService.noifityAlipay(outtradeno, tradeNo);
        }
      }
    } catch (Exception e) {
      log.error("alipayNotify-error:{}", e);
    } finally {
      try {
        response.getWriter().write("success");
      } catch (IOException e) {
        log.error("IOException-error:{}", e);
      }
    }
  }

  @ApiIgnore
  @PostMapping("/weixin/vipOrder")
  public WXPayResult weixinVipOrderNotify(@RequestBody WXNotifyParam wxp) {
    WXPayResult wxPayResult = new WXPayResult();
    log.info("==============================>>VIP订单=》微信异步通知结果：" + wxp);
    Map<String, String> xmlDate = null;
    try {
      xmlDate = WXPayUtil.convertBean(wxp);
      log.info("convertBean-info:{} ", xmlDate);
      boolean b = WXPayUtil.isSignatureValid(xmlDate, ResourcesUtil.WX_API_KEY);
      orderVipService.noifityWXin(wxp, b);
    } catch (Exception e) {
      log.error("weixinNotify-error:{}", e);
    } finally {
      wxPayResult.setReturn_code(ResourcesUtil.SUCCESS);
    }
    return wxPayResult;
  }

  @ApiIgnore
  @PostMapping("/lianlian/vipOrder")
  public LLResult llvipOrder(@RequestBody LLNotifyParam param) {
    log.info("==============================>>VIP订单=》连连异步通知结果：" + param);
    try {
      boolean b =
          checkSignRSA(
              JSONObject.parseObject(JSONObject.toJSONString(param)),
              ResourcesUtil.getProperty("lianlian_public_key"));

      if (b) {
        log.info("VIP订单=》连连异步通知 验签正确");
        orderVipService.noifitylianlian(param);
      } else {
        log.info("VIP订单=》连连异步通知 验签错误！！！！！！");
      }
    } catch (Exception e) {
      log.error("weixinNotify-error:{}", e);
    }
    return new LLResult();
  }

  @ApiIgnore
  @PostMapping("/alipay/vipOrder")
  public void alipayVipOrderNotify(ServletRequest request, HttpServletResponse response) {
    log.info("==============================>>VIP订单=》支付宝的异步通知结果：" + request);
    try {
      if (null != request) {
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
          String name = (String) iter.next();
          String[] values = (String[]) requestParams.get(name);
          String valueStr = "";
          for (int i = 0; i < values.length; i++) {
            valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
          }
          // 乱码解决，这段代码在出现乱码时使用。
          // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
          params.put(name, valueStr);
        }
        // 切记alipayPublicCertPath是支付宝公钥证书路径，请去open.alipay.com对应应用下载。
        // boolean AlipaySignature.rsaCertCheckV1(Map<String, String> params, String
        // publicKeyCertPath, String charset,String signType)
        String tradeStatus = (String) params.get("trade_status");
        boolean signVerified =
            AlipaySignature.rsaCertCheckV1(
                params, "/nginx/cert/alipayCertPublicKey_RSA2.crt", "UTF-8", "RSA2");
        if (signVerified && "TRADE_SUCCESS".equals(tradeStatus)) {
          // 验证成功
          String sellerId =
              new String(
                  request.getParameter("seller_id").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          // 商户订单号
          String outtradeno =
              new String(
                  request.getParameter("out_trade_no").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          // 第三方号
          String tradeNo =
              new String(
                  request.getParameter("trade_no").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          log.info("支付宝订单Id" + outtradeno);
          orderVipService.noifityAlipay(outtradeno, tradeNo);
        }
      }
    } catch (Exception e) {
      log.error("alipayNotify-error:{}", e);
    } finally {
      try {
        response.getWriter().write("success");
      } catch (IOException e) {
        log.error("IOException-error:{}", e);
      }
    }
  }

  @ApiIgnore
  @PostMapping("/weixin/orderTreasure")
  public WXPayResult weixinOrderTreasureNotify(@RequestBody WXNotifyParam wxp) {
    WXPayResult wxPayResult = new WXPayResult();
    log.info("==============================>>藏品订单=》微信异步通知结果：" + wxp);
    Map<String, String> xmlDate = null;
    try {
      xmlDate = WXPayUtil.convertBean(wxp);
      log.info("convertBean-info:{} ", xmlDate);
      boolean b = WXPayUtil.isSignatureValid(xmlDate, ResourcesUtil.WX_API_KEY);
      orderTreasurePoolService.noifityWXin(wxp, b);
    } catch (Exception e) {
      log.error("weixinNotify-error:{}", e);
    } finally {
      wxPayResult.setReturn_code(ResourcesUtil.SUCCESS);
    }
    return wxPayResult;
  }

  @ApiIgnore
  @PostMapping("/sd/orderTreasure")
  public String llorderTreasure(HttpServletRequest request) {
    log.info("充值成功回调：");
    String signature = request.getHeader("Signature-Data");
    BufferedReader reader = null;
    try {
      // 从请求体中获取源串
      reader =
          new BufferedReader(
              new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
      String line;
      StringBuilder stringBuilder = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
      }
      log.info("[接收来自连连下发的异步通知] 签名值为：" + signature);
      log.info("[接收来自连连下发的异步通知] 签名源串为：" + stringBuilder.toString());

      // 进行验签
      if (LLianPayAccpSignature.getInstance().checkSign(stringBuilder.toString(), signature)) {
        //      if (true) {
        log.info("藏品订单=》SD异步通知 验签正确,请求参数：" + stringBuilder.toString());
        JSONObject data = JSONObject.parseObject(stringBuilder.toString());
        String orderCode = data.getJSONObject("orderInfo").getString("txn_seqno");
        String tradeNo = data.getString("accp_txno");
        SdNotifyBody p = new SdNotifyBody();
        p.setOrderCode(orderCode);
        p.setTradeNo(tradeNo);
        String result = orderTreasurePoolService.noifitySD(p);
        return result;
      } else {
        // 验签失败，进行预警。
        log.error("验签失败！！！");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }
    }
    // 没有其他意义，异步通知响应连连这边只认"Success"，返回非"Success"，连连会进行重发
    return "error";
  }
  //  @ApiIgnore
  //  @PostMapping("/sd/orderTreasure")
  //  public String llorderTreasure(@RequestParam Map<String, String> params) {
  //    String param = JSONObject.toJSONString(params);
  //    log.info("==============================>>藏品订单=》SD异步通知结果：" + param);
  //    log.info("==============================>>藏品订单=》SD异步通知结果：" + param);
  //    log.info("==============================>>藏品订单=》SD异步通知结果：" + param);
  //
  //    try {
  //      log.info("SdNotifyBody: " + params.get("data"));
  //      boolean check = YanQian.check(params.get("data"), params.get("sign"));
  //      log.info("验签结果：" + check);
  //      log.info("验签结果：" + check);
  //      log.info("验签结果：" + check);
  //      log.info("验签结果：" + check);
  //      log.info("验签结果：" + check);
  //
  //      if (check) {
  //        log.info("藏品订单=》SD异步通知 验签正确");
  //        JSONObject data = JSONObject.parseObject(params.get("data"));
  //        JSONObject body = data.getJSONObject("body");
  //        String orderCode = body.getString("orderCode");
  //        String tradeNo = body.getString("tradeNo");
  //        SdNotifyBody p = new SdNotifyBody();
  //        p.setOrderCode(orderCode);
  //        p.setTradeNo(tradeNo);
  //        String result = orderTreasurePoolService.noifitySD(p);
  //        return result;
  //      } else {
  //        log.info("藏品订单=》SD异步通知 验签错误！！！！！！");
  //      }
  //    } catch (Exception e) {
  //      e.printStackTrace();
  //      log.error(e.getMessage());
  //    }
  //    return "respCode=000000";
  //  }

  @ApiIgnore
  @PostMapping("/alipay/orderTreasure")
  public void alipayOrderTreasureNotify(ServletRequest request, HttpServletResponse response) {
    log.info("==============================>>藏品订单=》支付宝的异步通知结果：" + request);
    try {
      if (null != request) {
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
          String name = (String) iter.next();
          String[] values = (String[]) requestParams.get(name);
          String valueStr = "";
          for (int i = 0; i < values.length; i++) {
            valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
          }
          // 乱码解决，这段代码在出现乱码时使用。
          // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
          params.put(name, valueStr);
        }
        // 切记alipayPublicCertPath是支付宝公钥证书路径，请去open.alipay.com对应应用下载。
        // boolean AlipaySignature.rsaCertCheckV1(Map<String, String> params, String
        // publicKeyCertPath, String charset,String signType)
        String tradeStatus = (String) params.get("trade_status");
        boolean signVerified =
            AlipaySignature.rsaCertCheckV1(
                params, "/nginx/cert/alipayCertPublicKey_RSA2.crt", "UTF-8", "RSA2");
        if (signVerified && "TRADE_SUCCESS".equals(tradeStatus)) {
          // 验证成功
          String sellerId =
              new String(
                  request.getParameter("seller_id").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          // 商户订单号
          String outtradeno =
              new String(
                  request.getParameter("out_trade_no").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          // 第三方号
          String tradeNo =
              new String(
                  request.getParameter("trade_no").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          log.info("支付宝订单Id" + outtradeno);
          orderTreasurePoolService.noifityAlipay(outtradeno, tradeNo);
        }
      }
    } catch (Exception e) {
      log.error("alipayNotify-error:{}", e);
    } finally {
      try {
        response.getWriter().write("success");
      } catch (IOException e) {
        log.error("IOException-error:{}", e);
      }
    }
  }

  @ApiIgnore
  @PostMapping("/weixin/recharge")
  public WXPayResult weixinRecharge(@RequestBody WXNotifyParam wxp) {
    WXPayResult wxPayResult = new WXPayResult();
    log.info("==============================>>微信-充值-异步通知结果：" + wxp);
    Map<String, String> xmlDate = null;
    try {
      xmlDate = WXPayUtil.convertBean(wxp);
      log.info("recharge-info:{} ", xmlDate);
      boolean b = WXPayUtil.isSignatureValid(xmlDate, ResourcesUtil.WX_API_KEY);
      orderTreasurePoolService.weixinRecharge(wxp, b);
    } catch (Exception e) {
      log.error("weixinNotify-error:{}", e);
    } finally {
      wxPayResult.setReturn_code(ResourcesUtil.SUCCESS);
    }
    return wxPayResult;
  }

  @ApiIgnore
  @PostMapping("/sd/recharge")
  public String llrecharge(HttpServletRequest request) {
    log.info("充值成功回调：");
    String signature = request.getHeader("Signature-Data");
    BufferedReader reader = null;
    try {
      // 从请求体中获取源串
      reader =
          new BufferedReader(
              new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
      String line;
      StringBuilder stringBuilder = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
      }
      log.info("[接收来自连连下发的异步通知] 签名值为：" + signature);
      log.info("[接收来自连连下发的异步通知] 签名源串为：" + stringBuilder.toString());

      // 进行验签
      if (LLianPayAccpSignature.getInstance().checkSign(stringBuilder.toString(), signature)) {
        // 验签通过，处理系统业务逻辑
        log.info("验签通过！！！");
        log.info("藏品订单=》SD异步通知 验签正确");
        log.info("藏品订单=》SD异步通知 验签正确,请求参数：" + stringBuilder.toString());
        JSONObject data = JSONObject.parseObject(stringBuilder.toString());
        String orderCode = data.getJSONObject("orderInfo").getString("txn_seqno");
        String tradeNo = data.getString("accp_txno");
        SdNotifyBody p = new SdNotifyBody();
        p.setOrderCode(orderCode);
        p.setTradeNo(tradeNo);
        orderTreasurePoolService.sdRecharge(p);
        // 返回Success，响应本次异步通知已经成功
        return "Success";
      } else {
        // 验签失败，进行预警。
        log.error("验签失败！！！");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }
    }
    // 没有其他意义，异步通知响应连连这边只认"Success"，返回非"Success"，连连会进行重发
    return "error";
  }

  @ApiIgnore
  @PostMapping("/alipay/recharge")
  public void alipayRecharge(ServletRequest request, HttpServletResponse response) {
    log.info("==============================>>支付宝充值=》的异步通知结果：" + request);
    try {
      if (null != request) {
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
          String name = (String) iter.next();
          String[] values = (String[]) requestParams.get(name);
          String valueStr = "";
          for (int i = 0; i < values.length; i++) {
            valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
          }
          // 乱码解决，这段代码在出现乱码时使用。
          // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
          params.put(name, valueStr);
        }
        // 切记alipayPublicCertPath是支付宝公钥证书路径，请去open.alipay.com对应应用下载。
        // boolean AlipaySignature.rsaCertCheckV1(Map<String, String> params, String
        // publicKeyCertPath, String charset,String signType)
        String tradeStatus = (String) params.get("trade_status");
        boolean signVerified =
            AlipaySignature.rsaCertCheckV1(
                params, "/nginx/cert/alipayCertPublicKey_RSA2.crt", "UTF-8", "RSA2");
        if (signVerified && "TRADE_SUCCESS".equals(tradeStatus)) {
          // 验证成功
          String sellerId =
              new String(
                  request.getParameter("seller_id").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          // 商户订单号
          String outtradeno =
              new String(
                  request.getParameter("out_trade_no").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          // 第三方号
          String tradeNo =
              new String(
                  request.getParameter("trade_no").getBytes(ResourcesUtil.ALICHART3),
                  ResourcesUtil.ALICHART);
          log.info("支付宝订单Id" + outtradeno);
          orderTreasurePoolService.alipayRecharge(outtradeno, tradeNo);
        }
      }
    } catch (Exception e) {
      log.error("alipayNotify-error:{}", e);
    } finally {
      try {
        response.getWriter().write("success");
      } catch (IOException e) {
        log.error("IOException-error:{}", e);
      }
    }
  }

  private boolean checkSignRSA(JSONObject reqObj, String rsa_public) {
    if (reqObj == null) {
      return false;
    }
    String sign = reqObj.getString("sign");
    String sign_src = orderTreasurePoolService.genSign(reqObj);
    log.info("验证签名- 请求签名：\n" + sign);
    log.info("验证签名- 生成签名：\n" + sign_src);

    try {
      if (TraderRSAUtil.checksign(rsa_public, sign_src, sign)) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }

  @ApiIgnore
  @PostMapping("/lianlian/orderProductBack")
  public LLResult orderProductBack(@RequestBody LLOrderBackNotifyParam param) {
    log.info("==============================>>连连商品支付退款=》连连异步通知结果：" + param);
    try {
      boolean b =
          checkSignRSA(
              JSONObject.parseObject(JSONObject.toJSONString(param)),
              ResourcesUtil.getProperty("lianlian_public_key"));

      if (b) {
        log.info("连连商品支付退款=》连连异步通知 验签正确");
        orderProductService.orderProductBack(param);
      } else {
        log.info("连连商品支付退款=》连连异步通知 验签错误！！！！！！");
      }
    } catch (Exception e) {
      log.error("weixinNotify-error:{}", e);
    }
    return new LLResult();
  }
}
