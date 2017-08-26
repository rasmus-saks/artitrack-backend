package me.artitrack.backend.security;

import me.artitrack.backend.model.User;
import me.artitrack.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserService {
  private final UserRepository userRepository;

  @Autowired
  public JwtUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public JwtUser loadUserBySteam64(String id) {
    User user = userRepository.findOne(id);
    if (user == null) {
      throw new UsernameNotFoundException("No user found");
    }
    return new JwtUser(user);
  }
}
