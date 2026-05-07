package com.fraud_auth_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRequestDTO {
    @Email(message="invalid email format")
    @NotBlank (message="email is required")
    private String email;

    @NotBlank (message="password is required")
    private String password;
}
