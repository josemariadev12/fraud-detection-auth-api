package com.fraud_auth_api.dto;


import com.fraud_auth_api.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttemptResponseDTO {
    
    
     private String email;
     private Role role;
     private String acessToken;
     private String refreshToken;

}



