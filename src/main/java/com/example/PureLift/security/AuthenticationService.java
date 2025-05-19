package com.example.PureLift.security;


import com.example.PureLift.AppUserRole;
import com.example.PureLift.entity.VerificationToken;
import com.example.PureLift.messaging.EmailMessage;
import com.example.PureLift.messaging.EmailProducer;
import com.example.PureLift.repository.TokenRepository;
import com.example.PureLift.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import com.example.PureLift.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailProducer emailProducer;;
    private final TokenRepository tokenRepository;


    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email jest już zajęty.");
        }


        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Nazwa użytkownika jest już zajęta.");
        }

        var user = User.builder()
                .name(request.getFirstName())
                .lastname(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .appuserRole(AppUserRole.ROLE_USER)
                .build();
        userRepository.save(user);


        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        tokenRepository.save(verificationToken);

        String activationLink = "http://localhost:8080/api/auth/confirm?token=" + token;

        EmailMessage message = new EmailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Potwierdzenie rejestracji");
        message.setBody("Witaj " + user.getName() + ",\n\nKliknij w link, aby aktywować konto:\n" + activationLink);

        emailProducer.sendEmail(message);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
    }
}
