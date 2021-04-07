package com.nuxxxcake.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenVerifierService extends OncePerRequestFilter {
  @Value("${spring.custom.secret_salt_key}")
  public String SECRET_KEY;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String tokenHeader = request.getHeader("Authorization");
    if (tokenHeader == null || tokenHeader.length() == 0 || !tokenHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = tokenHeader.replace("Bearer ", "");

    try {
      Jws<Claims> claims = Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
        .build()
        .parseClaimsJws(token);

      Claims body = claims.getBody();
      String username = body.getSubject();

      List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

      Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities
        .stream()
        .map(authority -> new SimpleGrantedAuthority(authority.get("authority")))
        .collect(Collectors.toSet());

      Authentication auth = new UsernamePasswordAuthenticationToken(
        username,
        null,
        simpleGrantedAuthorities
      );

      SecurityContextHolder.getContext().setAuthentication(auth);
    } catch (JwtException e) {
      throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
    }
    filterChain.doFilter(request, response);
  }
}

