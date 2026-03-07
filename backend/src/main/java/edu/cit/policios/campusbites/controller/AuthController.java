package edu.cit.policios.campusbites.controller;

import edu.cit.policios.campusbites.entity.User;
import edu.cit.policios.campusbites.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") // Matches SDD: /auth/register and /auth/login
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.registerUser(user);
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User loginRequest) {
    return authService.login(loginRequest.getEmail(), loginRequest.getPassword())
        .map(user -> ResponseEntity.ok(user))
        .orElse(ResponseEntity.status(401).build());
}
}