package me.artitrack.backend.controller;

import io.jsonwebtoken.JwtException;
import me.artitrack.backend.config.ServerConfig;
import me.artitrack.backend.security.JwtAuthenticationResponse;
import me.artitrack.backend.security.JwtTokenUtil;
import me.artitrack.backend.security.JwtUserService;
import me.artitrack.backend.steam.SteamApiService;
import org.apache.http.HttpStatus;
import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Component
@Path("/auth")
public class AuthController {
  private static final String OPENID_ID_PREFIX = "http://steamcommunity.com/openid/id/";
  private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
  private static ConsumerManager manager = new ConsumerManager();
  private final ServerConfig serverConfig;
  private final UserDetailsService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;
  private final SteamApiService steamApiService;
  private final JwtUserService jwtUserService;
  private final DiscoveryInformation discovered;


  @Autowired
  public AuthController(ServerConfig serverConfig, UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil,
                        SteamApiService steamApiService, JwtUserService jwtUserService) throws DiscoveryException {
    this.serverConfig = serverConfig;
    this.userDetailsService = userDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.steamApiService = steamApiService;
    this.jwtUserService = jwtUserService;
    List list = manager.discover("https://steamcommunity.com/openid");
    manager.setMaxAssocAttempts(0);
    discovered = manager.associate(list);
  }

  @Path("login")
  @GET
  public Response login(@QueryParam("return_to") String returnTo) throws MessageException, ConsumerException {
    UriComponents components = UriComponentsBuilder.fromHttpUrl(serverConfig.getBaseUrl() + "auth/return")
        .queryParam("return_to", returnTo).build();
    AuthRequest authReq = manager.authenticate(discovered, components.toUriString());
    return Response.seeOther(URI.create(authReq.getDestinationUrl(true))).build();
  }

  @Path("validateToken")
  @GET
  @Produces("application/json")
  public Response validateToken(@QueryParam("token") String token) {
    try {
      String steam64 = jwtTokenUtil.getIdFromToken(token);
      return Response.ok(jwtUserService.loadUserBySteam64(steam64).getUser()).build();
    } catch (JwtException ex) {
      LOG.info("Failed to validate JWT token {}", token);
      return Response.status(HttpStatus.SC_UNAUTHORIZED).build();
    }
  }

  @Path("return")
  @GET
  @Produces("application/json")
  public Response returned(@Context HttpServletRequest request, @QueryParam("return_to") String returnTo)
      throws AssociationException, DiscoveryException, MessageException {
    ParameterList openidResp = new ParameterList(request.getParameterMap());
    StringBuffer receivingURL = request.getRequestURL();
    String queryString = request.getQueryString();
    if (queryString != null && queryString.length() > 0) {
      receivingURL.append('?').append(request.getQueryString());
    }

    // verify the response
    VerificationResult verification = manager.verify(receivingURL.toString(), openidResp, discovered);

    // examine the verification result and extract the verified identifier
    Identifier verified = verification.getVerifiedId();
    if (verified != null) {
      String steam64 = verified.getIdentifier().substring(OPENID_ID_PREFIX.length());
      UserDetails details = userDetailsService.loadUserByUsername(steam64);
      String token = jwtTokenUtil.generateToken(details);
      steamApiService.getSteamUser(details.getUsername()).thenAccept(user -> LOG.debug(user.getPersonaname()));
      if (returnTo != null && !returnTo.isEmpty()) {
        // Add 'token' query parameter to return URL
        UriComponents returnComponents = UriComponentsBuilder.fromHttpUrl(returnTo).queryParam("token", token).build();
        return Response.seeOther(returnComponents.toUri()).cookie(jwtTokenUtil.getCookieFromToken(token)).build();
      }
      return Response.ok(new JwtAuthenticationResponse(token)).cookie(jwtTokenUtil.getCookieFromToken(token)).build();
    }
    return Response.status(Response.Status.UNAUTHORIZED).build();
  }
}
