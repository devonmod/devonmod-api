package com.devonmod.api.dto.response;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponseDto {

  @NotBlank
  String accessToken;

  @NotBlank
  String refreshToken;

  Instant accessTokenExpiry;

  Instant refreshTokenExpiry;
}
