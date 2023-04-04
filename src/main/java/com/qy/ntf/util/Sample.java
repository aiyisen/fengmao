package com.qy.ntf.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

public class Sample {

  /**
   * 使用AK&SK初始化账号Client
   *
   * @param accessKeyId
   * @param accessKeySecret
   * @return Client
   * @throws Exception
   */
  public static com.aliyun.dysmsapi20170525.Client createClient(
      String accessKeyId, String accessKeySecret) throws Exception {
    Config config =
        new Config()
            // 您的 AccessKey ID
            .setAccessKeyId(accessKeyId)
            // 您的 AccessKey Secret
            .setAccessKeySecret(accessKeySecret);
    // 访问的域名
    config.endpoint = "dysmsapi.aliyuncs.com";
    return new com.aliyun.dysmsapi20170525.Client(config);
  }

  public void sendVerifyCode(String phone, String code) {
    try {
      com.aliyun.dysmsapi20170525.Client client =
          Sample.createClient(
              ResourcesUtil.getProperty("ali_access_key_id"),
              ResourcesUtil.getProperty("ali_access_key_secret"));
      SendSmsRequest sendSmsRequest = new SendSmsRequest();
      sendSmsRequest.setPhoneNumbers(phone);
      sendSmsRequest.setSignName("疯猫科技");
      sendSmsRequest.setTemplateCode("SMS_263410210");
      sendSmsRequest.setTemplateParam("{\"code\":" + code + "}");

      RuntimeOptions runtime = new RuntimeOptions();

      // 复制代码运行请自行打印 API 的返回值
      SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
      System.out.println(JSONObject.toJSONString(sendSmsResponse));
    } catch (TeaException error) {
      // 如有需要，请打印 error
      com.aliyun.teautil.Common.assertAsString(error.message);
    } catch (Exception _error) {
      TeaException error = new TeaException(_error.getMessage(), _error);
      // 如有需要，请打印 error
      com.aliyun.teautil.Common.assertAsString(error.message);
    }
  }
}
