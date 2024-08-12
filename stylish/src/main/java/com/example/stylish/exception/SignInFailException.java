package com.example.stylish.exception;

public class SignInFailException extends RuntimeException {
    public SignInFailException(String message) {
        super(message);
    }
}