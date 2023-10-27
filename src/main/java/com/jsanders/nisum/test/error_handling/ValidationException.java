package com.jsanders.nisum.test.error_handling;

import org.springframework.http.HttpStatus;

public class ValidationException extends CustomException {
  public ValidationException(String mensaje) {
    super(mensaje, HttpStatus.BAD_REQUEST);
  }
}
