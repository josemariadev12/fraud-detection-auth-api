package com.fraud_auth_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fraud_auth_api.entity.LoginAttempt;
import com.fraud_auth_api.entity.User;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, User> {
    List<LoginAttempt> findByUserAndSuccess(User user, boolean success);
    List<LoginAttempt> findByTimestampAfter(LocalDateTime timestamp);

    
}
