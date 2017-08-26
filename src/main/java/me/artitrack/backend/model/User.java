package me.artitrack.backend.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {
  private static final long serialVersionUID = 5327804285969688785L;
  @Id
  private String steam64;

  protected User() {
  }

  public User(String steam64) {
    this.steam64 = steam64;
  }


  public String getSteam64() {
    return steam64;
  }

  @Override
  public String toString() {
    return "User{" + "steam64='" + steam64 + '\'' + '}';
  }
}
