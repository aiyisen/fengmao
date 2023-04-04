package com.qy.ntf.bean.param;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "returnsms")
public class SendVerfiBack {
  @XmlElement(name = "returnstatus")
  private String returnstatus;

  @XmlElement(name = "message")
  private String message;

  @XmlElement(name = "remainpoint")
  private String remainpoint;

  @XmlElement(name = "taskID")
  private String taskID;

  @XmlElement(name = "successCounts")
  private String successCounts;
}
