package me.artitrack.backend.steam;

class SteamResponseException extends RuntimeException {
  private static final long serialVersionUID = 6758576532466480104L;

  SteamResponseException(String msg) {
    super(msg);
  }
}
