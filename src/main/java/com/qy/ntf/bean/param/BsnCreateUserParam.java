package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class BsnCreateUserParam {
  private String opbChainClientName;
  private Integer opbChainClientType = 2;
  private Integer opbChainId = 2;
  private String opbClientAddress;
  private String opbPublicKey;
  private Integer openDdc = 5;
  private Integer opbKeyType = 3;
  private String proof;

  public String getOpbChainClientName() {
    return opbChainClientName;
  }

  public void setOpbChainClientName(String opbChainClientName) {
    this.opbChainClientName = opbChainClientName;
  }

  public Integer getOpbChainClientType() {
    return opbChainClientType;
  }

  public void setOpbChainClientType(Integer opbChainClientType) {
    this.opbChainClientType = opbChainClientType;
  }

  public Integer getOpbChainId() {
    return opbChainId;
  }

  public void setOpbChainId(Integer opbChainId) {
    this.opbChainId = opbChainId;
  }

  public String getOpbClientAddress() {
    return opbClientAddress;
  }

  public void setOpbClientAddress(String opbClientAddress) {
    this.opbClientAddress = opbClientAddress;
  }

  public String getOpbPublicKey() {
    return opbPublicKey;
  }

  public void setOpbPublicKey(String opbPublicKey) {
    this.opbPublicKey = opbPublicKey;
  }

  public Integer getOpenDdc() {
    return openDdc;
  }

  public void setOpenDdc(Integer openDdc) {
    this.openDdc = openDdc;
  }

  public Integer getOpbKeyType() {
    return opbKeyType;
  }

  public void setOpbKeyType(Integer opbKeyType) {
    this.opbKeyType = opbKeyType;
  }

  public String getProof() {
    return proof;
  }

  public void setProof(String proof) {
    this.proof = proof;
  }
}
