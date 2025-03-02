package com.example.jwtTokenSpring.service.impl;

import com.example.jwtTokenSpring.dto.LoginRequestDTO;
import com.example.jwtTokenSpring.dto.LoginResponseDto;
import com.example.jwtTokenSpring.entity.Customer;
import com.example.jwtTokenSpring.entity.CustomerDTO;
import com.example.jwtTokenSpring.repository.CustomerRepository;
import com.example.jwtTokenSpring.service.UserManagerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.jwtTokenSpring.constant.AppConstant.*;

@Service
public class UserManagerServiceImpl implements UserManagerService {

    private CustomerRepository customerRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private Environment environment;

    public UserManagerServiceImpl(CustomerRepository customerRepository,
                                  PasswordEncoder passwordEncoder,
                                  AuthenticationManager authenticationManager,
                                  Environment environment) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.environment = environment;
    }

    @Override
    public Customer createUser(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        String hashPwd = passwordEncoder.encode(customerDTO.getPwd());
        customer.setPwd(hashPwd);
        customer.setCreateDt(new Date());
        customerRepository.save(customer);
        return customer;
    }

    @Override
    public Optional<Customer> findUsers(Authentication authentication) {
        return customerRepository.findByEmail(authentication.getName());
    }

    @Override
    public LoginResponseDto createToken(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequestDTO.username(),
                loginRequestDTO.password());
        authentication = authenticationManager.authenticate(authentication);
        String token = "";
        if(Objects.nonNull(authentication) && authentication.isAuthenticated()){
            // Generate JWT token
            if (Objects.nonNull(environment)) {
                String secret = environment.getProperty(JWT_SECRET,DEFAULT_JWT_SECRET_KEY);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                token = Jwts.builder()
                        .setIssuer("JWT Token Generation Filter")
                        .setSubject(authentication.getName())
                        .claim("username", authentication.getName())
                        .claim("authorities", authentication.getAuthorities()
                                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000))
                        .signWith(secretKey)
                        .compact();
            }
        }
        return new LoginResponseDto("Success",token);
    }

    @Override
    public LoginResponseDto refreshToken(String refreshToken) {
        String secret = environment.getProperty(JWT_SECRET, DEFAULT_JWT_SECRET_KEY);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parser().verifyWith(secretKey)
                    .build().parseSignedClaims(refreshToken).getPayload();
            String username = String.valueOf(claims.get("username"));
            String authorities = String.valueOf(claims.get("authorities"));
            // Generate new JWT token
            String newToken = Jwts.builder()
                    .setIssuer("JWT Token Generation Filter")
                    .setSubject(username)
                    .claim("username", username)
                    .claim("authorities", authorities)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000))
                    .signWith(secretKey)
                    .compact();

            return new LoginResponseDto("Success", newToken);
        } catch (Exception e) {
            return new LoginResponseDto("Failure", "Invalid refresh token");
        }
    }
}
