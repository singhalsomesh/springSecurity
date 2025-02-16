package com.example.jwtTokenSpring.config;

import com.example.jwtTokenSpring.exceptionHandling.CustomBasicAuthenticationEntryPoint;
import com.example.jwtTokenSpring.exceptionHandling.CustomerAccessDeniedExceptionHandling;
import com.example.jwtTokenSpring.filter.CsrfCookieFilter;
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

import java.util.Collection;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!production")
public class BankConfig {

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
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            }
        }));

        //http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
       // http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(3).maxSessionsPreventsLogin(true))

                http.securityContext(contextCofig -> contextCofig.requireExplicitSave(false))
                        .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                        .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                                .ignoringRequestMatchers("/myNotice","/myContact","/api/createUser")
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
                 .authorizeHttpRequests((request) -> request
                .requestMatchers("/myAccount","/myCards","/myBalance","/myLoan","/api/user").authenticated()
                .requestMatchers("/myNotice","/myContact","/api/createUser").permitAll());
        http.formLogin(withDefaults());
        //http.httpBasic(withDefaults());
        http.httpBasic(hsbc -> hsbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(hse -> hse.accessDeniedHandler(new CustomerAccessDeniedExceptionHandling()));
        return http.build();
    }

    // using InMemoryUserDetailsManager
    /*
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.withUsername("user").password("{noop}Airtel@85074").authorities("read").build();
        UserDetails admin = User.withUsername("admin").password("{bcrypt}$2a$12$CmV9LVYDawwxvZ6KK302muuSC7m.NqbttFdOSN0KN5UCfeW1dsPHO").authorities("lead").build();
        return new InMemoryUserDetailsManager(user,admin);
    } */

    // using JdbcUserDetailsManager
    // create a custom class BankUserDetailService and comment this to void confusion
    /*
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    } */

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
