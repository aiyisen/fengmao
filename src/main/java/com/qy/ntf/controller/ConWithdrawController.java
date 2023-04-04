package com.qy.ntf.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.ConWithdraw;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.param.GetRanParam;
import com.qy.ntf.bean.param.GetRandomParams;
import com.qy.ntf.bean.param.GetRandomResult;
import com.qy.ntf.config.AccessLimit;
import com.qy.ntf.service.ConWithdrawService;
import com.qy.ntf.service.SysUserService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.LLianPayDateUtils;
import com.qy.ntf.util.PageSelectParam;
import com.qy.ntf.util.llPay.LLianPayAccpSignature;
import com.qy.ntf.util.llPay.LLianPayClient;
import com.qy.ntf.util.llPay.LLianPayConstant;
import com.qy.ntf.util.llPay.WithDrawalResult;
import com.qy.ntf.util.wxPay.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 提现订单表 控制层接口
 */
@Api(tags = {"提现订单表 api接口"})
@RestController
@Slf4j
// @CrossOrigin(origins = "*")
@RequestMapping("/conWithdraw")
public class ConWithdrawController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 提现订单表 服务 */
  @Autowired private ConWithdrawService conWithdrawService;

  @ApiOperation(value = "提现订单表详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<ConWithdrawDto> detail(@PathVariable("id") Long id) {
    ConWithdrawDto dto = conWithdrawService.getConWithdrawById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @Autowired private SysUserService sysUserService;

  @ApiOperation(value = "APP - " + "发起提现", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<WithDrawalResult> add(
      @RequestBody ConWithdrawAddDto dto, HttpServletRequest request) {
    UserDto userData = getUserData();
    //    if (dto.getWithdrawTotal().compareTo(new BigDecimal(100)) < 0) {
    //      throw new RuntimeException("提现金额最小100");
    //    }
    //    if (!dto.getAliPayIdentity().equals(userData.getPhone())) {
    //      throw new RuntimeException("提现手机号与当前用户手机号不一致");
    //    }
    SysUser sysUser = sysUserService.selectDataById(userData.getId());
    //    if (!dto.getAliPayName().equals(sysUser.getRealName())) {
    //      throw new RuntimeException("提现用户名与实名用户名不一致");
    //    }
    if (sysUser.getIsOpening() != 1) {
      throw new RuntimeException("对不起用户暂未完成开户");
    }
    WithDrawalResult save = conWithdrawService.save(dto, userData, WXPayUtil.getIpAddress(request));
    return ApiResponse.success(save);
  }

  @ApiOperation(value = "APP - " + "发起提现二次确认", notes = "发起提现二次确认")
  @PostMapping("/confirmSecond")
  public ApiResponse<WithDrawalResult> confirmSecond(@RequestBody ConWithdrawConfirmDto dto) {
    ConWithdraw conWithdraw = conWithdrawService.selectById(dto.getTxnSeqno());
    JSONObject params = new JSONObject();
    String timestamp = LLianPayDateUtils.getTimestamp();
    params.put("timestamp", timestamp);
    params.put("oid_partner", LLianPayConstant.OidPartner);
    params.put("payer_type", "USER");
    params.put("payer_id", dto.getPhone());
    params.put("txn_seqno", dto.getTxnSeqno());
    params.put("token", dto.getToken());
    params.put("verify_code", dto.getVerifyCode());
    params.put("total_amount", conWithdraw.getWithdrawTotal().setScale(2, BigDecimal.ROUND_DOWN));
    String url = "https://accpapi.lianlianpay.com/v1/txn/validation-sms";
    LLianPayClient lLianPayClient = new LLianPayClient();
    String resultJsonStr = lLianPayClient.sendRequest(url, JSON.toJSONString(params));
    JSONObject result = JSONObject.parseObject(resultJsonStr, JSONObject.class);
    log.info("++++++++++提现审核申请确认：{}", resultJsonStr);
    // 小额免验，不需要验证码，直接返回0000
    if ("0000".equals(result.getString("ret_code"))) {
      conWithdraw.setAllocatedState(1);
      conWithdraw.setApplyFlag(1);
      conWithdrawService.updateById(conWithdraw);
      return ApiResponse.success();
    }
    return ApiResponse.fail(resultJsonStr);
  }

  @ApiOperation(value = "APP - " + "获取token", notes = "获取token")
  @PostMapping("/getToken")
  public ApiResponse<JSONObject> getRanToken(@RequestBody GetRanParam param) {
    JSONObject params = new JSONObject();
    String timestamp = LLianPayDateUtils.getTimestamp();
    params.put("timestamp", timestamp);
    params.put("oid_partner", LLianPayConstant.OidPartner);
    params.put("password_scene", "cashout_password");
    params.put("flag_chnl", "H5");
    params.put("amount", param.getAmount());
    params.put("user_id", param.getPhone());
    params.put("pyee_name", param.getPyeeName());
    String url = "https://accpgw.lianlianpay.com/v1/acctmgr/apply-password-element";
    LLianPayClient lLianPayClient = new LLianPayClient();
    String resultJsonStr = lLianPayClient.sendRequest(url, JSON.toJSONString(params));
    JSONObject result = JSONObject.parseObject(resultJsonStr, JSONObject.class);
    return ApiResponse.success(result);
  }

  @ApiOperation(value = "APP - " + "发起提现二次确认", notes = "发起提现二次确认")
  @PostMapping("/getRandom")
  public ApiResponse<JSONObject> confirmSecond() {
    UserDto userData = getUserData();
    return ApiResponse.success(getRandom(userData.getPhone()));
  }

  public JSONObject getRandom(String userId) {
    GetRandomParams params = new GetRandomParams();
    String timestamp = LLianPayDateUtils.getTimestamp();
    params.setTimestamp(timestamp);
    params.setOid_partner(LLianPayConstant.OidPartner);
    params.setUser_id(userId);
    /*
    交易发起渠道。
    ANDROID
    IOS
    H5
    PC
     */
    params.setFlag_chnl("H5");
    // 测试环境都传test，正式环境传真实域名/包名
    params.setPkg_name("fmcangping.com");
    // 测试环境都传test，正式环境传真实域名/应用名
    params.setApp_name("fmcangping.com");
    params.setEncrypt_algorithm("RSA");

    LLianPayClient lLianPayClient = new LLianPayClient();
    // 测试环境URL
    String url = "https://accpapi.lianlianpay.com/v1/acctmgr/get-random";
    String resultJsonStr = lLianPayClient.sendRequest(url, JSON.toJSONString(params));
    GetRandomResult randomResult = JSON.parseObject(resultJsonStr, GetRandomResult.class);

    // 构建前端需要的json
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("license", randomResult.getLicense());
    jsonObject.put("map_arr", randomResult.getMap_arr() == null ? "" : randomResult.getMap_arr());
    jsonObject.put("random_key", randomResult.getRandom_key());
    jsonObject.put("random_value", randomResult.getRandom_value());
    jsonObject.put(
        "rsa_public_content",
        randomResult.getRsa_public_content() == null ? "" : randomResult.getRsa_public_content());
    jsonObject.put("oid_partner", randomResult.getOid_partner());
    jsonObject.put(
        "sm2_key_hex", randomResult.getSm2_key_hex() == null ? "" : randomResult.getSm2_key_hex());
    jsonObject.put("user_id", randomResult.getUser_id());
    return jsonObject;
  }

  @ApiOperation(value = "ADMIN - " + "提现订单修改，即审核通过、驳回", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody ConWithdrawUpdateDto dto) {
    conWithdrawService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "提现订单表修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody ConWithdrawDto tmpDto) {
    ModelMapper md = new ModelMapper();
    ConWithdrawDto dto = new ConWithdrawDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    conWithdrawService.updateState(dto);
    return ApiResponse.success();
  }

  @Autowired private RedissonClient redissonClient;

  @ApiOperation(value = "ADMIN - " + "提现订单拨付", notes = "修改状态")
  @PostMapping("/doAllocate")
  @AccessLimit(times = 1, second = 3)
  public ApiResponse<Void> doAllocate(@RequestBody ConWithdrawDto tmpDto) {
    RLock rLock = redissonClient.getLock("doAllocate_" + tmpDto.getId());
    try {
      if (rLock.tryLock(0, 5, TimeUnit.SECONDS)) {
        String s = conWithdrawService.doAllocate(tmpDto, getAdminUserData());
        if (s.equals("success")) {
          return ApiResponse.success();
        } else {
          return ApiResponse.fail(s);
        }
      }
    } catch (Exception e) {
      if (rLock.isLocked()) {
        if (rLock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
          rLock.unlock(); // 释放锁
        }
      }
      return ApiResponse.fail(e.getMessage());
    }

    return ApiResponse.fail("");
  }

  @RequestMapping(method = RequestMethod.POST, value = "/notify")
  public String llianPayMessageNotify(HttpServletRequest request) {
    // 从请求头中获取签名值
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
        log.info("提币验签通过！！！");
        JSONObject jsonObject = JSONObject.parseObject(stringBuilder.toString());
        if (jsonObject.getString("txn_status").equals("TRADE_SUCCESS")) {
          conWithdrawService.updateStateByNotify(
              jsonObject.getJSONObject("orderInfo").getString("txn_seqno"));
        } else {
          log.info("提现操作失败：" + stringBuilder.toString());
          log.info("提现操作失败：" + stringBuilder.toString());
          log.info("提现操作失败：" + stringBuilder.toString());
          log.info("提现操作失败：" + stringBuilder.toString());
        }

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

  @ApiOperation(value = "提现订单表删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    conWithdrawService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "APP - " + "提现订单表分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<ConWithdrawDto>> listByPage(@RequestBody PageSelectParam<Object> param) {
    // 组合查询条件
    LambdaQueryWrapper<ConWithdraw> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper
        .eq(ConWithdraw::getUId, getUserData().getId())
        .orderByDesc(BaseEntity::getCreateTime);
    IPage<ConWithdrawDto> page =
        conWithdrawService.getListByPage(
            ConWithdrawDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "提现订单表分页列表")
  @PostMapping("/adminPage")
  public ApiResponse<IPage<ConWithdrawDto>> adminPage(@RequestBody PageSelectParam<Object> param) {
    // 组合查询条件
    LambdaQueryWrapper<ConWithdraw> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    IPage<ConWithdrawDto> page =
        conWithdrawService.getListByPage(
            ConWithdrawDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "提现订单表列表")
  @PostMapping("/list")
  public ApiResponse<List<ConWithdrawDto>> listByPage(@RequestBody ConWithdrawDto dto) {
    return ApiResponse.success(conWithdrawService.list(dto));
  }
}
