package com.qy.ntf.controller;

import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysAppVersionDto;
import com.qy.ntf.bean.dto.SysAppVersionUpdateDto;
import com.qy.ntf.service.SysAppVersionService;
import com.qy.ntf.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-07-22 00:02:04 DESC : 控制层接口
 */
@Api(tags = {" api接口"})
@RestController
@Slf4j
@RequestMapping("/sysAppVersion")
public class SysAppVersionController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 服务 */
  @Autowired private SysAppVersionService sysAppVersionService;

  @ApiOperation(value = "修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysAppVersionUpdateDto dto) {
    sysAppVersionService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "列表")
  @PostMapping("/list")
  public ApiResponse<List<SysAppVersionDto>> listByPage(@RequestBody SysAppVersionDto dto) {
    return ApiResponse.success(sysAppVersionService.list(dto));
  }
}
