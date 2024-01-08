package com.devonmod.api.exception;

public class ApiAccessDeniedException extends BaseException {

  public ApiAccessDeniedException(Error error) {
    super(error);
  }
}
