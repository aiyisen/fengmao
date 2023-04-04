package com.qy.ntf.controller.ignore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.StoreProductTemplateAddDto;
import com.qy.ntf.bean.dto.StoreProductTemplateDto;
import com.qy.ntf.bean.dto.StoreProductTemplateSelectDto;
import com.qy.ntf.bean.dto.StoreProductTemplateUpdateDto;
import com.qy.ntf.bean.entity.StoreProductTemplate;
import com.qy.ntf.service.StoreProductTemplateService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 特权商品材料模板 控制层接口
 */
@Api(tags = {"特权商品材料模板 api接口"})
@RestController
@ApiIgnore
@RequestMapping("/storeProductTemplate")
public class StoreProductTemplateController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 特权商品材料模板 服务 */
  @Autowired private StoreProductTemplateService storeProductTemplateService;

  @ApiOperation(value = "特权商品材料模板详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<StoreProductTemplateDto> detail(@PathVariable("id") Long id) {
    StoreProductTemplateDto dto = storeProductTemplateService.getStoreProductTemplateById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "特权商品材料模板添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody StoreProductTemplateAddDto dto) {

    storeProductTemplateService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "特权商品材料模板修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody StoreProductTemplateUpdateDto dto) {
    setUpdateUserInfo(dto);

    storeProductTemplateService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "特权商品材料模板修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody StoreProductTemplateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreProductTemplateDto dto = new StoreProductTemplateDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    storeProductTemplateService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "特权商品材料模板删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    storeProductTemplateService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "特权商品材料模板分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<StoreProductTemplateDto>> listByPage(
      @RequestBody PageSelectParam<StoreProductTemplateSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<StoreProductTemplate> queryWrapper =
        new LambdaQueryWrapper<StoreProductTemplate>();
    ModelMapper md = new ModelMapper();
    StoreProductTemplateDto dto = new StoreProductTemplateDto();
    md.map(param.getSelectParam(), dto);
    IPage<StoreProductTemplateDto> page =
        storeProductTemplateService.getListByPage(
            StoreProductTemplateDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "特权商品材料模板列表")
  @PostMapping("/list")
  public ApiResponse<List<StoreProductTemplateDto>> listByPage(
      @RequestBody StoreProductTemplateDto dto) {
    return ApiResponse.success(storeProductTemplateService.list(dto));
  }
}
