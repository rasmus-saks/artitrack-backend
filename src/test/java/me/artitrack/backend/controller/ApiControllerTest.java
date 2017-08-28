package me.artitrack.backend.controller;

import io.restassured.RestAssured;
import me.artitrack.backend.BaseIntegrationTest;
import me.artitrack.backend.model.User;
import me.artitrack.backend.repository.UserRepository;
import me.artitrack.backend.security.JwtTokenUtil;
import me.artitrack.backend.security.JwtUser;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ApiControllerTest extends BaseIntegrationTest {
  private static final String TEST_USER_STEAM64 = "1234567";

  @Value("${local.server.port}")
  private int port;

  @Autowired
  private JwtTokenUtil tokenUtil;

  @Autowired
  private UserRepository userRepository;
  private User user = new User(TEST_USER_STEAM64);

  @Test
  public void testUnauthorized() throws Exception {
    get("/api/steam64/" + TEST_USER_STEAM64)
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED);
  }


  @Test
  public void testAuthorized() throws Exception {
    UserDetails details = new JwtUser(user);
    String token = tokenUtil.generateToken(details);
    given()
        .header("Authorization", "Bearer " + token)
        .get("/api/steam64/" + TEST_USER_STEAM64)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("steam64", is(TEST_USER_STEAM64));
  }

  @Before
  public void setUp() throws Exception {
    RestAssured.port = port;
    user = userRepository.save(user);
  }

  @After
  public void tearDown() throws Exception {
    userRepository.delete(user);
  }
}