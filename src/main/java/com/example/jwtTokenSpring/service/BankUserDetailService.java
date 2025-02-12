package com.example.jwtTokenSpring.service;

import com.example.jwtTokenSpring.entity.Customer;
import com.example.jwtTokenSpring.repository.CustomerRepository;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankUserDetailService implements UserDetailsService {

    private CustomerRepository customerRepository;

    public BankUserDetailService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
        return User.withUsername(customer.getEmail()).password(customer.getPwd()).authorities(authorities).build();
    }
}
