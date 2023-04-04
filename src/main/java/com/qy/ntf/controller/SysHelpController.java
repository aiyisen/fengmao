package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysHelpAddDto;
import com.qy.ntf.bean.dto.SysHelpDto;
import com.qy.ntf.bean.dto.SysHelpSelectDto;
import com.qy.ntf.bean.dto.SysHelpUpdateDto;
import com.qy.ntf.bean.entity.SysHelp;
import com.qy.ntf.service.SysHelpService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 帮助中心 控制层接口
 */
@Api(tags = {"帮助中心 api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/sysHelp")
public class SysHelpController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 帮助中心 服务 */
  @Autowired private SysHelpService sysHelpService;

  @ApiOperation(value = "帮助中心详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysHelpDto> detail(@PathVariable("id") Long id) {
    SysHelpDto dto = sysHelpService.getSysHelpById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "ADMIN - " + "帮助中心添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysHelpAddDto dto) {
    setCreateUserInfo(dto);

    sysHelpService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "帮助中心修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysHelpUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysHelpService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "帮助中心修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysHelpDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysHelpDto dto = new SysHelpDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysHelpService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "帮助中心删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysHelpService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "GENERAL - " + "帮助中心分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysHelpDto>> listByPage(
      @RequestBody PageSelectParam<SysHelpSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysHelp> queryWrapper = new LambdaQueryWrapper<SysHelp>();
    ModelMapper md = new ModelMapper();
    SysHelpDto dto = new SysHelpDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysHelpDto> page =
        sysHelpService.getListByPage(
            SysHelpDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "帮助中心列表")
  @PostMapping("/list")
  public ApiResponse<List<SysHelpDto>> listByPage() {
    return ApiResponse.success(sysHelpService.list());
  }
}
