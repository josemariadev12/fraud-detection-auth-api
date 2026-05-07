package com.fraud_auth_api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttemptResponseDTO {
    
        private Long id;
        private Long userid;
        private String email;
        private LocalDateTime timestamp;
        private String ip;
        private boolean success;

}



