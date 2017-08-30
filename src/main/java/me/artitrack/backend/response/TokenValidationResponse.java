package me.artitrack.backend.response;

import me.artitrack.backend.model.User;

public class TokenValidationResponse {
  private boolean valid;
  private User user;

  public TokenValidationResponse(User user) {
    this.user = user;
    this.valid = user != null;
  }

  public boolean isValid() {
    return valid;
  }

  public User getUser() {
    return user;
  }
}
