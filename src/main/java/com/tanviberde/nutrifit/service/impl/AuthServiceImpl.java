package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.dto.auth.AuthResponse;
import com.tanviberde.nutrifit.dto.auth.LoginRequest;
import com.tanviberde.nutrifit.dto.auth.RegisterRequest;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.exception.DuplicateResourceException;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.security.JwtService;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(savedUser);
        String token = jwtService.generateToken(userPrincipal);

        return AuthResponse.builder()
                .token(token)
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new DuplicateResourceException("Invalid email or password"));

        UserPrincipal userPrincipal = new UserPrincipal(user);
        String token = jwtService.generateToken(userPrincipal);

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}