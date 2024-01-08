package com.devonmod.api.security;

import static com.devonmod.api.exception.Error.INSUFFICIENT_AUTHENTICATION;
import static com.devonmod.api.exception.Error.JWT_AUTHENTICATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.devonmod.api.exception.ApiError;
import com.devonmod.api.utils.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    final String expired =
        request.getAttribute("expired") != null ? request.getAttribute("expired").toString() : "";
    sendResponse(response, expired);
  }

  private void sendResponse(HttpServletResponse response, String expired) throws IOException {
    ApiError apiError =
        !expired.isEmpty() ? new ApiError(JWT_AUTHENTICATION.getHttpStatus(),
            JWT_AUTHENTICATION.getCode(), Instant.now(), expired)
            : new ApiError(INSUFFICIENT_AUTHENTICATION.getHttpStatus(),
                INSUFFICIENT_AUTHENTICATION.getCode(), Instant.now(),
                INSUFFICIENT_AUTHENTICATION.getMessage());

    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(JWT_AUTHENTICATION.getHttpStatus().value());
    response.getWriter().write(JsonUtil.writeToJson(apiError));
  }
}
