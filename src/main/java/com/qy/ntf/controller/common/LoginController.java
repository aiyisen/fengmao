package com.qy.ntf.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.customResult.GetMobileByTokenParam;
import com.qy.ntf.bean.customResult.MobileByTokenRes;
import com.qy.ntf.bean.dto.UserAdminInfo;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.dto.UserInfo;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.entity.SysUserAdmin;
import com.qy.ntf.bean.param.BindThirdParam;
import com.qy.ntf.bean.param.LoginParam;
import com.qy.ntf.bean.param.LoginWithCodeParam;
import com.qy.ntf.bean.param.LoginWithPassParam;
import com.qy.ntf.config.AccessLimit;
import com.qy.ntf.service.MenuService;
import com.qy.ntf.service.SysUserService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.JwtTokenUtil;
import com.qy.ntf.util.Sample;
import com.qy.ntf.util.TokenInvalidException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.web3j.utils.Strings;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Api(tags = {"通用-登录接口支持"})
@RestController
@Slf4j
public class LoginController extends BaseController {
  @Autowired private SysUserService sysUserService;
  @Autowired private JwtTokenUtil jwtTokenUtil;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private MenuService menuService;

  @ApiOperation(value = "ADMIN - " + "用户密码登录接口")
  @PostMapping("/login")
  public ApiResponse login(@RequestBody LoginParam param) {


    UserAdminInfo userInfo = sysUserService.checkAdminLogin(param);
    if (userInfo != null) {
      redisTemplate
          .opsForValue()
          .set(userInfo.getToken(), JSONObject.toJSONString(userInfo.getUser()), 30, TimeUnit.DAYS);

      return ApiResponse.success(userInfo);
    }
    return ApiResponse.fail("验证失败");
  }

  @ApiOperation(value = "APP - " + "一键登录获取用户手机号接口，并返回是否注册及token")
  @PostMapping("/getMobileByToken")
  public ApiResponse<MobileByTokenRes> getMobileByToken(@RequestBody GetMobileByTokenParam param) {
    return ApiResponse.success(sysUserService.getMobileByToken(param.getToken()));
  }

  @ApiOperation(value = "APP - " + "第三方登录绑定openID", notes = "")
  @PostMapping("/openIdBind")
  public ApiResponse<UserInfo> openIdBind(@RequestBody BindThirdParam param) {
    UserInfo userInfo = sysUserService.openIdBind(param);
    return ApiResponse.success(userInfo);
  }

  @ApiOperation(value = "APP - " + "openID登录", notes = "")
  @PostMapping("/loginByOpenId")
  public ApiResponse<UserInfo> loginByOpenId(@RequestBody BindThirdParam param) {
    UserInfo userInfo = sysUserService.loginByOpenId(param);
    return ApiResponse.success(userInfo);
  }

  @ApiOperation(value = "APP - " + "用户验证码注册绑定登录接口-验证码登录也可使用此接口")
  @PostMapping("/loginWithCode")
  public ApiResponse<UserInfo> loginWithCode(@RequestBody LoginWithCodeParam param) {
    String code = redisTemplate.opsForValue().get(param.getPhone());
    if ("12345678".equals(param.getVerifyCode())) {
      // 一键登录默认验证码12345678
      UserInfo userInfo = sysUserService.loginWithCode(param);
      if (userInfo != null) {
        return ApiResponse.success(userInfo);
      } else {
        return ApiResponse.fail("用户已被禁用");
      }
    } else if (code != null && code.equals(param.getVerifyCode())) {
      UserInfo userInfo = sysUserService.loginWithCode(param);
      if (userInfo != null) {
        return ApiResponse.success(userInfo);
      } else {
        return ApiResponse.fail("用户已被禁用");
      }

    } else {
      return ApiResponse.fail("验证码不正确");
    }
  }

  @ApiOperation(value = "APP - " + "手机密码登录")
  @PostMapping("/loginWithPass")
  public ApiResponse<UserInfo> loginWithPass(
      @RequestBody @Valid LoginWithPassParam param, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      SysUser sysUser = sysUserService.selectByPhone(param.getPhone());
      if (sysUser != null) {
        if (sysUser.getPass() == null) {
          throw new RuntimeException("账户暂未设置登录密码");
        }
        if (sysUser.getPass().equals(DigestUtils.md5Hex(param.getPass()))) {
          LoginWithCodeParam tmpParam = new LoginWithCodeParam();
          tmpParam.setPhone(param.getPhone());
          UserInfo userInfo = sysUserService.loginWithCode(tmpParam);
          if (userInfo != null) {
            return ApiResponse.success(userInfo);
          } else {
            return ApiResponse.fail("用户已被禁用");
          }
        }
        throw new RuntimeException("密码不正确");

      } else {
        return ApiResponse.fail("用户不存在");
      }
    }
  }

  @ApiOperation(value = "APP - " + "手机验证码密码注册")
  @PostMapping("/regWithPass")
  public ApiResponse<UserInfo> regWithPass(
      @RequestBody @Valid LoginWithPassParam param, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      SysUser sysUser = sysUserService.selectByPhone(param.getPhone());
      String code = redisTemplate.opsForValue().get(param.getPhone());
      if (sysUser != null) {
        throw new RuntimeException("该用户已注册");
      } else {
        if (code != null && code.equals(param.getVerifyCode())) {
          LoginWithCodeParam tmpParam = new LoginWithCodeParam();
          tmpParam.setPhone(param.getPhone());
          tmpParam.setInviteCode(param.getInviteCode());
          tmpParam.setPass(DigestUtils.md5Hex(param.getPass()));
          UserInfo userInfo = sysUserService.loginWithCode(tmpParam);
          if (userInfo != null) {
            return ApiResponse.success(userInfo);
          } else {
            return ApiResponse.fail("用户已被禁用");
          }
        } else {
          throw new RuntimeException("验证码错误");
        }
      }
    }
  }

  @ApiOperation(value = "APP - " + "修改密码")
  @PostMapping("/updatePass")
  public ApiResponse<UserInfo> updatePass(
      @RequestBody @Valid LoginWithPassParam param, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      SysUser sysUser = sysUserService.selectByPhone(param.getPhone());
      if (sysUser == null) throw new RuntimeException("该手机号暂未注册");
      String code = redisTemplate.opsForValue().get(param.getPhone());
      if (code != null && code.equals(param.getVerifyCode())) {
        sysUser.setPass(DigestUtils.md5Hex(param.getPass()));
        sysUserService.updateDataById(sysUser);
        return ApiResponse.success();
      } else {
        throw new RuntimeException("验证码错误");
      }
    }
  }

  @ApiOperation(value = "APP - " + "修改操作密码")
  @PostMapping("/updateOperationPass")
  public ApiResponse<UserInfo> updateOperationPass(@RequestBody LoginWithPassParam param) {
    if (Strings.isEmpty(param.getPass())) throw new RuntimeException("操作密码不可为空");
    SysUser sysUser = sysUserService.selectDataById(getUserData().getId());
    sysUser.setOperationPass(DigestUtils.md5Hex(param.getPass()));
    sysUserService.updateDataById(sysUser);
    return ApiResponse.success();
  }

  @ApiOperation(value = "GENERAL - " + "令牌刷新")
  @PostMapping("/refreshToken")
  public ApiResponse<String> refreshToken() {

    String userJson = (String) redisTemplate.opsForValue().get(getToken());
    if (userJson != null && !"".equals(userJson)) {
      redisTemplate.opsForValue().set(getToken(), userJson, 30, TimeUnit.DAYS);
      return ApiResponse.success("已重置30天有效期");
    }

    return ApiResponse.fail("令牌失效请重新登录获取", -2);
  }

  @PostMapping("/logout")
  @ApiOperation(value = "GENERAL - " + "退出登录")
  public ApiResponse logout() {
    Subject subject = SecurityUtils.getSubject();
    subject.logout();
    String token = getToken();
    redisTemplate.delete(token);
    UserDto userData = getUserData();
    Object oldToken = redisTemplate.opsForHash().get("allToken", userData.getPhone());
    if (oldToken != null) {
      redisTemplate.opsForHash().delete("allToken", userData.getPhone());
    }
    return ApiResponse.success();
  }

  @ApiOperation(value = "GENERAL - " + "用户登录后需要使用的信息")
  @GetMapping("/userinfo/{userId}")
  public ApiResponse<UserAdminInfo> loginInfo(@PathVariable("userId") Long userId)
      throws Exception {
    SysUserAdmin user = sysUserService.selectAdminDataById(userId);
    if (user != null && user.getId() != null && user.getState() == 1) {
      user.setAdminPass("");
      UserAdminInfo userInfo = new UserAdminInfo();
      userInfo.setUser(user);
      List<Map<String, Object>> dynamicRoutingList =
          menuService.getDynamicRoutingList(user.getId());
      List<Map<String, Object>> buttonPermissions = menuService.getButtonPermissions(userId);
      userInfo.setMenusList(dynamicRoutingList);
      userInfo.setButtonList(buttonPermissions);

      return ApiResponse.success(userInfo);
    } else {
      throw new Exception("没有这个用户:  userid=" + userId);
    }
  }

  @ApiOperation(value = "APP - " + "根据手机号查询用户信息")
  @GetMapping("/userinfoByPhone/{phone}")
  public ApiResponse<SysUser> userinfoByPhone(@PathVariable("phone") String phone)
      throws Exception {
    SysUser user = sysUserService.getUserByPhone(phone);
    if (user != null && user.getId() != null && user.getState() == 1) {
      user.setPass(null);
      if (user.getVipEndTime() != null
          && user.getVipEndTime().getTime() > System.currentTimeMillis()) {
        user.setIsVip(1);
      } else {
        user.setIsVip(0);
      }

      return ApiResponse.success(user);
    } else {
      throw new Exception("没有这个用户:  phone=" + phone);
    }
  }

  @ApiOperation(value = "APP - " + "根据手邀请码查询用户信息")
  @GetMapping("/userinfoByUserIndex/{userIndex}")
  public ApiResponse<List<SysUser>> userinfoByUserIndex(@PathVariable("userIndex") String userIndex)
      throws Exception {
    List<SysUser> user = sysUserService.userinfoByUserIndex(userIndex);
    return ApiResponse.success(user);
  }

  @ApiOperation(value = "APP - " + "发送验证码")
  @PutMapping("/sendCode/{phone}")
  @AccessLimit(times = 1, second = 60)
  public ApiResponse<String> loginInfo(@PathVariable("phone") String phone) throws Exception {
    String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
    redisTemplate.opsForValue().set(phone, code, 15L, TimeUnit.MINUTES);
    log.info("手机号：" + phone);
    //    SendVerfiBack s = SendViaAspx.sendCode(phone, code);
    new Sample().sendVerifyCode(phone, code);
    //    if (!"操作成功".equals(s.getMessage()))
    //      return ApiResponse.fail("验证码发送异常，请联系管理员，错误信息：" + JSONObject.toJSONString(s));
    return ApiResponse.success("发送成功");
  }

  @ApiOperation(value = "APP - " + "校验验证码-修改手机号时使用")
  @PutMapping("/verifyCode/{phone}/{code}")
  public ApiResponse<String> verifyCode(
      @PathVariable("phone") String phone, @PathVariable("code") String code) throws Exception {
    log.info("手机号：" + phone + " 验证码： " + code);
    String trueCode = redisTemplate.opsForValue().get(phone);
    if (trueCode != null && trueCode.equals(code)) {
      return ApiResponse.success("验证成功");
    }
    throw new RuntimeException("验证码不正确");
  }

  @ApiOperation(value = "APP - " + "注销用户-谨慎使用")
  @PutMapping("/removeUser")
  public ApiResponse<String> removeUser() throws Exception {
    UserDto userData = getUserData();
    log.info("注销用户：" + JSONObject.toJSONString(userData));
    sysUserService.removeUser(userData, getToken());
    return ApiResponse.success();
  }

  @ApiOperation(value = "APP - " + "修改用户昵称")
  @PutMapping("/updateUserName/{userName}")
  public ApiResponse<String> updateUserName(@PathVariable("userName") String userName)
      throws Exception {
    return ApiResponse.success(sysUserService.updateUserName(userName, getUserData()));
  }

  @ApiOperation(value = "APP - " + "修改用户操作密码")
  @PutMapping("/updateOperationPass/{phone}/{code}/{pass}")
  public ApiResponse<String> updateOperationPass(
      @PathVariable("phone") String phone,
      @PathVariable("code") String code,
      @PathVariable String pass)
      throws Exception {
    return ApiResponse.success(
        sysUserService.updateOperationPass(phone, code, pass, getUserData().getId()));
  }

  @ApiOperation(value = "APP - " + "修改用户头像")
  @PutMapping("/updateHeadImg")
  public ApiResponse<String> updateHeadImg(@RequestBody SysUser sysuser) throws Exception {
    log.info("修改用户头像，请求参数：" + sysuser.getHeadImg());
    return ApiResponse.success(sysUserService.updateHeadImg(sysuser, getUserData()));
  }

  @ApiOperation(value = "APP - " + "上传实名认证信息")
  @PutMapping("/updateTrue")
  @AccessLimit(times = 1, second = 5)
  public ApiResponse<String> updateTrue(@RequestBody SysUser sysuser) throws Exception {
    return ApiResponse.success(sysUserService.updateTrue(sysuser, getUserData()));
  }

  @ApiOperation(value = "APP - " + "上传实名认证信息")
  @PutMapping("/updateTrue/{code}")
  @AccessLimit(times = 1, second = 5)
  public ApiResponse<String> updateTrue(
      @RequestBody SysUser sysuser, @PathVariable("code") String code) throws Exception {
    SysUser sysUser = sysUserService.selectDataById(sysuser.getId());
    String trueCode = redisTemplate.opsForValue().get(sysUser.getPhone());
    if (trueCode != null && code.equals(trueCode)) {
      return ApiResponse.success(sysUserService.updateTrue(sysuser, getUserData()));
    } else {
      return ApiResponse.fail("验证码错误");
    }
  }

  @ApiOperation(value = "APP - " + "根据验证码修改用户手机号")
  @PutMapping("/updateUserCode/{oldPhone}/{newPhone}/{code}")
  public ApiResponse<UserInfo> updateUserPhone(
      @PathVariable("oldPhone") String oldPhone,
      @PathVariable("newPhone") String newPhone,
      @PathVariable("code") String code)
      throws Exception {
    String trueCode = redisTemplate.opsForValue().get(newPhone);
    if (trueCode != null && trueCode.equals(code)) {
      UserDto userData = getUserData();
      if (userData.getPhone().equals(oldPhone)) {
        UserInfo res = sysUserService.updatePhone(getToken(), newPhone);
        return ApiResponse.success(res);
      } else {
        throw new TokenInvalidException("令牌不匹配");
      }
    }
    throw new RuntimeException("验证码不正确");
  }
}
