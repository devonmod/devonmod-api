package com.devonmod.api.security.user;

import java.util.Collection;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
public class AppUserDetails {

  private UUID id;
  private String name;
  private String surname;
  private String username;
  private String email;
  private Collection<GrantedAuthority> roles;
}
