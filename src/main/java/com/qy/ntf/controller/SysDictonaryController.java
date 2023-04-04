package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.SysDictonaryAddDto;
import com.qy.ntf.bean.dto.SysDictonaryDto;
import com.qy.ntf.bean.dto.SysDictonaryUpdateDto;
import com.qy.ntf.bean.entity.SysDictonary;
import com.qy.ntf.service.SysDictonaryService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-27 21:29:38 DESC : 控制层接口
 */
@Api(tags = {" api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/sysDictonary")
public class SysDictonaryController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 服务 */
  @Autowired private SysDictonaryService sysDictonaryService;

  @ApiOperation(value = "详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysDictonaryDto> detail(@PathVariable("id") Long id) {
    SysDictonaryDto dto = sysDictonaryService.getSysDictonaryById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "ADMIN - " + "添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysDictonaryAddDto dto) {
    dto.setCreateId(getAdminUserData().getId());
    if (!dto.getAlias().equals("pool_tag") && !dto.getAlias().equals("order_product_back_reason")) {
      throw new RuntimeException(
          "目前仅支持添加或修改聚合池标签 pool_tag 、 权益商城退货理由 order_product_back_reason字典值");
    }
    if (dto.getAlias().equals("pool_tag")) {
      dto.setDicTitle("聚合池标签");
    } else if (dto.getAlias().equals("order_product_back_reason")) {
      dto.setDicTitle("权益商城退货理由");
    }
    sysDictonaryService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysDictonaryUpdateDto dto) {
    dto.setUpdateId(getAdminUserData().getId());
    dto.setUpdateTime(new Date());
    if (dto.getAlias().equals("order_back_address")) {
      String[] split = dto.getThreshold().split(";");
      if (split.length != 3) {
        throw new RuntimeException("对不起，收货地址需严格按照 手机号;收货人姓名;收货地址填写");
      }
      if (!split[0].matches("^[0-9]*$")) {
        throw new RuntimeException("手机号不正确");
      }
    }
    if (dto.getAlias().equals("consignment_fee")) {
      try {
        BigDecimal bigDecimal = new BigDecimal(dto.getThreshold());
        if (bigDecimal.compareTo(new BigDecimal(1)) >= 0
            || bigDecimal.compareTo(new BigDecimal(0)) <= 0) {
          return ApiResponse.fail("请输入正确的占比");
        }
      } catch (Exception e) {
        return ApiResponse.fail("请输入正确的数字");
      }
    }
    if (dto.getAlias().equals("login_send_count")
        || dto.getAlias().equals("vip_pol_meta_multiple")
        || dto.getAlias().equals("no_vip_pol_max_count")
        || dto.getAlias().equals("pool_send_max_meta")
        || dto.getAlias().equals("pool_send_percent")
        || dto.getAlias().equals("invite_user_integer")) {
      try {
        Double.valueOf(dto.getThreshold());
      } catch (NumberFormatException e) {
        throw new RuntimeException("非文本字典值，请输入阿拉伯数字");
      }
    }
    if (dto.getAlias().equals("ddc_url")) {
      if (!dto.getThreshold().startsWith("h")) {
        throw new RuntimeException("合约地址必须http开始");
      }
    }
    sysDictonaryService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysDictonaryDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysDictonaryDto dto = new SysDictonaryDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysDictonaryService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    Optional<SysDictonaryDto> sysDictonaryDto =
        sysDictonaryService.selectDataById(SysDictonaryDto.class, id);
    if (!sysDictonaryDto.isPresent()) throw new RuntimeException("id异常");
    SysDictonaryDto dto = sysDictonaryDto.get();
    if (!dto.getAlias().equals("pool_tag") && !dto.getAlias().equals("order_product_back_reason")) {
      throw new RuntimeException("目前仅支持删除聚合池标签 pool_tag 、 权益商城退货理由 order_product_back_reason字典值");
    }
    sysDictonaryService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysDictonaryDto>> listByPage(@RequestBody PageSelectParam param) {
    // 组合查询条件
    LambdaQueryWrapper<SysDictonary> queryWrapper = new LambdaQueryWrapper<SysDictonary>();
    IPage<SysDictonaryDto> page =
        sysDictonaryService.getListByPage(
            SysDictonaryDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "GENERAL - " + "根据别名查询字典值")
  @PostMapping("/list/{alias}")
  public ApiResponse<String> listByPage(@PathVariable("alias") String alias) {
    return ApiResponse.success(sysDictonaryService.getDicByAlias(alias));
  }

  @ApiOperation(value = "GENERAL - " + "根据别名查询字典值--返回数组")
  @PostMapping("/valueList/{alias}")
  public ApiResponse<List<String>> valueList(@PathVariable("alias") String alias) {
    return ApiResponse.success(sysDictonaryService.valueList(alias));
  }
}
