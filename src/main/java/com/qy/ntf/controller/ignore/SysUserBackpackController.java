package com.qy.ntf.controller.ignore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysUserBackpackAddDto;
import com.qy.ntf.bean.dto.SysUserBackpackDto;
import com.qy.ntf.bean.dto.SysUserBackpackSelectDto;
import com.qy.ntf.bean.dto.SysUserBackpackUpdateDto;
import com.qy.ntf.bean.entity.SysUserBackpack;
import com.qy.ntf.service.SysUserBackpackService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 用户背包（记录虚拟商品购买订单标识） 控制层接口
 */
@Api(tags = {"用户背包（记录虚拟商品购买订单标识） api接口"})
@RestController
@ApiIgnore
@RequestMapping("/sysUserBackpack")
public class SysUserBackpackController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 用户背包（记录虚拟商品购买订单标识） 服务 */
  @Autowired private SysUserBackpackService sysUserBackpackService;

  @ApiOperation(value = "用户背包（记录虚拟商品购买订单标识）详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysUserBackpackDto> detail(@PathVariable("id") Long id) {
    SysUserBackpackDto dto = sysUserBackpackService.getSysUserBackpackById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "用户背包（记录虚拟商品购买订单标识）添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysUserBackpackAddDto dto) {
    setCreateUserInfo(dto);

    sysUserBackpackService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "用户背包（记录虚拟商品购买订单标识）修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysUserBackpackUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysUserBackpackService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "用户背包（记录虚拟商品购买订单标识）修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysUserBackpackDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysUserBackpackDto dto = new SysUserBackpackDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysUserBackpackService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "用户背包（记录虚拟商品购买订单标识）删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysUserBackpackService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "用户背包（记录虚拟商品购买订单标识）分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysUserBackpackDto>> listByPage(
      @RequestBody PageSelectParam<SysUserBackpackSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysUserBackpack> queryWrapper = new LambdaQueryWrapper<SysUserBackpack>();
    ModelMapper md = new ModelMapper();
    SysUserBackpackDto dto = new SysUserBackpackDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysUserBackpackDto> page =
        sysUserBackpackService.getListByPage(
            SysUserBackpackDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "用户背包（记录虚拟商品购买订单标识）列表")
  @PostMapping("/list")
  public ApiResponse<List<SysUserBackpackDto>> listByPage(@RequestBody SysUserBackpackDto dto) {
    return ApiResponse.success(sysUserBackpackService.list(dto));
  }
}
