package com.example.jwtTokenSpring.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class AuthenticationLoginFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationLoginFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.nonNull(authentication)){
            log.info("User {} is logged in", authentication.getName());
        } else {
            log.info("No user is logged in");
        }
        chain.doFilter(request,response);
    }
}
