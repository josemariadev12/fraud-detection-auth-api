package com.fraud_auth_api.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fraud_auth_api.dto.UserRequestDTO;
import com.fraud_auth_api.dto.UserResponseDTO;
import com.fraud_auth_api.entity.User;
import com.fraud_auth_api.enums.Role;
import com.fraud_auth_api.enums.UserStatus;
import com.fraud_auth_api.repository.UserRepository;

@Service
public class UserService {
    

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserResponseDTO toDTO(User user){
        return new UserResponseDTO(user.getId(),
        user.getEmail(),
        user.getRole(),
        user.getStatus());
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto){ 

        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.ROLE_USER);

        User savedUser = userRepository.save(user);

        return toDTO(savedUser);
    }

    @Transactional(readOnly=true)
    public UserResponseDTO findById(Long id){
        User user = userRepository.findById(id)
        .orElseThrow (() -> new IllegalArgumentException("Usuário não existente"));

        return toDTO(user);

    }
    @Transactional
    public UserResponseDTO updateStatus(Long id, UserStatus status){
        if(status == null) throw new IllegalArgumentException("Usuario não pode ser nulo");
        User user = userRepository.findById(id)
        .orElseThrow(()-> new IllegalArgumentException("Usuario não Existe"));
        user.setStatus(status);
        userRepository.save(user);
        return toDTO(user);
    }

}
    
