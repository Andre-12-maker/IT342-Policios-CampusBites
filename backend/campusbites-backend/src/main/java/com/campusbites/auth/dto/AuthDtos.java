package com.campusbites.auth.dto;

import jakarta.validation.constraints.*;

public class AuthDtos {

    public record RegisterRequest(
            @NotBlank(message = "First name is required") String firstname,
            @NotBlank(message = "Last name is required")  String lastname,
            @NotBlank @Email(message = "Email must be valid") String email,
            @NotBlank @Size(min = 8, message = "Password must be at least 8 characters") String password
    ) {}

    public record LoginRequest(
            @NotBlank @Email(message = "Email must be valid") String email,
            @NotBlank String password
    ) {}

    public record UserDto(String id, String firstname, String lastname, String email, String role) {}

    public record AuthResponse(String accessToken, UserDto user) {}
}