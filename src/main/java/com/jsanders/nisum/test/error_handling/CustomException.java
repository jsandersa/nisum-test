package com.jsanders.nisum.test.error_handling;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

  HttpStatus httpStatus;

  public CustomException(String mensaje, HttpStatus httpStatus) {
    super(mensaje);
    this.httpStatus = httpStatus;
  }
}
