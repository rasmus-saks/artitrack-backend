package me.artitrack.backend.steam;

import me.artitrack.backend.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SteamApiServiceTest extends BaseTest {

  private static final String TEST_USER_STEAM64 = "76561198015149576";
  @Autowired
  private SteamApiService steamApiService;

  @Test
  public void successfulSteamUser() throws Exception {
    SteamUser user = steamApiService.getSteamUser(TEST_USER_STEAM64).join();
    assertNotNull(user);
    assertNotNull(user.getAvatar());
    assertNotNull(user.getAvatarfull());
    assertNotNull(user.getAvatarmedium());
    assertNotNull(user.getPersonaname());
    assertNotNull(user.getProfileurl());
    assertEquals(TEST_USER_STEAM64, user.getSteamid());
  }

  @Test(expected = SteamResponseException.class)
  public void failedSteamUser() throws Exception {
    steamApiService.getSteamUser("123").join();
  }
}