package me.artitrack.backend.endpoint;

import me.artitrack.backend.BaseIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class Api extends BaseIntegrationTest {

  @Test
  public void user_WhenNotLoggedIn_Rejected() throws Exception {
    get("/api/user/" + TEST_USER_STEAM64)
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED);
  }

  @Test
  public void user_WhenLoggedIn_ReturnsRequestedUser() throws Exception {
    authed()
        .get("/api/user/" + TEST_USER_STEAM64)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("steam64", is(TEST_USER_STEAM64));
  }

  @Test
  public void user_InvalidSteam64_Rejected() throws Exception {
    authed()
        .get("/api/user/1123")
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void user_AuthTokenCookie_Accepted() throws Exception {
    given()
        .cookie("auth-token", getToken()).
        get("/api/user/" + TEST_USER_STEAM64)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("steam64", is(TEST_USER_STEAM64));
  }

  @Test
  public void user_WithoutParams_ReturnsLoggedInUser() throws Exception {
    authed()
        .get("/api/user")
        .then()
        .body("steam64", is(TEST_USER_STEAM64));
  }
}