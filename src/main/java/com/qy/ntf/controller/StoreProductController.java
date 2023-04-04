package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.customResult.NeedBuyTreasure;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.StoreProduct;
import com.qy.ntf.service.StoreProductService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 权益商城-普通专区/特权专区/兑换专区 主体 控制层接口
 */
@Api(tags = {"权益商城-普通专区、特权专区、兑换专区 主体 api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/storeProduct")
public class StoreProductController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 权益商城-普通专区/特权专区/兑换专区 主体 服务 */
  @Autowired private StoreProductService storeProductService;

  @ApiOperation(value = "权益商城-普通专区、特权专区、兑换专区 主体详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<StoreProductDto> detail(@PathVariable("id") Long id) {
    StoreProductDto dto = storeProductService.getStoreProductById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "ADMIN - " + "权益商城-普通专区、兑换专区 主体添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(
      @RequestBody @Valid StoreProductAddDto dto, BindingResult bindingResult) {
    setCreateUserInfo(dto);

    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      storeProductService.save(dto);
    }
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "权益商城-普通专区、兑换专区 主体修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(
      @RequestBody @Valid StoreProductUpdateDto dto, BindingResult bindingResult) {
    setUpdateUserInfo(dto);
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      storeProductService.update(dto);
    }
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "特权专区主体添加", notes = "添加")
  @PostMapping("/addLin")
  public ApiResponse<Void> add(
      @RequestBody @Valid StoreProductAddLinDto dto, BindingResult bindingResult) {
    setCreateUserInfo(dto);
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      storeProductService.saveTemp(dto);
    }
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "特权专区 主体修改", notes = "修改")
  @PostMapping("/updateLin")
  public ApiResponse<Void> update(
      @RequestBody @Valid StoreProductAddLinDto dto, BindingResult bindingResult) {
    setUpdateUserInfo(dto);
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      storeProductService.updateTemp(dto);
    }
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "权益商城-普通专区、特权专区、兑换专区 主体修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody StoreProductDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreProductDto dto = new StoreProductDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    storeProductService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "权益商城-普通专区、特权专区、兑换专区 主体删除", notes = "删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {

    storeProductService.deleteById(id, getAdminUserData());
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "权益商城-普通专区、特权专区、兑换专区 主体分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<StoreProductDto>> listByPage(
      @RequestBody PageSelectParam<StoreProductSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<StoreProduct>();
    ModelMapper md = new ModelMapper();
    StoreProductDto dto = new StoreProductDto();
    md.map(param.getSelectParam(), dto);
    if (dto.getProType() != null) {
      queryWrapper.eq(StoreProduct::getProType, dto.getProType());
    }
    if (dto.getProTitle() != null) {
      queryWrapper.like(StoreProduct::getProTitle, dto.getProTitle());
    }
    IPage<StoreProductDto> page =
        storeProductService.getListByPage(
            StoreProductDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "权益商城-普通专区、特权专区、兑换专区 主体列表")
  @PostMapping("/list")
  public ApiResponse<List<StoreProductDto>> listByPage(@RequestBody StoreProductDto dto) {
    return ApiResponse.success(storeProductService.list(dto));
  }

  @ApiOperation(value = "APP - " + "权益商城-普通专区、特权专区、兑换专区 主体列表")
  @PostMapping("/appPageList")
  public ApiResponse<IPage<StoreProductDto>> appPagelist(
      @RequestBody PageSelectParam<StoreProductSelectDto> param) {
    return ApiResponse.success(storeProductService.appPagelist(param, getUserData()));
  }

  @ApiOperation(value = "APP - " + "NFT 特权专区 主体列表")
  @PostMapping("/privilegeList")
  public ApiResponse<IPage<StoreProductDto>> privilegeList(
      @RequestBody PageSelectParam<Integer> param) {
    return ApiResponse.success(storeProductService.privilegeList(param, getUserData()));
  }

  @ApiOperation(value = "APP - " + "权益商城-特权专区， 详情页面获取所需商品列表")
  @GetMapping("/getNeedTreasure/{id}")
  public ApiResponse<List<NeedBuyTreasure>> getNeedTreasure(@PathVariable("id") Long id) {
    UserDto userData = getUserData();
    return ApiResponse.success(storeProductService.getNeedTreasure(id, userData));
  }
}
