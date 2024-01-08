package com.devonmod.api.exception;

public class JsonWriteException extends BaseException {

  public JsonWriteException(Error error) {
    super(error);
  }
}
