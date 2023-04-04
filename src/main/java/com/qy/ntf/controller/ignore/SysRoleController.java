package com.qy.ntf.controller.ignore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysRoleAddDto;
import com.qy.ntf.bean.dto.SysRoleDto;
import com.qy.ntf.bean.dto.SysRoleSelectDto;
import com.qy.ntf.bean.dto.SysRoleUpdateDto;
import com.qy.ntf.bean.entity.SysRole;
import com.qy.ntf.service.SysRoleService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统角色 控制层接口
 */
@Api(tags = {"系统角色 api接口"})
@RestController
@ApiIgnore
@RequestMapping("/sysRole")
public class SysRoleController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 系统角色 服务 */
  @Autowired private SysRoleService sysRoleService;

  @ApiOperation(value = "系统角色详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysRoleDto> detail(@PathVariable("id") Long id) {
    SysRoleDto dto = sysRoleService.getSysRoleById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "系统角色添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysRoleAddDto dto) {
    setCreateUserInfo(dto);

    sysRoleService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统角色修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysRoleUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysRoleService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统角色修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysRoleDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysRoleDto dto = new SysRoleDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysRoleService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统角色删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysRoleService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统角色分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysRoleDto>> listByPage(
      @RequestBody PageSelectParam<SysRoleSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<SysRole>();
    ModelMapper md = new ModelMapper();
    SysRoleDto dto = new SysRoleDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysRoleDto> page =
        sysRoleService.getListByPage(
            SysRoleDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "系统角色列表")
  @PostMapping("/list")
  public ApiResponse<List<SysRoleDto>> listByPage(@RequestBody SysRoleDto dto) {
    return ApiResponse.success(sysRoleService.list(dto));
  }
}
