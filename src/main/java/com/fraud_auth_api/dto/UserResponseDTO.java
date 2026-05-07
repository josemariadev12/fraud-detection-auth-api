package com.fraud_auth_api.dto;

import com.fraud_auth_api.enums.Role;
import com.fraud_auth_api.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class UserResponseDTO {
    
    private Long id;
    private String email;
    private Role role;
    private UserStatus status;

}
