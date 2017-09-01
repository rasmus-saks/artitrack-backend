package me.artitrack.backend.security;

import me.artitrack.backend.BaseTest;
import me.artitrack.backend.model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class JwtUserFactoryTest extends BaseTest {
  @Test
  public void create_GivenValidUser_ReturnsValidUser() throws Exception {
    JwtUser user = JwtUserFactory.create(new User("1234", "nickname", "avatar"));
    assertNotNull(user);
    assertEquals("1234", user.getUsername());
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_GivenNull_ExceptionThrown() throws Exception {
    JwtUserFactory.create(null);
  }
}