package com.devonmod.api.security;

import com.devonmod.api.entity.UserEntity;
import com.devonmod.api.security.user.AppUserDetails;
import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationManager {

  private final JwtTokenProvider jwtTokenProvider;

  public Authentication getAuthentication(String token) {
    log.info("Getting authentication for token: {}", token);

    UserEntity userEntity = this.extractUserEntityFromToken(token);
    UUID userId = userEntity.getId();
    String username = userEntity.getUsername();
    String userRole = String.valueOf(userEntity.getUserRole());

    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
        String.valueOf(userEntity.getUserRole()));
    grantedAuthorities.add(grantedAuthority);

    AppUserDetails appUserDetails = new AppUserDetails(userId, userEntity.getName(),
        userEntity.getSurname(), userEntity.getEmail(), username, grantedAuthorities);

    log.info("Authentication created for user: {}, userId: {}, userRole: {}", username, userId,
        userRole);
    return new UsernamePasswordAuthenticationToken(appUserDetails, null, appUserDetails.getRoles());
  }


  private UserEntity extractUserEntityFromToken(String token) {
    UUID userId = UUID.fromString(
        jwtTokenProvider.getClaimFromToken(token, key -> key.get("userId", String.class)));
    String userRole = jwtTokenProvider.getClaimFromToken(token,
        key -> key.get("userRole", String.class));
    String name = jwtTokenProvider.getClaimFromToken(token, key -> key.get("name", String.class));
    String surname = jwtTokenProvider.getClaimFromToken(token,
        key -> key.get("surname", String.class));
    String email = jwtTokenProvider.getClaimFromToken(token, key -> key.get("email", String.class));
    String username = jwtTokenProvider.getClaimFromToken(token, Claims::getSubject);

    return new UserEntity(userId, name, surname, username, email, userRole);
  }
}
