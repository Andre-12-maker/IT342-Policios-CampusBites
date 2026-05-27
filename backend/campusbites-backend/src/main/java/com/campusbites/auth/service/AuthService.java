package com.campusbites.auth.service;

import com.campusbites.auth.dto.AuthDtos;
import com.campusbites.auth.model.User;
import com.campusbites.auth.repository.UserRepository;
import com.campusbites.common.exception.AppException;
import com.campusbites.common.security.JwtTokenProvider;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository        userRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtTokenProvider      jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository        = userRepository;
        this.passwordEncoder       = passwordEncoder;
        this.jwtTokenProvider      = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest req) {
        if (userRepository.existsByEmail(req.email()))
            throw AppException.conflict("An account with this email already exists");
        User user = new User();
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setFirstname(req.firstname());
        user.setLastname(req.lastname());
        user.setRole(User.Role.CUSTOMER);
        user = userRepository.save(user);
        return new AuthDtos.AuthResponse(jwtTokenProvider.generate(user), toDto(user));
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> AppException.unauthorized("User not found"));
        return new AuthDtos.AuthResponse(jwtTokenProvider.generate(user), toDto(user));
    }

    public static AuthDtos.UserDto toDto(User user) {
        return new AuthDtos.UserDto(user.getId(), user.getFirstname(), user.getLastname(),
                user.getEmail(), user.getRole().name());
    }
}