package com.qy.ntf.bean.param;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAdminRoleParam {
  private Long adminId;
  private List<Long> roleIds;
}
