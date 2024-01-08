package com.devonmod.api.service.impl;

import static com.devonmod.api.exception.Error.ACCESS_DENIED;
import static com.devonmod.api.exception.Error.DUPLICATE_USERNAME;
import static com.devonmod.api.exception.Error.USER_NOT_FOUND;

import com.devonmod.api.dto.request.UserCreateRequestDto;
import com.devonmod.api.dto.request.UserUpdateRequestDto;
import com.devonmod.api.dto.response.UserResponseDto;
import com.devonmod.api.entity.UserEntity;
import com.devonmod.api.enumeration.UserRole;
import com.devonmod.api.exception.ApiAccessDeniedException;
import com.devonmod.api.exception.DuplicateUsernameException;
import com.devonmod.api.exception.NotFoundException;
import com.devonmod.api.mapper.UserMapper;
import com.devonmod.api.repository.UserRepository;
import com.devonmod.api.service.SecurityContextService;
import com.devonmod.api.service.UserService;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final SecurityContextService securityContextService;

  @Override
  public UserResponseDto getById(UUID id) {
    UserEntity userEntity = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

    UUID authenticatedUserId = securityContextService.getUserDetails().getId();
    if (userEntity.getUserRole() != UserRole.ADMIN && !authenticatedUserId.equals(id)) {
      throw new ApiAccessDeniedException(ACCESS_DENIED);
    }

    return mapper.mapToDto(userEntity);
  }

  @Override
  public UserEntity getByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElse(null);
  }

  @Override
  public UserEntity getByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElse(null);
  }

  @Override
  public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
    String username = userCreateRequestDto.getUsername();
    if (userRepository.findByUsername(username).isPresent()) {
      log.warn("Attempt to create user with duplicate username: {}", username);
      throw new DuplicateUsernameException(DUPLICATE_USERNAME);
    }

    String password = passwordEncoder.encode(userCreateRequestDto.getPassword());
    userCreateRequestDto.setPassword(password);

    UserEntity userEntity = userRepository.save(mapper.mapToEntity(userCreateRequestDto));
    log.info("User created with username: {}", username);

    return mapper.mapToDto(userEntity);
  }

  @Override
  public UserResponseDto update(UUID id, UserUpdateRequestDto updateRequestDto) {
    log.info("Updating user with ID: {}", id);
    UserEntity userEntity = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND)).toBuilder()
        .name(updateRequestDto.getName())
        .surname(updateRequestDto.getSurname())
        .username(updateRequestDto.getUsername())
        .email(updateRequestDto.getEmail())
        .updatedAt(Instant.now())
        .build();

    userRepository.save(userEntity);
    log.info("User with ID {} updated successfully", id);

    return mapper.mapToDto(userEntity);
  }

  @Override
  public void delete(UUID id) {
    log.info("Deleting user with ID: {}", id);

    if (!userRepository.existsById(id)) {
      log.warn("User with ID {} not found for deletion", id);
      throw new NotFoundException(USER_NOT_FOUND);
    }
    userRepository.deleteById(id);
    log.info("User with ID {} deleted successfully", id);
  }
}
