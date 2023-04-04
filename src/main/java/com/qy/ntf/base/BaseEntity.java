package com.qy.ntf.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

// 用于自动填充字段
@Data
public class BaseEntity { // implements Serializable {

  //	private static final long serialVersionUID = 12345768123L;

  @TableField(value = "createId", fill = FieldFill.INSERT)
  @ApiModelProperty("创建者ID")
  private Long createId;

  @TableField(value = "createTime", fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @ApiModelProperty("创建时间")
  private Date createTime;

  @TableField(value = "updateId", fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty("修改者id")
  private Long updateId;

  @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @ApiModelProperty("修改时间")
  private Date updateTime;

  @TableField(value = "state")
  @ApiModelProperty("-1逻辑删除0禁用1正常")
  private Integer state;

  //  @TableField(value = "s_t_r_i_p_id")
  //  private Long stripId;
}
