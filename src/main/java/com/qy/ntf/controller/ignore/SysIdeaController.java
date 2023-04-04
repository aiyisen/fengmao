package com.qy.ntf.controller.ignore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysIdeaAddDto;
import com.qy.ntf.bean.dto.SysIdeaDto;
import com.qy.ntf.bean.dto.SysIdeaSelectDto;
import com.qy.ntf.bean.dto.SysIdeaUpdateDto;
import com.qy.ntf.bean.entity.SysIdea;
import com.qy.ntf.service.SysIdeaService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 反馈建议 控制层接口
 */
@Api(tags = {"反馈建议 api接口"})
@RestController
@RequestMapping("/sysIdea")
public class SysIdeaController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 反馈建议 服务 */
  @Autowired private SysIdeaService sysIdeaService;

  @ApiOperation(value = "反馈建议详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysIdeaDto> detail(@PathVariable("id") Long id) {
    SysIdeaDto dto = sysIdeaService.getSysIdeaById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "APP - " + "反馈建议添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysIdeaAddDto dto) {
    setCreateUserInfo(dto);

    sysIdeaService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "反馈建议修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysIdeaUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysIdeaService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "反馈建议修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysIdeaDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysIdeaDto dto = new SysIdeaDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysIdeaService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "反馈建议删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysIdeaService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "反馈建议分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysIdeaDto>> listByPage(
      @RequestBody PageSelectParam<SysIdeaSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysIdea> queryWrapper = new LambdaQueryWrapper<SysIdea>();
    ModelMapper md = new ModelMapper();
    SysIdeaDto dto = new SysIdeaDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysIdeaDto> page =
        sysIdeaService.getListByPage(
            SysIdeaDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "反馈建议列表")
  @PostMapping("/list")
  public ApiResponse<List<SysIdeaDto>> listByPage(@RequestBody SysIdeaDto dto) {
    return ApiResponse.success(sysIdeaService.list(dto));
  }
}
