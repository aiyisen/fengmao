package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.StoreCategroyAddDto;
import com.qy.ntf.bean.dto.StoreCategroyDto;
import com.qy.ntf.bean.dto.StoreCategroySelectDto;
import com.qy.ntf.bean.dto.StoreCategroyUpdateDto;
import com.qy.ntf.bean.entity.StoreCategroy;
import com.qy.ntf.service.StoreCategroyService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-08-06 13:01:44 DESC : 藏品分类 控制层接口
 */
@Api(tags = {"藏品分类 api接口"})
@RestController
@Slf4j
@RequestMapping("/storeCategroy")
public class StoreCategroyController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 藏品分类 服务 */
  @Autowired private StoreCategroyService storeCategroyService;

  @ApiOperation(value = "ADMIN - " + "藏品分类添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody StoreCategroyAddDto dto) {
    setCreateUserInfo(dto);

    storeCategroyService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "藏品分类修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody StoreCategroyUpdateDto dto) {
    setUpdateUserInfo(dto);

    storeCategroyService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "藏品分类删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    storeCategroyService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "藏品分类分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<StoreCategroyDto>> listByPage(
      @RequestBody PageSelectParam<StoreCategroySelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<StoreCategroy> queryWrapper = new LambdaQueryWrapper<StoreCategroy>();
    ModelMapper md = new ModelMapper();
    StoreCategroyDto dto = new StoreCategroyDto();
    md.map(param.getSelectParam(), dto);
    IPage<StoreCategroyDto> page =
        storeCategroyService.getListByPage(
            StoreCategroyDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "GENERAL - " + "藏品分类列表")
  @PostMapping("/list")
  public ApiResponse<List<StoreCategroyDto>> listByPage(@RequestBody StoreCategroyDto dto) {
    return ApiResponse.success(storeCategroyService.list(dto));
  }
}
