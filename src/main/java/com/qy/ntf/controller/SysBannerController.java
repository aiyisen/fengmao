package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysBannerAddDto;
import com.qy.ntf.bean.dto.SysBannerDto;
import com.qy.ntf.bean.dto.SysBannerSelectDto;
import com.qy.ntf.bean.dto.SysBannerUpdateDto;
import com.qy.ntf.bean.entity.SysBanner;
import com.qy.ntf.service.SysBannerService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统轮播 控制层接口
 */
@Api(tags = {"系统轮播 api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/sysBanner")
public class SysBannerController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 系统轮播 服务 */
  @Autowired private SysBannerService sysBannerService;

  @ApiOperation(value = "系统轮播详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysBannerDto> detail(@PathVariable("id") Long id) {
    SysBannerDto dto = sysBannerService.getSysBannerById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "ADMIN - " + "系统轮播添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysBannerAddDto dto) {
    setCreateUserInfo(dto);

    sysBannerService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "系统轮播修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysBannerUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysBannerService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统轮播修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysBannerDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysBannerDto dto = new SysBannerDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysBannerService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "系统轮播删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysBannerService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "系统轮播分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysBannerDto>> listByPage(
      @RequestBody PageSelectParam<SysBannerSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysBanner> queryWrapper = new LambdaQueryWrapper<SysBanner>();
    IPage<SysBannerDto> page =
        sysBannerService.getListByPage(
            SysBannerDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "APP - " + "首页轮播列表")
  @ApiModelProperty
  @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "类型：0首页1权益商城", required = true)})
  @PostMapping("/list/{type}")
  public ApiResponse<List<SysBannerDto>> listByPage(@PathVariable("type") Integer type) {
    return ApiResponse.success(sysBannerService.list(type));
  }
}
