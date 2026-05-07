package com.fraud_auth_api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fraud_auth_api.dto.UserRequestDTO;
import com.fraud_auth_api.dto.UserResponseDTO;
import com.fraud_auth_api.enums.UserStatus;
import com.fraud_auth_api.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid UserRequestDTO dto){
        UserResponseDTO created = userService.createUser(dto);
        return ResponseEntity.created(URI.create("/auth/register")).body(created);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){
        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponseDTO> updateStatus(@PathVariable Long id,@RequestParam UserStatus status){
        UserResponseDTO user = userService.updateStatus(id, status);
        return ResponseEntity.ok(user);
    }

}   
