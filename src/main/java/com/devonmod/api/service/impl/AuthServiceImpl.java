package com.devonmod.api.service.impl;

import static com.devonmod.api.exception.Error.INVALID_CREDENTIALS;
import static com.devonmod.api.exception.Error.INVALID_REFRESH_TOKEN;

import com.devonmod.api.config.propeties.JwtProperties;
import com.devonmod.api.dto.request.AuthRequestDto;
import com.devonmod.api.dto.response.AuthResponseDto;
import com.devonmod.api.entity.UserEntity;
import com.devonmod.api.enumeration.JwtTokenType;
import com.devonmod.api.exception.InvalidCredentialsException;
import com.devonmod.api.exception.JwtAuthenticationException;
import com.devonmod.api.repository.UserRepository;
import com.devonmod.api.security.JwtTokenProvider;
import com.devonmod.api.service.AuthService;
import com.devonmod.api.service.UserService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

  private final JwtProperties jwtProperties;

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;

  @Override
  public AuthResponseDto authenticateByCredentials(AuthRequestDto authRequestDto) {
    log.info("Authenticated by user credentials, username: {}", authRequestDto.getUsername());

    String username = authRequestDto.getUsername();
    UserEntity userEntity = fetchUserEntityByUsername(username);

    if (userEntity == null) {
      log.warn("Invalid credentials for username: {}", username);
      throw new InvalidCredentialsException(INVALID_CREDENTIALS);
    }

    if (!passwordEncoder.matches(authRequestDto.getPassword(), userEntity.getPassword())) {
      log.error("Invalid credentials for user: {}", userEntity.getUsername());
      throw new InvalidCredentialsException(INVALID_CREDENTIALS);
    }

    String accessToken = generateToken(userEntity, JwtTokenType.ACCESS_TOKEN);
    String refreshToken = generateToken(userEntity, JwtTokenType.REFRESH_TOKEN);

    userEntity.setRefreshToken(refreshToken);
    userRepository.save(userEntity);

    log.info("Authentication successful for user: {}", username);

    return this.createAuthResponse(accessToken, refreshToken);
  }

  @Override
  public AuthResponseDto authenticateByRefreshToken(String refreshToken) {
    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new JwtAuthenticationException(INVALID_REFRESH_TOKEN);
    }

    UUID userId = UUID.fromString(
        jwtTokenProvider.getClaimFromToken(refreshToken, key -> key.get("userId", String.class)));

    UserEntity userEntity = userRepository.findById(userId)
        .orElseThrow(() -> new JwtAuthenticationException(INVALID_REFRESH_TOKEN));

    if (!refreshToken.equals(userEntity.getRefreshToken())) {
      log.warn("Access denied for user: {} with token: {}", userEntity.getId(), refreshToken);
      throw new JwtAuthenticationException(INVALID_REFRESH_TOKEN);
    }

    String accessToken = generateToken(userEntity, JwtTokenType.ACCESS_TOKEN);

    log.info("Authentication successful over refresh token for user: {}", userEntity.getUsername());

    return this.createAuthResponse(accessToken, refreshToken);
  }

  private UserEntity fetchUserEntityByUsername(final String username) {
    if (username.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      log.info("Fetching user by email: {}", username);
      return userService.getByEmail(username);
    } else if (username.matches("^[A-Za-z0-9]+$")) {
      log.info("Fetching user by username: {}", username);
      return userService.getByUsername(username);
    }

    log.warn("Invalid username format: {}", username);
    return null;
  }

  private String generateToken(UserEntity userEntity, JwtTokenType tokenType) {
    return jwtTokenProvider.createJwt(userEntity.getId(), userEntity.getName(),
        userEntity.getSurname(), userEntity.getUsername(), userEntity.getEmail(),
        userEntity.getUserRole().name(), tokenType);
  }

  private AuthResponseDto createAuthResponse(String accessToken, String refreshToken) {
    return AuthResponseDto.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .accessTokenExpiry(
            Instant.now().plus(jwtProperties.getAccessTokenValidity(), ChronoUnit.MINUTES))
        .refreshTokenExpiry(
            Instant.now().plus(jwtProperties.getRefreshTokenValidity(), ChronoUnit.MINUTES))
        .build();
  }
}
