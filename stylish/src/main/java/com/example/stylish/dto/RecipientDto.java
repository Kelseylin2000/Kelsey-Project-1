package com.example.stylish.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientDto {

    @Size(max = 30) 
    @NotBlank
    private String name;

    @Size(max = 50) 
    @NotBlank
    private String phone;

    @Size(max = 255)
    @NotBlank
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String time;
}
