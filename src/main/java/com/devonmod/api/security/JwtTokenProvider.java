package com.devonmod.api.security;

import static com.devonmod.api.exception.Error.ALGORITHM_NOT_AVAILABLE;
import static com.devonmod.api.exception.Error.FILE_READ;
import static com.devonmod.api.exception.Error.INVALID_KEY_FORMAT;
import static com.devonmod.api.exception.Error.JWT_AUTHENTICATION;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devonmod.api.config.propeties.JwtProperties;
import com.devonmod.api.enumeration.JwtTokenType;
import com.devonmod.api.exception.AlgorithmNotAvailableException;
import com.devonmod.api.exception.FileReadException;
import com.devonmod.api.exception.InvalidKeyFormatException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final JwtProperties jwtProperties;

  public String createJwt(UUID userId, String name, String surname, String username, String email,
      String userRole, JwtTokenType tokenType) {
    long validityMinutes =
        (tokenType == JwtTokenType.ACCESS_TOKEN) ? jwtProperties.getAccessTokenValidity()
            : jwtProperties.getRefreshTokenValidity();

    return JWT
        .create()
        .withSubject(username)
        .withClaim("userId", userId.toString())
        .withClaim("name", name)
        .withClaim("surname", surname)
        .withClaim("email", email)
        .withClaim("userRole", userRole)
        .withClaim("tokenType", tokenType.toString())
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plus(validityMinutes, ChronoUnit.MINUTES))
        .sign(Algorithm.RSA256((RSAPrivateKey) getPrivateKey()));
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = Jwts.
        parserBuilder()
        .setSigningKey(getPublicKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claimsResolver.apply(claims);
  }

  public boolean validateToken(String token, HttpServletRequest servletRequest) {
    try {
      return this.getClaimFromToken(token, Claims::getExpiration).after(new Date());
    } catch (JwtException e) {
      servletRequest.setAttribute("expired", JWT_AUTHENTICATION.getMessage());
      return false;
    }
  }

  public boolean validateToken(String token) {
    try {
      return this.getClaimFromToken(token, Claims::getExpiration).after(new Date());
    } catch (JwtException e) {
      return false;
    }
  }

  public String resolveToken(HttpServletRequest request) {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    return header == null ? null : header.replace("Bearer", "").strip();
  }

  private Key generateRSAKeyFromPath(boolean isPublic, String keyPath) {
    try {
      byte[] keyBytes = new ClassPathResource(keyPath).getInputStream().readAllBytes();
      KeyFactory kf = KeyFactory.getInstance("RSA");
      if (isPublic) {
        return kf.generatePublic(new X509EncodedKeySpec(keyBytes));
      } else {
        return kf.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
      }
    } catch (IOException ex) {
      throw new FileReadException(FILE_READ);
    } catch (InvalidKeySpecException ex) {
      throw new InvalidKeyFormatException(INVALID_KEY_FORMAT);
    } catch (NoSuchAlgorithmException ex) {
      throw new AlgorithmNotAvailableException(ALGORITHM_NOT_AVAILABLE);
    }
  }

  private Key getPrivateKey() {
    return generateRSAKeyFromPath(false, jwtProperties.getPrivateKeyFilePath());
  }

  private Key getPublicKey() {
    return generateRSAKeyFromPath(true, jwtProperties.getPublicKeyFilePath());
  }
}
