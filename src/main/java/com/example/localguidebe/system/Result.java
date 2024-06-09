package com.example.localguidebe.system;

import java.util.ArrayList;
import lombok.Getter;

/**
 * This class defines the schema of the response. It is used to encapsulate data prepared by the
 * server side, this object will be serialized to JSON before sent back to the client end.
 */
@Getter
public class Result {

  private boolean flag; // Two values: true means success, false means not success

  private Integer statusCode; // Status code. e.g., 200

  private String message; // Response message

  private Object data; // The response payload

  public Result() {}

  public Result(boolean flag, Integer statusCode, String message) {
    this.flag = flag;
    this.statusCode = statusCode;
    this.message = message;
    this.data = new ArrayList<>();
  }

  public Result(boolean flag, Integer statusCode, String message, Object data) {
    this.flag = flag;
    this.statusCode = statusCode;
    this.message = message;
    if (data == null) data = new ArrayList<>();
    this.data = data;
  }

  public void setFlag(boolean flag) {
    this.flag = flag;
  }

  public void setCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
