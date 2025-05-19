package com.example.PureLift.service;

import com.example.PureLift.entity.User;
import com.example.PureLift.entity.VerificationToken;
import com.example.PureLift.repository.UserRepository;
import com.example.PureLift.repository.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public AuthService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void confirmUserToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);
    }
}