package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.auth.AuthResponse;
import com.tanviberde.nutrifit.dto.auth.LoginRequest;
import com.tanviberde.nutrifit.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}