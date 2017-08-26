package me.artitrack.backend.repository;

import me.artitrack.backend.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


@Component
public interface UserRepository extends CrudRepository<User, String> {
  @Cacheable("userCache")
  User findOneBySteam64(String steam64);
}
