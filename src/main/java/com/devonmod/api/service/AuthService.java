package com.devonmod.api.service;

import com.devonmod.api.dto.request.AuthRequestDto;
import com.devonmod.api.dto.response.AuthResponseDto;

public interface AuthService {

  AuthResponseDto authenticateByCredentials(AuthRequestDto authRequestDto);

  AuthResponseDto authenticateByRefreshToken(String refreshToken);
}
