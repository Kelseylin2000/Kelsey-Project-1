package com.example.stylish.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.stylish.dto.AuthResponseDto;
import com.example.stylish.dto.DataResponseDto;
import com.example.stylish.dto.SignInRequestDto;
import com.example.stylish.dto.SignUpRequestDto;
import com.example.stylish.dto.UserDto;
import com.example.stylish.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/1.0/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDto signUpRequest, @RequestHeader("Content-Type") String contentType) {
        DataResponseDto<AuthResponseDto> reponse = userService.signUp(signUpRequest);
        return ResponseEntity.ok(reponse);
    }

    @PostMapping(value = "/signin", consumes = "application/json")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequestDto signInRequest, @RequestHeader("Content-Type") String contentType) {
        DataResponseDto<AuthResponseDto> response = userService.signIn(signInRequest);;
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        DataResponseDto<UserDto> reponse = userService.getUserProfile(token);
        return ResponseEntity.ok(reponse);
    }   
}
