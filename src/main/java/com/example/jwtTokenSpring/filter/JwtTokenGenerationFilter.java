package com.example.jwtTokenSpring.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.jwtTokenSpring.constant.AppConstant.*;

public class JwtTokenGenerationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.nonNull(authentication)){
            // Generate JWT token
            Environment environment = getEnvironment();
            if (Objects.nonNull(environment)) {
                String secret = environment.getProperty(JWT_SECRET,DEFAULT_JWT_SECRET_KEY);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                String jwtToken = Jwts.builder()
                        .setIssuer("JWT Token Generation Filter")
                        .setSubject(authentication.getName())
                        .claim("username", authentication.getName())
                        .claim("authorities", authentication.getAuthorities()
                                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000))
                        .signWith(secretKey)
                        .compact();
                response.addHeader(JWT_Authorization, "Bearer " + jwtToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/api/user");
    }
}
