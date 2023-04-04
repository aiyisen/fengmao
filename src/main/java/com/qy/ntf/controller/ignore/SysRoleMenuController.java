package com.qy.ntf.controller.ignore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysRoleMenuAddDto;
import com.qy.ntf.bean.dto.SysRoleMenuDto;
import com.qy.ntf.bean.dto.SysRoleMenuSelectDto;
import com.qy.ntf.bean.dto.SysRoleMenuUpdateDto;
import com.qy.ntf.bean.entity.SysRoleMenu;
import com.qy.ntf.service.SysRoleMenuService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 角色-菜单 控制层接口
 */
@Api(tags = {"角色-菜单 api接口"})
@RestController
@ApiIgnore
@RequestMapping("/sysRoleMenu")
public class SysRoleMenuController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 角色-菜单 服务 */
  @Autowired private SysRoleMenuService sysRoleMenuService;

  @ApiOperation(value = "角色-菜单详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysRoleMenuDto> detail(@PathVariable("id") Long id) {
    SysRoleMenuDto dto = sysRoleMenuService.getSysRoleMenuById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "角色-菜单添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysRoleMenuAddDto dto) {
    setCreateUserInfo(dto);

    sysRoleMenuService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "角色-菜单修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysRoleMenuUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysRoleMenuService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "角色-菜单修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysRoleMenuDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysRoleMenuDto dto = new SysRoleMenuDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysRoleMenuService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "角色-菜单删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysRoleMenuService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "角色-菜单分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysRoleMenuDto>> listByPage(
      @RequestBody PageSelectParam<SysRoleMenuSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<SysRoleMenu>();
    ModelMapper md = new ModelMapper();
    SysRoleMenuDto dto = new SysRoleMenuDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysRoleMenuDto> page =
        sysRoleMenuService.getListByPage(
            SysRoleMenuDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "角色-菜单列表")
  @PostMapping("/list")
  public ApiResponse<List<SysRoleMenuDto>> listByPage(@RequestBody SysRoleMenuDto dto) {
    return ApiResponse.success(sysRoleMenuService.list(dto));
  }
}
