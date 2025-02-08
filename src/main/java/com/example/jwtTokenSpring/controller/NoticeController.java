package com.example.jwtTokenSpring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticeController {
    @GetMapping("/myNotice")
    public String myNotice(){
        return "Welcome to the notice controller";
    }
}
