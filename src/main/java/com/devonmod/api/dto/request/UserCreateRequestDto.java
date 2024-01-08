package com.devonmod.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserCreateRequestDto {

  @NotBlank(message = "Name should not be empty")
  private String name;

  @NotBlank(message = "Surname should not be empty")
  private String surname;

  @NotBlank(message = "Username should not be empty")
  private String username;

  @NotBlank(message = "Email should not be empty")
  @Pattern(regexp = "^[a-zA-Z0-9.!#$%&‘*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password should not be empty")
  private String password;

  @NotBlank(message = "UserRole should not be empty")
  private String userRole;
}
