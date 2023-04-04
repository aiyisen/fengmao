package com.qy.ntf.bean.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BindBoxAddParam {
  private Long id;

  @NotNull(message = " ios价格不可为空")
  private Long stripId;

  private String treasureTitle;
  @NotNull private String indexImgPath;

  @Min(value = 0, message = "价格最小为0")
  @NotNull
  private BigDecimal price;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @NotNull
  private Date upTime;

  @NotNull private Long sysOrgId;

  @Min(value = 0, message = "限购数量最小为0,标识不限购")
  private Integer ruleCount;

  private String introduce;

  private String authInfo;
  private String linkInfo;
}
