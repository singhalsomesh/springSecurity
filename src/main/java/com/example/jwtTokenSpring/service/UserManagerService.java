package com.example.jwtTokenSpring.service;

import com.example.jwtTokenSpring.entity.Customer;
import com.example.jwtTokenSpring.entity.CustomerDTO;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface UserManagerService {
    Customer createUser(CustomerDTO customerDTO);

    Optional<Customer> findUsers(Authentication authentication);
}
