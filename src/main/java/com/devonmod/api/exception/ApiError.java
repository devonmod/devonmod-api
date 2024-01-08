package com.devonmod.api.exception;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiError {

  private HttpStatus status;

  private Integer errorCode;

  private Instant timestamp;

  private String message;
}
