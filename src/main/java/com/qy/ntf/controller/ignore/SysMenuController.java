package com.qy.ntf.controller.ignore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysMenuAddDto;
import com.qy.ntf.bean.dto.SysMenuDto;
import com.qy.ntf.bean.dto.SysMenuSelectDto;
import com.qy.ntf.bean.dto.SysMenuUpdateDto;
import com.qy.ntf.bean.entity.SysMenu;
import com.qy.ntf.service.SysMenuService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统权限 控制层接口
 */
@Api(tags = {"系统权限 api接口"})
@RestController
@ApiIgnore
@RequestMapping("/sysMenu")
public class SysMenuController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 系统权限 服务 */
  @Autowired private SysMenuService sysMenuService;

  @ApiOperation(value = "系统权限详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysMenuDto> detail(@PathVariable("id") Long id) {
    SysMenuDto dto = sysMenuService.getSysMenuById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "系统权限添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysMenuAddDto dto) {
    setCreateUserInfo(dto);

    sysMenuService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统权限修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysMenuUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysMenuService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统权限修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysMenuDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysMenuDto dto = new SysMenuDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysMenuService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统权限删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysMenuService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统权限分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysMenuDto>> listByPage(
      @RequestBody PageSelectParam<SysMenuSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>();
    ModelMapper md = new ModelMapper();
    SysMenuDto dto = new SysMenuDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysMenuDto> page =
        sysMenuService.getListByPage(
            SysMenuDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "系统权限列表")
  @PostMapping("/list")
  public ApiResponse<List<SysMenuDto>> listByPage(@RequestBody SysMenuDto dto) {
    return ApiResponse.success(sysMenuService.list(dto));
  }
}
