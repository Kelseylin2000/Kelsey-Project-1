package com.example.stylish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int id;

    @Size(max = 20, message = "Provider should not be longer than 20 characters")
    private String provider;

    @Size(max = 30, message = "Name should not be longer than 30 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Size(max = 100, message = "Email should not be longer than 100 characters")
    private String email;

    @Size(max = 255, message = "Picture URL should not be longer than 255 characters")
    private String picture;
}