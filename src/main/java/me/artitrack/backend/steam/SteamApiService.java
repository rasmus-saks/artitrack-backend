package me.artitrack.backend.steam;

import me.artitrack.backend.config.SteamApiConfig;
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
    UriComponents uri =
        builder().queryParam("steamids", steam64).buildAndExpand("ISteamUser", "GetPlayerSummaries", "v2");
    SteamPlayerSummariesResponse response = restTemplate.getForObject(uri.toUri(), SteamPlayerSummariesResponse.class);
    if (response == null || response.getResponse() == null || response.getResponse().getPlayers() == null
        || response.getResponse().getPlayers().isEmpty()) {
      return null;
    }
    return CompletableFuture.completedFuture(response.getResponse().getPlayers().get(0));
  }

  private UriComponentsBuilder builder() {
    return UriComponentsBuilder.fromHttpUrl(URL_TEMPLATE).queryParam("key", steamApiConfig.getApikey());
  }
}
