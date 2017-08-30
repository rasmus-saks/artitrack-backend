package me.artitrack.backend.endpoint;

import me.artitrack.backend.BaseIntegrationTest;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class ValidateToken extends BaseIntegrationTest {
  @Test
  public void auth_GivenEmptyToken_ReturnsInvalid() throws Exception {
    get("/auth/validateToken")
        .then()
        .body("valid", is(false));
  }

  @Test
  public void auth_GivenInvalidToken_ReturnsInvalid() throws Exception {
    given()
        .queryParam("token", "invalidtoken")
        .get("/auth/validateToken")
        .then()
        .body("valid", is(false));
  }

  @Test
  public void auth_GivenValidToken_ReturnsValidAndUser() throws Exception {
    given()
        .queryParam("token", getToken())
        .get("/auth/validateToken")
        .then()
        .body("valid", is(true))
        .body("user.steam64", is(TEST_USER_STEAM64));
  }
}
