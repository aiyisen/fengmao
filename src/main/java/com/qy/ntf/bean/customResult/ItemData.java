package com.qy.ntf.bean.customResult;
/** Copyright 2022 json.cn */
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Auto-generated: 2022-06-21 22:55:58
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class ItemData {
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date time;

  private String context;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date ftime;

  private String areaCode;
  private String areaName;
  private String status;
  private String location;
  private String areaCenter;
  private String areaPinYin;
  private String statusCode;

  public void setTime(Date time) {
    this.time = time;
  }

  public Date getTime() {
    return time;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getContext() {
    return context;
  }

  public void setFtime(Date ftime) {
    this.ftime = ftime;
  }

  public Date getFtime() {
    return ftime;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getLocation() {
    return location;
  }

  public void setAreaCenter(String areaCenter) {
    this.areaCenter = areaCenter;
  }

  public String getAreaCenter() {
    return areaCenter;
  }

  public void setAreaPinYin(String areaPinYin) {
    this.areaPinYin = areaPinYin;
  }

  public String getAreaPinYin() {
    return areaPinYin;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusCode() {
    return statusCode;
  }
}
