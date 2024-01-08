package com.devonmod.api.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class UserResponseDto {

  private UUID id;

  private String name;

  private String surname;

  private String username;

  private String email;

  private String userRole;

  private Instant createdAt;

  private Instant updatedAt;
}
