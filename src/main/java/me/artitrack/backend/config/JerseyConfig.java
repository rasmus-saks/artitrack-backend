package me.artitrack.backend.config;

import me.artitrack.backend.controller.ApiController;
import me.artitrack.backend.controller.AuthController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
  public JerseyConfig() {
    register(ApiController.class);
    register(AuthController.class);
  }
}
