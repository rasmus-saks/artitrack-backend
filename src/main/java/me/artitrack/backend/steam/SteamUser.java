package me.artitrack.backend.steam;

public class SteamUser {
  private String steamid;
  private String personaname;
  private String profileurl;
  private String avatar;
  private String avatarmedium;
  private String avatarfull;

  protected SteamUser() {
  }

  public String getSteamid() {
    return steamid;
  }

  public String getPersonaname() {
    return personaname;
  }

  public String getProfileurl() {
    return profileurl;
  }

  public String getAvatar() {
    return avatar;
  }

  public String getAvatarmedium() {
    return avatarmedium;
  }

  public String getAvatarfull() {
    return avatarfull;
  }
}
