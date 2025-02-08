package com.example.jwtTokenSpring.service.impl;

import com.example.jwtTokenSpring.entity.Customer;
import com.example.jwtTokenSpring.entity.CustomerDTO;
import com.example.jwtTokenSpring.repository.CustomerRepository;
import com.example.jwtTokenSpring.service.UserManagerService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagerServiceImpl implements UserManagerService {

    private CustomerRepository customerRepository;

    private PasswordEncoder passwordEncoder;

    public UserManagerServiceImpl(CustomerRepository customerRepository,PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer createUser(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        String hashPwd = passwordEncoder.encode(customerDTO.getPassword());
        customer.setPassword(hashPwd);
        customerRepository.save(customer);
        return customer;
    }
}
