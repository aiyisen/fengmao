package com.qy.ntf.controller;

import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysHelpDto;
import com.qy.ntf.bean.dto.SysPlateConfigDto;
import com.qy.ntf.bean.dto.SysPlateConfigUpdateDto;
import com.qy.ntf.service.SysHelpService;
import com.qy.ntf.service.SysPlateConfigService;
import com.qy.ntf.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 平台信息 控制层接口
 */
@Api(tags = {"平台信息接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/sysPlateConfig")
public class SysPlateConfigController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 平台信息 服务 */
  @Autowired private SysPlateConfigService sysPlateConfigService;

  //  @ApiOperation(value = "平台信息详情", notes = "根据id查询详情")
  //  @GetMapping("/detail/{id}")
  //  public ApiResponse<SysPlateConfigDto> detail(@PathVariable("id") Long id) {
  //    SysPlateConfigDto dto = sysPlateConfigService.getSysPlateConfigById(id);
  //    if (null != dto) {
  //      return ApiResponse.success(dto);
  //    } else {
  //      return ApiResponse.fail("查询信息不存在");
  //    }
  //  }
  //
  //  @ApiOperation(value = "平台信息添加", notes = "添加")
  //  @PostMapping("/add")
  //  public ApiResponse<Void> add(@RequestBody SysPlateConfigAddDto dto) {
  //    setCreateUserInfo(dto);
  //
  //    sysPlateConfigService.save(dto);
  //    return ApiResponse.success();
  //  }

  @ApiOperation(value = "ADMIN - " + "平台信息修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysPlateConfigUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysPlateConfigService.update(dto);
    return ApiResponse.success();
  }

  //  @ApiOperation(value = "平台信息修改状态", notes = "修改状态")
  //  @PostMapping("/update/state")
  //  public ApiResponse<Void> updateState(@RequestBody SysPlateConfigDto tmpDto) {
  //    ModelMapper md = new ModelMapper();
  //    SysPlateConfigDto dto = new SysPlateConfigDto();
  //    md.map(tmpDto, dto);
  //    setUpdateUserInfo(dto);
  //
  //    sysPlateConfigService.updateState(dto);
  //    return ApiResponse.success();
  //  }
  //
  //  @ApiOperation(value = "平台信息删除", notes = "根据id删除")
  //  @DeleteMapping("/delete/{id}")
  //  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
  //    sysPlateConfigService.delete(id);
  //    return ApiResponse.success();
  //  }
  //
  //  @ApiOperation(value = "平台信息分页列表")
  //  @PostMapping("/page")
  //  public ApiResponse<IPage<SysPlateConfigDto>> listByPage(
  //      @RequestBody PageSelectParam<SysPlateConfigSelectDto> param) {
  //    // 组合查询条件
  //    LambdaQueryWrapper<SysPlateConfig> queryWrapper = new LambdaQueryWrapper<SysPlateConfig>();
  //    ModelMapper md = new ModelMapper();
  //    SysPlateConfigDto dto = new SysPlateConfigDto();
  //    md.map(param.getSelectParam(), dto);
  //    IPage<SysPlateConfigDto> page =
  //        sysPlateConfigService.getListByPage(
  //            SysPlateConfigDto.class,
  //            param.getPageNum(),
  //            Long.parseLong(param.getPageSize().toString()),
  //            queryWrapper);
  //
  //    return ApiResponse.success(page);
  //  }
  //
  //  @ApiOperation(value = "ADMIN - " + "平台信息列表")
  //  @PostMapping("/plateInfo")
  //  public ApiResponse<SysPlateConfigDto> plateInfo() {
  //    return ApiResponse.success(sysPlateConfigService.plateInfo());
  //  }

  @ApiOperation(value = "GENERAL - " + "获取平台信息")
  @PostMapping("/appGetPlate")
  public ApiResponse<SysPlateConfigDto> appGetPlate() {
    return ApiResponse.success(sysPlateConfigService.appGetPlate());
  }

  @Autowired private SysHelpService sysHelpService;

  @ApiOperation(value = "APP - " + "帮助中心列表")
  @PostMapping("/helpList")
  public ApiResponse<List<SysHelpDto>> listByPage() {
    return ApiResponse.success(sysHelpService.list());
  }
}
