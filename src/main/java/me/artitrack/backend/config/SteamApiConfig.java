package me.artitrack.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("steamapi")
public class SteamApiConfig {
  private String apikey;

  public String getApikey() {
    return apikey;
  }

  public void setApikey(String apikey) {
    this.apikey = apikey;
  }
}
