package com.favour.Horizon.Banking.App.service;

import com.favour.Horizon.Banking.App.payload.request.LoginRequest;
import com.favour.Horizon.Banking.App.payload.request.UserRequest;
import com.favour.Horizon.Banking.App.payload.response.APIResponse;
import com.favour.Horizon.Banking.App.payload.response.BankResponse;
import com.favour.Horizon.Banking.App.payload.response.JwtAuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    BankResponse registerUser(UserRequest userRequest);

    ResponseEntity<APIResponse<JwtAuthResponse>> login(LoginRequest loginRequest);
}
