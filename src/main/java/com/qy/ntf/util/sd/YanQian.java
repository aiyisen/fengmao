package com.qy.ntf.util.sd;

// 测试签名验签

import com.qy.ntf.util.sd.moudle.CertUtil;
import com.qy.ntf.util.sd.moudle.CryptoUtil;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

public class YanQian {

  // 默认配置的是UTF-8
  public static String encoding = "UTF-8";

  public static PublicKey getPublicKey(String module, String exponent) throws Exception {
    BigInteger e1 = new BigInteger(exponent);
    BigInteger n = new BigInteger(module, 16);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(n, e1);
    PublicKey key = keyFactory.generatePublic(pubSpec);
    return key;
  }

  public static void main(String[] args) {
    boolean check = check(null, null);
    System.out.println(check);
  }

  public static boolean check(String reqStr, String reqSign) {
    // 加载配置文件

    // 加载证书
    try {

      CertUtil.init("/nginx/sd/sand.cer", "/nginx/sd/prikey.pfx", "131230");
      //      CertUtil.init("E:\\sand.cer", "E:\\prikey.pfx", "131230");
      //      reqStr =
      //
      // "{\"head\":{\"version\":\"1.0\",\"respTime\":\"20220928202314\",\"respCode\":\"000000\",\"respMsg\":\"成功\"},\"body\":{\"mid\":\"6888800117061\",\"orderCode\":\"e63de9a8c0854717b850d7647a1d202b\",\"tradeNo\":\"e63de9a8c0854717b850d7647a1d202b\",\"clearDate\":\"20220928\",\"totalAmount\":\"000000000020\",\"orderStatus\":\"1\",\"payTime\":\"20220928202312\",\"settleAmount\":\"000000000020\",\"buyerPayAmount\":\"000000000020\",\"discAmount\":\"000000000000\",\"txnCompleteTime\":\"20220928202313\",\"payOrderCode\":\"0928j10000034404\",\"accLogonNo\":\"623081******8247\",\"accNo\":\"623081******8247\",\"midFee\":\"000000000010\",\"extraFee\":\"000000000000\",\"specialFee\":\"000000000000\",\"plMidFee\":\"000000000000\",\"bankserial\":\"\",\"externalProductCode\":\"00000016\",\"cardNo\":\"623081******8247\",\"creditFlag\":\"\",\"bid\":\"SDSMP00688880011706120220928071411991956\",\"benefitAmount\":\"000000000000\",\"remittanceCode\":\"\",\"extend\":\"\"}}";
      //      reqSign =
      //
      // "aR0hmnamGsJcE/zMGVYQ/1gELToQNXHf8+YlCU67Bm/FxBcHBlIQe3LrL1Xzr5UBexB2fBEa3aeONRKrbGovheI7MLjoosgfQTkrHxwhkn9OxvkTWuPa/yxumNcf35gfxY+oBkx4B9usqfjAwVOWJpQmHS5HiNXDXxlJNyEUOx3Hj3+0hQdICw5WSK6Bc2KFh9uhGwOICX8uDy+2kWxQevJGExZ6NckwW66TmQoU2t2KJ8laTNa5GnPOrE8e/qvDArt16CsPdetViSIjVrNZHEYpbj+AWJIqU0Ji+jpNj7oEGfJP2/lNN3Uwzvd8YCHKJxeWscd7DYCg7fpyOKTr1g==";

      System.out.println("date数据:" + reqStr);
      System.out.println("sign数据:" + reqSign);
      boolean valid =
          CryptoUtil.verifyDigitalSign(
              reqStr.getBytes(encoding),
              Base64.decodeBase64(reqSign),
              CertUtil.getPublicKey(),
              "SHA1WithRSA");
      if (!valid) {
        return false;
      } else {
        return true;
      }

    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }
}
