package com.project.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get JWT token from Authorization header
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        Authentication auth = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            System.out.println("JWT Token: " + token);

            // 2. Validate token and create Authentication
            try {
                auth = jwtUtil.validateToken(token); // auth object 
            } catch (Exception e) {
                System.out.println("Invalid JWT token: " + e.getMessage());
            }
        }

        // 3. Attach authentication to SecurityContext if valid
        if (auth != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(auth); //  auth => jwtUtil.validateToken(token)
        }

        // 4. Continue with next filter
        filterChain.doFilter(request, response);
    }
}
