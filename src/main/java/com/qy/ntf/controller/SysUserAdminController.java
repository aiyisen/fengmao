package com.qy.ntf.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.entity.SysUserAdmin;
import com.qy.ntf.bean.param.UpdateRuleParam;
import com.qy.ntf.service.SysUserAdminService;
import com.qy.ntf.service.SysUserService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import com.qy.ntf.util.llPay.H5OpenacctDemo;
import com.qy.ntf.util.llPay.LLianPayAccpSignature;
import com.qy.ntf.util.llPay.OpenacctApplyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author 王振读 2022-05-31 22:14:55 DESC : 管理员表 控制层接口
 */
@Api(tags = {"管理员表 api接口"})
@RestController
@Slf4j
@RequestMapping("/sysUserAdmin")
public class SysUserAdminController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 管理员表 服务 */
  @Autowired private SysUserAdminService sysUserAdminService;
  // 新增用户连连账户开户接口
  @RequestMapping(method = RequestMethod.GET, value = "/openUser")
  public ApiResponse<OpenacctApplyResult> openUser() {
    UserDto userData = getUserData();
    OpenacctApplyResult openacctApplyResult = H5OpenacctDemo.innerUser(userData.getPhone());
    return ApiResponse.success(openacctApplyResult);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/userOpenNotify")
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
        log.info("验签通过！！！");
        JSONObject user = JSONObject.parseObject(stringBuilder.toString());
        String mobile = user.getString("user_id");
        sysUserService.updateUserOpening(mobile);

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

  @ApiOperation(value = "管理员表详情", notes = "根据id查询详情", hidden = true)
  @GetMapping("/detail/{id}")
  public ApiResponse<SysUserAdminDto> detail(@PathVariable("id") Long id) {
    SysUserAdminDto dto = sysUserAdminService.getSysUserAdminById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "管理员表添加", notes = "添加", hidden = true)
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysUserAdminAddDto dto) {
    setCreateUserInfo(dto);

    sysUserAdminService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "管理员表修改", notes = "修改", hidden = true)
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysUserAdminUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysUserAdminService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "管理员表修改状态", notes = "修改状态", hidden = true)
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysUserAdminDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysUserAdminDto dto = new SysUserAdminDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysUserAdminService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "管理员表删除", notes = "根据id删除", hidden = true)
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysUserAdminService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "管理员表分页列表", hidden = true)
  @PostMapping("/page")
  public ApiResponse<IPage<SysUserAdminDto>> listByPage(
      @RequestBody PageSelectParam<SysUserAdminSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysUserAdmin> queryWrapper = new LambdaQueryWrapper<SysUserAdmin>();
    ModelMapper md = new ModelMapper();
    SysUserAdminDto dto = new SysUserAdminDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysUserAdminDto> page =
        sysUserAdminService.getListByPage(
            SysUserAdminDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @Autowired private SysUserService sysUserService;

  @ApiOperation(value = "ADMIN - " + "系统用户分页列表")
  @PostMapping("/sysUserPage")
  public ApiResponse<IPage<UserDto>> sysUserPage(@RequestBody PageSelectParam<SysUser> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
    if (param.getSelectParam().getUsername() != null) {
      queryWrapper.like(SysUser::getUsername, "%" + param.getSelectParam().getUsername() + "%");
    }
    if (param.getSelectParam().getPhone() != null) {
      queryWrapper.like(SysUser::getPhone, "%" + param.getSelectParam().getPhone() + "%");
    }
    if (param.getSelectParam().getState() != null) {
      queryWrapper.eq(SysUser::getState, param.getSelectParam().getState());
    }
    IPage<UserDto> page =
        sysUserService.selectPageList(
            UserDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @Autowired private RedisTemplate<String, String> redisTemplate;

  @ApiOperation(value = "ADMIN - " + "冻结/解冻账户", notes = "冻结/解冻账户", hidden = true)
  @DeleteMapping("/update/state/{id}")
  public ApiResponse<Void> updateState(@PathVariable("id") Long id) {
    SysUser sysUser = sysUserService.selectDataById(id);
    if (sysUser != null) {
      if (sysUser.getState() == 0) {
        sysUser.setState(1);
      } else {
        sysUser.setState(0);
        Object oldToken = redisTemplate.opsForHash().get("allToken", sysUser.getPhone());
        if (oldToken != null) {
          redisTemplate.delete(oldToken.toString());
        }
      }

      sysUserService.updateDataById(sysUser);
      return ApiResponse.success();
    }
    return ApiResponse.fail("无效的id");
  }

  @ApiOperation(value = "管理员表列表", hidden = true)
  @PostMapping("/list")
  public ApiResponse<List<SysUserAdminDto>> listByPage(@RequestBody SysUserAdminDto dto) {
    return ApiResponse.success(sysUserAdminService.list(dto));
  }
  // endRegion
  @ApiOperation(value = "ADMIN - " + "优先购用户列表")
  @PostMapping("/beforeSysUserPage/{streId}")
  public ApiResponse<IPage<UserDto>> beforeSysUserPage(
      @RequestBody PageSelectParam<SysUser> param, @PathVariable("streId") Long streId) {
    // 组合查询条件
    IPage<UserDto> page =
        sysUserService.beforeSysUserPage(
            UserDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            param,
            streId);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "修改限购数量")
  @PostMapping("/updateRuleCount")
  public ApiResponse updateRuleCount(@RequestBody UpdateRuleParam param) {
    // 组合查询条件
    sysUserService.updateRuleCount(param);

    return ApiResponse.success();
  }

  @PostMapping("/uploadBeforeUser/{streId}")
  public ApiResponse<String> importTest(
      @RequestParam("file") MultipartFile file, @PathVariable("streId") Long streId) {
    // 判断上传文件为空返回
    if (file.isEmpty()) {
      return ApiResponse.fail("上传文件不可为空");
    }
    // 判断文件是否是excel文件
    String filename = file.getOriginalFilename();
    if (!StringUtils.isEmpty(filename)
        && (!filename.matches("^.+\\.(?i)(xls)$") && !filename.matches("^.+\\.(?i)(xlsx)$"))) {
      return ApiResponse.fail("上传文件格式错误，请上传后缀为.xls或.xlsx的文件");
    }
    // 读取文件
    ExcelReader readerExcel = null;
    try {
      readerExcel = ExcelUtil.getReader(file.getInputStream());
    } catch (Exception e) {
      e.printStackTrace();
    }

    int total = readerExcel.getRowCount();
    // 存读取到的内容
    if (total > 2) {
      Set<Map<String, Object>> mobiles = new HashSet<>();
      List<List<Object>> read = readerExcel.read(2);
      System.out.println(JSONObject.toJSONString(read));
      if (read != null && read.size() > 0) {
        for (List<Object> cell : read) {
          if (cell != null
              && cell.size() > 0
              && cell.get(0) != null
              && Strings.isNotEmpty(cell.get(0).toString())) {
            HashMap<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("mobile", cell.get(0).toString());
            tmpMap.put("ruleCount", cell.get(1).toString());
            mobiles.add(tmpMap);
          }
        }
        String s = sysUserService.addImportUser(mobiles, streId, 1L);
        return ApiResponse.success(s);
      }
      return ApiResponse.success();
    } else {
      return ApiResponse.fail("导入表格数据为空");
    }
  }

  @ApiOperation(value = "ADMIN - " + "添加优先购用户")
  @PostMapping("/addBeforeUser/{streId}/{count}")
  public ApiResponse<String> addBeforeUser(
      @RequestBody List<String> phones,
      @PathVariable("streId") Long streId,
      @PathVariable("count") Long count) {
    if (phones != null && phones.size() > 0) {
      sysUserService.addBeforeUser(phones, streId, count);

      return ApiResponse.success("success");
    } else {
      return ApiResponse.fail("用户列表不可为空");
    }
  }

  @ApiOperation(value = "ADMIN - " + "删除优先购用户")
  @PostMapping("/deleteBeforeUser/{streId}")
  public ApiResponse<String> deleteBeforeUser(
      @RequestBody List<String> phones, @PathVariable("streId") Long streId) {
    if (phones.size() == 0) {
      return ApiResponse.fail("请求参数不可为空");
    }
    sysUserService.deleteBeforeUser(phones, streId);
    return ApiResponse.success("success");
  }

  @ApiOperation(value = "根据持有藏品查询用户列表-无分页")
  @PostMapping("/findUserByTreaId")
  public ApiResponse<List<SysUser>> findUserByTreaId(@RequestBody List<Long> ids) {
    if (ids != null && ids.size() > 0) {
      List<SysUser> sysUsers = sysUserService.findUserByTreaId(ids);
      return ApiResponse.success(sysUsers);
    } else {
      throw new RuntimeException("持有藏品不可为空");
    }
  }

  // 登录返回是否优先购用户
  // 根据手机号查询用户信息返回是否优先购
  // 根据持有藏品筛选用户列表
  // 首页藏品，盲盒，下单判定用户是否优先购

  // 空投功能

}
