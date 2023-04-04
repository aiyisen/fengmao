package com.qy.ntf.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@Slf4j
public class PayUtils {
  public static String yuanToFen(BigDecimal money) {
    try {
      BigDecimal bigDecimal = money.multiply(new BigDecimal(100));
      String fee = bigDecimal.toString();
      if (fee.indexOf(".") != -1) {
        fee = fee.substring(0, fee.indexOf("."));
      }
      return fee;
    } catch (NumberFormatException e) {
      log.error("【CommonUtils】yuanToFen数据转换异常");
      return "0";
    }
  }

  public static String getIp() {
    try {
      InetAddress candidateAddress = null;
      // 遍历所有的网络接口
      for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces();
          ifaces.hasMoreElements(); ) {
        NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
        // 在所有的接口下再遍历IP
        for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
          InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
          if (!inetAddr.isLoopbackAddress()) { // 排除loopback类型地址
            if (inetAddr.isSiteLocalAddress()) {
              // 如果是site-local地址，就是它了
              return inetAddr.getHostAddress();
            } else if (candidateAddress == null) {
              // site-local类型的地址未被发现，先记录候选地址
              candidateAddress = inetAddr;
            }
          }
        }
      }
      if (candidateAddress != null) {
        return candidateAddress.getHostAddress();
      }
      // 如果没有发现 non-loopback地址.只能用最次选的方案
      InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
      return jdkSuppliedAddress.getHostAddress();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
