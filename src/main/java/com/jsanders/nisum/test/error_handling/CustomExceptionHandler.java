package com.jsanders.nisum.test.error_handling;

import com.jsanders.nisum.test.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomExceptionHandler {

  Logger logger = LoggerFactory.getLogger(UserController.class);

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException ex) {
    CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage());
    return new ResponseEntity<>(errorResponse, ex.httpStatus);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<CustomErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
    CustomErrorResponse errorResponse = new CustomErrorResponse("not found");
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<CustomErrorResponse> handleThrowable(Throwable th) {
    logger.error("failure", th);
    String message = th.getMessage();
    if (message == null) {
      message = th.getClass().getCanonicalName();
    }
    CustomErrorResponse errorResponse = new CustomErrorResponse(message);
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Handle other exceptions as needed
}
