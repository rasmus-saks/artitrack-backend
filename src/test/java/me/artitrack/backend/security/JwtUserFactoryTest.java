package me.artitrack.backend.security;

import me.artitrack.backend.BaseTest;
import me.artitrack.backend.model.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JwtUserFactoryTest extends BaseTest {
  @Test
  public void create() throws Exception {
    JwtUser user = JwtUserFactory.create(new User("1234"));
    assertNotNull(user);
    assertEquals("1234", user.getUsername());
  }

}