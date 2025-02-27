package com.bank.msauthentication.controllers;

import com.bank.msauthentication.entity.UserEntity;
import com.bank.msauthentication.service.AuthService;
import io.reactivex.rxjava3.core.Single;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1.0/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Single<ResponseEntity<String>> register(@RequestBody UserEntity user) {
        return authService.registerUser(user)
                .map(id -> ResponseEntity.ok("Usuario registrado con ID: " + id));
    }

    @PostMapping("/login")
    public Single<ResponseEntity<String>> login(@RequestParam String username, @RequestParam String password) {
        return authService.authenticate(username, password)
                .map(token -> ResponseEntity.ok("Bearer " + token));
    }
}
