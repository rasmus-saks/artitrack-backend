package me.artitrack.backend.controller;

import me.artitrack.backend.BaseIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ApiControllerTest extends BaseIntegrationTest {

  @Test
  public void testUnauthorized() throws Exception {
    get("/api/user/" + TEST_USER_STEAM64)
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED);
  }

  @Test
  public void testAuthorized() throws Exception {
    authed()
        .get("/api/user/" + TEST_USER_STEAM64)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("steam64", is(TEST_USER_STEAM64));
  }

  @Test
  public void testInvalidSteam64() throws Exception {
    authed()
        .get("/api/user/1123")
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void testCookieAuthentication() throws Exception {
    given()
        .cookie("auth-token", getToken()).
        get("/api/user/" + TEST_USER_STEAM64)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("steam64", is(TEST_USER_STEAM64));
  }

  @Test
  public void testLoggedUser() throws Exception {
    authed()
        .get("/api/user")
        .then()
        .body("steam64", is(TEST_USER_STEAM64));
  }
}