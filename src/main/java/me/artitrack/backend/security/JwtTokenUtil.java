package me.artitrack.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.artitrack.backend.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.NewCookie;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
  private static final String CLAIM_KEY_USERNAME = "sub";
  private static final String CLAIM_KEY_CREATED = "iat";
  private final TimeProvider timeProvider;
  @Value("${jwt.secret}")
  private String secret;
  @Value("${jwt.expiration}")
  private Long expiration;
  @Value("${auth.cookie.name:auth-token}")
  private String tokenCookieName;

  @Autowired
  public JwtTokenUtil(TimeProvider timeProvider) {
    this.timeProvider = timeProvider;
  }

  public String getIdFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getIssuedAtDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getIssuedAt);
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Boolean isTokenExpired(String token) {
    final Date exp = getExpirationDateFromToken(token);
    return exp.before(timeProvider.now());
  }

  private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
    return lastPasswordReset != null && created.before(lastPasswordReset);
  }

  public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
    final Date created = getIssuedAtDateFromToken(token);
    return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && !isTokenExpired(token);
  }

  public String refreshToken(String token) {
    final Claims claims = getAllClaimsFromToken(token);
    claims.setIssuedAt(timeProvider.now());
    return doRefreshToken(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  public String doRefreshToken(Claims claims) {
    return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  public String getTokenCookieName() {
    return tokenCookieName;
  }

  public Boolean validateToken(String token, JwtUser user) {
    final String subject = getIdFromToken(token);
    return subject.equals(user.getUser().getSteam64()) && !isTokenExpired(token);
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
    claims.put(CLAIM_KEY_CREATED, new Date());
    return generateToken(claims);
  }

  private String generateToken(Map<String, Object> claims) {
    return Jwts.builder()
        .setClaims(claims)
        .setExpiration(generateExpirationDate())
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  private Date generateExpirationDate() {
    return new Date(System.currentTimeMillis() + expiration * 1000);
  }

  public NewCookie getCookieFromToken(String token) {
    return new NewCookie(tokenCookieName, token, "/", "", "", expiration.intValue(), false);
  }
}
