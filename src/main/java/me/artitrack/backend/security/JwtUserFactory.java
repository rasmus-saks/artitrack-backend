package me.artitrack.backend.security;

import me.artitrack.backend.model.User;

public class JwtUserFactory {
  private JwtUserFactory() {
  }

  public static JwtUser create(User user) {
    if (user == null)
      throw new IllegalArgumentException("user cannot be null");
    return new JwtUser(user);

  }
}
