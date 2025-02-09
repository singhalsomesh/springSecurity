package com.example.jwtTokenSpring.events;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationEvents {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationEvents.class);

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent authenticationSuccessEvent){
        log.info("Login successfully for the user {}",authenticationSuccessEvent.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failureEvent){
        log.info("Login failed for the user {} due to {}",failureEvent.getAuthentication().getName(),
                failureEvent.getException().getMessage());
    }
}
