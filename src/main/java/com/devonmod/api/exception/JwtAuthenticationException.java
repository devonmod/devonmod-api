package com.devonmod.api.exception;

import lombok.Getter;

@Getter
public class JwtAuthenticationException extends BaseException {

  public JwtAuthenticationException(Error error) {
    super(error);
  }
}
