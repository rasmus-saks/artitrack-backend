package me.artitrack.backend.security;

import me.artitrack.backend.model.User;

public class JwtUserFactory {
  private JwtUserFactory() {
  }

  public static JwtUser create(User user) {
    return new JwtUser(user);

  }
}
