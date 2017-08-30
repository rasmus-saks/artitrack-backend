package me.artitrack.backend.security;

import me.artitrack.backend.BaseTest;
import me.artitrack.backend.model.User;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.junit.Assert.*;

public class JwtUserTest extends BaseTest {
  @Test
  public void constructor_GivenValidUser_RetainsValidUser() throws Exception {
    User user = new User("54321");
    JwtUser jwtUser = new JwtUser(user);
    assertEquals(user, jwtUser.getUser());
  }

  @Test
  public void constructor_GivenValidUser_HasUserAuthority() throws Exception {
    User user = new User("54321");
    JwtUser jwtUser = new JwtUser(user);
    boolean found = false;
    for (GrantedAuthority authority : jwtUser.getAuthorities()) {
      if (authority.getAuthority().equals("ROLE_USER"))
        found = true;
    }
    assertTrue("JwtUser should have ROLE_USER", found);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_GivenNull_ThrowsException() throws Exception {
    new JwtUser(null);
  }
}
