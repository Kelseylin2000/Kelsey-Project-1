package com.example.stylish.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SigninPageController {
    
    @GetMapping("/signin.html")
    public String facebookSigninPage() {
        return "signin";
    }
}
