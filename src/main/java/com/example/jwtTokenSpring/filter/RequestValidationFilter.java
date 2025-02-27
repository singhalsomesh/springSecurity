package com.example.jwtTokenSpring.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class RequestValidationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.nonNull(header)) {
            header = header.trim();
            if (StringUtils.startsWithIgnoreCase(header, "Basic ")) {
                byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decoded;
                try{
                    decoded = Base64.getDecoder().decode(base64Token);
                    String token = new String(decoded, StandardCharsets.UTF_8);
                    int delimiter = token.indexOf(":");
                    if (delimiter == -1) {
                        throw new RuntimeException("Invalid basic authentication token");
                    }
                    String username = token.substring(0, delimiter);
                    String password = token.substring(delimiter + 1);
                    if(username.toLowerCase().contains("test")){
                        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        httpServletResponse.setContentType("application/json");
                        httpServletResponse.getWriter().write("{\"message\": \"Invalid username\"}");
                        return;
                    }
                } catch (Exception e) {
                    throw new BadRequestException("Failed to decode basic authentication token");
                }
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    }
}
