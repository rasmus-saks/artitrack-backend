package me.artitrack.backend.config;

import me.artitrack.backend.security.JwtAuthenticationEntryPoint;
import me.artitrack.backend.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.ws.rs.HttpMethod;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtAuthenticationEntryPoint unauthorizedHandler;

  @Autowired
  public WebSecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler) {
    this.unauthorizedHandler = unauthorizedHandler;
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  @SuppressWarnings("PMD.SignatureDeclareThrowsException")
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        // we don't need CSRF because our token is invulnerable
        .csrf()
        .disable()

        .exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler)
        .and()

        // don't create session
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()

        .authorizeRequests()
        //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // allow anonymous resource requests
        .antMatchers(HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js")
        .permitAll()
        .antMatchers("/auth/**")
        .permitAll()
        .anyRequest()
        .authenticated();

    // Custom JWT based security filter
    httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

    // disable page caching
    httpSecurity.headers().cacheControl();
  }

  @Bean
  public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
    return new JwtAuthenticationTokenFilter();
  }
}
