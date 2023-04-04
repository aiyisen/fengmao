package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.OrderProAfterDto;
import com.qy.ntf.bean.dto.OrderProAfterAddDto;
import com.qy.ntf.bean.dto.OrderProAfterUpdateDto;
import com.qy.ntf.bean.entity.OrderProAfter;
import com.qy.ntf.base.BaseService;
import java.util.List;
/**
 * @author 王振读
 * 2022-06-13 22:37:54
 * DESC : 权益商城售后订单
仅退款014
退货退款0234
退货024 service服务
 */
public interface OrderProAfterService extends BaseService<OrderProAfterDto,OrderProAfter> {

    /**
    * 主键详情
    * @param id
    * @return
    */
        OrderProAfterDto getOrderProAfterById(Long id);

    /**
    * 保存方法
    * @param dto
    */
    void save(OrderProAfterAddDto dto);

    /**
    * 更新方法
    * @param dto
    */
    void update(OrderProAfterUpdateDto dto);

    /**
    * 更新状态方法
    * @param dto
    */
    void updateState(OrderProAfterDto dto);

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
    List<OrderProAfterDto> list(OrderProAfterDto dto);

    /**
    * 常规分页查询
    * @param currentPage 当前页数 （第一页或者第三页）
    * @param pageSize 页数大小（每页记录数）
    * @param queryWrapper 查询条件
    * @return 分页的设备参数
    */
    IPage<OrderProAfterDto> getListByPage(Class<OrderProAfterDto> clazz, long currentPage, Long pageSize, LambdaQueryWrapper<OrderProAfter> queryWrapper);

}

