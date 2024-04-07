package com.favour.Horizon.Banking.App.infrastrusture.controller;

import com.favour.Horizon.Banking.App.payload.request.LoginRequest;
import com.favour.Horizon.Banking.App.payload.request.UserRequest;
import com.favour.Horizon.Banking.App.payload.response.APIResponse;
import com.favour.Horizon.Banking.App.payload.response.BankResponse;
import com.favour.Horizon.Banking.App.payload.response.JwtAuthResponse;
import com.favour.Horizon.Banking.App.service.AuthService;
import com.favour.Horizon.Banking.App.validations.ValidEmail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@Tag(name = "User Authentication Management APIs")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register new User Account",
            description = "Create a new User and assign Account Number"
    )
    @PostMapping("register-user")
    public BankResponse createAccount(@Valid @RequestBody UserRequest userRequest){
        return authService.registerUser(userRequest);
    }

    @PostMapping("login-user")
    public ResponseEntity<APIResponse<JwtAuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

}
