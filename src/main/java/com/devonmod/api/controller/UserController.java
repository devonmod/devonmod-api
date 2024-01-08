package com.devonmod.api.controller;

import com.devonmod.api.dto.request.UserCreateRequestDto;
import com.devonmod.api.dto.request.UserUpdateRequestDto;
import com.devonmod.api.dto.response.UserResponseDto;
import com.devonmod.api.service.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getById(@PathVariable(value = "id") UUID id) {
    return ResponseEntity.ok(userService.getById(id));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping
  public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateRequestDto dto) {
    return ResponseEntity.ok(userService.create(dto));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDto> update(
      @PathVariable(value = "id") UUID id,
      @Valid @RequestBody UserUpdateRequestDto requestDto) {
    return ResponseEntity.ok(userService.update(id, requestDto));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestParam UUID id) {
    userService.delete(id);
    return ResponseEntity.ok().build();
  }
}
