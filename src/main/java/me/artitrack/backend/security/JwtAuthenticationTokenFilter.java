package me.artitrack.backend.security;

import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  private static final String BEARER = "Bearer ";
  private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

  @Autowired
  private JwtUserService userService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String authToken = getTokenFromRequest(request);

    String id;
    try {
      id = jwtTokenUtil.getIdFromToken(authToken);
    } catch (IllegalArgumentException | MalformedJwtException e) {
      id = null;
    }

    LOG.trace("Checking authentication for user {}", id);

    if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        JwtUser userDetails = this.userService.loadUserBySteam64(id);
        if (jwtTokenUtil.validateToken(authToken, userDetails)) {
          JwtAuthenticationToken authentication = new JwtAuthenticationToken(authToken, userDetails);
          authentication.setAuthenticated(true);
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);
          LOG.trace("Authenticated user {}, set security context ", id);
        }

      } catch (UsernameNotFoundException e) {
        //Failed to find user, reset auth token
        response.addCookie(new Cookie(jwtTokenUtil.getTokenCookieName(), ""));
      }


    }
    chain.doFilter(request, response);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String authToken = "";
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (cookie.getName().equals(jwtTokenUtil.getTokenCookieName())) {
          authToken = cookie.getValue();
          break;
        }
      }
    }
    // Allow header token to override cookie
    String headerToken = request.getHeader("Authorization");
    if (headerToken != null && headerToken.startsWith(BEARER)) {
      authToken = headerToken.substring(BEARER.length());
    }
    return authToken;
  }
}
