package com.qy.ntf.controller;

import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.service.SysUserService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.EncryptUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.controller @ClassName:
 * SystemController @Author: 王振读 @Description: ${description} @Date: 2021/11/26 9:31 @Version: 1.0
 */
@RestController
@RequestMapping("/app")
// @CrossOrigin(origins = "*")
@Api(tags = {"关于我们，协议相关内容"})
public class AppController extends BaseController {

  @Autowired private SysUserService userService;

  @Value("${password-key}")
  private String passwordKey;

  @ApiOperation(value = "APP - 修改用户密码")
  @PostMapping("updateUserPassword")
  public ApiResponse<Void> updateUserPassword(String oldPassword, String newPassword) {
    UserDto userData = getUserData();
    SysUser userDto = userService.selectDataById(userData.getId());
    if (EncryptUtil.getInstance().MD5(oldPassword, passwordKey).equals(userDto.getPass())) {
      userData.setPass(EncryptUtil.getInstance().MD5(newPassword, passwordKey));
      userData.setUpdateTime(new Date());
      userData.setUpdateTime(new Date());
      userService.updateDataById(userData.getId(), userData);
    } else {
      return ApiResponse.fail("原密码不正确");
    }
    return ApiResponse.success();
  }
  // TODO 生产环境需要关闭该接口
  //  @PutMapping("updateUserBalance/{total}")
  //  public ApiResponse<Void> updateUserBalance(@PathVariable("total") Integer total) {
  //    SysUser sysUser = userService.selectDataById(getUserData().getId());
  //    sysUser.setBalance(sysUser.getBalance().add(new BigDecimal(total)));
  //    userService.updateDataById(sysUser);
  //    return ApiResponse.success();
  //  }
}
