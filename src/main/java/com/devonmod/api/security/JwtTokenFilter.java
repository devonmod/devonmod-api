package com.devonmod.api.security;

import static com.devonmod.api.exception.Error.ACCESS_RESTRICTED;
import static com.devonmod.api.exception.Error.JWT_AUTHENTICATION;

import com.devonmod.api.config.propeties.JwtProperties;
import com.devonmod.api.enumeration.JwtTokenType;
import com.devonmod.api.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  public static final String TOKEN_TYPE = "tokenType";
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;
  private final JwtProperties jwtProperties;

  @SneakyThrows
  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest servletRequest,
      @NonNull HttpServletResponse servletResponse, @NonNull FilterChain filterChain) {
    String token = jwtTokenProvider.resolveToken(servletRequest);

    try {
      if (token != null && jwtTokenProvider.validateToken(token, servletRequest)) {
        String tokenType = jwtTokenProvider.getClaimFromToken(token,
            key -> key.get(TOKEN_TYPE, String.class));

        if (tokenType.equals(JwtTokenType.REFRESH_TOKEN.name())) {
          String servletPath = servletRequest.getServletPath();
          if (!servletPath.equals(jwtProperties.getReLoginPath())) {
            servletRequest.setAttribute("expired", ACCESS_RESTRICTED.getMessage());
          }
        }

        Authentication authentication = authenticationManager.getAuthentication(token);
        if (authentication != null) {
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
      filterChain.doFilter(servletRequest, servletResponse);
    } catch (JwtAuthenticationException e) {
      log.error("JWT Authentication Exception: {}", e.getMessage());
      servletRequest.setAttribute("expired", JWT_AUTHENTICATION.getMessage());
    } finally {
      SecurityContextHolder.clearContext();
    }
  }
}
