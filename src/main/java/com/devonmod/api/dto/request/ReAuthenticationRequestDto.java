package com.devonmod.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReAuthenticationRequestDto {

  @NotBlank(message = "RefreshToken should not be empty")
  private String refreshToken;
}
