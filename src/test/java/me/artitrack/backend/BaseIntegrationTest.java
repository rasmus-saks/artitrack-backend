package me.artitrack.backend;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import me.artitrack.backend.model.User;
import me.artitrack.backend.repository.UserRepository;
import me.artitrack.backend.security.JwtTokenUtil;
import me.artitrack.backend.security.JwtUser;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public abstract class BaseIntegrationTest extends BaseTest {
  protected static final String TEST_USER_STEAM64 = "1234567";

  @Value("${local.server.port}")
  private int port;

  @Autowired
  private JwtTokenUtil tokenUtil;

  @Autowired
  private UserRepository userRepository;
  private User user = new User(TEST_USER_STEAM64);

  @Before
  public void setUp() throws Exception {
    RestAssured.port = port;
    user = userRepository.save(user);
  }

  @After
  public void tearDown() throws Exception {
    userRepository.delete(user);
  }

  public RequestSpecification authed() {
    String token = getToken();
    return given().header("Authorization", "Bearer " + token);
  }

  public String getToken() {
    UserDetails details = new JwtUser(user);
    return tokenUtil.generateToken(details);
  }
}
