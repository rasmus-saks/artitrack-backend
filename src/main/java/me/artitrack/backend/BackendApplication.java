package me.artitrack.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@SuppressWarnings("PMD.UseUtilityClass")
public class BackendApplication {
  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }
}
