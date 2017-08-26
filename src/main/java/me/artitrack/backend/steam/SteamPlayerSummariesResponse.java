package me.artitrack.backend.steam;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;

public class SteamPlayerSummariesResponse {
  private ResponseData response;

  protected SteamPlayerSummariesResponse() {
  }

  public ResponseData getResponse() {
    return response;
  }

  public static class ResponseData {
    private List<SteamUser> players;

    protected ResponseData() {
    }

    @SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
    public List<SteamUser> getPlayers() {
      return players;
    }
  }
}
