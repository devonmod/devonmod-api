package com.devonmod.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDto {

  @NotBlank(message = "Username should not be empty")
  private String username;

  @NotBlank(message = "Password should not be empty")
  private String password;
}
