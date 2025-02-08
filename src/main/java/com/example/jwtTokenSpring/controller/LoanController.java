package com.example.jwtTokenSpring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    @GetMapping("/myLoan")
    public String myLoan(){
        return "Welcome to the loan controller";
    }
}
