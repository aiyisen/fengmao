package com.qy.ntf;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lianpay.share.security.LianLianPaySecurity;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.entity.OrderTreasurePool;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.param.BankcardBindRequestBean;
import com.qy.ntf.dao.OrderTreasurePoolDao;
import com.qy.ntf.dao.SysUserDao;
import com.qy.ntf.service.OrderProductService;
import com.qy.ntf.service.OrderTreasurePoolService;
import com.qy.ntf.util.BsnUtil;
import com.qy.ntf.util.ResourcesUtil;
import com.qy.ntf.util.SHA256withRSAUtil;
import com.reddate.wuhanddc.dto.ddc.Account;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NtfApplication.class)
public class AbstractToolkitApplicationTests {
  @Autowired private OrderTreasurePoolService orderTreasurePoolService;

  public void createOrder() {}

  public String getTimeStamp() {
    DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    Date date = new Date();
    return df.format(date);
  }

  @Test
  public void fun23() {
    //    String orderF = "bbc985a1bcac42baa8b454977f97cd41";
    //    String thridNo = "bbc985a1bcac42baa8b454977f97cd41";
    //    orderTreasurePoolService.updateOrderByNotify(orderF, thridNo, 1);
    //    WenchangDDC bsnUtil = new WenchangDDC();
    //    bsnUtil.userAddGas("0xF37B6BDEC1F6EE83F160F5D86772399A2967F58D", sysUser);
    //    WenchangDDC bsnUtil = new WenchangDDC();
    //    com.qy.ntf.util.wenchang.dto.Account count = bsnUtil.createCount();
    //    String resCode = bsnUtil.create("123456789", count);
    //    bsnUtil.checkAccountCreateStatus(resCode);
    //    System.out.println(JSONObject.toJSONString(count));
    //    System.out.println("0x" + bsnUtil.accountBech32ToHex(count.getAddress()));
    //    System.out.println(count.getPublicKey());
    //    System.out.println(count.getPrivateKey());
    //    System.out.println(count.getKeyseed());
  }

  @Test
  public void bankcardbind() {
    BankcardBindRequestBean bean = new BankcardBindRequestBean();
    bean.setAcct_name("王振读");
    bean.setCard_no("6217732906746734");
    bean.setBind_mob("15140088201");
    bean.setBusi_partner("109001");
    bean.setDt_order(getTimeStamp());
    bean.setNo_order(getTimeStamp());
    bean.setId_type("0");
    bean.setId_no("211321199510208653");
    bean.setUser_id("130");
    bean.setNotify_url("http://test");
    bean.setPay_type("D");
    bean.setSign_type("RSA");
    bean.setOid_partner(ResourcesUtil.getProperty("lianlian_oid"));
    bean.setSign(
        orderTreasurePoolService.genSign(JSONObject.parseObject(JSONObject.toJSONString(bean))));

    try {
      String encryptStr =
          LianLianPaySecurity.encrypt(
              JSONObject.toJSONString(bean), ResourcesUtil.getProperty("lianlian_YT_public_key"));
      JSONObject json = new JSONObject();
      json.put("oid_partner", ResourcesUtil.getProperty("lianlian_oid"));
      json.put("pay_load", encryptStr);
      System.out.println("请求签约参数：" + json.toJSONString());
      String res =
          doPost("https://test.lianlianpay-inc.com/mpayapi/v1/bankcardbind", json, "UTF-8");

      JSONObject jsonRs = JSONObject.parseObject(res);

      boolean checkSign =
          SHA256withRSAUtil.getInstance()
              .checksign(
                  ResourcesUtil.getProperty("lianlian_public_key"),
                  genSignData(jsonRs),
                  jsonRs.getString("sign"));
      System.out.println("签名验证结果为:" + checkSign);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String genSignData(JSONObject jsonObject) {
    StringBuffer content = new StringBuffer();
    List<String> keys = new ArrayList<String>(jsonObject.keySet());
    Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
    for (int i = 0; i < keys.size(); i++) {
      String key = keys.get(i);
      if ("sign".equals(key)) {
        continue;
      }
      String value = jsonObject.getString(key);
      if (StringUtils.isBlank(value)) {
        continue;
      }
      content.append((i == 0 ? "" : "&") + key + "=" + value);
    }
    String signSrc = content.toString();
    if (signSrc.startsWith("&")) {
      signSrc = signSrc.replaceFirst("&", "");
    }
    return signSrc;
  }

  public static String doPost(String url, JSONObject json, String charset) {
    return doPost(url, json.toJSONString(), charset);
  }

  public static String doPost(String url, String jsonStr, String charset) {
    HttpClient httpClient = null;
    HttpPost httpPost = null;
    String result = null;
    try {
      httpClient = new DefaultHttpClient();
      httpPost = new HttpPost(url);
      System.out.println(jsonStr);
      // 设置参数
      StringEntity s = new StringEntity(jsonStr);
      s.setContentEncoding("UTF-8");
      s.setContentType("application/json");
      httpPost.setEntity(s);
      HttpResponse response = httpClient.execute(httpPost);
      if (response != null) {
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
          result = EntityUtils.toString(resEntity, charset);
        }
        System.out.println("response body:" + result);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }
  // 申购抽奖
  @Test
  public void fun() throws Exception {
    orderTreasurePoolService.checkTreasure(209L);
  }

  @Test
  public void fun3() throws Exception {
    ArrayList<String> objects = new ArrayList<>();
    objects.add("150");
    objects.add("151");
    objects.add("157");
    objects.add("158");
    objects.add("159");
    for (int i = 0; i < objects.size(); i++) {
      RBlockingQueue<Object> blockingFairQueue =
          redissonClient.getBlockingQueue("delay_queue_call");
      RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
      delayedQueue.offer("blodJoin_" + objects.get(i), 15 + i * 2L, TimeUnit.SECONDS);
    }
  }

  @Test
  public void fun2() throws Exception {
    orderTreasurePoolService.orderOutDate(220L);
  }

  @Autowired private SysUserDao sysUserDao;
  // 纠正所有用户链账户地址
  @Test
  public void checkAllUserAddress() {
    List<SysUser> sysUsers = sysUserDao.selectList(Wrappers.lambdaQuery(SysUser.class));
    BsnUtil bsnUtil = new BsnUtil();
    for (SysUser sysUser : sysUsers) {
      try {
        bsnUtil.checkUserAddress(sysUser);
      } catch (Exception e) {
        System.out.println(sysUser.getPhone() + " 用户地址异常 重新生成");
        Account user = bsnUtil.createUser(sysUser.getPhone() + 1);
        sysUser.setLinkAddress(user.getAddress());
        sysUser.setPublicKey(user.getPublicKey());
        sysUser.setPrivateKey(user.getPrivateKey());
        sysUser.setMnemonic(user.getMnemonic());
        sysUserDao.updateById(sysUser);
        try {
          Thread.sleep(500);
        } catch (InterruptedException ex) {
          throw new RuntimeException(ex);
        }
      }
    }
    System.out.println("已完成");
  }

  @Test
  public void updateDDcId() throws Exception {
    // 0xf99376bba1bb194e29b3a7f58b57e021f28e0dee69e10ee979f2c3a4138e2fbf 交易地址、
    // sysBackId
    orderTreasurePoolService.executeAsync(
        "0xf99376bba1bb194e29b3a7f58b57e021f28e0dee69e10ee979f2c3a4138e2fbf",
        193L,
        "https://xskymeta.com/nft/0xf99376bba1bb194e29b3a7f58b57e021f28e0dee69e10ee979f2c3a4138e2fbf");
  }

  @Autowired private RedissonClient redissonClient;

  @Test
  public void redisson() {
    RBlockingQueue<Object> blockingFairQueue = redissonClient.getBlockingQueue("delay_queue_call");
    RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
    delayedQueue.offer("测试中文乱码不" + "adsfasdfasdf" + 6666, 65, TimeUnit.SECONDS);
    System.out.println(delayedQueue.stream().count());
    System.out.println(JSONObject.toJSONString(delayedQueue));
  }

  @Autowired private OrderProductService orderProductService;

  @Test
  public void orderOutDate() {
    orderProductService.orderOutDate(504L);
  }

  @Autowired private OrderTreasurePoolDao orderTreasurePoolDao;
  // 清理重复手机号用户
  @Test
  public void cleanUser() {
    List<SysUser> allNeedCleanUser = sysUserDao.getUnInvalidUser();
    List<Long> needDele = new ArrayList<>();
    LambdaQueryWrapper<OrderTreasurePool> que = new LambdaQueryWrapper<>();
    List<OrderTreasurePool> orderTreasurePools = orderTreasurePoolDao.selectList(que);
    Set<Long> createOrderUserIds =
        orderTreasurePools.stream().map(BaseEntity::getCreateId).collect(Collectors.toSet());
    for (SysUser sysUser : allNeedCleanUser) {
      // 指定手机号重复的所有用户
      List<SysUser> itemUsers = sysUserDao.selectByPhone(sysUser.getPhone());
      if (itemUsers.stream().filter(o -> createOrderUserIds.contains(o.getId())).count() > 0) {
        itemUsers =
            itemUsers.stream()
                .filter(o -> !createOrderUserIds.contains(o.getCreateId()))
                .collect(Collectors.toList());
      } else {
        itemUsers.remove(0);
      }
      for (SysUser itemUser : itemUsers) {
        if (itemUser.getUsername().equals("新用户")) {
          needDele.add(itemUser.getId());
        }
      }
    }
    sysUserDao.deleteBatchIds(needDele);
    System.out.println("删除用户id: " + Strings.join(needDele, ','));
  }
}
