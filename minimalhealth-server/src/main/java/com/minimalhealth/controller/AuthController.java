package com.minimalhealth.controller;

import com.minimalhealth.model.dto.request.*;
import com.minimalhealth.model.dto.response.ApiResponse;
import com.minimalhealth.model.dto.response.LoginResponse;
import com.minimalhealth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/password")
    public ApiResponse<LoginResponse> passwordLogin(@Valid @RequestBody PasswordLoginRequest request) {
        LoginResponse response = authService.passwordLogin(request.getAccount(), request.getPassword());
        return ApiResponse.success(response);
    }

    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request.getPhone(), request.getPassword());
        return ApiResponse.success("注册成功", response);
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request.getRefreshToken());
        return ApiResponse.success(response);
    }
}
