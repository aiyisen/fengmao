package com.qy.ntf;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.teaopenapi.models.Config;
import com.qy.ntf.util.AvataUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NtfApplication.class)
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

  public static void main(String[] args) {
    String url = "https://apis.avata.bianjie.ai/v1beta1/account";
    String path = "/v1beta1/account";
    Map<String, Object> query = new HashMap<>();
    JSONObject body = new JSONObject();
    body.put("name", "震度2");
    body.put("operation_id", "asd2");
    String apiSecret = "M2d2s1c2A182l1F24164H0Z0a7n0L8pc";
    long time = System.currentTimeMillis();
    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("Content-Type", "application/json");
    headerMap.put("X-Api-Key", "A2A2A1R2o1g2J1j2U1X4z0e0G780f886");
    headerMap.put("X-Timestamp", time + "");
    headerMap.put("X-Signature", AvataUtils.signRequest(path, query, body, time, apiSecret));

    String result =
        HttpRequest.post(url).addHeaders(headerMap).body(body.toJSONString()).execute().body();
    JSONObject jsonObject = JSONObject.parseObject(result);
    if (jsonObject.get("data") == null) {}

    System.out.println(result);
  }

  //  public static void main(String[] args) {
  //    try {
  //      com.aliyun.dysmsapi20170525.Client client =
  //          Sample.createClient("LTAI5tJ38NFEpoEPuPZHZh9B", "MXSlQKWWTaDwB45pF5CB2wG0ZFL5TQ");
  //      SendSmsRequest sendSmsRequest = new SendSmsRequest();
  //      sendSmsRequest.setPhoneNumbers("18509862060");
  //      sendSmsRequest.setSignName("疯猫科技");
  //      sendSmsRequest.setTemplateCode("SMS_263410210");
  //      sendSmsRequest.setTemplateParam("{\"code\":" + 1234 + "}");
  //
  //      RuntimeOptions runtime = new RuntimeOptions();
  //
  //      // 复制代码运行请自行打印 API 的返回值
  //      SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
  //      System.out.println(JSONObject.toJSONString(sendSmsResponse));
  //    } catch (TeaException error) {
  //      // 如有需要，请打印 error
  //      com.aliyun.teautil.Common.assertAsString(error.message);
  //    } catch (Exception _error) {
  //      TeaException error = new TeaException(_error.getMessage(), _error);
  //      // 如有需要，请打印 error
  //      com.aliyun.teautil.Common.assertAsString(error.message);
  //    }
  //  }
}
