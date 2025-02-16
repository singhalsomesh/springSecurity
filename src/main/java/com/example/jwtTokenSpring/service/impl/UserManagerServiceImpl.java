package com.example.jwtTokenSpring.service.impl;

import com.example.jwtTokenSpring.entity.Customer;
import com.example.jwtTokenSpring.entity.CustomerDTO;
import com.example.jwtTokenSpring.repository.CustomerRepository;
import com.example.jwtTokenSpring.service.UserManagerService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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
}
