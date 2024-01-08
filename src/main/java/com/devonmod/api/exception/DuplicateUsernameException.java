package com.devonmod.api.exception;

public class DuplicateUsernameException extends BaseException{

  public DuplicateUsernameException(Error error) {
    super(error);
  }
}
