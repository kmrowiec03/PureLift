package com.example.PureLift.controller;


import com.example.PureLift.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token) {
        authService.confirmUserToken(token);
        return ResponseEntity.ok("Konto zostało potwierdzone!");
    }
}