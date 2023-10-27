package com.jsanders.nisum.test.error_handling;

public class Validator {
  public static void check(boolean condition, String message) {
    if (!condition) {
      throw new ValidationException(message);
    }
  }

  public static void check(boolean condition, String fmt, Object...args) {
    if (!condition) {
      throw new ValidationException(String.format(fmt, args));
    }
  }
}
