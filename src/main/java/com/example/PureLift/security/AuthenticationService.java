package com.example.PureLift.security;


import com.example.PureLift.AppUserRole;
import com.example.PureLift.entity.VerificationToken;
import com.example.PureLift.messaging.EmailMessage;
import com.example.PureLift.messaging.EmailProducer;
import com.example.PureLift.repository.TokenRepository;
import com.example.PureLift.repository.UserRepository;
import com.example.PureLift.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.example.PureLift.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
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
    private final UserService userService;


    public ResponseEntity<Map<String, String>> register(RegisterRequest request) {

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

        return ResponseEntity.ok(Map.of("message", "Rejestracja zakończona. Sprawdź e-mail."));
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        String accessToken = jwtService.generateToken(user, 15* 60 * 1000);//15MIN
        String refreshToken = jwtService.generateToken(user,24 * 60 * 60 * 1000);//24H


        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Lax")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/api/security/refresh-token")
                .maxAge(Duration.ofDays(1))
                .sameSite("Lax")
                .build();


        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return ResponseEntity.ok(Map.of("message", "Zalogowano pomyślnie"));
    }

    public ResponseEntity<Object> logout(HttpServletResponse response) {
        ResponseCookie expiredAccessToken = ResponseCookie.from("accessToken", "")
                .path("/")
                .maxAge(0)
                .httpOnly(false)
                .secure(false)
                .sameSite("Lax")
                .build();

        ResponseCookie expiredRefreshToken = ResponseCookie.from("refreshToken", "")
                .path("/api/security/refresh-token")
                .maxAge(0)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", expiredAccessToken.toString());
        response.addHeader("Set-Cookie", expiredRefreshToken.toString());

        return ResponseEntity.ok().body(Map.of("message", "Wylogowano pomyślnie"));
    }

    public boolean isTokenValidFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    try {
                        String email = jwtService.extractUsername(token);
                        User user = userRepository.findByEmail(email).orElse(null);
                        return user != null && jwtService.isTokenValid(token, user);
                    } catch (Exception e) {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshToken = getCookieValue(request, "refreshToken");
            if (refreshToken == null) {
                return forbidden("Brak refresh tokena w ciasteczkach");
            }

            String email = jwtService.extractUsername(refreshToken);
            if (email == null) {
                return forbidden("Nieprawidłowy refresh token");
            }

            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return forbidden("Użytkownik nie istnieje");
            }

            if (!jwtService.isTokenValid(refreshToken, user)) {
                return forbidden("Refresh token wygasł lub jest nieprawidłowy");
            }

            String newAccessToken = jwtService.generateToken(user, 15* 60 * 1000);//15MIN

            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                    .httpOnly(false)
                    .secure(false)
                    .path("/")
                    .maxAge(Duration.ofMinutes(1))
                    .sameSite("Lax")
                    .build();

            response.addHeader("Set-Cookie", accessTokenCookie.toString());

            return ResponseEntity.ok(Map.of("message", "Token odświeżony"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Błąd serwera: " + e.getMessage());
        }
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private ResponseEntity<String> forbidden(String msg) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
    }
}
