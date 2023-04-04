package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.SysSeriesAddDto;
import com.qy.ntf.bean.dto.SysSeriesDto;
import com.qy.ntf.bean.dto.SysSeriesSelectDto;
import com.qy.ntf.bean.dto.SysSeriesUpdateDto;
import com.qy.ntf.bean.entity.SysSeries;
import com.qy.ntf.service.SysSeriesService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @author 王振读 2022-07-15 19:26:43 DESC : 系列主体 控制层接口
 */
@Api(tags = {"系列主体 api接口"})
@RestController
@Slf4j
@RequestMapping("/sysSeries")
public class SysSeriesController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 系列主体 服务 */
  @Autowired private SysSeriesService sysSeriesService;

  @ApiOperation(value = "ADMIN - " + "系列主体添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(
      @RequestBody @Valid SysSeriesAddDto dto, BindingResult bindingResult) {
    if (dto.getBgcolor() == null) throw new RuntimeException("背景顔色格式不正确，举例：#152432 #000000");
    String regex = "^#([0-9a-fA-F]{6})$";
    boolean matches = dto.getBgcolor().matches(regex);
    if (!matches) {
      throw new RuntimeException("背景顔色格式不正确，举例：#15F4F2 #000000");
    }
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), -1);
    } else {
      sysSeriesService.save(dto, getAdminUserData());
      return ApiResponse.success();
    }
  }

  @ApiOperation(value = "ADMIN - " + "系列主体修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(
      @RequestBody @Valid SysSeriesUpdateDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), -1);
    } else {
      sysSeriesService.update(dto, getAdminUserData());
      return ApiResponse.success();
    }
  }

  @ApiOperation(value = "ADMIN - " + "系列主体修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysSeriesDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysSeriesDto dto = new SysSeriesDto();
    md.map(tmpDto, dto);

    sysSeriesService.updateState(dto, getAdminUserData());
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "系列主体删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysSeriesService.delete(id, getAdminUserData());
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "系列主体分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysSeriesDto>> listByPage(
      @RequestBody PageSelectParam<SysSeriesSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysSeries> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.ne(BaseEntity::getState, 0);
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    ModelMapper md = new ModelMapper();
    SysSeriesDto dto = new SysSeriesDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysSeriesDto> page =
        sysSeriesService.getListByPage(
            SysSeriesDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "APP- " + "系列主体分页列表")
  @PostMapping("/appPage")
  public ApiResponse<IPage<SysSeriesDto>> appPage(
      @RequestBody PageSelectParam<SysSeriesSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysSeries> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(BaseEntity::getState, 1);
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    ModelMapper md = new ModelMapper();
    SysSeriesDto dto = new SysSeriesDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysSeriesDto> page =
        sysSeriesService.getListByPage(
            SysSeriesDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }
}
