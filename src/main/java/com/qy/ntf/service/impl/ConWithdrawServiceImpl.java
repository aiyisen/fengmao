package com.qy.ntf.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.ConBalanceRecord;
import com.qy.ntf.bean.entity.ConWithdraw;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.dao.ConBalanceRecordDao;
import com.qy.ntf.dao.ConWithdrawDao;
import com.qy.ntf.dao.SysDictonaryDao;
import com.qy.ntf.dao.SysUserDao;
import com.qy.ntf.service.ConWithdrawService;
import com.qy.ntf.util.LLianPayDateUtils;
import com.qy.ntf.util.ResourcesUtil;
import com.qy.ntf.util.llPay.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.utils.Strings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 提现订单表 service服务实现
 */
@Service("conWithdrawService")
@Slf4j
public class ConWithdrawServiceImpl implements ConWithdrawService {

  @Autowired private ConWithdrawDao conWithdrawDao;

  @Override
  public BaseMapper<ConWithdraw> getDao() {
    return conWithdrawDao;
  }

  @Override
  public IPage<ConWithdrawDto> getListByPage(
      Class<ConWithdrawDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<ConWithdraw> queryWrapper) {
    IPage<ConWithdrawDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public String doAllocate(ConWithdrawDto tmpDto, SysUserAdminDto adminDto) {
    ConWithdraw conWithdraw = conWithdrawDao.selectById(tmpDto.getId());
    if (conWithdraw == null) throw new RuntimeException("id异常");
    if (conWithdraw.getApplyFlag() == 1 && conWithdraw.getAllocatedState() == 0) {
      // 需要输入短信验证码，调用交易二次短信验证接口
      // 用Debug模式，断点打到这里，Debug的时候把verifyCode设置成手机收到的真实验证码
      ValidationSmsParams validationSmsParams = new ValidationSmsParams();
      validationSmsParams.setTimestamp(LLianPayDateUtils.getTimestamp());
      validationSmsParams.setOid_partner(ResourcesUtil.getProperty("lianlian_oid"));
      validationSmsParams.setPayer_id(ResourcesUtil.getProperty("lianlian_oid"));
      validationSmsParams.setPayer_type("MERCHANT");
      validationSmsParams.setTxn_seqno(conWithdraw.getId() + "");
      validationSmsParams.setTotal_amount(conWithdraw.getRealPay().doubleValue() + "");
      validationSmsParams.setToken(conWithdraw.getToken());
      validationSmsParams.setVerify_code(tmpDto.getVerifyCode());

      // 测试环境URL
      String validationSmsUrl = "https://accpapi.lianlianpay.com/v1/txn/validation-sms";
      LLianPayClient lLianPayClient = new LLianPayClient();
      String resultJsonStr2 =
          lLianPayClient.sendRequest(validationSmsUrl, JSON.toJSONString(validationSmsParams));
      ValidationSmsResult validationSmsResult =
          JSON.parseObject(resultJsonStr2, ValidationSmsResult.class);
      return "提交成功";
      //      conWithdraw.setAllocatedState(1);
      //      conWithdraw.setUpdateTime(new Date());
      //      conWithdraw.setUpdateId(adminDto.getId());
      //      conWithdrawDao.updateById(conWithdraw);
      //      try {
      //        String result =
      //            AgentPayDemo.withDraw(
      //                conWithdraw.getAliPayIdentity(),
      //                conWithdraw.getAliPayName(),
      //                conWithdraw.getRealPay());
      //        if (!result.equals("success")) {
      //          conWithdraw.setApplyFlag(0);
      //          conWithdraw.setAllocatedState(0);
      //          conWithdraw.setUpdateTime(new Date());
      //          conWithdraw.setUpdateId(adminDto.getId());
      //          conWithdrawDao.updateById(conWithdraw);
      //        }
      //        return result;
      //      } catch (Exception e) {
      //        conWithdraw.setApplyFlag(0);
      //        conWithdraw.setAllocatedState(0);
      //        conWithdraw.setUpdateTime(new Date());
      //        conWithdraw.setUpdateId(adminDto.getId());
      //        conWithdrawDao.updateById(conWithdraw);
      //        return "提现异常请核对账户信息";
      //      }
    } else {
      throw new RuntimeException("待审核记录状态异常，当前状态：" + conWithdraw.getApplyFlag());
    }
  }

  @Override
  public void updateStateByNotify(String string) {
    ConWithdraw conWithdraw = conWithdrawDao.selectById(Long.valueOf(string));
    conWithdraw.setAllocatedState(1);
    conWithdrawDao.updateById(conWithdraw);
  }

  @Override
  public ConWithdraw selectById(String txnSeqno) {
    return conWithdrawDao.selectById(Long.valueOf(txnSeqno));
  }

  @Override
  public void updateById(ConWithdraw conWithdraw) {
    conWithdrawDao.updateById(conWithdraw);
  }

  @Autowired private ConBalanceRecordDao conBalanceRecordDao;

  private Object aliPay(ConWithdraw conWithdraw) throws Exception {
    String appId = ResourcesUtil.ALI_APP_ID;
    AlipayConfig alipayConfig = new AlipayConfig();
    alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
    alipayConfig.setAppId(appId);
    alipayConfig.setPrivateKey(ResourcesUtil.getProperty("ali_75private_key"));
    alipayConfig.setAppCertPath("/nginx/cert/appCertPublicKey_2021003146630241.crt");
    alipayConfig.setAlipayPublicCertPath("/nginx/cert/alipayCertPublicKey_RSA2.crt");
    alipayConfig.setRootCertPath("/nginx/cert/alipayRootCert.crt");
    alipayConfig.setFormat("json");
    alipayConfig.setCharset("UTF-8");
    alipayConfig.setSignType("RSA2");
    DefaultAlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
    AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
    AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
    model.setOutBizNo(conWithdraw.getOrderFingerprint());
    model.setRemark("用户提现");
    //    model.setBusinessParams("{\\\"payer_show_name_use_alias\\\":\\\"true\\\"}");
    model.setBizScene("DIRECT_TRANSFER");
    Participant payeeInfo = new Participant();
    payeeInfo.setIdentity(conWithdraw.getAliPayIdentity());
    payeeInfo.setIdentityType("ALIPAY_LOGON_ID");
    payeeInfo.setName(conWithdraw.getAliPayName());
    model.setPayeeInfo(payeeInfo);
    model.setTransAmount(conWithdraw.getRealPay().doubleValue() + "");
    model.setProductCode("TRANS_ACCOUNT_NO_PWD");
    model.setOrderTitle("提现");
    request.setBizModel(model);
    AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
    log.info("提现支付宝拨付返回值：" + JSONObject.toJSONString(response));
    if (response.isSuccess()) {
      ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
      conBalanceRecord.setUId(conWithdraw.getCreateId());
      conBalanceRecord.setReType(0);
      conBalanceRecord.setOrderId(conWithdraw.getId() + "");
      conBalanceRecord.setOrderType(0);
      conBalanceRecord.setTotalPrice(conWithdraw.getWithdrawTotal());
      conBalanceRecord.setTradingChannel(1);
      conBalanceRecord.setCreateId(conWithdraw.getCreateId());
      conBalanceRecord.setCreateTime(new Date());
      conBalanceRecordDao.insert(conBalanceRecord);
      return "success";
    } else {
      throw new RuntimeException(response.getSubMsg());
    }
  }

  @Override
  public ConWithdrawDto getConWithdrawById(Long id) {
    Optional<ConWithdrawDto> optional = selectDataById(ConWithdrawDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Autowired private SysUserDao sysuserdao;
  @Autowired private SysDictonaryDao sysDictonaryDao;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public WithDrawalResult save(ConWithdrawAddDto tmpDto, UserDto userData, String ipAddress) {
    SysUser sysUser = sysuserdao.selectById(userData.getId());
    if (sysUser.getBalance().compareTo(tmpDto.getWithdrawTotal()) >= 0) {

      sysUser.setBalance(sysUser.getBalance().subtract(tmpDto.getWithdrawTotal()));
      sysuserdao.updateById(sysUser);
      ConWithdraw conWithdraw = new ConWithdraw();
      conWithdraw.setUId(sysUser.getId());
      conWithdraw.setOrderFingerprint(UUID.randomUUID().toString().toString().replaceAll("-", ""));
      conWithdraw.setWithdrawTotal(tmpDto.getWithdrawTotal());
      conWithdraw.setAliPayIdentity(tmpDto.getAliPayIdentity());
      //      conWithdraw.setAliPayName(tmpDto.getAliPayName());
      conWithdraw.setCreateId(sysUser.getId());
      conWithdraw.setCreateTime(new Date());
      String withdraw_per = sysDictonaryDao.selectByAlias("withdraw_per");
      conWithdraw.setRealPay(conWithdraw.getWithdrawTotal().setScale(2, RoundingMode.DOWN));
      //      conWithdraw.setRealPay(
      //          conWithdraw
      //              .getWithdrawTotal()
      //              .multiply(new BigDecimal("1").subtract(new BigDecimal(withdraw_per))));
      if (conWithdraw.getRealPay().setScale(2, RoundingMode.DOWN).doubleValue() > 0) {
        conWithdrawDao.insert(conWithdraw);
        ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
        conBalanceRecord.setUId(userData.getId());
        conBalanceRecord.setReType(1);
        conBalanceRecord.setOrderId(conWithdraw.getId() + "");
        conBalanceRecord.setOrderType(1);
        conBalanceRecord.setTotalPrice(conWithdraw.getWithdrawTotal());
        conBalanceRecord.setTradingChannel(4);
        conBalanceRecord.setCreateId(userData.getId());
        conBalanceRecord.setCreateTime(new Date());
        conBalanceRecord.setState(1);
        conBalanceRecordDao.insert(conBalanceRecord);
        WithDrawalResult withdraw = withdraw(conWithdraw, sysUser, tmpDto, ipAddress);
        if (withdraw != null) {
          return withdraw;
        }
      } else {
        throw new RuntimeException("提现金额有误");
      }

    } else {
      throw new RuntimeException("余额不足");
    }
    return null;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(ConWithdrawUpdateDto tmpDto) {
    ConWithdraw conWithdraw = conWithdrawDao.selectById(tmpDto.getId());
    if (conWithdraw == null) throw new RuntimeException("id异常");
    if (conWithdraw.getApplyFlag() == 0) {
      conWithdraw.setApplyFlag(tmpDto.getApplyFlag());
      conWithdraw.setApplyCause(tmpDto.getApplyCause());
      conWithdraw.setApplyTime(new Date());
      conWithdraw.setAllocatedState(0);
      conWithdraw.setUpdateTime(new Date());
      conWithdrawDao.updateById(conWithdraw);
      SysUser sysUser = sysuserdao.selectById(conWithdraw.getCreateId());
      //      if (tmpDto.getApplyFlag() == 2) {
      //        sysUser.setBalance(sysUser.getBalance().add(conWithdraw.getWithdrawTotal()));
      //        sysuserdao.updateById(sysUser);
      //      }
      //      else {
      //        withdraw(conWithdraw, sysUser);
      //      }
    } else {
      throw new RuntimeException("待审核记录状态异常，当前状态：" + conWithdraw.getApplyFlag());
    }
  }

  private WithDrawalResult withdraw(
      ConWithdraw conWithdraw, SysUser sysUser, ConWithdrawAddDto tmpDto, String ipAddress) {
    WithDrawalParams params = new WithDrawalParams();
    String timestamp = LLianPayDateUtils.getTimestamp();
    params.setTimestamp(timestamp);
    params.setOid_partner(LLianPayConstant.OidPartner);
    params.setNotify_url("http://api.fmcangping.com/nftApi/conWithdraw/notify");
    params.setRisk_item(
        "{\"frms_ware_category\":\"4007\",\"goods_name\":\"用户提现\",\"user_info_mercht_userno\":\""
            + sysUser.getPhone()
            + "\",\"user_info_dt_register\":\"20220823101239\",\"user_info_bind_phone\":\""
            + sysUser.getPhone()
            + "\",\"user_info_full_name\":\""
            + sysUser.getRealName()
            + "\",\"user_info_id_no\":\""
            + sysUser.getIdCard()
            + "\",\"user_info_identify_state\":\"1\",\"user_info_identify_type\":\"1\",\"user_info_id_type\":\"1\",\"frms_client_chnl\":\"16\",\"frms_ip_addr\":\""
            + ipAddress
            + "\",\"user_auth_flag\":\"1\"}");
    params.setLinked_acctno(conWithdraw.getAliPayIdentity());

    // 设置商户订单信息
    WithDrawalOrderInfo orderInfo = new WithDrawalOrderInfo();
    orderInfo.setTxn_seqno(conWithdraw.getId() + "");
    orderInfo.setTxn_time(timestamp);
    orderInfo.setTotal_amount(
        conWithdraw.getRealPay().setScale(2, RoundingMode.DOWN).doubleValue());
    orderInfo.setPostscript("用户提现");
    params.setOrderInfo(orderInfo);

    // 设置付款方信息
    WithDrawalPayerInfo payerInfo = new WithDrawalPayerInfo();
    payerInfo.setPayer_type("USER");
    payerInfo.setPayer_id(sysUser.getPhone());
    payerInfo.setPassword(tmpDto.getPassword());
    payerInfo.setRandom_key(tmpDto.getRandomKey());
    payerInfo.setPayer_accttype("USEROWN");

    //    payerInfo.setPayer_accttype("MCHOWN");
    // 用户：LLianPayTest-In-User-12345 密码：qwerty，本地测试环境测试，没接入密码控件，使用本地加密方法加密密码（仅限测试环境使用）
    //    payerInfo.setPassword(LLianPayAccpSignature.getInstance().localEncrypt("qwerty"));
    params.setPayerInfo(payerInfo);
    String url = "https://accpapi.lianlianpay.com/v1/txn/withdrawal";
    LLianPayClient lLianPayClient = new LLianPayClient();
    String resultJsonStr = lLianPayClient.sendRequest(url, JSON.toJSONString(params));
    WithDrawalResult drawalResult = JSON.parseObject(resultJsonStr, WithDrawalResult.class);
    log.info("++++++++++提现审核申请结果：{}", drawalResult);
    if (drawalResult.getToken() != null && !Strings.isEmpty(drawalResult.getToken())) {
      conWithdraw.setToken(drawalResult.getToken());
      conWithdrawDao.updateById(conWithdraw);
      // 小额免验，不需要验证码，直接返回0000
      if ("0000".equals(drawalResult.getRet_code())) {
        System.out.println("支付成功！！！");
        conWithdraw.setAllocatedState(1);
        conWithdraw.setApplyFlag(1);
        conWithdrawDao.updateById(conWithdraw);
        return null;
      } else if ("8888".equals(drawalResult.getRet_code())) {
        conWithdraw.setAllocatedState(0);
        conWithdraw.setApplyFlag(1);
        conWithdrawDao.updateById(conWithdraw);
        return drawalResult;
      }
    } else {
      conWithdraw.setApplyFlag(0);
      conWithdrawDao.updateById(conWithdraw);
      throw new RuntimeException(drawalResult.getRet_msg());
    }
    return null;
  }

  public BigDecimal getBal(String phone) {
    String timestamp = LLianPayDateUtils.getTimestamp();
    JSONObject params = new JSONObject();
    params.put("timestamp", timestamp);
    params.put("oid_partner", ResourcesUtil.getProperty("lianlian_oid"));
    params.put("user_id", phone);
    params.put("user_type", "INNERUSER");
    // 设置商户订单信息

    // 设置付款方信息
    String url = "https://accpapi.lianlianpay.com/v1/acctmgr/query-acctinfo";
    LLianPayClient lLianPayClient = new LLianPayClient();
    String resultJsonStr = lLianPayClient.sendRequest(url, JSON.toJSONString(params));
    JSONObject result = JSONObject.parseObject(resultJsonStr, JSONObject.class);
    JSONArray acctinfo_list = result.getJSONArray("acctinfo_list");

    for (int i = 0; i < acctinfo_list.size(); i++) {
      JSONObject jsonObject = acctinfo_list.getJSONObject(i);
      if (jsonObject != null) {
        if (jsonObject.getString("acct_type").equals("USEROWN_AVAILABLE")) {
          return jsonObject.getBigDecimal("amt_balaval");
        }
      }
    }
    log.info("结果：{}", resultJsonStr);
    return BigDecimal.ZERO;
  }

  @Override
  public void updateState(ConWithdrawDto dto) {
    ConWithdrawDto conWithdraw = getConWithdrawById(dto.getId());
    if (conWithdraw.getState() == 1) {
      conWithdraw.setState(2);
    } else {
      conWithdraw.setState(1);
    }
    conWithdraw.setUpdateTime(new Date());
    conWithdraw.setUpdateId(dto.getUpdateId());
    updateDataById(conWithdraw.getId(), conWithdraw);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<ConWithdrawDto> list(ConWithdrawDto dto) {
    LambdaQueryWrapper<ConWithdraw> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(ConWithdrawDto.class, queryWrapper);
  }
}
