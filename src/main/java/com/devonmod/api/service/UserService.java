package com.devonmod.api.service;

import com.devonmod.api.dto.request.UserCreateRequestDto;
import com.devonmod.api.dto.request.UserUpdateRequestDto;
import com.devonmod.api.dto.response.UserResponseDto;
import com.devonmod.api.entity.UserEntity;
import java.util.UUID;

public interface UserService {

  /**
   * Retrieves a user's details based on the provided unique identifier.
   *
   * @param id The unique identifier of the user.
   * @return An instance of {@link UserResponseDto} containing the user's details.
   */
  UserResponseDto getById(UUID id);

  /**
   * Retrieves a user entity based on the provided username.
   *
   * @param username The username of the user.
   * @return The {@link UserEntity} corresponding to the given username.
   */
  UserEntity getByUsername(String username);

  /**
   * Retrieves a user entity based on the provided email address.
   *
   * @param email The email address of the user.
   * @return The {@link UserEntity} corresponding to the given email address.
   */
  UserEntity getByEmail(String email);

  /**
   * Creates a new user based on the provided request data.
   *
   * @param userCreateRequestDto The data required to create the user.
   * @return An instance of {@link UserResponseDto} representing the created user.
   */
  UserResponseDto create(UserCreateRequestDto userCreateRequestDto);

  /**
   * Updates an existing user's details based on the provided identifier and request data.
   *
   * @param id               The unique identifier of the user to be updated.
   * @param updateRequestDto The data containing the updates for the user.
   * @return An instance of {@link UserResponseDto} representing the updated user details.
   */
  UserResponseDto update(UUID id, UserUpdateRequestDto updateRequestDto);

  /**
   * Deletes a user based on the provided unique identifier.
   *
   * @param id The unique identifier of the user to be deleted.
   */
  void delete(UUID id);
}
