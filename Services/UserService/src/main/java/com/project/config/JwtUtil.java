package com.project.config;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${jwt.token.expiration.millis}")
    private long jwtExpiration;

    @Value("${jwt.token.secret}")
    private String jwtSecret;

    private Key jwtKey;// Key interface is the top-level interface for all keys. It defines the functionality shared by all Key objects.

    @PostConstruct
    public void init() {
        jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    

//  @PostConstruct
//  public void init() {
//     byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
//       jwtKey = Keys.hmacShaKeyFor(keyBytes);
//      }

    // ---------- Create Token - // 1. Subject: email                                 | 2.Role claim: comma-separated roles (e.g., "ROLE_CUSTOMER")
                                 // 3. Expiration: current time + configured lifetime | 4.Signature: HMAC SHA-256 using jwtSecret
    
    
    // Authentication : Represents the token for an authentication request or for an authenticated principal once the request has been processed by the AuthenticationManager.authenticate(Authentication) method.
    public String createToken(Authentication auth) { 
    	// UsernamePasswordAuthenticationToken - setAuthenticated(false);
        String email = ((UserDetails) auth.getPrincipal()).getUsername();  // email as subject
        String roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("role", roles)
                .signWith(jwtKey, SignatureAlgorithm.HS256)
                .compact();
    }


    // ---------- Validate Token ----------
    public Authentication validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String email = claims.getSubject();
        String roles = (String) claims.get("role");
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
        return new UsernamePasswordAuthenticationToken(email, null, authorities); // super.setAuthenticated(true);
    }                                              // credentials = null

    // ---------- Extract Claims ----------
    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoles(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}
