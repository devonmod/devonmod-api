package com.devonmod.api.controller;

import com.devonmod.api.dto.request.AuthRequestDto;
import com.devonmod.api.dto.request.ReAuthenticationRequestDto;
import com.devonmod.api.dto.response.AuthResponseDto;
import com.devonmod.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/auth")
  public ResponseEntity<AuthResponseDto> authenticate(@Valid @RequestBody AuthRequestDto request) {
    return ResponseEntity.ok(authService.authenticateByCredentials(request));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<AuthResponseDto> reAuthenticate(
      @Valid @RequestBody ReAuthenticationRequestDto request) {
    return ResponseEntity.ok(
        authService.authenticateByRefreshToken(request.getRefreshToken()));
  }
}
