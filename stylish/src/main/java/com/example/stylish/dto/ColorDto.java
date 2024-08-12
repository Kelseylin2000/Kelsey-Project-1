package com.example.stylish.dto;

public class ColorDto {
    private String name;
    private String code;

    // Default constructor
    public ColorDto() {}

    // Parameterized constructor
    public ColorDto(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

