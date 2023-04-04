package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysAreaAddDto;
import com.qy.ntf.bean.dto.SysAreaDto;
import com.qy.ntf.bean.dto.SysAreaSelectDto;
import com.qy.ntf.bean.dto.SysAreaUpdateDto;
import com.qy.ntf.bean.entity.SysArea;
import com.qy.ntf.service.SysAreaService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 全国区域信息表 控制层接口
 */
@Api(tags = {"全国区域信息表 api接口"})
@RestController
@ApiIgnore
// @CrossOrigin(origins = "*")
@RequestMapping("/sysArea")
public class SysAreaController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 全国区域信息表 服务 */
  @Autowired private SysAreaService sysAreaService;

  @ApiOperation(value = "全国区域信息表详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysAreaDto> detail(@PathVariable("id") Long id) {
    SysAreaDto dto = sysAreaService.getSysAreaById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "全国区域信息表添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysAreaAddDto dto) {
    //    setCreateUserInfo(dto);

    sysAreaService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "全国区域信息表修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysAreaUpdateDto dto) {
    //    setUpdateUserInfo(dto);

    sysAreaService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "全国区域信息表修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysAreaDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysAreaDto dto = new SysAreaDto();
    md.map(tmpDto, dto);
    //    setUpdateUserInfo(dto);

    sysAreaService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "全国区域信息表删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysAreaService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "全国区域信息表分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysAreaDto>> listByPage(
      @RequestBody PageSelectParam<SysAreaSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysArea> queryWrapper = new LambdaQueryWrapper<SysArea>();
    if (param.getSelectParam().getPid() == null) {
      queryWrapper.eq(SysArea::getPid, 0);
    } else {
      queryWrapper.eq(SysArea::getPid, param.getSelectParam().getPid());
    }
    IPage<SysAreaDto> page =
        sysAreaService.getListByPage(
            SysAreaDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "全国区域信息表列表")
  @PostMapping("/list")
  public ApiResponse<List<SysAreaDto>> listByPage(@RequestBody SysAreaDto dto) {
    return ApiResponse.success(sysAreaService.list(dto));
  }
}
