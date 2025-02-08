package com.example.jwtTokenSpring.config;

import com.example.jwtTokenSpring.exceptionHandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Component
@Profile("production")
public class ProductionBankConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.requiresChannel(rcc -> rcc.anyRequest().requiresSecure())
                .csrf(c -> c.disable()).authorizeHttpRequests((request) -> request
                .requestMatchers("/myAccount","/myCards","/myBalance","/myLoan").authenticated()
                .requestMatchers("/myNotice","/myContact","/api/createUser").permitAll());
        http.formLogin(withDefaults());
        //http.httpBasic(withDefaults());
        http.httpBasic(hsbc -> hsbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
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
