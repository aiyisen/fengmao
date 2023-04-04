package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.SysOrgAddDto;
import com.qy.ntf.bean.dto.SysOrgDto;
import com.qy.ntf.bean.dto.SysOrgSelectDto;
import com.qy.ntf.bean.dto.SysOrgUpdateDto;
import com.qy.ntf.bean.entity.SysOrg;
import com.qy.ntf.service.SysOrgService;
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
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-07-05 20:10:31 DESC : 发行方 控制层接口
 */
@Api(tags = {"发行方 api接口"})
@RestController
@Slf4j
@RequestMapping("/sysOrg")
public class SysOrgController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 发行方 服务 */
  @Autowired private SysOrgService sysOrgService;

  @ApiOperation(value = "ADMIN - " + "发行方添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody @Valid SysOrgAddDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      sysOrgService.save(dto, getAdminUserData());
      return ApiResponse.success();
    }
  }

  @ApiOperation(value = "ADMIN - " + "发行方修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(
      @RequestBody @Valid SysOrgUpdateDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      sysOrgService.update(dto, getAdminUserData());
      return ApiResponse.success();
    }
  }

  @ApiOperation(value = "发行方删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysOrgService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "发行方分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysOrgDto>> listByPage(
      @RequestBody PageSelectParam<SysOrgSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysOrg> queryWrapper = new LambdaQueryWrapper<SysOrg>();
    queryWrapper.eq(BaseEntity::getState, 1);
    ModelMapper md = new ModelMapper();
    SysOrgDto dto = new SysOrgDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysOrgDto> page =
        sysOrgService.getListByPage(
            SysOrgDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }
}
