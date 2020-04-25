package xyz.romros.miwtask.security.config;

import static java.time.Instant.now;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService {

  @Value("${jwt.client-secret}")
  private String secret;
  @Value("${jwt.access-token-validitity-seconds}")
  private int accessTokenValiditySeconds;

  public String getUsernameFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public String generateToken(UserDetails userDetails) {
    final int tokenValidityInMillis = accessTokenValiditySeconds * 1000;
    final Instant now = now();

    return Jwts.builder()//
               .setClaims(new HashMap<>())//
               .setSubject(userDetails.getUsername())//
               .setIssuedAt(Date.from(now))//
               .setExpiration(Date.from(now.plusMillis(tokenValidityInMillis)))//
               .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)), SignatureAlgorithm.HS512)//
               .compact();
  }

  public Boolean isTokenValid(String token, UserDetails userDetails) {
    return getUsernameFromToken(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expirationDate = getAllClaimsFromToken(token).getExpiration();
    return expirationDate.before(new Date());
  }

}
