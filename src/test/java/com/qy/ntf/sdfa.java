package com.qy.ntf;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.param.OrderTreaCopyParam;
import com.qy.ntf.dao.OrderTreasurePoolDao;
import com.qy.ntf.dao.StoreTreasureRecordDao;
import com.qy.ntf.dao.SysUserDao;
import com.qy.ntf.service.OrderTreasurePoolService;
import com.qy.ntf.service.StoreProPoolService;
import com.qy.ntf.util.PageSelectParam;
import com.qy.ntf.util.ResourcesUtil;
import com.qy.ntf.util.WenchangDDC;
import com.qy.ntf.util.wenchang.DDCSdkClient;
import com.qy.ntf.util.wenchang.dto.TxInfo;
import com.qy.ntf.util.wenchang.util.UserSignEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NtfApplication.class)
public class sdfa {
  @Autowired private OrderTreasurePoolDao orderTreasurePoolDao;
  @Autowired private StoreTreasureRecordDao storeTreasureRecordDao;
  @Autowired private StoreProPoolService storeProPoolService;
  @Autowired private OrderTreasurePoolService orderTreasurePoolService;

  @Test
  public void checkNVhuang() {
    PageSelectParam<OrderTreaCopyParam> param = new PageSelectParam<>();
    OrderTreaCopyParam orderTreaCopyParam = new OrderTreaCopyParam();
    ArrayList<Long> arrayList = new ArrayList<>();
    arrayList.add(776L);
    orderTreaCopyParam.setStreaIds(arrayList);
    param.setSelectParam(orderTreaCopyParam);
    IPage<SysUser> page =
        orderTreasurePoolService.orderTreaCopy(new Page(1, 1000), param.getSelectParam());
    int sum = page.getRecords().stream().mapToInt(SysUser::getHasCount).sum();
    Integer hasCount = page.getRecords().get(2).getHasCount();
    String phone = page.getRecords().get(2).getPhone();
    System.out.println(phone + "拥有：" + hasCount);
    System.out.println("总量：" + sum);
    System.out.println("总量：" + sum);
    System.out.println("总量：" + sum);
    System.out.println("总量：" + sum);
    System.out.println("总量：" + sum);
    System.out.println("总量：" + sum);
    System.out.println("总量：" + sum);
    System.out.println("总量：" + sum);
    System.out.println("总量：" + sum);
    System.out.println("总量：" + sum);
  }

  @Test
  public void check() {
    //    checkApprove(4228L);
    try {
      DDCSdkClient client =
          new DDCSdkClient.Builder()
              .setAuthorityLogicAddress("0xFa1d2d3EEd20C4E4F5b927D9730d9F4D56314B29")
              .setChargeLogicAddress("0x0B8ae0e1b4a4Eb0a0740A250220eE3642d92dc4D")
              .setDDC721Address("0x354c6aF2cB870BEFEA8Ea0284C76e4A46B8F2870")
              .setDDC1155Address("0x0E762F4D11439B1130D402995328b634cB9c9973")
              .setGasLimit("35000")
              .setGasPrice("1")
              .setSignEventListener(
                  new UserSignEvent(
                      "a0f72ab8d68d4ec05c72a4806b62ba0021e33efa163a730cb7fd99b3dde959e7"))
              .init();
      TxInfo transByHash =
          client
              .getDDC1155Service()
              .getTransByHash("0xed301fd8979b2e70d38eeccba9e943c4412096f77026e7196345bfc62f2bceb4");
      System.out.println(JSONObject.toJSONString(transByHash));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Autowired private SysUserDao sysUserDao;

  public void checkApprove(Long userId) {
    //    List<SysUser> sysUsers = sysUserDao.selectList(Wrappers.lambdaQuery());
    SysUser sysUser = sysUserDao.selectById(userId);
    //    for (SysUser sysUser : sysUsers) {
    try {
      DDCSdkClient client =
          new DDCSdkClient.Builder()
              .setAuthorityLogicAddress("0xFa1d2d3EEd20C4E4F5b927D9730d9F4D56314B29")
              .setChargeLogicAddress("0x0B8ae0e1b4a4Eb0a0740A250220eE3642d92dc4D")
              .setDDC721Address("0x354c6aF2cB870BEFEA8Ea0284C76e4A46B8F2870")
              .setDDC1155Address("0x0E762F4D11439B1130D402995328b634cB9c9973")
              .setGasLimit("35000")
              .setGasPrice("1")
              .setSignEventListener(new UserSignEvent(ResourcesUtil.getProperty("")))
              .init();
      String bsn_plate_addr =
          client
              .getDDC1155Service()
              .setApprovalForAll(
                  sysUser.getLinkAddress(), ResourcesUtil.getProperty("bsn_plate_addr"), true);
      System.out.println("授权完成等待校验userId: " + userId + " 用户信息：" + JSONObject.toJSONString(sysUser));
      System.out.println("交易结果：" + bsn_plate_addr);
    } catch (Exception e) {
      System.out.println("用户授权失败，重新充值用户能量值");
      WenchangDDC bsnUtil = new WenchangDDC();
      //      SysUser sysUser1 = bsnUtil.userAddGas(sysUser.getLinkAddress(), sysUser);
      //      if (sysUser1 != null) {
      //        sysUserDao.updateById(sysUser1);
      //      }
      throw new RuntimeException(e);
    }
    System.out.println("用户授权校验开始，userID: " + userId);
    try {
      DDCSdkClient client =
          new DDCSdkClient.Builder()
              .setAuthorityLogicAddress("0xFa1d2d3EEd20C4E4F5b927D9730d9F4D56314B29")
              .setChargeLogicAddress("0x0B8ae0e1b4a4Eb0a0740A250220eE3642d92dc4D")
              .setDDC721Address("0x354c6aF2cB870BEFEA8Ea0284C76e4A46B8F2870")
              .setDDC1155Address("0x0E762F4D11439B1130D402995328b634cB9c9973")
              .setGasLimit("35000")
              .setGasPrice("1")
              .setSignEventListener(new UserSignEvent(sysUser.getPrivateKey()))
              .init();
      Boolean bsn_plate_addr =
          client
              .getDDC1155Service()
              .isApprovedForAll(
                  sysUser.getLinkAddress(), ResourcesUtil.getProperty("bsn_plate_addr"));
      if (!bsn_plate_addr) {

        System.out.println(
            "授权校验未能通过，重新授权： userId: " + userId + " 用户信息：" + JSONObject.toJSONString(sysUser));
      } else {
        System.out.println("授权校验通过userId: " + userId + " 用户信息：" + JSONObject.toJSONString(sysUser));
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    //    }
  }
}
