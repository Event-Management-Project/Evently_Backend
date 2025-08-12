package com.cdac.security;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration

public class SecurityConfig {
	private final JwtUtils jwtUtils;
	

	public SecurityConfig(JwtUtils jwtUtils) {
		super();
		this.jwtUtils = jwtUtils;
	}
	/*
	 * ServerHttpSecurity -  is similar to Spring Security's - HttpSecurity but
	 * for WebFlux.
	 * - It allows configuring web based security for specific http requests. 
	 * - By default it will be applied to all requests, but can be restricted using
     *   securityMatcher(ServerWebExchangeMatcher) or other similar methods.
	 */
	@Bean
     SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		//disable csrf
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        //disable http basic auth (??? check if it needs to be explicitly disabled or happens by default
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        //url based auth rules
            .authorizeExchange(exchange -> exchange
                .pathMatchers("/swagger-ui/**", "/v3/api-docs/**"
                		, "/users/**", "/users/**","/events", "/organiser/organiser/**","/customer/customers/**")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/events/**").hasAnyRole("CUSTOMER","ORGANISER") 
                .pathMatchers(HttpMethod.GET, "/customer/**").hasAnyRole("CUSTOMER","ORGANISER") 
                .pathMatchers(HttpMethod.POST, "/add-event/**").hasRole("ORGANISER")
                .pathMatchers(HttpMethod.PUT, "/edit-event/**").hasRole("ORGANISER")
                .anyExchange().authenticated()
                
            )
            .securityContextRepository(securityContextRepository())
            .exceptionHandling(exc -> 
            exc.authenticationEntryPoint(authenticationEntryPoint()));

   

        return http.build();
    }
	 @Bean
	     ReactiveAuthenticationManager reactiveAuthenticationManager() {
	        return authentication -> {
	            String token = authentication.getCredentials().toString();
	            try {
	            Authentication auth= jwtUtils.populateAuthenticationTokenFromJWT(token);
	                return Mono.just(auth);
	            } catch (Exception e) {
					System.out.println("ERROR!!!!!!!!!"+e);
					return Mono.error(new BadCredentialsException("Invalid token"));
		    }
	        };
	    }

	    @Bean
	     ServerSecurityContextRepository securityContextRepository() {
	        return new ServerSecurityContextRepository() {
	            @Override
	            public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
	            		                return Mono.empty(); // stateless
	            }
	            //equivalent code to JWT custom filter - in webflux(async scenario)
	            @Override
	            public Mono<SecurityContext> load(ServerWebExchange exchange) {
	                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
	                if (authHeader != null && authHeader.startsWith("Bearer ")) {
	                    String token = authHeader.substring(7);
	                    Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
	                    return reactiveAuthenticationManager().authenticate(auth)
	                            .map(SecurityContextImpl::new);
	                }
	                return Mono.empty();
	            }
	        };
	    }
	    @Bean
	     ServerAuthenticationEntryPoint authenticationEntryPoint() {
	        return (exchange, ex) -> {
	        		            var response = exchange.getResponse();
	            response.setStatusCode(HttpStatus.UNAUTHORIZED);
	            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
	            String body = """
	                {
	                  "status": 401,
	                  "error": "Unauthorized",
	                  "message": "Invalid or missing token"
	                }
	                """;
	            var buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
	            return response.writeWith(Mono.just(buffer));
	        };
	    }


//	    @Bean
//	    public ServerAccessDeniedHandler accessDeniedHandler() {
//	        return new HttpStatusServerAccessDeniedHandler(HttpStatus.FORBIDDEN); // 403
//	    }
	}

