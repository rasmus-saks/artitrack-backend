package me.artitrack.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("server")
public class ServerConfig {
  private String baseUrl;

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }
}
