package com.devonmod.api.config.propeties;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  private long accessTokenValidity;
  private long refreshTokenValidity;
  private String publicKeyFilePath;
  private String privateKeyFilePath;
  private String reLoginPath;
}
