package com.fraud_auth_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fraud_auth_api.dto.LoginAttemptRequestDTO;
import com.fraud_auth_api.dto.LoginAttemptResponseDTO;
import com.fraud_auth_api.services.LoginAttemptService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginAttemptService loginAttemptService;

    public AuthController(LoginAttemptService loginAttemptService){
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginAttemptResponseDTO> login(@RequestBody @Valid LoginAttemptRequestDTO dto, HttpServletRequest request){
        String ip = request.getRemoteAddr();
        LoginAttemptResponseDTO initLogin = loginAttemptService.login(dto, ip);


        return ResponseEntity.ok(initLogin);
    }
}
