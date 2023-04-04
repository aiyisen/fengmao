package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.StoreProPoolColAddDto;
import com.qy.ntf.bean.dto.StoreProPoolDto;
import com.qy.ntf.service.StoreProPoolColService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池藏品收藏 控制层接口
 */
@Api(tags = {"聚合池藏品收藏 api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/storeProPoolCol")
public class StoreProPoolColController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 聚合池藏品收藏 服务 */
  @Autowired private StoreProPoolColService storeProPoolColService;

  @ApiOperation(value = "APP - " + "聚合池藏品收藏添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody StoreProPoolColAddDto dto) {
    setCreateUserInfo(dto);
    dto.setUId(getUserData().getId());
    storeProPoolColService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "APP - " + "聚合池藏品收藏分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<StoreProPoolDto>> listByPage(
      @RequestBody PageSelectParam<Object> param) {
    // 组合查询条件
    IPage<StoreProPoolDto> page = storeProPoolColService.getListByPage(param, getUserData());
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "APP - " + "删除聚合池藏品收藏")
  @PostMapping("/deleteCollect")
  public ApiResponse<Object> deleteCollect(@RequestBody List<String> param) {
    // 组合查询条件
    storeProPoolColService.deleteCollect(param, getUserData());
    return ApiResponse.success();
  }
}
