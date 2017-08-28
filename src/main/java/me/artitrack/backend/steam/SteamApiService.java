package me.artitrack.backend.steam;

import me.artitrack.backend.config.SteamApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;


@Service
public class SteamApiService {
  private static final Logger LOG = LoggerFactory.getLogger(SteamApiService.class);
  private static final String URL_TEMPLATE = "https://api.steampowered.com/{interface}/{method}/{version}";
  private final SteamApiConfig steamApiConfig;
  private final RestTemplate restTemplate;

  @Autowired
  public SteamApiService(SteamApiConfig steamApiConfig, RestTemplateBuilder restTemplateBuilder) {
    this.steamApiConfig = steamApiConfig;
    this.restTemplate = restTemplateBuilder.build();
  }

  @Async
  public CompletableFuture<SteamUser> getSteamUser(String steam64) {
    UriComponents uri = buildUri("ISteamUser", "GetPlayerSummaries", "v2", "steamids=" + steam64);
    LOG.debug("Requesting Steam user from {}", uri.toUriString());
    SteamPlayerSummariesResponse response = restTemplate.getForObject(uri.toUri(), SteamPlayerSummariesResponse.class);
    if (response == null || response.getResponse() == null || response.getResponse().getPlayers() == null
        || response.getResponse().getPlayers().isEmpty()) {
      throw new SteamResponseException("Failed requesting Steam user " + steam64);
    }
    return CompletableFuture.completedFuture(response.getResponse().getPlayers().get(0));
  }

  private UriComponents buildUri(String iface, String method, String version, String query) {
    return builder().query(query).buildAndExpand(iface, method, version);
  }

  private UriComponentsBuilder builder() {
    return UriComponentsBuilder.fromHttpUrl(URL_TEMPLATE).queryParam("key", steamApiConfig.getApikey());
  }
}
