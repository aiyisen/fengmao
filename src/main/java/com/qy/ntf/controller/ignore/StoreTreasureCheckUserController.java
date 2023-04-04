package com.qy.ntf.controller.ignore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.StoreTreasureCheckUserAddDto;
import com.qy.ntf.bean.dto.StoreTreasureCheckUserDto;
import com.qy.ntf.bean.dto.StoreTreasureCheckUserSelectDto;
import com.qy.ntf.bean.dto.StoreTreasureCheckUserUpdateDto;
import com.qy.ntf.bean.entity.StoreTreasureCheckUser;
import com.qy.ntf.service.StoreTreasureCheckUserService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 超亲申购中奖名单 控制层接口
 */
@Api(tags = {"超亲申购中奖名单 api接口"})
@RestController
@ApiIgnore
@RequestMapping("/storeTreasureCheckUser")
public class StoreTreasureCheckUserController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 超亲申购中奖名单 服务 */
  @Autowired private StoreTreasureCheckUserService storeTreasureCheckUserService;

  @ApiOperation(value = "超亲申购中奖名单详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<StoreTreasureCheckUserDto> detail(@PathVariable("id") Long id) {
    StoreTreasureCheckUserDto dto = storeTreasureCheckUserService.getStoreTreasureCheckUserById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "超亲申购中奖名单添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody StoreTreasureCheckUserAddDto dto) {
    setCreateUserInfo(dto);

    storeTreasureCheckUserService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "超亲申购中奖名单修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody StoreTreasureCheckUserUpdateDto dto) {
    setUpdateUserInfo(dto);

    storeTreasureCheckUserService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "超亲申购中奖名单修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody StoreTreasureCheckUserDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreTreasureCheckUserDto dto = new StoreTreasureCheckUserDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    storeTreasureCheckUserService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "超亲申购中奖名单删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    storeTreasureCheckUserService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "超亲申购中奖名单分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<StoreTreasureCheckUserDto>> listByPage(
      @RequestBody PageSelectParam<StoreTreasureCheckUserSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<StoreTreasureCheckUser> queryWrapper =
        new LambdaQueryWrapper<StoreTreasureCheckUser>();
    ModelMapper md = new ModelMapper();
    StoreTreasureCheckUserDto dto = new StoreTreasureCheckUserDto();
    md.map(param.getSelectParam(), dto);
    IPage<StoreTreasureCheckUserDto> page =
        storeTreasureCheckUserService.getListByPage(
            StoreTreasureCheckUserDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "超亲申购中奖名单列表")
  @PostMapping("/list")
  public ApiResponse<List<StoreTreasureCheckUserDto>> listByPage(
      @RequestBody StoreTreasureCheckUserDto dto) {
    return ApiResponse.success(storeTreasureCheckUserService.list(dto));
  }
}
