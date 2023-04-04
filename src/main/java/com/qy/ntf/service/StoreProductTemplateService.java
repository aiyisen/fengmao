package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.StoreProductTemplateAddDto;
import com.qy.ntf.bean.dto.StoreProductTemplateDto;
import com.qy.ntf.bean.dto.StoreProductTemplateUpdateDto;
import com.qy.ntf.bean.entity.StoreProductTemplate;

import java.util.List;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 特权商品材料模板 service服务
 */
public interface StoreProductTemplateService
    extends BaseService<StoreProductTemplateDto, StoreProductTemplate> {

  /**
   * 主键详情
   *
   * @param id
   * @return
   */
  StoreProductTemplateDto getStoreProductTemplateById(Long id);

  /**
   * 保存方法
   *
   * @param dto
   */
  void save(StoreProductTemplateAddDto dto);

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(StoreProductTemplateUpdateDto dto);

  /**
   * 更新状态方法
   *
   * @param dto
   */
  void updateState(StoreProductTemplateDto dto);

  /**
   * 删除方法
   *
   * @param id
   */
  void delete(Long id);

  /**
   * 列表查询
   *
   * @param dto
   * @return
   */
  List<StoreProductTemplateDto> list(StoreProductTemplateDto dto);

  /**
   * 常规分页查询
   *
   * @param currentPage 当前页数 （第一页或者第三页）
   * @param pageSize 页数大小（每页记录数）
   * @param queryWrapper 查询条件
   * @return 分页的设备参数
   */
  IPage<StoreProductTemplateDto> getListByPage(
      Class<StoreProductTemplateDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreProductTemplate> queryWrapper);
}
