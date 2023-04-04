package com.qy.ntf.util;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AvataUtil {
  public static String createUser(String name, String operationId) {
    String url = "https://apis.avata.bianjie.ai/v1beta1/account";
    String path = "/v1beta1/account";
    Map<String, Object> query = new HashMap<>();
    JSONObject body = new JSONObject();
    body.put("name", name);
    body.put("operation_id", operationId);
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
    System.out.println("创建用户失败：" + result);
    if (jsonObject.get("data") == null) {
      throw new RuntimeException("Avata平台账户资金不足创建失败");
    }
    return jsonObject.getJSONObject("data").getString("account");
  }

  public static String createNFTCategory() {
    String url = "https://apis.avata.bianjie.ai/v1beta1/nft/classes";
    String path = "/v1beta1/nft/classes";
    Map<String, Object> query = new HashMap<>();
    JSONObject body = new JSONObject();
    body.put("name", "wgn9-NFT");
    body.put("owner", "iaa1tvt9xkw5t2znq687u23mtqvmpk9spakpe5kezx");
    body.put("class_id", "wgn9nft");
    body.put("operation_id", UUID.randomUUID().toString());
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
    System.out.println(jsonObject.toJSONString());
    if (jsonObject.get("data") == null) {
      throw new RuntimeException("Avata平台账户资金不足创建失败");
    }
    return jsonObject.getJSONObject("data").getString("account");
  }

  public static void mint(String imgUrl, String id, Integer count, String treasureTitle) {
    String url = "https://apis.avata.bianjie.ai/v1beta1/nft/batch/nfts/wgn9nft";
    String path = "/v1beta1/nft/batch/nfts/wgn9nft";
    Map<String, Object> query = new HashMap<>();
    JSONObject body = new JSONObject();
    body.put("name", treasureTitle);
    body.put("uri", imgUrl);
    ArrayList<JSONObject> list = new ArrayList<>();
    JSONObject tmp = new JSONObject();
    tmp.put("amount", count);
    tmp.put("recipient", "iaa1tvt9xkw5t2znq687u23mtqvmpk9spakpe5kezx");
    list.add(tmp);
    body.put("recipients", list);

    body.put("operation_id", id);
    String apiSecret = "M2d2s1c2A182l1F24164H0Z0a7n0L8pc";
    long time = System.currentTimeMillis();
    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("Content-Type", "application/json");
    headerMap.put("X-Api-Key", "A2A2A1R2o1g2J1j2U1X4z0e0G780f886");
    headerMap.put("X-Timestamp", time + "");
    headerMap.put("X-Signature", AvataUtils.signRequest(path, query, body, time, apiSecret));

    String result =
        HttpRequest.post(url).addHeaders(headerMap).body(body.toJSONString()).execute().body();
    System.out.println("请求avata,创建nft结果：" + result);
    JSONObject jsonObject = JSONObject.parseObject(result);
    if (jsonObject.get("data") == null) {
      throw new RuntimeException("Avata平台账户资金不足创建失败");
    }
  }

  public static String trans(String from, String nftId, String operationId, String to) {
    String url =
        "https://apis.avata.bianjie.ai/v1beta1/nft/nft-transfers/wgn9nft/" + from + "/" + nftId;
    String path = "/v1beta1/nft/nft-transfers/wgn9nft/" + from + "/" + nftId;
    Map<String, Object> query = new HashMap<>();
    JSONObject body = new JSONObject();
    body.put("recipient", to);
    body.put("operation_id", operationId);
    String apiSecret = "M2d2s1c2A182l1F24164H0Z0a7n0L8pc";
    long time = System.currentTimeMillis();
    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("Content-Type", "application/json");
    headerMap.put("X-Api-Key", "A2A2A1R2o1g2J1j2U1X4z0e0G780f886");
    headerMap.put("X-Timestamp", time + "");
    headerMap.put("X-Signature", AvataUtils.signRequest(path, query, body, time, apiSecret));

    String result =
        HttpRequest.post(url).addHeaders(headerMap).body(body.toJSONString()).execute().body();
    System.out.println("转移异常：" + result);
    JSONObject jsonObject = JSONObject.parseObject(result);
    if (jsonObject.get("data") == null) {
      throw new RuntimeException("Avata平台账户资金不足调用失败");
    }
    return jsonObject.getJSONObject("data").getString("account");
  }

  public static void findUsersNft(String userAddress) {
    String url = "https://apis.avata.bianjie.ai/v1beta1/nft/nfts?owner=" + userAddress;
    String path = "/v1beta1/nft/nfts";
    Map<String, Object> query = new HashMap<>();
    query.put("owner", userAddress);
    String apiSecret = "M2d2s1c2A182l1F24164H0Z0a7n0L8pc";
    long time = System.currentTimeMillis();
    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("Content-Type", "application/json");
    headerMap.put("X-Api-Key", "A2A2A1R2o1g2J1j2U1X4z0e0G780f886");
    headerMap.put("X-Timestamp", time + "");
    headerMap.put("X-Signature", AvataUtils.signRequest(path, query, null, time, apiSecret));

    String result = HttpRequest.get(url).addHeaders(headerMap).execute().body();
    System.out.println("结果：" + result);
    JSONObject jsonObject = JSONObject.parseObject(result);
  }

  public static void main(String[] args) {
    //    createNFTCategory();
    //    mint("http://www.baidu.com", "1");
    findUsersNft("iaa1et9jhthz7pw50pdczgx6wqec26l85enu88cthq"); // 15140088201
  }
}
