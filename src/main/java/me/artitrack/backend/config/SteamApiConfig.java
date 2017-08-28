package me.artitrack.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties("steamapi")
public class SteamApiConfig {
  private static final Logger LOG = LoggerFactory.getLogger(SteamApiConfig.class);
  private String apikey;

  public String getApikey() {
    return apikey;
  }

  public void setApikey(String apikey) {
    this.apikey = apikey;
  }

  @PostConstruct
  private void logConfiguration() {
    LOG.info("Steam API configuration: [apikey = {}]", apikey);
  }
}
