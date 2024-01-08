package com.devonmod.api.exception;

public class NotFoundException extends BaseException{

  public NotFoundException(Error error) {
    super(error);
  }
}
