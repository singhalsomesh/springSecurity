package com.example.jwtTokenSpring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

    @GetMapping("/myCards")
    public String myCards(){
        return "Welcome to the card controller";
    }
}
