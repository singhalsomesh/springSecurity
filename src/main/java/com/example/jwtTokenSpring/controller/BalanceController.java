package com.example.jwtTokenSpring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    @GetMapping("/myBalance")
    public String myBalance(){
        return "Welcome to the balance controller";
    }

}
