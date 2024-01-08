package com.devonmod.api.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Error contains: CODE a unique code which value's first 3 numbers represent the response status
 * code. HTTP STATUS the response status code MESSAGE the error message
 */
@Getter
@RequiredArgsConstructor
public enum Error {
  INVALID_KEY_FORMAT(4001, BAD_REQUEST, "Invalid key specification"),
  JOB_DELETED(4002, BAD_REQUEST, "The requested job is no longer available or has been deleted."),
  REQUEST_PROCESSING(4003, BAD_REQUEST,
      "It seems there was an error processing your request. Please retry."),
  CONSTRAINT_VIOLATION(4004, BAD_REQUEST, "There is An Invalid Value In Input"),

  USER_NOT_FOUND(4041, NOT_FOUND, "User not found or does not exist"),

  JWT_AUTHENTICATION(4011, UNAUTHORIZED,
      "Invalid or expired token. Please re-authenticate."),
  INVALID_CREDENTIALS(4012, UNAUTHORIZED, "Incorrect username or password."),
  INVALID_REFRESH_TOKEN(4013, UNAUTHORIZED, "Invalid or expired refresh token."),
  ACCESS_RESTRICTED(4014, UNAUTHORIZED, "Access restricted to specific paths."),
  INSUFFICIENT_AUTHENTICATION(4015, UNAUTHORIZED,
      "Authentication credentials are required to access this resource."),

  ACCESS_DENIED(4031, FORBIDDEN, "Permission Denied To Requested Resource"),

  DUPLICATE_USERNAME(4091, CONFLICT, "Username already exists"),
  KEY_ALREADY_EXISTS(4092, CONFLICT, "A Content with the same key already exists"),

  INTERNAL_ERROR(5000, INTERNAL_SERVER_ERROR, "Something went wrong"),
  FILE_READ(5001, INTERNAL_SERVER_ERROR, "Error reading key file"),
  ALGORITHM_NOT_AVAILABLE(5002, INTERNAL_SERVER_ERROR, "Algorithm not found"),
  JSON_WRITE(5003, INTERNAL_SERVER_ERROR, "Error occurred while converting object to JSON"),
  FILE_CREATION(5004, INTERNAL_SERVER_ERROR, "Failed to create directories"),
  FILE_WRITE(5004, INTERNAL_SERVER_ERROR, "Failed to write to file");

  private final Integer code;
  private final HttpStatus httpStatus;
  private final String message;
}
