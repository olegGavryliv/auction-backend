package com.havryliv.auction.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;

@Data
public class BusinessException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String message;
  private Object[] params;
  private HttpStatus httpStatus;

  public BusinessException() {

  }

  public BusinessException(String message, Object[] params, @NotNull HttpStatus httpStatus) {
    this.message = message;
    this.params = params;
    this.httpStatus = httpStatus;
  }

  public BusinessException(String message, @NotNull HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  @Override
  public String getMessage() {
    return message;
  }

}
