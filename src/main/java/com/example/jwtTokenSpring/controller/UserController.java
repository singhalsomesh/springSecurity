package com.example.jwtTokenSpring.controller;

import com.example.jwtTokenSpring.entity.Customer;
import com.example.jwtTokenSpring.entity.CustomerDTO;
import com.example.jwtTokenSpring.service.UserManagerService;
import com.example.jwtTokenSpring.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserManagerService userManagerService;

    public UserController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @PostMapping("/api/createUser")
    ResponseEntity<?> createUser(@RequestBody CustomerDTO customerDTO){
        Customer createdCustomer = userManagerService.createUser(customerDTO);
        ResponseDTO<Customer> response = new ResponseDTO<>("success", "User created successfully", createdCustomer);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

}
