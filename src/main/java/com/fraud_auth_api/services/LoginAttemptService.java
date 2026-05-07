package com.fraud_auth_api.services;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fraud_auth_api.dto.LoginAttemptRequestDTO;
import com.fraud_auth_api.dto.LoginAttemptResponseDTO;
import com.fraud_auth_api.entity.LoginAttempt;
import com.fraud_auth_api.entity.User;
import com.fraud_auth_api.enums.UserStatus;
import com.fraud_auth_api.repository.LoginAttemptRepository;
import com.fraud_auth_api.repository.UserRepository;

@Service
public class LoginAttemptService {
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptRepository loginAttemptRepository;
    private final UserRepository userRepository;

    public LoginAttemptService(LoginAttemptRepository loginAttemptRepository, 
    PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.loginAttemptRepository = loginAttemptRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    //transforma ResponseDto -> login (mapper)
    private LoginAttemptResponseDTO toDTO(LoginAttempt login){
        return new LoginAttemptResponseDTO(login.getId(),
        login.getUser().getId(),
        login.getUser().getEmail(),
        login.getTimestamp(),
        login.getIp(),
        login.isSuccess());

    }
    //Attempt Register
    private LoginAttempt attemptRegister(User user, String ip, boolean success){
        LoginAttempt attempt =  new LoginAttempt();
        attempt.setUser(user);
        attempt.setIp(ip);
        attempt.setTimestamp(LocalDateTime.now());
        attempt.setSuccess(success);
        loginAttemptRepository.save(attempt);
        loginAttemptRepository.flush();
        return attempt;
    }
    //Verify Block on Login Attempts failed until 10min
    private void verifyBlock(User user){
        LocalDateTime time = LocalDateTime.now().minusMinutes(10);

        List<LoginAttempt> attemptFails = loginAttemptRepository
        .findByUserAndSuccess(user, false)
        .stream()
        .filter(a -> a.getTimestamp().isAfter(time))
        .toList();
        System.out.println(attemptFails.size());
        if(attemptFails.size()+1 >= 5){
            user.setStatus(UserStatus.BLOCKED);
            userRepository.save(user);
            throw new IllegalArgumentException("Usuário excedeu numero de tentativas");
        }
    }
    //verify suspect IP 
    private void verifySuspectIP(User user){
        LocalDateTime time = LocalDateTime.now().minusDays(7);

        List<String> attemptIP = loginAttemptRepository
        .findByUserAndSuccess(user, true)
        .stream()
        .filter(a -> a.getTimestamp().isAfter(time))
        .map(LoginAttempt::getIp)
        .distinct()
        .toList();

        if(attemptIP.size() >= 3){
            user.setStatus(UserStatus.UNDER_REVIEW);
            userRepository.save(user);
        }

    }

    //login
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public LoginAttemptResponseDTO login(LoginAttemptRequestDTO dto, String ip){
        //verifica se email não existe 
        User user = userRepository.findByEmail(dto.getEmail())
        .orElseThrow(()-> new IllegalArgumentException("Usuário não cadastradado"));

        //verifica se o usurio ja está bloquado e impede acesso
        if(user.getStatus().equals(UserStatus.BLOCKED)){
            throw new IllegalArgumentException("Usuário Bloqueado");            
        }
        //verifica se a senha da conta está correta e adiciona bloqueio
        if(passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            LoginAttempt login = attemptRegister(user, ip, true);
            verifySuspectIP(user);
            return toDTO(login);
        }else{
            attemptRegister(user, ip, false);
            verifyBlock(user);
            throw new IllegalArgumentException("Senha invalida");
        }  
    }


}
