package com.fraud_auth_api.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.fraud_auth_api.repository.UserRepository;



@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)throws IllegalArgumentException{
        return userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));
    }
}
