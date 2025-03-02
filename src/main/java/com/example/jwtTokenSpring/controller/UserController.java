package com.example.jwtTokenSpring.controller;

import com.example.jwtTokenSpring.dto.LoginRequestDTO;
import com.example.jwtTokenSpring.dto.LoginResponseDto;
import com.example.jwtTokenSpring.entity.Customer;
import com.example.jwtTokenSpring.entity.CustomerDTO;
import com.example.jwtTokenSpring.service.UserManagerService;
import com.example.jwtTokenSpring.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @RequestMapping("/api/user")
    public ResponseEntity<?> getUserDetailsAfterLogin(Authentication authentication) {
        Optional<Customer> optionalCustomer = userManagerService.findUsers(authentication);
        return new ResponseEntity<>(optionalCustomer, HttpStatus.OK);
    }

    @PostMapping("/api/createToken")
    ResponseEntity<LoginResponseDto> getToken(@RequestBody LoginRequestDTO loginRequestDTO){
        return new ResponseEntity<>(userManagerService.createToken(loginRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/api/refreshToken")
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestParam String refreshToken) {
        return new ResponseEntity<>(userManagerService.refreshToken(refreshToken), HttpStatus.OK);
    }

}
