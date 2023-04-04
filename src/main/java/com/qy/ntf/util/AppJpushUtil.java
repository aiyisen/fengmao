package com.qy.ntf.util;

import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class AppJpushUtil {

  private static final String APP_KEY = ResourcesUtil.getProperty("jPush.key");
  private static final String MASTER_SECRET = ResourcesUtil.getProperty("jPush.secret");
  private static final AppJpushUtil INSTANCE = new AppJpushUtil();

  private AppJpushUtil() {}

  public static void main(String[] arg) {
    System.out.println(APP_KEY);
    System.out.println(MASTER_SECRET);
    sendPush(Collections.singletonList(1L), "title0", "content0");
  }

  public static void sendPush(List<Long> param, String title, String content) {
    //    List<String> ids = param.stream().map(o -> o + "").collect(Collectors.toList());
    //    ClientConfig clientConfig = ClientConfig.getInstance();
    //    final JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null,
    // clientConfig);
    //    final PushPayload payload = buildPushObject_android_and_ios();
    //
    //    try {
    //      PushResult result = jpushClient.sendPush(payload);
    //      log.info("Got result - " + result);
    //      System.out.println(result);
    //    } catch (APIConnectionException e) {
    //      log.error("Connection error. Should retry later. ", e);
    //      log.error("Sendno: " + payload.getSendno());
    //
    //    } catch (APIRequestException e) {
    //      log.error("Error response from JPush server. Should review and fix it. ", e);
    //      log.info("HTTP Status: " + e.getStatus());
    //      log.info("Error Code: " + e.getErrorCode());
    //      log.info("Error Message: " + e.getErrorMessage());
    //      log.info("Msg ID: " + e.getMsgId());
    //      log.error("Sendno: " + payload.getSendno());
    //    }
  }
  /**
   * Could modify the contents for pushing The comments are showing how to use it
   *
   * @return
   */
  public static PushPayload buildPushObject_android_and_ios() {
    Map<String, String> extras = new HashMap<String, String>();
    extras.put("test", "https://community.jiguang.cn/push");
    // you can set anything you want in this builder, read the document to avoid collision.
    return PushPayload.newBuilder()
        .setPlatform(Platform.android_ios())
        .setAudience(Audience.all())
        //                .setMessage(Message.newBuilder()
        //                        .setMsgContent("Hi, JPush")
        //                        .build())
        .setNotification(
            Notification.newBuilder()
                .setAlert("testing alert content")
                .addPlatformNotification(
                    AndroidNotification.newBuilder()
                        .setTitle("Android Title")
                        .addExtras(extras)
                        .build())
                .addPlatformNotification(
                    IosNotification.newBuilder()
                        .incrBadge(1)
                        .addExtra("extra_key", "extra_value")
                        .build())
                .build())
        //                .setSMS(SMS.newBuilder()
        //                        .setDelayTime(1000)
        //                        .setTempID(2000)
        //                        .addPara("Test", 1)
        //                        .setActiveFilter(true)
        //                        .build())
        //                .setNotification3rd(Notification3rd.newBuilder()
        //                        .setContent("Hi, JPush")
        //                        .setTitle("msg testing")
        //                        .setChannelId("channel1001")
        //                        .setUriActivity("cn.jpush.android.ui.OpenClickActivity")
        //                        .setUriAction("cn.jpush.android.intent.CONNECTION")
        //                        .setBadgeAddNum(1)
        //                        .setBadgeClass("com.test.badge.MainActivity")
        //                        .setSound("sound")
        //                        .addExtra("news_id", 124)
        //                        .addExtra("my_key", "a value")
        //                        .build())
        .setOptions(Options.newBuilder().setApnsProduction(false).setTimeToLive(43200).build())
        .build();
  }
}
