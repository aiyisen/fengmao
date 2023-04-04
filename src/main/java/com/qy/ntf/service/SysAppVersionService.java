package com.qy.ntf.service;

import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.SysAppVersionDto;
import com.qy.ntf.bean.dto.SysAppVersionUpdateDto;
import com.qy.ntf.bean.entity.SysAppVersion;

import java.util.List;
/**
 * @author 王振读 2022-07-22 00:02:04 DESC : service服务
 */
public interface SysAppVersionService extends BaseService<SysAppVersionDto, SysAppVersion> {

  /**
   * 更新方法
   *
   * @param dto
   */
  void update(SysAppVersionUpdateDto dto);

  /**
   * 列表查询
   *
   * @param dto
   * @return
   */
  List<SysAppVersionDto> list(SysAppVersionDto dto);
}
