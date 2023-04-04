package com.qy.ntf.util;

import lombok.Data;

@Data
public class TokenInvalidException extends RuntimeException {
  private String msg;
  private int code = -2;

  public TokenInvalidException(String msg) {
    super(msg);
    this.msg = msg;
  }

  public TokenInvalidException(String msg, Throwable e) {
    super(msg, e);
    this.msg = msg;
  }

  public TokenInvalidException(String msg, int code) {
    super(msg);
    this.msg = msg;
    this.code = code;
  }

  public TokenInvalidException(String msg, int code, Throwable e) {
    super(msg, e);
    this.msg = msg;
    this.code = code;
  }
}
