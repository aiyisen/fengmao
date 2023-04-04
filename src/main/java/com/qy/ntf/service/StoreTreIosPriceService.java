package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.StoreTreIosPriceDto;
import com.qy.ntf.bean.dto.StoreTreIosPriceAddDto;
import com.qy.ntf.bean.dto.StoreTreIosPriceUpdateDto;
import com.qy.ntf.bean.entity.StoreTreIosPrice;
import com.qy.ntf.base.BaseService;
import java.util.List;
/**
 * @author 王振读
 * 2022-07-31 20:17:27
 * DESC : ios价格 service服务
 */
public interface StoreTreIosPriceService extends BaseService<StoreTreIosPriceDto,StoreTreIosPrice> {

    /**
    * 主键详情
    * @param id
    * @return
    */
        StoreTreIosPriceDto getStoreTreIosPriceById(Long id);

    /**
    * 保存方法
    * @param dto
    */
    void save(StoreTreIosPriceAddDto dto);

    /**
    * 更新方法
    * @param dto
    */
    void update(StoreTreIosPriceUpdateDto dto);

    /**
    * 更新状态方法
    * @param dto
    */
    void updateState(StoreTreIosPriceDto dto);

    /**
    * 删除方法
    * @param id
    */
    void delete(Long id);

    /**
   * 列表查询
   * @param dto
   * @return
   */
    List<StoreTreIosPriceDto> list(StoreTreIosPriceDto dto);

    /**
    * 常规分页查询
    * @param currentPage 当前页数 （第一页或者第三页）
    * @param pageSize 页数大小（每页记录数）
    * @param queryWrapper 查询条件
    * @return 分页的设备参数
    */
    IPage<StoreTreIosPriceDto> getListByPage(Class<StoreTreIosPriceDto> clazz, long currentPage, Long pageSize, LambdaQueryWrapper<StoreTreIosPrice> queryWrapper);

}

