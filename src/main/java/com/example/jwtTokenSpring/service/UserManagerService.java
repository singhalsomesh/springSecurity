package com.example.jwtTokenSpring.service;

import com.example.jwtTokenSpring.entity.Customer;
import com.example.jwtTokenSpring.entity.CustomerDTO;

public interface UserManagerService {
    Customer createUser(CustomerDTO customerDTO);
}
