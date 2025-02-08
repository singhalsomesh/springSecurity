package com.example.jwtTokenSpring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    @GetMapping("/myContact")
    public String myContact(){
        return "Welcome to the card controller";
    }
}
