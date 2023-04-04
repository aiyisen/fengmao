package com.qy.ntf.util;

import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.entity.SysUserAdmin;
import com.qy.ntf.config.UserExt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author admin
 */
@Component
public class JwtTokenUtil implements Serializable {
  private static final long serialVersionUID = -3301605591108950415L;

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  private Clock clock = DefaultClock.INSTANCE;

  public String generateToken(SysUser userExt, Long expiration) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("USER_INFO", userExt);
    return doGenerateToken(claims, userExt.getUsername());
  }

  public String generateToken(SysUserAdmin userExt, Long expiration) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("USER_INFO", userExt);
    return doGenerateToken(claims, userExt.getAdminName());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {
    final Date createdDate = clock.now();
    final Date expirationDate = calculateExpirationDate(createdDate);
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(createdDate)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  private Date calculateExpirationDate(Date createdDate) {
    return new Date(createdDate.getTime() + expiration);
  }

  public Boolean validateToken(String token, UserExt userExt) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userExt.getUsername()) && !isTokenExpired(token));
  }

  public Object getObjectFromClaim(String token, String key) {
    final Claims claims = getAllClaimsFromToken(token);
    Object o = claims.get(key);
    return o;
  }

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  public Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(clock.now());
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }
}
