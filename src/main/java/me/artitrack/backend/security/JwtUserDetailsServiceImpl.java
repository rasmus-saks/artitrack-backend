package me.artitrack.backend.security;

import me.artitrack.backend.model.User;
import me.artitrack.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

  private static final Logger LOG = LoggerFactory.getLogger(JwtUserDetailsServiceImpl.class);

  private final UserRepository userRepository;

  @Autowired
  public JwtUserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String steam64) {
    User user = userRepository.findOneBySteam64(steam64);

    if (user == null) {
      LOG.info("Created new user with steam64 %s", steam64);
      user = userRepository.save(new User(steam64));
    }
    return JwtUserFactory.create(user);
  }
}
