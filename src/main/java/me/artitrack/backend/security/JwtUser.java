package me.artitrack.backend.security;

import me.artitrack.backend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class JwtUser implements UserDetails {
  private static final long serialVersionUID = 6823517710205917477L;
  private User user;
  private final Collection<GrantedAuthority> authorities = new ArrayList<>();

  public JwtUser(User user) {
    if (user == null)
      throw new IllegalArgumentException("user cannot be null");
    this.user = user;
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return user.getSteam64();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
