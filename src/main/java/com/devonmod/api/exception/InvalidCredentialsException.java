package com.devonmod.api.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends BaseException {

  public InvalidCredentialsException(Error error) {
    super(error);
  }
}
