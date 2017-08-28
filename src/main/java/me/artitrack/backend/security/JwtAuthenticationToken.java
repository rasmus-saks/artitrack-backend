package me.artitrack.backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
  private final String token;
  private final JwtUser user;

  public JwtAuthenticationToken(String token, JwtUser user) {
    super(user.getAuthorities());
    this.token = token;
    this.user = user;
  }

  @Override
  public Object getCredentials() {
    return token;
  }

  @Override
  public Object getPrincipal() {
    return user;
  }

}
