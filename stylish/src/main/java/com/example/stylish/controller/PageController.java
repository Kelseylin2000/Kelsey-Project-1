package com.example.stylish.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    @GetMapping("/")
    public String leadToHomePage() {
        return "redirect:/index.html";
    }

    @GetMapping("/index.html")
    public String homePage() {
        return "index";
    }

    @GetMapping("/product.html")
    public String productPage() {
        return "product";
    }

    @GetMapping("/profile.html")
    public String profilePage() {
        return "profile";
    }

    @GetMapping("/thankyou.html")
    public String thankyouPage() {
        return "thankyou";
    }

    @GetMapping("/accessDenied.html")
    public String AccessDeniedPage() {
        return "accessDenied";
    }
    
}
