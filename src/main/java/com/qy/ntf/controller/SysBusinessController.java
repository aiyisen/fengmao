package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysBusinessAddDto;
import com.qy.ntf.bean.dto.SysBusinessDto;
import com.qy.ntf.bean.dto.SysBusinessSelectDto;
import com.qy.ntf.bean.dto.SysBusinessUpdateDto;
import com.qy.ntf.bean.entity.SysBusiness;
import com.qy.ntf.service.SysBusinessService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 商务合作记录 控制层接口
 */
@Api(tags = {"商务合作记录 api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/sysBusiness")
public class SysBusinessController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 商务合作记录 服务 */
  @Autowired private SysBusinessService sysBusinessService;

  @ApiOperation(value = "商务合作记录详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysBusinessDto> detail(@PathVariable("id") Long id) {
    SysBusinessDto dto = sysBusinessService.getSysBusinessById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "APP - " + "商务合作记录添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysBusinessAddDto dto) {
    setCreateUserInfo(dto);

    sysBusinessService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "商务合作记录修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysBusinessUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysBusinessService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "商务合作记录修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysBusinessDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysBusinessDto dto = new SysBusinessDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysBusinessService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "商务合作记录删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysBusinessService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "商务合作记录分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysBusinessDto>> listByPage(
      @RequestBody PageSelectParam<SysBusinessSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysBusiness> queryWrapper = new LambdaQueryWrapper<SysBusiness>();
    ModelMapper md = new ModelMapper();
    SysBusinessDto dto = new SysBusinessDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysBusinessDto> page =
        sysBusinessService.getListByPage(
            SysBusinessDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "商务合作记录列表")
  @PostMapping("/list")
  public ApiResponse<List<SysBusinessDto>> listByPage(@RequestBody SysBusinessDto dto) {
    return ApiResponse.success(sysBusinessService.list(dto));
  }
}
