package com.cdac.security;


import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component //to declare spring bean , for validating JWT , geberatinJWT will be done by User MS

public class JwtUtils {

	@Value("${jwt.secret.key}") 	
	private String jwtSecret;

	

	private SecretKey key;//=> represents symmetric key

	@PostConstruct
	public void init() {
		System.out.printf("Key %s ",jwtSecret);
		/*create secret key instance from  Keys class
		 * Keys - builder of Secret key
		 * Create a Secret Key using HMAC-SHA256 encryption algo.
		 */		
		key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}

	
	// this method will be invoked by our custom JWT filter
	public String getUserNameFromJwtToken(Claims claims) {
		return claims.getSubject();
	}
//
//	// this method will be invoked by our custom JWT filter
//	public Claims validateJwtToken(String jwtToken) {
//		// try {
//		Claims claims = Jwts.parser()
//
//				.verifyWith(key) // sets the SAME secret key for JWT signature verification
//				.build()
//
//				// rets the JWT parser set with the Key
//				.parseSignedClaims(jwtToken) // rets JWT with Claims added in the body
//				.getPayload();// => JWT valid , rets the Claims(payload)
//		/*
//		 * parseSignedClaims - Throws:
//		 * 	UnsupportedJwtException - if the jwt argument does not represent 
//		 * a signed Claims JWT
//		 * JwtException - if the jwt string cannot be parsed or 
//		 * validated as required.
//		 * IllegalArgumentException - if the jwt string is null or 
//		 * empty or only whitespace
//		 */
//		return claims;
//	}
	public Claims validateJwtToken(String jwtToken) {
		// try {
		Claims claims = Jwts.parser()

				.verifyWith(key) // sets the SAME secret key for JWT signature verification
				.build()

				// rets the JWT parser set with the Key
				.parseSignedClaims(jwtToken) // rets JWT with Claims added in the body
				.getPayload();// => JWT valid , rets the Claims(payload)
		/*
		 * parseClaimsJws - throws:UnsupportedJwtException -if the JWT body | payload
		 * does not represent any Claims JWSMalformedJwtException - if the JWT body |
		 * payload is not a valid JWSSignatureException - if the JWT signature
		 * validation fails ExpiredJwtException - if the specified JWT is expired
		 * IllegalArgumentException - if the JWT claims body | payload is null or empty
		 * or only whitespace
		 */
		return claims;
	}

//	private List<String> getAuthoritiesInString(Collection<? extends GrantedAuthority> authorities) {
//		return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
//	}

	// this method will be invoked by our custom JWT filter to get list of granted
	// authorities n store it in auth token
	@SuppressWarnings("unchecked")
	public List<GrantedAuthority> getAuthoritiesFromClaims(Claims claims) {

		List<String> authorityNamesFromJwt = 
				(List<String>) claims.get("authorities");
		List<GrantedAuthority> authorities = 
				authorityNamesFromJwt.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());		

		authorities.forEach(System.out::println);
		return authorities;
	}

	

	public Authentication populateAuthenticationTokenFromJWT(String jwt) {
		// validate JWT n retrieve JWT body (claims)
		Claims payloadClaims = validateJwtToken(jwt);
		// get user name from the claims or you can store user id in the generate token n extract it here
		String email = getUserNameFromJwtToken(payloadClaims);
		// get granted authorities as a custom claim
		List<GrantedAuthority> authorities = getAuthoritiesFromClaims(payloadClaims);
			// add user name/email , null:password granted authorities in Authentication object
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(email, null, authorities);
//		UsernamePasswordAuthenticationToken token = 
//				new UsernamePasswordAuthenticationToken(new User(email,"", authorities), null, authorities);
//	
		System.out.println("is authenticated " + token.isAuthenticated());// true
		return token;

	}



	
}
