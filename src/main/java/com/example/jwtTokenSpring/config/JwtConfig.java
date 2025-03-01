package com.example.jwtTokenSpring.config;

import com.example.jwtTokenSpring.exceptionHandling.CustomBasicAuthenticationEntryPoint;
import com.example.jwtTokenSpring.exceptionHandling.CustomerAccessDeniedExceptionHandling;
import com.example.jwtTokenSpring.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!production")
public class JwtConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.addAllowedOrigin("http://localhost:4200");
                corsConfiguration.addAllowedMethod("*");
                corsConfiguration.addAllowedHeader("*");
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            }
        }));

        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/myNotice","/myContact","/api/createUser")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthenticationLoginFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenGenerationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter() , BasicAuthenticationFilter.class)
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                        .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                        .requestMatchers("/myBalance").hasAnyAuthority("VIEWBALANCE","VIEWACCOUNT")
                        .requestMatchers("/myLoan").hasAuthority("VIEWLOAN")
                        .requestMatchers("/api/user").authenticated()
                        .requestMatchers("/myNotice","/myContact","/api/createUser").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hsbc -> hsbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(hse -> hse.accessDeniedHandler(new CustomerAccessDeniedExceptionHandling()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
